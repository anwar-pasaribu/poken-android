package id.unware.poken.ui.customercollection.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.CustomerCollection;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.ui.customercollection.presenter.ICustomerCollectionPresenter;

public class CustomerCollectionAdapter extends RecyclerView.Adapter<CustomerCollectionAdapter.ViewHolder> {

    private final List<CustomerCollection> mValues;
    private final ICustomerCollectionPresenter mListener;

    public CustomerCollectionAdapter(List<CustomerCollection> items, ICustomerCollectionPresenter listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_collected_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        CustomerCollection item = mValues.get(position);

        String  strProductImageUrl = item.product_image,
                strProductName = item.product_name;
        double productPrice = item.product_price;

        Picasso.with(holder.itemView.getContext())
                .load(strProductImageUrl)
                .resize(holder.productImageSizeM, holder.productImageSizeM)
                .centerCrop()
                .into(holder.itemImage);

        holder.tvTitle.setText(strProductName);
        holder.tvPrice.setText(StringUtils.formatCurrency(String.valueOf(productPrice)));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.startDetailScreen(holder.mItem);
                }
            }
        });

        holder.ibRemoveCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.removeCollection(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemImage) ImageView itemImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvPrice) TextView tvPrice;
        @BindView(R.id.ibRemoveCollection) ImageButton ibRemoveCollection;

        @BindDimen(R.dimen.img_grid_m) int productImageSizeM;

        public final View mView;
        public CustomerCollection mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
