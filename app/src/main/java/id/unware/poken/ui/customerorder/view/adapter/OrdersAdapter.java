package id.unware.poken.ui.customerorder.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDate;
import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.models.OrderStatus;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.customerorder.presenter.IOrdersPresenter;
import id.unware.poken.ui.customerorder.view.OrdersFragment.OnOrderFragmentListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private final List<ShoppingOrder> mValues;
    private final IOrdersPresenter mListener;

    public OrdersAdapter(List<ShoppingOrder> items, IOrdersPresenter listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_customer_orders, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        ShoppingOrder shoppingOrder = mValues.get(position);

        if (shoppingOrder == null) return;

        OrderDetail orderDetail = shoppingOrder.order_details;

        if (orderDetail == null) return;

        AddressBook addressBook = orderDetail.address_book;
        Shipping shipping = orderDetail.shipping;
        ArrayList<ShoppingCart> shoppingCarts = shoppingOrder.shopping_carts;

        if (shoppingCarts.isEmpty()) return;

        Product product = shoppingCarts.get(0).product;

        double totalPrice = product.price * shoppingCarts.get(0).quantity;

        Utils.Log("OrderesAdapter", "Clickable size: " + holder.clickableSize64);

        Picasso.with(holder.itemView.getContext())
                .load(product.images.get(0).path)
                .resize(holder.clickableSize64, holder.clickableSize64)
                .centerCrop()
                .into(holder.ivProductImage);

        holder.tvProductName.setText(String.valueOf(product.name));
        holder.tvProductQuantity.setText(holder.itemView.getContext().getString(R.string.lbl_quantity, shoppingCarts.get(0).quantity));
        holder.tvProductTotalPrice.setText(StringUtils.formatCurrency(String.valueOf(totalPrice)));
        holder.tvOrderStatus.setText(OrderStatus.getOrderStatusText(shoppingOrder.status));
        holder.tvOrderStatusUpdatedOn.setText(ControllerDate.getInstance().getShortDateWithHourFormat(orderDetail.date));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.startOrderDetailScreen(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProductImage) ImageView ivProductImage;
        @BindView(R.id.tvProductName) TextView tvProductName;
        @BindView(R.id.tvProductQuantity) TextView tvProductQuantity;
        @BindView(R.id.tvProductTotalPrice) TextView tvProductTotalPrice;
        @BindView(R.id.tvOrderStatus) TextView tvOrderStatus;
        @BindView(R.id.tvOrderStatusUpdatedOn) TextView tvOrderStatusUpdatedOn;

        @BindDimen(R.dimen.clickable_size_64) int clickableSize64;

        public final View mView;
        public ShoppingOrder mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }
}
