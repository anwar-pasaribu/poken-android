package id.unware.poken.ui.pickup.history.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.GeneralListItem;
import id.unware.poken.tools.Utils;


public class AdapterPickupDetail extends RecyclerView.Adapter<AdapterPickupDetail.ViewHolder> {

    private List<GeneralListItem> generalListItems;
    private OnClickRecyclerItem listener;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageViewListIcon) ImageView imageViewListIcon;
        @BindView(R.id.textViewTitle) TextView textViewTitle;
        @BindView(R.id.textViewContent) TextView textViewContent;
        @BindView(R.id.imageButtonAction) ImageButton imageButtonAction;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            imageButtonAction.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }


    public AdapterPickupDetail(List<GeneralListItem> listData, OnClickRecyclerItem listener) {
        this.generalListItems = listData;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vhItem = new ViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.row_pickup_history_detail, parent, false));
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GeneralListItem item = generalListItems.get(position);

        if (item.getListIcon() == 0) {
            // Set Vendor Image
            Picasso.with(holder.imageViewListIcon.getContext())
                    .load(item.getStrListIcon())
                    .into(holder.imageViewListIcon);
        } else {
            holder.imageViewListIcon.setImageResource(item.getListIcon());
        }

        holder.textViewTitle.setText(item.getTitle());
        holder.textViewContent.setText(item.getContent());

        configureListItemAction(holder, item);
    }

    private void configureListItemAction(ViewHolder holder, GeneralListItem item) {
        if (Utils.isEmpty(item.getActionUri())) {
            holder.imageButtonAction.setVisibility(View.GONE);
        } else {
            holder.imageButtonAction.setVisibility(View.VISIBLE);
        }

        holder.imageButtonAction.setImageResource(item.getActionIcon());
    }

    @Override
    public int getItemCount() {
        return generalListItems.size();
    }

}