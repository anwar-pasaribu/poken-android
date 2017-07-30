package id.unware.poken.ui.customerorder.view.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.BindViews;
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

    private static final String TAG = "OrdersAdapter";

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

        int orderDetailStatus = orderDetail.order_status;

        AddressBook addressBook = orderDetail.address_book;
        Shipping shipping = orderDetail.shipping;
        ArrayList<ShoppingCart> shoppingCarts = shoppingOrder.shopping_carts;

        if (shoppingCarts.isEmpty()) return;

        // First ordered product
        Product product = shoppingCarts.get(0).product;
        int totalProductCount = 0;
        double totalPrice = shoppingOrder.total_shopping;
        Picasso.with(holder.itemView.getContext())
                .load(product.images.get(0).path)
                .resize(holder.clickableSize64, holder.clickableSize64)
                .centerCrop()
                .into(holder.ivProductImage);

        // Count all ordered product
        for (ShoppingCart sc : shoppingCarts) {
            totalProductCount += sc.quantity;
        }

        // Setup mutli ordered products
        if (shoppingCarts.size() > 1) {
            int orderedProductsCount = Math.min(shoppingCarts.size() - 1, 3);

            holder.parentOtherOrderedProduct.setVisibility(View.VISIBLE);

            holder.customerOrderTvOtherProducts.setText("Total ".concat(String.valueOf(shoppingCarts.size())).concat(" jenis barang."));

            for (int i = 1; i <= orderedProductsCount; i++) {

                holder.ivOtherProductImages.get(i - 1).setVisibility(View.VISIBLE);

                String imageUrl = shoppingCarts.get(i).product.images.get(0).path;
                if (!StringUtils.isEmpty(imageUrl)) {

                    Picasso.with(holder.itemView.getContext())
                            .load(imageUrl)
                            .resize(holder.clickableSize32, holder.clickableSize32)
                            .centerCrop()
                            .into(holder.ivOtherProductImages.get(i - 1));
                }
            }

        } else {
            holder.parentOtherOrderedProduct.setVisibility(View.GONE);
        }

        holder.tvProductName.setText(String.valueOf(product.name));
        holder.tvProductQuantity.setText(holder.itemView.getContext().getString(R.string.lbl_quantity, totalProductCount));
        holder.tvProductTotalPrice.setText(StringUtils.formatCurrency(String.valueOf(totalPrice)));
        holder.tvOrderStatus.setText(OrderStatus.getOrderStatusText(orderDetailStatus));
        holder.tvOrderStatusUpdatedOn.setText(ControllerDate.getInstance().getShortDateWithHourFormat(orderDetail.date));

        // TOGGLE PAYMENT REQUIRED BANNER VISIBILITY
        if (orderDetailStatus == OrderStatus.ORDERED) {
            holder.parentInfoPaymentRequired.setVisibility(View.VISIBLE);
            String expireDateTime = holder.itemView.getContext().getString(R.string.lbl_order_payment_expires,
                    ControllerDate.getInstance().getShortDateWithHourFormat(orderDetail.payment_expiration_date)
            );
            holder.tvSubtitle.setText(expireDateTime);
        } else {
            holder.parentInfoPaymentRequired.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.parentOrderedProduct) CardView parentOrderedProduct;

        @BindView(R.id.ivProductImage) ImageView ivProductImage;
        @BindView(R.id.tvProductName) TextView tvProductName;
        @BindView(R.id.tvProductQuantity) TextView tvProductQuantity;
        @BindView(R.id.tvProductTotalPrice) TextView tvProductTotalPrice;
        @BindView(R.id.tvOrderStatus) TextView tvOrderStatus;
        @BindView(R.id.tvOrderStatusUpdatedOn) TextView tvOrderStatusUpdatedOn;

        // OTHER ORDERED PRODUCTS
        @BindView(R.id.parentOtherOrderedProduct) LinearLayout parentOtherOrderedProduct;
        @BindViews({R.id.ivOtherProductImage1, R.id.ivOtherProductImage2, R.id.ivOtherProductImage3}) List<ImageView> ivOtherProductImages;
        @BindView(R.id.customerOrderTvOtherProducts) TextView customerOrderTvOtherProducts;

        // PAYMENT REQUIRED INDICATOR
        @BindView(R.id.parentInfoPaymentRequired) CardView parentInfoPaymentRequired;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvSubtitle) TextView tvSubtitle;

        @BindDimen(R.dimen.clickable_size_64) int clickableSize64;
        @BindDimen(R.dimen.clickable_size_32) int clickableSize32;

        public ShoppingOrder mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            parentOrderedProduct.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.startOrderDetailScreen(mValues.get(getAdapterPosition()));
            }
        }
    }
}
