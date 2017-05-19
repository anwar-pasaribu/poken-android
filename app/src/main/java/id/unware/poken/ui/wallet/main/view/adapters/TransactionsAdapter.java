package id.unware.poken.ui.wallet.main.view.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDate;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.GeneralListItem;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoUserTransaction;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;

/**
 * Adapter to handle wallet transaction history list.
 *
 * @since [V49] - Feb 6 NEW!
 */
public class TransactionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "AdapterTransactions";

    private final int TYPE_ITEM = 0;
    private final int TYPE_HEADER_PACKAGE_STATUS = 99;
    private static final int TYPE_EMPTY_STATE_ITEM = 88;

    /**
     * Decimal format for Indonesia
     */
    private DecimalFormat mMoneyFormatIndo = new DecimalFormat(
            "#,##0", new DecimalFormatSymbols(new Locale("id", "ID")));

    private List<Object> mItemList;
    private Context mContext;
    private OnClickRecyclerItem mListener;


    public TransactionsAdapter(Context context, List<Object> adapterList, OnClickRecyclerItem listener) {
        this.mItemList = adapterList;
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {

        // [V49] Identify row type list item instance
        Object item = mItemList.get(position);
        if (item instanceof PojoUserTransaction) {
            return TYPE_ITEM;
        } else if (item instanceof GeneralListItem) {
            return TYPE_EMPTY_STATE_ITEM;
        }

        return TYPE_ITEM;
    }

    @SuppressWarnings("WeakerAccess")
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
                Utils.Log(TAG, "Header item is clicked at pos: " + getAdapterPosition());
            }
        }
    }

    @SuppressWarnings("WeakerAccess")
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
            }

        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textViewAmount) TextView textViewAmount;
        @BindView(R.id.textViewBalance) TextView textViewBalance;
        @BindView(R.id.textViewTransactionId) TextView textViewTransactionId;
        @BindView(R.id.textViewTransactionInfo) TextView textViewTransactionInfo;
        @BindView(R.id.textViewTransactionStatus) TextView textViewTransactionStatus;
        @BindView(R.id.textViewDatetime) TextView textViewDatetime;

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (getAdapterPosition() >= 0 && mItemList.size() > 0) {
                Object item = mItemList.get(getAdapterPosition());

                if (item != null
                        && item instanceof PojoUserTransaction
                        && mListener != null) {

                    Utils.Log(TAG, "User transaction item clicked.");

                    mListener.onItemClick(v, getAdapterPosition());

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
            case TYPE_ITEM:
            default:
                view = inflater.inflate(R.layout.row_transaction, parent, false);
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
        Context context = holder.itemView.getContext();

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

            if (item != null && item instanceof PojoUserTransaction) {
                PojoUserTransaction pojoUserTransaction = (PojoUserTransaction) item;

                String strAmount = pojoUserTransaction.getAmount();
                String strLastBalance = pojoUserTransaction.getLastBalance();
                String strTransactionId = pojoUserTransaction.getTransactionId();
                String strTransactionType = pojoUserTransaction.getTransactionType();
                String strInfo = pojoUserTransaction.getInfo();
                String strTransactionStatus = pojoUserTransaction.getTransactionStatusText();
                //noinspection unused
                String strTransactionDesc = pojoUserTransaction.getTransactionStatusDescription();
                String strDatetime = pojoUserTransaction.getCreatedOn();

                // Format amount (Indonesia decimal format)
                double parsedAmount = Utils.getParsedDouble(strAmount);
                double parsedLastBalance = Utils.getParsedDouble(strLastBalance);

                String formattedAmount = mMoneyFormatIndo.format(parsedAmount);
                String formattedLastBalnce = mMoneyFormatIndo.format(parsedLastBalance);

                // Decide amount color
                int intColor = ContextCompat.getColor(mContext, R.color.black_90);

                // [V49] Feb 22 2017 - Color based on: negative (RED) or positive (GREEN)
                intColor = parsedAmount < 0
                        ? ContextCompat.getColor(mContext, R.color.red)
                        : ContextCompat.getColor(mContext, R.color.green);

                holder.textViewAmount.setTextColor(intColor);

                holder.textViewAmount.setText(makeCharSequence(formattedAmount));
                holder.textViewBalance.setText(makeCharSequence(formattedLastBalnce));

                // Trans ID <bullet> Trans. type.
                //noinspection deprecation
                holder.textViewTransactionId.setText(Html.fromHtml(strTransactionId + " &#8226; " + strTransactionType));

                holder.textViewTransactionInfo.setText(strInfo);
                holder.textViewTransactionStatus.setText(strTransactionStatus);

                // Set formatted date time (ex. 20 Agustus 2017, 20:30)
                holder.textViewDatetime.setText(ControllerDate.getInstance().toTransactionHistory(strDatetime));

            }

        } catch (IndexOutOfBoundsException | IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private CharSequence makeCharSequence(String strBalance) {

        if (Utils.isEmpty(strBalance)) return "";


        char firstChar = strBalance.charAt(0);

        String prefix = mContext.getString(R.string.lbl_idr);

        // Detect minus amount
        if (firstChar == '-') {

            // Remove minus from amount and add "-" before prefix.
            strBalance = strBalance.replaceAll("-", "");
            prefix = "-" + prefix;

            Utils.Log(TAG, "Make MINUS char seq: " + strBalance);

        }

        String sequence = prefix + " " + strBalance;

        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(Typeface.NORMAL), 0, prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), prefix.length(), sequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    @Override
    public int getItemCount() {
        return mItemList != null ? mItemList.size() : -1;
    }

}