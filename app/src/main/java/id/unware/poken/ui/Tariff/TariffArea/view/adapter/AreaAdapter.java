package id.unware.poken.ui.Tariff.TariffArea.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.unware.poken.R;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.PojoArea;
import id.unware.poken.tools.Utils;

/*
 * Area adapter to show search result. This adapter provides two kind of item,
 * header item and regular item. Head item showed when AreaItem.tariffCode is empty.
 *
 * @since Oct. 20, 2016 (V 24) - Extracted from inner class on {@link id.paket.tiki.fragment.AreaFragment}
 */
public class AreaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "AreaAdapter";

    private List<PojoArea> mListArea = new ArrayList<>();
    private final OnClickRecyclerItem mListener;

    // Row type
    private final int TYPE_MORE = 0;
    private final int TYPE_ITEM = 1;

    public AreaAdapter(List<PojoArea> items, OnClickRecyclerItem listener) {
        mListArea = items;
        mListener = listener;
    }

    /**
     * Set/ Change adapter data. In case when user tap "More" item.
     */
    public void setHeaderText(int headerPos, String strHeader) {

        if (headerPos < 0 || headerPos >= mListArea.size()) return;

        mListArea.get(headerPos).daerah = strHeader;
        mListArea.get(headerPos).setExpanded(true);
    }

    // Get all list item
    public List<PojoArea> getAdapterData() {
        return mListArea;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Utils.Log(TAG, "On Create View Holder ViewType No: " + viewType);

        RecyclerView.ViewHolder mViewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_MORE:
                View moreView = inflater.inflate(R.layout.item_list_more, parent, false);
                mViewHolder = new MoreItemAreaViewHolder(moreView);
                break;
            default:
                View itemView = inflater.inflate(R.layout.item_single_text, parent, false);
                mViewHolder = new ItemAreaViewHolder(itemView);
                break;
        }

        return mViewHolder;

    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Utils.Log(TAG, "Bind view holder");

        if (holder.getItemViewType() == TYPE_MORE) {

            configureMoreItem((MoreItemAreaViewHolder) holder, position);

        } else if (holder.getItemViewType() == TYPE_ITEM) {
            final ItemAreaViewHolder itemViewHolder = (ItemAreaViewHolder) holder;
            itemViewHolder.mItem = mListArea.get(position);
            itemViewHolder.mContentView.setText(mListArea.get(position).daerah);
        }
    }

    /**
     * Configure "More" item.
     *
     * @param holder    : {@link MoreItemAreaViewHolder} object.
     * @param position  : Item position on adapter.
     *
     * @since Oct. 20, 2016 (V 24) - NEW!
     */
    private void configureMoreItem(final MoreItemAreaViewHolder holder, int position) {
        final Context context = holder.mView.getContext();

        // Set PojoArea for list item
        holder.mMoreItem = mListArea.get(position);

        holder.mHeaderTextView.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        holder.mHeaderImageView.setVisibility(View.VISIBLE);

        // When item state is expanded (indicate network load is succeed)
        // set this item position as a header for next items.
        if (holder.mMoreItem.isExpanded() && !holder.mMoreItem.daerah.equals("")) {

            holder.mView.setClickable(false);
            holder.mHeaderTextView.setText(context.getString(R.string.lbl_rate_district));
            holder.mHeaderImageView.animate().rotation(90);

        // User click "More" item, then change it to "Loading..."
        } else if (!holder.mMoreItem.isExpanded() && holder.mMoreItem.daerah.equals(context.getString(R.string.loading))) {

            // Set item text to "Loading..."
            holder.mView.setClickable(false);
            holder.mHeaderTextView.setText(holder.mMoreItem.daerah);

        // Empty state (no String on "daerah" and isExpanded true)
        } else if (holder.mMoreItem.isExpanded() && holder.mMoreItem.daerah.equals("")) {

            // Set empty state "No Data"
            holder.mView.setClickable(false);
            holder.mHeaderTextView.setText(context.getString(R.string.lbl_no_data));
            holder.mHeaderTextView.setTextColor(ContextCompat.getColor(context, R.color.style_overlay_grey));
            holder.mHeaderImageView.setVisibility(View.GONE);

        } else {

            // Set item text to "More"
            holder.mView.setClickable(true);
            holder.mHeaderTextView.setText(holder.mMoreItem.daerah);

            holder.mHeaderImageView.setImageResource(R.drawable.ic_chevron);

            holder.mHeaderImageView.setRotation(0);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position < 0 || position >= mListArea.size()) return TYPE_ITEM;

        if (mListArea.get(position).tariffCode.equals("")) {
            return TYPE_MORE;
        } else if (mListArea.get(position).isExpanded()) {
            return TYPE_MORE;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        if (mListArea != null && !mListArea.isEmpty())
            return mListArea.size();
        else
            return 0;
    }

    /**
     * Search item view to hold search sesult.
     */
    private class ItemAreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View mView;
        final TextView mContentView;
        PojoArea mItem;

        ItemAreaViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.textViewContent);

            mView.setClickable(true);
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Utils.Log(TAG, "Area item clicked at pos: " + getAdapterPosition());

            if (mListener != null)
                mListener.onItemClick(view, getAdapterPosition());
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


    /**
     * More view indicate end of offline data.
     */
    private class MoreItemAreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View mView;
        final TextView mHeaderTextView;
        final ImageView mHeaderImageView;
        PojoArea mMoreItem;

        MoreItemAreaViewHolder(View view) {
            super(view);
            mView = view;
            mHeaderTextView = (TextView) view.findViewById(R.id.textViewShowMore);
            mHeaderImageView = (ImageView) view.findViewById(R.id.imageViewRightArrow);

            mView.setClickable(true);
            mView.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mHeaderTextView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            Utils.Log(TAG, "More item clicked at pos: " + getAdapterPosition());

            if (mListener != null)
                mListener.onItemClick(v, getAdapterPosition());
        }
    }


}
