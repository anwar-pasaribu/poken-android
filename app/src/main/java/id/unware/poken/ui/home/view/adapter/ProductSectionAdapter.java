package id.unware.poken.ui.home.view.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideRequest;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.home.presenter.IHomePresenter;

/**
 * Horizontal adapter for products
 * @author Anwar Pasaribu
 */
public class ProductSectionAdapter extends RecyclerView.Adapter<ProductSectionAdapter.SingleItemRowHolder> {

    private ArrayList<Product> itemsList;
    private Context mContext;
    private IHomePresenter homePresenter;
    private final GlideRequest<Drawable> requestBuilder;

    public ProductSectionAdapter(Context context, ArrayList<Product> itemsList, IHomePresenter homePresenter, GlideRequests glideRequest) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.homePresenter = homePresenter;
        requestBuilder = glideRequest.asDrawable().fitCenter();
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.mContext).inflate(R.layout.list_single_card_product, viewGroup, false);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {

        final Product item = itemsList.get(position);
        String  strProductImageUrl = String.valueOf(item.images.get(0).thumbnail),
                strProductName = item.name;
        double  productPrice = item.price,
                discountAmount = item.discount_amount,
                discountedPrice = productPrice - ((productPrice * discountAmount) / 100);
        final String formattedProductPrice = StringUtils.formatCurrency((String.valueOf(productPrice)));

        requestBuilder
                .clone()
                .load(strProductImageUrl)
                .into(holder.itemImage);

        holder.tvTitle.setText(strProductName);
        holder.tvPrice.setText(formattedProductPrice);

        // tvPrice2 to show SALE item
        holder.tvPrice2.setText(StringUtils.formatCurrency(String.valueOf(productPrice)));
        holder.tvPrice2.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);  // Strike
        holder.tvDiscountedPrice.setText(StringUtils.formatCurrency(String.valueOf(discountedPrice)));
        holder.tvDiscountAmount.setText((int) discountAmount + "%");

        // Discount view
        if (discountAmount > 0D) {
            holder.viewFlipperProductPrice.setDisplayedChild(0);
        } else {
            holder.viewFlipperProductPrice.setDisplayedChild(1);
        }

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

        @BindView(R.id.itemImage) ImageView itemImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvPrice) TextView tvPrice;

        // Sale item
        @BindView(R.id.tvPrice2) TextView tvPrice2;
        @BindView(R.id.tvDiscountedPrice) TextView tvDiscountedPrice;
        @BindView(R.id.tvDiscountAmount) TextView tvDiscountAmount;
        @BindView(R.id.viewFlipperProductPrice) ViewFlipper viewFlipperProductPrice;

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