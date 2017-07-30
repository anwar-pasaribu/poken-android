package id.unware.poken.ui.seller.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.ui.seller.presenter.ISellerPagePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 17 2017
 */

public class SellerProductAdapter extends RecyclerView.Adapter<SellerProductAdapter.ViewHolder> {

    private final List<Product> mValues;
    private final ISellerPagePresenter mListener;

    public SellerProductAdapter(List<Product> items, ISellerPagePresenter listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_seller_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product item = mValues.get(position);

        String  strProductImageUrl = String.valueOf(item.images.get(0).path),
                strProductName = item.name;
        double productPrice = item.price;

        Picasso.with(holder.itemView.getContext())
                .load(strProductImageUrl)
                .resize(holder.productImageSizeM, holder.productImageSizeM)
                .centerCrop()
                .into(holder.itemImage);

        holder.tvTitle.setText(strProductName);
        holder.tvPrice.setText(StringUtils.formatCurrency(String.valueOf(productPrice)));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.parentSellerProduct) CardView parentSellerProduct;
        @BindView(R.id.itemImage) ImageView itemImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvPrice) TextView tvPrice;

        @BindDimen(R.dimen.img_grid_m) int productImageSizeM;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            parentSellerProduct.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.startDetailScreen(mValues.get(getAdapterPosition()));
            }
        }
    }
}
