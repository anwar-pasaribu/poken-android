package id.unware.poken.ui.packages.view.adapters;

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
import id.unware.poken.tools.Utils;

/**
 * Area adapter to show search result. This adapter provides two kind of item,
 * header item and regular item. Head item showed when AreaItem.tariffCode is empty.
 *
 * @since Nov 23 2016 - NEW!
 */
public class AdapterRecentKeyword extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "AreaAdapter";

    private List<String> stringList = new ArrayList<>();
    private final OnClickRecyclerItem mListener;

    // Row type, just prepare for various item type
    private final int TYPE_REGULAR = 0;
    private final int TYPE_MORE = 1;

    public AdapterRecentKeyword(List<String> items, OnClickRecyclerItem listener) {
        stringList = items;
        mListener = listener;
    }

    public List<String> getAdapterData() {
        return stringList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder mViewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_MORE:
                View moreView = inflater.inflate(R.layout.row_list_more, parent, false);
                mViewHolder = new MoreItemAreaViewHolder(moreView);
                break;
            default:
                View itemView = inflater.inflate(R.layout.row_single_text, parent, false);
                mViewHolder = new ItemAreaViewHolder(itemView);
                break;
        }

        return mViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (stringList.get(position).equals("H")) {
            return TYPE_MORE;
        } else {
            return TYPE_REGULAR;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == TYPE_MORE) {

            configureMoreItem((MoreItemAreaViewHolder) holder, position);

        } else if (holder.getItemViewType() == TYPE_REGULAR) {

            configureRegularItem((ItemAreaViewHolder) holder, position);

        }

    }

    private void configureRegularItem(ItemAreaViewHolder holder, int position) {
        String keyword = stringList.get(position);

        holder.mContentView.setText(keyword);
    }

    @Override
    public int getItemCount() {
        if (stringList != null && !stringList.isEmpty())
            return stringList.size();
        else
            return 0;
    }

    private void configureMoreItem(final MoreItemAreaViewHolder holder, int position) {
        final Context context = holder.mView.getContext();

        String keyword = stringList.get(position);

        holder.mHeaderTextView.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        holder.mHeaderImageView.setVisibility(View.VISIBLE);

        // When item state is expanded (indicate network load is succeed)
        // set this item position as a header for next items.
        if (keyword.equals("H")) {

            holder.mView.setClickable(false);
            holder.mHeaderTextView.setText(keyword);
            holder.mHeaderImageView.animate().rotation(90);

            // User click "More" item, then change it to "Loading..."
        }
    }

    /**
     * Search item view to hold search sesult.
     */
    private class ItemAreaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final View mView;
        final TextView mContentView;

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

        MoreItemAreaViewHolder(View view) {
            super(view);
            mView = view;
            mHeaderTextView = (TextView) view.findViewById(R.id.textViewShowMore);
            mHeaderImageView = (ImageView) view.findViewById(R.id.imageViewRightButton);

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
