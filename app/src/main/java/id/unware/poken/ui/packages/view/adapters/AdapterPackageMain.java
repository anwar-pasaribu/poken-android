package id.unware.poken.ui.packages.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDate;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.GeneralListItem;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.packages.presenter.PackagesPresenter;

/**
 * Adapter to show Package list include empty state item.
 *
 * @since   [V54] - March 12 - NO Package Image <br />
 *          [V49] - Jan 25 NEW!
 */
public class AdapterPackageMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "AdapterPackageMain";

    private final int TYPE_ITEM = 0;
    private final int TYPE_HEADER_PACKAGE_STATUS = 99;
    private static final int TYPE_EMPTY_STATE_ITEM = 88;

    private List<Object> mItemList = new ArrayList<>();
    private PackagesPresenter mPackagesPresenter;
    private OnClickRecyclerItem mListener;

    private Context mContext;


    public AdapterPackageMain(Context context, List<Object> listObject, PackagesPresenter packagesPresenter) {
        this.mItemList.clear();
        this.mItemList.addAll(listObject);
        this.mPackagesPresenter = packagesPresenter;
        this.mContext = context;
    }

    public void addPackage(PojoBooking pojoBooking) {
        Utils.Log(TAG, "Add pojobooking ID: \"" + pojoBooking.getBooking_id() + "\"");
        this.mItemList.add(mItemList.size(), pojoBooking);
        notifyItemInserted(mItemList.size());
    }

    public void addPackage(int pos, Object pojoBooking) {

        if (pos < 0 || pos > this.mItemList.size() ) return;

        Utils.Log(TAG, "Add object to adapter at pos: \"" + pos + "\"");
        this.mItemList.add(pos, pojoBooking);
        notifyItemInserted(pos);
    }

    public void addAllPackage(List<PojoBooking> pojoBookingList) {
        Utils.Log(TAG, "Add all pojobooking size: " + pojoBookingList.size());
        this.mItemList.addAll(mItemList.size(), pojoBookingList);
        notifyItemInserted(mItemList.size());
    }

    public void addAllPackage(int pos, List<PojoBooking> pojoBookingList) {

        if (pos < 0 || pos > this.mItemList.size()) return;

        Utils.Log(TAG, "Add all at pos pojobooking size: " + pojoBookingList.size() + ", pos: " + pos);
        this.mItemList.addAll(pos, pojoBookingList);
        notifyItemRangeInserted(pos, pojoBookingList.size());
    }

    public void replaceAllPackage(List<PojoBooking> pojoBookingList) {
        this.mItemList.clear();
        this.mItemList.addAll(pojoBookingList);
        notifyDataSetChanged();
    }

    public void replaceWithEmptyItem(Object object) {
        Utils.Log(TAG, "Replace with empty item.");
        this.mItemList.clear();
        this.mItemList.add(object);
        notifyDataSetChanged();
    }

    public void removeFirstPackage() {

        if (this.mItemList.size() < 0 ) return;

        Utils.Log(TAG, "Remove first pojobooking.");
        this.mItemList.remove(0);
        notifyItemRemoved(0);
    }

    public void removeAt(int pos) {

        if (pos < 0 || pos >= this.mItemList.size()) return;

        Utils.Log(TAG, "Remove pojobooking at: " + pos);
        this.mItemList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void removeLastPackage() {

        if (this.mItemList.size() < 0 ) return;

        Utils.Log(TAG, "Remove last pojobooking.");
        this.mItemList.remove(this.mItemList.size() - 1 );
        notifyItemRemoved(this.mItemList.size());
    }

    public Object getItem(int position) {

        if (position < 0 || position >= this.mItemList.size()) return null;

        return mItemList.get(position);
    }

    @Override
    public int getItemViewType(int position) {

        // [V49] Identify row type list item instance
        Object item = mItemList.get(position);
        if (item instanceof PojoBooking) {

            final long bookingId = ((PojoBooking) item).getBooking_id();

            return bookingId == Constants.HEADER_ITEM_ID
                    || bookingId == Constants.FOOTER_LOAD_MORE_ITEM_ID
                    ? TYPE_HEADER_PACKAGE_STATUS
                    : TYPE_ITEM;

        } else if (item instanceof GeneralListItem) {
            return TYPE_EMPTY_STATE_ITEM;
        }

        return TYPE_ITEM;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textViewHeader) TextView textViewHeader;
        @BindView(R.id.buttonHeader) Button buttonHeader;

        public HeaderViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            buttonHeader.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Utils.Log("AdapterPackage", "Header clicked");

            if (getAdapterPosition() >= 0 && mItemList.size() > 0) {

                if (mPackagesPresenter != null) {

                    Object item = mItemList.get(getAdapterPosition());
                    if (item != null && item instanceof PojoBooking) {

                        PojoBooking pojoBooking = (PojoBooking) item;

                        final long bookingId = pojoBooking.getBooking_id();

                        Utils.Log(TAG, "Clicked booking id: " + bookingId);

                        if (bookingId == Constants.FOOTER_LOAD_MORE_ITEM_ID) {
                            // Footer item
                            mPackagesPresenter.requestMorePackagesOnline();
                        } else if (bookingId == Constants.HEADER_ITEM_ID) {
                            // Header with "request pickup"
                            mPackagesPresenter.startPickupMap();
                        }
                    }

                } else if (mListener != null) {
                    mListener.onItemClick(v, getAdapterPosition());
                }
            }
        }
    }

    public class EmptyStateViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.emptyStateImage) ImageView emptyStateImage;
        @BindView(R.id.txtEmptyPackage) TextView txtEmptyPackage;

        public EmptyStateViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            txtEmptyPackage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Object item = mItemList.get(getAdapterPosition());
            if (item != null && item instanceof GeneralListItem) {
                Utils.Log(TAG, "Empty state text clicked. View: " + v);

                if (mPackagesPresenter != null) {
                    // mPackagesPresenter.startTutorial();
                    MyLog.FabricLog(Log.INFO, "User still clik on Empty State text.");
                }
            }

        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txtCode) TextView txtCode;
        @BindView(R.id.txtName) TextView txtName;
        @BindView(R.id.txtMessage) TextView txtMessage;
        @BindView(R.id.txtDate) TextView txtDate;
        @BindView(R.id.txtStatus) TextView txtStatus;

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (getAdapterPosition() >= 0 && mItemList.size() > 0) {
                Object item = mItemList.get(getAdapterPosition());

                if (item != null && item instanceof PojoBooking) {
                    PojoBooking pojoBooking = (PojoBooking) item;

                    if (mPackagesPresenter != null) {

                        if (pojoBooking.getBooking_id() == Constants.HEADER_ITEM_ID) {
                            mPackagesPresenter.requestMorePackagesOnline();
                        } else {
                            mPackagesPresenter.startPackageDetail(pojoBooking.getBooking_id(), getAdapterPosition());
                        }

                    } else if (mListener != null) {
                        mListener.onItemClick(v, getAdapterPosition());
                    }
                }
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vhItem;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;

        switch (viewType) {
            case TYPE_HEADER_PACKAGE_STATUS:
                view = inflater.inflate(R.layout.row_header_general, parent, false);
                vhItem = new HeaderViewHolder(view);
                break;
            case TYPE_EMPTY_STATE_ITEM:
                view = inflater.inflate(R.layout.row_empty_state, parent, false);
                vhItem = new EmptyStateViewHolder(view);
                break;

            default:
                view = inflater.inflate(R.layout.row_package, parent, false);
                vhItem = new ItemViewHolder(view);
        }

        return vhItem;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case TYPE_HEADER_PACKAGE_STATUS:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                configureHeaderItem(headerViewHolder, position);
                break;
            case TYPE_EMPTY_STATE_ITEM:
                EmptyStateViewHolder emptyStateViewHolder = (EmptyStateViewHolder) holder;
                configureEmptyStateItem(emptyStateViewHolder, position);
                break;
            default:
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                configureRegularItem(itemViewHolder, position);
        }

    }

    private void configureEmptyStateItem(EmptyStateViewHolder holder, int position) {
        Object item = mItemList.get(position);

        if (item != null && item instanceof GeneralListItem) {
            GeneralListItem generalListItem = (GeneralListItem) item;
            String strTitle = generalListItem.getTitle();
            int intImageRes = generalListItem.getListIcon();

            holder.txtEmptyPackage.setText(strTitle);
            holder.emptyStateImage.setImageResource(intImageRes);
        }
    }

    private void configureHeaderItem(HeaderViewHolder headerViewHolder, int position) {
        try {

            final Object item = mItemList.get(position);

            if (item instanceof PojoBooking) {

                final PojoBooking pojoPackage = (PojoBooking) mItemList.get(position);

                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) headerViewHolder.itemView.getLayoutParams();
                layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.clickable_size);
                layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT;

                switch ((int) pojoPackage.getBooking_id()) {
                    case (Constants.FOOTER_LOAD_MORE_ITEM_ID):
                        // LOAD MORE ITEM
                        headerViewHolder.itemView.setLayoutParams(layoutParams);
                        headerViewHolder.textViewHeader.setVisibility(View.GONE);

                        // Set button to fill all parent view.
                        headerViewHolder.buttonHeader.setLayoutParams(
                                new RelativeLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT)
                        );

                        headerViewHolder.buttonHeader.setText(mContext.getString(R.string.lbl_load_more));
                        headerViewHolder.textViewHeader.setText(null);

                        break;
                    case (Constants.HEADER_ITEM_ID):
                        // REQUEST PICKUP ITEM
                        int intBookedCount = Utils.getParsedInt(pojoPackage.getContent());

                        String headerTitle = mContext.getResources()
                                .getQuantityString(R.plurals.lbl_package_ready_to_ship, intBookedCount, intBookedCount);

                        headerViewHolder.itemView.setLayoutParams(layoutParams);
                        headerViewHolder.textViewHeader.setVisibility(View.VISIBLE);

                        // Relative Layoutparam to put button to the parent's right.
                        RelativeLayout.LayoutParams relLayoutParams = new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        relLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

                        headerViewHolder.buttonHeader.setLayoutParams(relLayoutParams);

                        headerViewHolder.buttonHeader.setText(mContext.getString(R.string.lbl_pickup_now));
                        headerViewHolder.textViewHeader.setText(headerTitle);
                        break;
                }
            }

        } catch (IndexOutOfBoundsException | IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void configureRegularItem(ItemViewHolder holder, int position) {
        try {
            final Object item = mItemList.get(position);

            if (item != null && item instanceof PojoBooking) {

                PojoBooking pojoPackage = (PojoBooking) item;

                String  strBookingCode = pojoPackage.getBooking_code(),
                        // Remove NON-ASCII chars
                        strReceiverName = StringUtils.getAsciiString(pojoPackage.getTo_name()),
                        strPackageContent = pojoPackage.getContent(),
                        strFormattedDate = String.valueOf(ControllerDate.getFormattedDate(pojoPackage.getBooking_date(), mContext)),
                        strBookingStatus = pojoPackage.getBooking_status_text();

                holder.txtCode.setText(strBookingCode);
                // Format Date first then set to textView
                holder.txtDate.setText(strFormattedDate);
                holder.txtName.setText(strReceiverName);

                holder.txtMessage.setVisibility(Utils.isEmpty(strPackageContent)? View.GONE : View.VISIBLE);
                holder.txtMessage.setText(strPackageContent);

                // Set TextView text color
                holder.txtStatus.setTextColor(pojoPackage.getStatusTextColor(mContext));
                holder.txtStatus.setText(strBookingStatus);
            }

        } catch (IndexOutOfBoundsException | IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return mItemList != null ? mItemList.size() : -1;
    }

}