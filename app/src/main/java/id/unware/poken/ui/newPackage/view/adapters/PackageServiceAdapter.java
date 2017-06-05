package id.unware.poken.ui.newPackage.view.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.GeneralListItem;

/**
 * Provide horizontal recyler view for Package service selection
 *
 * @author Anwar Pasaribu
 * @since Feb 08 2017 - NEW
 */

public class PackageServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "PackageServiceAdapter";

    private Context mContext;
    private List<Object> mList;
    private OnClickRecyclerItem mListener;

    public PackageServiceAdapter(Context context, List<Object> list, OnClickRecyclerItem listener) {
        this.mContext = context;
        this.mList = list;
        this.mListener = listener;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textViewContent) TextView textViewContent;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(this.mContext)
                .inflate(R.layout.row_package_service, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        configureRegularItem(itemViewHolder, position);
    }

    private void configureRegularItem(ItemViewHolder itemViewHolder, int position) {
        GeneralListItem item = (GeneralListItem) this.mList.get(position);

        GradientDrawable drawable = (GradientDrawable) itemViewHolder.textViewContent.getBackground();

        int intColor;
        if (item.isSelected()) {
            itemViewHolder.textViewContent.setTypeface(Typeface.DEFAULT_BOLD);
            intColor = ContextCompat.getColor(this.mContext, R.color.white_100);

            // REG
            if (item.getId() == 0) {
                drawable.setColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            } else if (item.getId() == 1) {
                drawable.setColor(ContextCompat.getColor(mContext, R.color.purple));
            } else {
                drawable.setColor(ContextCompat.getColor(mContext, R.color.green));
            }

        } else {
            itemViewHolder.textViewContent.setTypeface(Typeface.DEFAULT);
            intColor = ContextCompat.getColor(this.mContext, R.color.black_90);

            // Set badge color to transparent
            drawable.setColor(ContextCompat.getColor(mContext, R.color.separator_view_lighter));
        }

        itemViewHolder.textViewContent.setTextColor(intColor);

        itemViewHolder.textViewContent.setText(item.getTitle());
    }


    @Override
    public int getItemCount() {
        return this.mList != null? this.mList.size() : -1;
    }
}
