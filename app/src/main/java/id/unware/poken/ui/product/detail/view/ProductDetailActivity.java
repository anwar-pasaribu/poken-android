package id.unware.poken.ui.product.detail.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductImage;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.product.detail.model.ProductDetailModel;
import id.unware.poken.ui.product.detail.presenter.ProductDetailPresenter;
import id.unware.poken.ui.shoppingcart.view.ShoppingCartActivity;

public class ProductDetailActivity extends AppCompatActivity implements IProductDetailView {

    private static final String TAG = "ProductDetailActivity";

    @BindView(R.id.swipeRefreshLayoutParent) SwipeRefreshLayout swipeRefreshLayoutParent;

    @BindView(R.id.ivProduct) ImageView ivProduct;
    @BindView(R.id.tvProductName) TextView tvProductName;
    @BindView(R.id.tvProductPrice) TextView tvProductPrice;
    @BindView(R.id.tvProductStock) TextView tvProductStock;
    @BindView(R.id.tvProductSold) TextView tvProductSold;
    @BindView(R.id.tvProductLeft) TextView tvProductLeft;
    @BindView(R.id.tvProductDescription) TextView tvProductDescription;
    @BindView(R.id.ivSellerAvatar) ImageView ivSellerAvatar;
    @BindView(R.id.tvSellerName) TextView tvSellerName;
    @BindView(R.id.tvSellerAddress) TextView tvSellerAddress;

    // BUY
    @BindView(R.id.btnBuy) Button btnBuy;

    private long productId;

    private Unbinder unbinder;

    private ProductDetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        unbinder = ButterKnife.bind(this);

        presenter = new ProductDetailPresenter(new ProductDetailModel(), this);

        if (getIntent().getExtras() != null) {
            productId = getIntent().getExtras().getLong(Product.KEY_PRODUCT_ID, -1L);
            Utils.Log(TAG, "Product ID from intent: " + productId);
        }

        // Load product detail
        presenter.getProductData(productId);

        // Add product to Shopping Cart
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.onBuyNow(productId);
                }
            }
        });

        swipeRefreshLayoutParent.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    presenter.getProductData(productId);
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void openShoppingCartAndInsertProduct(long productId) {
        Intent shoppingCartIntent = new Intent(this, ShoppingCartActivity.class);
        shoppingCartIntent.putExtra(Product.KEY_PRODUCT_ID, productId);
        this.startActivity(shoppingCartIntent);
    }

    @Override
    public void populateProductImage(ArrayList<ProductImage> productImages) {
        Picasso.with(this)
                .load(String.valueOf(productImages.get(0).path))
                .into(ivProduct);
    }

    @Override
    public void populateProductGeneralInfo(Product product) {

        this.setTitle(String.valueOf(product.name));

        tvProductName.setText(String.valueOf(product.name));
        tvProductDescription.setText(String.valueOf(product.description));
        tvProductStock.setText(String.valueOf(product.stock));

        // Store info
        tvSellerName.setText(product.seller.store_name);
        tvSellerAddress.setText(product.seller.location);
    }

    @Override
    public void updateProductPrice(String formattedPrice) {
        tvProductPrice.setText(formattedPrice);
    }

    @Override
    public void showShoppingCartScreen(ShoppingCart shoppingCart) {
        openShoppingCartAndInsertProduct(productId);
    }

    @Override
    public void showViewState(UIState uiState) {
        Utils.Logs('i', TAG, "View state: " + String.valueOf(uiState));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
