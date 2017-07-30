package id.unware.poken.ui.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.home.presenter.IHomePresenter;

/**
 * Horizontal adapter for products
 * @author Anwar Pasaribu
 */
public class ProductSectionAdapter extends RecyclerView.Adapter<ProductSectionAdapter.SingleItemRowHolder> {

    private ArrayList<Product> itemsList;
    private Context mContext;
    private IHomePresenter homePresenter;

    public ProductSectionAdapter(Context context, ArrayList<Product> itemsList, IHomePresenter homePresenter) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.homePresenter = homePresenter;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.mContext).inflate(R.layout.list_single_card_product, viewGroup, false);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {

        final Product singleItem = itemsList.get(position);
        final double productPrice = singleItem.price;
        final String formattedProductPrice = StringUtils.formatCurrency((String.valueOf(productPrice)));

        holder.tvTitle.setText(singleItem.name);
        holder.tvPrice.setText(formattedProductPrice);

        Picasso.with(mContext)
                .load(singleItem.images.get(0).path)
                .resize(holder.productImageSizeM, holder.productImageSizeM)
                .centerCrop()
                .into(holder.itemImage);

        if (position == 0) {
            RecyclerView.LayoutParams recyclerViewLayoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            recyclerViewLayoutParams.leftMargin = holder.itemGapL;
        } else if (position == itemsList.size() - 1) {
            RecyclerView.LayoutParams recyclerViewLayoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            recyclerViewLayoutParams.rightMargin = holder.itemGapL;
        }
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.productParent) ViewGroup productParent;

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvPrice) TextView tvPrice;

        @BindView(R.id.itemImage) ImageView itemImage;

        @BindDimen(R.dimen.img_grid_m) int productImageSizeM;
        @BindDimen(R.dimen.item_gap_l) int itemGapL;


        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            productParent.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (homePresenter != null) {

                homePresenter.onProductClick(
                        getAdapterPosition(),
                        itemsList.get(getAdapterPosition())
                );
            }
        }
    }

}