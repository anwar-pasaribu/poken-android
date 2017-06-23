package id.unware.poken.ui.home.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        for (Product product : itemsList) {
        Utils.Logs('v', "ProductSectionAdapter", "Product name: " + product.name);
        }
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card_product, null);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, final int position) {

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
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvPrice) TextView tvPrice;

        @BindView(R.id.itemImage) ImageView itemImage;

        @BindDimen(R.dimen.img_grid_m) int productImageSizeM;


        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
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