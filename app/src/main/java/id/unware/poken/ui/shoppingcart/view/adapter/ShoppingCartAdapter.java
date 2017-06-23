package id.unware.poken.ui.shoppingcart.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductImage;
import id.unware.poken.domain.Seller;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingcart.presenter.IShoppingCartPresenter;


public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private Context context;
    private List<ShoppingCart> listData;
    private IShoppingCartPresenter presenter;

    public ShoppingCartAdapter(Context context, List<ShoppingCart> items, IShoppingCartPresenter listener) {
        this.context = context;
        this.listData = items;
        this.presenter = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_shopping_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ShoppingCart item = listData.get(position);
        if (item == null) return;

        // Set item val for view holder
        holder.itemValue = item;

        Product product = item.product;
        if (product == null) return;

        Seller seller = product.seller;
        if (seller == null) return;

        ArrayList<ProductImage> images = product.images;
        if (images.isEmpty()) return;

        final long shoppingCartId = item.id;
        String storeName = seller.store_name;
        String productImage = images.get(0).path;
        String productName = product.name;
        final double productPrice = product.price;
        final int productStock = product.stock;

        // Product image thumbnail size
        int imageSize = context.getResources().getDimensionPixelSize(R.dimen.clickable_size_64);
        holder.tvStoreName.setText(storeName);
        Picasso.with(context)
                .load(productImage)
                .resize(imageSize, imageSize)
                .centerCrop()
                .into(holder.ivProductImage);
        holder.tvProductName.setText(productName);
        holder.tvProductTotalPrice.setText(StringUtils.formatCurrency(String.valueOf(productPrice)));
        holder.textItemQuantity.setText(
                String.valueOf(item.quantity)
        );

        holder.cbSelectAllStoreItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (presenter != null) {
                    presenter.onItemChecked(holder.getAdapterPosition(), isChecked, shoppingCartId, item.quantity, productPrice, item);
                }
            }
        });

        holder.btnAddQuantity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.quantity = holder.controlItemQuantity(item.quantity, productStock, true);
                        Utils.Log("ShoppingCartAdapter", "[add] Q: " + item.quantity + ", stok: " + productStock);
                        holder.textItemQuantity.setText(
                                String.valueOf(item.quantity)
                        );

                        // Change shopping cart counter on list page
                        if (presenter != null) {
                            presenter.onItemQuantityChanges(holder.getAdapterPosition(), shoppingCartId, item.quantity, productPrice, item);
                        }
                    }
                }
        );

        holder.btnSubstractQuantity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.quantity = holder.controlItemQuantity(item.quantity, productStock, false);
                        Utils.Log("ShoppingCartAdapter", "[substract] Q: " + item.quantity + ", stok: " + productStock);
                        holder.textItemQuantity.setText(
                                String.valueOf(item.quantity)
                        );

                        // Change shopping cart counter on list page
                        if (presenter != null) {
                            presenter.onItemQuantityChanges(holder.getAdapterPosition(), shoppingCartId, item.quantity, productPrice, item);
                        }
                    }
                }
        );

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ShoppingCart itemValue;

        @BindView(R.id.cbSelectAllStoreItem) CheckBox cbSelectAllStoreItem;
        @BindView(R.id.ivStoreAvatar) ImageView ivStoreAvatar;
        @BindView(R.id.tvStoreName) TextView tvStoreName;
        @BindView(R.id.ivProductImage) ImageView ivProductImage;
        @BindView(R.id.tvProductName) TextView tvProductName;
        @BindView(R.id.btnDeleteCartItem) ImageButton btnDeleteCartItem;
        @BindView(R.id.tvProductTotalPrice) TextView tvProductTotalPrice;

        // ITEM QUANTITY
        @BindView(R.id.parentQuantityControl) CardView parentQuantityControl;
        @BindView(R.id.btnAddQuantity) ImageButton btnAddQuantity;
        @BindView(R.id.btnSubstractQuantity) ImageButton btnSubstractQuantity;
        @BindView(R.id.textItemQuantity) TextView textItemQuantity;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            btnDeleteCartItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnDeleteCartItem) {
                ControllerDialog.getInstance().showYesNoDialog(
                    context.getString(R.string.msg_shopping_cart_confirm_deletion),
                    context,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (DialogInterface.BUTTON_POSITIVE == which) {
                                if (presenter != null) {
                                    // Delete cart item
                                    presenter.deleteShoppingCartItem(
                                            getAdapterPosition(),
                                            itemValue.id);
                                }
                            }
                        }
                    },
                    context.getString(R.string.btn_shopping_cart_confirm_deletion),  // YES
                    context.getString(R.string.btn_negative_cancel)  // NO
                );

            }
        }

        private int controlItemQuantity(int currentQuantity, int maxQuantity, boolean isAdd) {
            if (isAdd && currentQuantity < maxQuantity) {
                currentQuantity = currentQuantity + 1;
            } else if (!isAdd && currentQuantity > 1) {
                currentQuantity = currentQuantity - 1;
            }

            return currentQuantity;
        }
    }
}
