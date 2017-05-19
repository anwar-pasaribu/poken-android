package id.unware.poken.ui.TrackPackage.TrackingResult.view.adapter;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDate;
import id.unware.poken.pojo.PojoTracking;
import id.unware.poken.pojo.PojoTrackingStatus;
import id.unware.poken.pojo.RecentSearchKeyword;
import id.unware.poken.tools.BitmapUtil;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;


/**
 * Products adapter to manage how products and services showed on PRODUCTS activity.
 */
public class TrackingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> mList = new ArrayList<>();
    private Context mContext;

    // Interface for click listener
    // INTERFACE#1 : Init field
    private OnItemClickListener mItemClickListener;

    private static final int TYPE_TRACKING_H = -25;
    private static final int TYPE_TRACKING_RECENT = -26;
    private static final int TYPE_TRACKING_ITEM = -27;
    private static final int TYPE_NO_TRACKING_ITEM = -28;

    public TrackingAdapter(Context context, List<Object> mList) {
        this.mList = mList;
        this.mContext = context;
    }

    // Interface for click listener
    // INTERFACE#2 : Register action
    public interface OnItemClickListener {
        void onItemClick(View view, int position, String airWayBill);
    }

    // Interface for click listener
    // INTERFACE#3 : Set #1 value.
    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    // Layout view holder for recent search
    public class ViewHolderRecent extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textViewContent) TextView textViewContent;
        @BindView(R.id.textViewUpdatedTime) TextView textViewUpdatedTime;
        @BindView(R.id.viewSeparator) View viewSeparator;

        public ViewHolderRecent(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // INTERFACE#5 : Register click to view.
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (getAdapterPosition() < 0 || getAdapterPosition() >= mList.size()) return;

            Object clickedItem = mList.get(getAdapterPosition());

            // INTERFACE#4 : Set action to on click event.
            if (mItemClickListener != null && clickedItem instanceof RecentSearchKeyword) {

                String keyword = ((RecentSearchKeyword) clickedItem).getKeywordString();
                Utils.Log("TrackingAdapter", "Pos: " + getAdapterPosition() + ", keyword: " + keyword);
                mItemClickListener.onItemClick(itemView, getAdapterPosition(), keyword);
            }
        }
    }

    // Layout view holder for tracking item
    public class ViewHolderTrackingItem extends RecyclerView.ViewHolder {

        @BindView(R.id.imageViewStat) ImageView imageViewStat;
        @BindView(R.id.textViewDest) TextView textViewDest;
        @BindView(R.id.textViewNote) TextView textViewNote;
        @BindView(R.id.textViewDateTime) TextView textViewDateTime;

        public ViewHolderTrackingItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    // Layout view holder for header / footer
    public class ViewHolderHeader extends RecyclerView.ViewHolder {

        // For selected service badge
        @BindView(R.id.parentBadge) ViewGroup parentBadge;
        @BindView(R.id.imageViewBagdeColor) ImageView imageViewBagdeColor;

        @BindView(R.id.textViewService) TextView textViewService;
        @BindView(R.id.textViewSenderNameVal) TextView textViewSenderNameVal;
        @BindView(R.id.textViewSenderAddress) TextView textViewSenderAddress;
        @BindView(R.id.textViewReceiverNameVal) TextView textViewReceiverNameVal;
        @BindView(R.id.textViewReceiverAddress) TextView textViewReceiverAddress;

        public ViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder mViewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_TRACKING_H:
                View headerView = inflater.inflate(R.layout.item_tracking_header, parent, false);
                mViewHolder = new ViewHolderHeader(headerView);
                break;
            case TYPE_NO_TRACKING_ITEM:
            case TYPE_TRACKING_RECENT:
                View recentView = inflater.inflate(R.layout.item_recent_tracking, parent, false);
                mViewHolder = new ViewHolderRecent(recentView);
                break;
            default:
                View itemView = inflater.inflate(R.layout.item_tracking, parent, false);
                mViewHolder = new ViewHolderTrackingItem(itemView);
        }

        return mViewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Data item
        Object currentItem = mList.get(position);
        switch (holder.getItemViewType()) {
            case TYPE_TRACKING_H:
                ViewHolderHeader headerViewHolder = (ViewHolderHeader) holder;
                configureHeaderViewHolder(headerViewHolder, currentItem);
                break;
            case TYPE_TRACKING_RECENT:
                ViewHolderRecent recentViewHolder = (ViewHolderRecent) holder;
                configureRecentViewHolder(recentViewHolder, currentItem);
                break;
            case TYPE_TRACKING_ITEM:
                ViewHolderTrackingItem trackingItemViewHolder = (ViewHolderTrackingItem) holder;
                configureTrackingViewHolder(trackingItemViewHolder, currentItem);
                break;
            case TYPE_NO_TRACKING_ITEM:
                ViewHolderRecent noTrackingViewHolder = (ViewHolderRecent) holder;
                configureNoTrackingItem(noTrackingViewHolder, currentItem);

        }

    }

    private void configureNoTrackingItem(ViewHolderRecent noTrackingViewHolder, Object currentItem) {
        Utils.Log("Adapter", "Configure no trackings");
        if (currentItem instanceof String) {
            final String item = (String) currentItem;

            Utils.Log("Adapter", "Item: " + item);

            noTrackingViewHolder.itemView.setEnabled(false);

            noTrackingViewHolder.textViewContent.setVisibility(View.VISIBLE);
            noTrackingViewHolder.textViewUpdatedTime.setVisibility(View.GONE);

            noTrackingViewHolder.textViewContent.setText(item);
            noTrackingViewHolder.textViewContent.setGravity(Gravity.CENTER);

            noTrackingViewHolder.viewSeparator.setVisibility(View.GONE);
        }
    }

    private void configureTrackingViewHolder(ViewHolderTrackingItem trackingHolder, Object currentItem) {

        Utils.Log("Adapter", "Configure Tracking");

        if (currentItem instanceof PojoTrackingStatus) {
            PojoTrackingStatus item = (PojoTrackingStatus) currentItem;

            // Date time formatter
            final String fixedEtTime = (item.getEntryDate().split(":").length != 3)
                    ? String.format("%s:00", item.getEntryDate())
                    : item.getEntryDate();
            CharSequence charSeqDateTime = ControllerDate.getInstance()
                    .toTrackingPageDateFormat(
                            String.format("%s %s", item.getEntryDate(), fixedEtTime));

            trackingHolder.textViewDateTime.setText(charSeqDateTime);
            trackingHolder.textViewDest.setText(item.getEntryName());
            trackingHolder.textViewNote.setText(item.getNoted());

            LayerDrawable ld = (LayerDrawable) ContextCompat.getDrawable(mContext, R.drawable.tracking_icon);

            Integer icon = R.drawable.check;
            Drawable replace = ContextCompat.getDrawable(mContext, icon);
            int inset = mContext.getResources().getDimensionPixelSize(R.dimen.tracking_icon_inset);
            ld.setLayerInset(1, inset, inset, inset, inset);
            ld.setDrawableByLayerId(R.id.stat_icon, replace);

            Drawable replaceBg = ContextCompat.getDrawable(mContext, R.drawable.tracking_bg_success);
            ld.setDrawableByLayerId(R.id.stat_icon_bg, replaceBg);

            trackingHolder.imageViewStat.setImageDrawable(ld);
        }

    }

    private void configureRecentViewHolder(ViewHolderRecent recentHolder, Object currentItem) {

        Utils.Log("Adapter", "Configure Recent");

        if (currentItem instanceof RecentSearchKeyword) {
            final RecentSearchKeyword item = (RecentSearchKeyword) currentItem;

            Utils.Log("TrackingAdapter2", "Time stamp: " + item.getTimeStamp());

            if (item.getId() == Constants.TAG_HEADER_RECENT) {

                recentHolder.itemView.setEnabled(false);

                recentHolder.textViewContent.setVisibility(View.GONE);
                recentHolder.textViewUpdatedTime.setVisibility(View.VISIBLE);
                recentHolder.textViewUpdatedTime.setText(mContext.getString(R.string.lbl_recent));

            } else {

                recentHolder.itemView.setEnabled(true);

                recentHolder.textViewContent.setVisibility(View.VISIBLE);
                recentHolder.textViewUpdatedTime.setVisibility(View.GONE);

                recentHolder.textViewContent.setText(item.getKeywordString());

            }
        }

    }

    private void configureHeaderViewHolder(ViewHolderHeader viewHolderHeader, Object headerObject) {
        Utils.Log("Adapter", "Configure Header");
        if (headerObject instanceof PojoTracking) {

            Context context = viewHolderHeader.itemView.getContext();

            PojoTracking pojoTracking = (PojoTracking) headerObject;

            List<PojoTrackingStatus> statuses = pojoTracking.getStatusList();
            String strLastUpdateNote = (statuses != null && statuses.size() > 0)
                    ? statuses.get(0).getNoted()
                    : "";

            // Date time formatter
            String strSelectedService = (!TextUtils.isEmpty(pojoTracking.getProduct())
                    ? pojoTracking.getProduct().toUpperCase()
                    : "");
            String strSenderName = pojoTracking.getConsignorName();
            String strSenderAddress = pojoTracking.getConsignorAddress();
            String strReceiverName = pojoTracking.getConsigneeName();
            String strReceiverAddress = pojoTracking.getConsigneeAddress();

            // Service text color based on service type
            Integer intColor = R.color.white_100;
            PorterDuffColorFilter colorFilter = BitmapUtil.getEnabledColor(context);

            if (strSelectedService.contains("REG")) {
                colorFilter = BitmapUtil.getDrawableFilter(context, R.color.reg);
            } else if (strSelectedService.contains("SDS")){
                colorFilter = BitmapUtil.getDrawableFilter(context, R.color.sds);
                intColor = R.color.black_90;
            } else if (strSelectedService.contains("TDS")){
                colorFilter = BitmapUtil.getDrawableFilter(context, R.color.sds);
                intColor = R.color.white_100;
            } else if (strSelectedService.contains("ECO")){
                colorFilter = BitmapUtil.getDrawableFilter(context, R.color.eco);
                intColor = R.color.white_100;
            } else if (strSelectedService.contains("HDS")){
                colorFilter = BitmapUtil.getDrawableFilter(context, R.color.hds);
                intColor = R.color.white_100;
            } else if (strSelectedService.contains("INT")){
                colorFilter = BitmapUtil.getDrawableFilter(context, R.color.international);
                intColor = R.color.black_90;
            } else if (strSelectedService.contains("ONS")){
                colorFilter = BitmapUtil.getDrawableFilter(context, R.color.sds);
                intColor = R.color.white_100;
            }

            // Badge
            viewHolderHeader.imageViewBagdeColor.setColorFilter(colorFilter);
            viewHolderHeader.textViewService.setText(strSelectedService);
            viewHolderHeader.textViewService.setTextColor(ContextCompat.getColor(mContext, intColor));

            viewHolderHeader.textViewSenderNameVal.setText(strSenderName);
            viewHolderHeader.textViewSenderAddress.setText(strSenderAddress);
            viewHolderHeader.textViewReceiverNameVal.setText(strReceiverName);
            viewHolderHeader.textViewReceiverAddress.setText(strReceiverAddress);
        }

    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {

        Object currentItem = mList.get(position);

        if (currentItem instanceof PojoTracking) {
            return TYPE_TRACKING_H;
        } else if (currentItem instanceof PojoTrackingStatus) {
            return TYPE_TRACKING_ITEM;
        } else if (currentItem instanceof RecentSearchKeyword) {
            return TYPE_TRACKING_RECENT;
        } else if (currentItem instanceof String) {
            return TYPE_NO_TRACKING_ITEM;
        }

        return TYPE_TRACKING_ITEM;
    }
}
