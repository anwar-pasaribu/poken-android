package id.unware.poken.ui.shoppingcart.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductImage;
import id.unware.poken.domain.Seller;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.jnews.dummy.PojoNews.News;
import id.unware.poken.ui.shoppingcart.presenter.IShoppingCartPresenter;

import static com.google.android.gms.internal.zznu.ii;
import static id.unware.poken.R.id.spinnerBanks;


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
        ShoppingCart item = listData.get(position);
        if (item == null) return;

        // Set item val for view holder
        holder.itemValue = item;

        Product product = item.product;
        if (product == null) return;

        Seller seller = product.seller;
        if (seller == null) return;

        ArrayList<ProductImage> images = product.images;
        if (images.isEmpty()) return;

        long shoppingCartId = item.id;
        int quantity = item.quantity;
        String storeName = seller.store_name;
        String productImage = images.get(0).path;
        String productName = product.name;
        double productPrice = product.price;
        int productStock = product.stock;

        // Product image thumbnail size
        int imageSize = context.getResources().getDimensionPixelSize(R.dimen.clickable_size_64);
        Utils.Log("Image size: ", imageSize + " pixels.");
        holder.tvStoreName.setText(storeName);
        Picasso.with(context)
                .load(productImage)
                .resize(imageSize, imageSize)
                .centerCrop()
                .into(holder.ivProductImage);
        holder.tvProductName.setText(productName);
        holder.tvProductTotalPrice.setText(StringUtils.formatCurrency(String.valueOf(productPrice)));

        // Setup spinner
        ArrayList<String> quantityOptionsString = new ArrayList<>();
        int quantityLimit = Math.min(10, productStock);
        int quantityIndex = quantity - 1;
        for (int i = 1; i <= quantityLimit; i++) {
            quantityOptionsString.add(String.valueOf(i));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, quantityOptionsString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setNotifyOnChange(true);
        holder.spinnerQuantity.setSelection(quantityIndex);
        holder.spinnerQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Utils.Log("ShoppingCartAdapter", "Selected quantity: " + position);
                Utils.Log("ShoppingCartAdapter", "Item view: " + view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.Log("ShoppingCartAdapter", " Nothing selected");
            }
        });
        holder.spinnerQuantity.setAdapter(adapter);
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
        @BindView(R.id.spinnerQuantity) Spinner spinnerQuantity;
        @BindView(R.id.tvProductTotalPrice) TextView tvProductTotalPrice;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            btnDeleteCartItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (presenter != null) {
                if (v.getId() == R.id.btnDeleteCartItem) {

                    // Delete cart item
                    presenter.deleteShoppingCartItem(itemValue.id);
                }
            }
        }
    }
}
