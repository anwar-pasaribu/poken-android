package id.unware.poken.ui.pickup.history.view.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.BuildConfig;
import id.unware.poken.R;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;

/**
 * Adapter to show simple purpose Package list. The adapter
 */
public class AdapterPackage extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "AdapterPackage";

    private final int TYPE_ITEM = 0;
    private final int TYPE_HEADER_PACKAGE_STATUS = 99;

    private List<PojoBooking> listPackage = new ArrayList<>();
    private OnClickRecyclerItem listener;

    private Context mContext;


    public AdapterPackage(Context context, List<PojoBooking> listPromotion, OnClickRecyclerItem listener) {
        this.listPackage.clear();
        this.listPackage.addAll(listPromotion);
        this.listener = listener;
        this.mContext = context;
    }

    public void addPackage(PojoBooking pojoBooking) {
        Utils.Log(TAG, "Add pojobooking ID: \"" + pojoBooking.getBooking_id() + "\"");
        this.listPackage.add(listPackage.size(), pojoBooking);
        notifyItemInserted(listPackage.size());
    }

    public void addPackage(int pos, PojoBooking pojoBooking) {

        if (pos < 0 || pos > this.listPackage.size()) return;

        Utils.Log(TAG, "Add pojobooking at pos with id: \"" + pojoBooking.getBooking_id() + "\"");
        this.listPackage.add(pos, pojoBooking);
        notifyItemInserted(pos);
    }

    public void addAllPackage(List<PojoBooking> pojoBookingList) {
        Utils.Log(TAG, "Add all pojobooking size: " + pojoBookingList.size());
        this.listPackage.addAll(listPackage.size(), pojoBookingList);
        notifyItemInserted(listPackage.size());
    }

    public void addAllPackage(int pos, List<PojoBooking> pojoBookingList) {

        if (pos < 0 || pos > this.listPackage.size()) return;

        Utils.Log(TAG, "Add all at pos pojobooking size: " + pojoBookingList.size() + ", pos: " + pos);
        this.listPackage.addAll(pos, pojoBookingList);
        notifyItemRangeInserted(pos, pojoBookingList.size());
    }

    public void replaceAllPackage(List<PojoBooking> pojoBookingList) {
        this.listPackage.clear();
        this.listPackage.addAll(pojoBookingList);
        notifyDataSetChanged();
    }

    public void removeFirstPackage() {

        if (this.listPackage.size() < 0 ) return;

        Utils.Log(TAG, "Remove first pojobooking.");
        this.listPackage.remove(0);
        notifyItemRemoved(0);
    }

    public void removeLastPackage() {
        Utils.Log(TAG, "Remove last pojobooking.");
        this.listPackage.remove(this.listPackage.size() - 1 );
        notifyItemRemoved(this.listPackage.size());
    }

    @Nullable
    public PojoBooking getPackage(int position) {

        if (position < 0 || position >= this.listPackage.size()) return null;

        return this.listPackage != null ? this.listPackage.get(position) : null;
    }

    @Override
    public int getItemViewType(int position) {
        final long bookingId = listPackage.get(position).getBooking_id();
        return bookingId == Constants.HEADER_ITEM_ID ? TYPE_HEADER_PACKAGE_STATUS : TYPE_ITEM;
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
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
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
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vhItem;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_HEADER_PACKAGE_STATUS:
                View headerItemView = inflater.inflate(R.layout.row_header_general, parent, false);
                vhItem = new HeaderViewHolder(headerItemView);
                break;
            default:
                View regularItemView = inflater.inflate(R.layout.row_package, parent, false);
                vhItem = new ItemViewHolder(regularItemView);
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
            default:
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                configureRegularItem(itemViewHolder, position);
        }

    }

    private void configureHeaderItem(HeaderViewHolder headerViewHolder, int position) {
        try {

            final PojoBooking pojoPackage = listPackage.get(position);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) headerViewHolder.itemView.getLayoutParams();
            layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.clickable_size);
            layoutParams.width = RecyclerView.LayoutParams.MATCH_PARENT;

            switch (pojoPackage.getContent()) {
                case "*":
                    // LOAD MORE ITEM
                    headerViewHolder.itemView.setLayoutParams(layoutParams);
                    headerViewHolder.textViewHeader.setVisibility(View.GONE);

                    // Set button to fill all parent view.
                    headerViewHolder.buttonHeader.setLayoutParams(
                            new RelativeLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT)
                    );

                    headerViewHolder.buttonHeader.setText(pojoPackage.getBooking_status_text());
                    headerViewHolder.textViewHeader.setText(null);

                    break;
                default:
                    // REQUEST PICKUP ITEM
                    int intBookedCount = 0;
                    try {
                        intBookedCount = Integer.parseInt(pojoPackage.getContent());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

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

        } catch (IndexOutOfBoundsException | IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void configureRegularItem(ItemViewHolder holder, int position) {
        try {

            PojoBooking pojoPackage = listPackage.get(position);
            String  strBookingCode = pojoPackage.getBooking_code(),
                    // Remove NON-ASCII chars
                    strReceiverName = StringUtils.getAsciiString(pojoPackage.getTo_name()),
                    strPackageContent = pojoPackage.getContent(),
                    strFormattedDate = String.valueOf(pojoPackage.getBooking_date()),
                    strBookingStatus = pojoPackage.getBooking_status_text();

            if (BuildConfig.DEV_MODE) {
                strBookingCode = String.format("%s - %s", strBookingCode, pojoPackage.getBooking_id());
            }

            holder.txtCode.setText(strBookingCode);

            // Format Date first then set to textView
            holder.txtDate.setText(strFormattedDate);

            // Remove NON-ASCII chars
            holder.txtName.setText(strReceiverName);

            if (Utils.isEmpty(strPackageContent)) {
                holder.txtMessage.setVisibility(View.GONE);
            } else {
                holder.txtMessage.setVisibility(View.VISIBLE);
                holder.txtMessage.setText(strPackageContent);
            }

            // Set TextView text color
            holder.txtStatus.setTextColor(pojoPackage.getStatusTextColor(mContext));
            holder.txtStatus.setText(strBookingStatus);

        } catch (IndexOutOfBoundsException | IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listPackage != null ? listPackage.size() : -1;
    }

}