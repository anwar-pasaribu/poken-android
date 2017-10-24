package id.unware.poken.ui.browse.view.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Seller;
import id.unware.poken.tools.glide.GlideRequest;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.browse.presenter.IBrowsePresenter;
import id.unware.poken.ui.home.presenter.IHomePresenter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 * Seller item on home screen.
 */
public class SellerListAdapter extends RecyclerView.Adapter<SellerListAdapter.SingleItemRowHolder> {

    private ArrayList<Seller> itemsList;
    private Context mContext;
    private IBrowsePresenter homePresenter;
    private final GlideRequest<Drawable> requestBuilder;

    public SellerListAdapter(Context context,
                             ArrayList<Seller> itemsList,
                             IBrowsePresenter presenter,
                             GlideRequests glideRequest) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.homePresenter = presenter;
        this.requestBuilder = glideRequest.asDrawable().fitCenter();
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.mContext).inflate(R.layout.list_featured_seller, viewGroup, false);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int position) {

        final Seller singleItem = itemsList.get(position);

        holder.tvTitle.setText(singleItem.store_name);
        holder.tvDescription.setText(singleItem.tag_line);
        holder.featuredSellerTvAddress.setText(singleItem.location.city);

        this.requestBuilder
                .clone()
                .load(singleItem.store_avatar)
                .circleCrop()
                .placeholder(R.drawable.ic_store_black_24dp)
                .into(holder.featuredSellerIvAvatar);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePresenter.onSellerClick(holder.getAdapterPosition(), singleItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvDescription) TextView tvDescription;
        @BindView(R.id.featuredSellerTvAddress) TextView featuredSellerTvAddress;
        @BindView(R.id.featuredSellerIvAvatar) ImageView featuredSellerIvAvatar;

        @BindDimen(R.dimen.item_gap_l) int itemGapL;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}