package id.unware.poken.ui.product.detail.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.product.detail.model.ProductDetailModel;
import id.unware.poken.ui.product.detail.presenter.ProductDetailPresenter;
import id.unware.poken.ui.product.detail.view.fragment.FragmentDialogShippings;
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

    // SHIPPING OPTIONS
    @BindView(R.id.parentClickableShippingMethod) ViewGroup parentClickableShippingMethod;
    @BindView(R.id.tvCurierName) TextView tvCurierName;
    @BindView(R.id.tvCurierService) TextView tvCurierService;
    @BindView(R.id.productDetailIbMoreShipping) ImageButton productDetailIbMoreShipping;

    private long productId;

    // SAVE LAST SELECTED SHIPPING OPTION INDEX FROM LIST
    private int selectedShippingOptionsIndex = -1;
    private Shipping selectedShipping;

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
        presenter.getShippingOptionData(productId);  // Init Shipping option

        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Add product to Shopping Cart
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    long shippingId = selectedShipping != null ? selectedShipping.id : 3;
                    presenter.onBuyNow(shippingId, productId);
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

        productDetailIbMoreShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.startShippingOptionsScreen();
                }
            }
        });
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
    public void showDefaultShippingOption(Shipping shipping) {
        // NO Shipping method selected
        if (shipping != null
                && selectedShipping == null
                && selectedShippingOptionsIndex == -1) {

            selectedShipping = shipping;
            selectedShippingOptionsIndex = 0;

            setupSelectedShippingMethodView(selectedShipping);

        }
    }

    @Override
    public void showShippingOptionsScreen(boolean isCod, ArrayList<Shipping> shippings) {

        showDialogShippingOptions(isCod, shippings);

    }

    @Override
    public void populateShippingOptionsScreen(ArrayList<Shipping> shippings) {

    }

    @Override
    public void showViewState(UIState uiState) {
        Utils.Logs('i', TAG, "View state: " + String.valueOf(uiState));
        switch (uiState) {
            case LOADING:
                // swipeRefreshLayoutParent.setRefreshing(true);
                Utils.Log(TAG, "Loading ");
                break;
            case FINISHED:
                swipeRefreshLayoutParent.setRefreshing(false);
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            Utils.Log(TAG, "Home navigation clicked.");
            this.onBackPressed();
            return true; // To make sure no more
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private FragmentTransaction hideDialog(String strTag) {

        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        android.support.v4.app.Fragment prev = getSupportFragmentManager().findFragmentByTag(strTag);

        if (prev != null) {
            Utils.Log(TAG, "Prev fragment is not null. Tag: " + strTag);

            ((DialogFragment) prev).dismiss();
            ft.remove(prev);
        }

        return ft;
    }

    private void showDialogShippingOptions(boolean isCod, ArrayList<Shipping> shippings) {
        try {

            String strPackagesTag = "dialog_shippings_options";

            FragmentTransaction ft = hideDialog(strPackagesTag);
            ft.addToBackStack(null);

            FragmentDialogShippings shippingOptionDialog = FragmentDialogShippings.newInstance(
                    isCod,
                    productId,
                    selectedShippingOptionsIndex
            );
            shippingOptionDialog.setupListener(new FragmentDialogShippings.OnShippingOptionDialogListener() {
                @Override
                public void onShippingOptionSelected(int pos, Shipping shipping) {
                    selectedShippingOptionsIndex = pos;
                    selectedShipping = shipping;
                    setupSelectedShippingMethodView(shipping);
                }
            });
            shippingOptionDialog.show(ft, strPackagesTag);

        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private void setupSelectedShippingMethodView(Shipping shipping) {
        tvCurierName.setText(shipping.name);
        tvCurierService.setText(StringUtils.formatCurrency(String.valueOf(shipping.fee)));
        tvCurierService.setVisibility(View.GONE);

    }
}
