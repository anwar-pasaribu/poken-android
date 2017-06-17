package id.unware.poken.ui.customersubscription.view.adapter;

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
import id.unware.poken.domain.CustomerSubscription;
import id.unware.poken.ui.customersubscription.presenter.ICustomerSubscriptionPresenter;

public class CustomerSubscriptionAdapter extends RecyclerView.Adapter<CustomerSubscriptionAdapter.ViewHolder> {

    private final List<CustomerSubscription> mValues;
    private final ICustomerSubscriptionPresenter mListener;

    public CustomerSubscriptionAdapter(List<CustomerSubscription> items, ICustomerSubscriptionPresenter listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_customer_subscription, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        CustomerSubscription item = mValues.get(position);

        String  strSellerImageUrl = item.seller_profile_pic,
                strSellerName = item.seller_name,
                strSellerTagLine = item.seller_tag_line,
                strSellerLocation = item.seller_location;

        Picasso.with(holder.itemView.getContext())
                .load(strSellerImageUrl)
                .placeholder(R.drawable.ic_circle_24dp)
                .error(R.drawable.ic_circle_24dp)
                .resize(holder._64dp, holder._64dp)
                .centerCrop()
                .into(holder.itemImage);

        holder.tvSellerName.setText(strSellerName);
        holder.tvSellerTagLine.setText(strSellerTagLine);
        holder.tvSellerLocation.setText(strSellerLocation);

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
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemImage) ImageView itemImage;
        @BindView(R.id.tvSellerName) TextView tvSellerName;
        @BindView(R.id.tvSellerTagLine) TextView tvSellerTagLine;
        @BindView(R.id.tvSellerLocation) TextView tvSellerLocation;

        @BindDimen(R.dimen.clickable_size_64) int _64dp;

        public final View mView;
        public CustomerSubscription mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
