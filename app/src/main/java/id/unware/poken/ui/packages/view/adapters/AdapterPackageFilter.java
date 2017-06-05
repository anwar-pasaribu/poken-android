package id.unware.poken.ui.packages.view.adapters;

import android.content.Context;
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


public class AdapterPackageFilter extends RecyclerView.Adapter<AdapterPackageFilter.ViewHolder> {

    private List<GeneralListItem> mListData;
    private OnClickRecyclerItem mListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txtTitle) TextView textViewTitle;
        @BindView(R.id.textViewBadgeCount) TextView textViewBadgeCount;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setClickable(true);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public AdapterPackageFilter(List<GeneralListItem> list, OnClickRecyclerItem listener) {
        this.mListData = list;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vhItem = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_package_filter, parent, false));
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Context ctx = holder.itemView.getContext();

        if (position > -1) {
            GeneralListItem item = mListData.get(position);
            GradientDrawable drawable = (GradientDrawable) holder.textViewBadgeCount.getBackground();
            drawable.setColor(item.isSelected()
                    ? ContextCompat.getColor(ctx, R.color.colorAccent)
                    : ContextCompat.getColor(ctx, R.color.colorPrimaryDark));

            holder.textViewTitle.setTextColor(item.isSelected()
                    ? ContextCompat.getColor(ctx, R.color.colorAccent)
                    : ContextCompat.getColor(ctx, R.color.black_90));

            holder.textViewTitle.setText(item.getTitle());
            holder.textViewBadgeCount.setText(String.valueOf(item.getBadgeCount()));
        }
    }


    @Override
    public int getItemCount() {
        return mListData != null ? mListData.size() : -1;
    }



}