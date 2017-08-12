package id.unware.poken.ui.product.detail.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

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
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.pokenaccount.LoginActivity;
import id.unware.poken.ui.product.detail.model.ProductDetailModel;
import id.unware.poken.ui.product.detail.presenter.ProductDetailPresenter;
import id.unware.poken.ui.product.detail.view.adapter.ProductImagesPagerAdapter;
import id.unware.poken.ui.product.detail.view.fragment.FragmentDialogShippings;
import id.unware.poken.ui.product.detail.view.fragment.NewlyShoppingCartDialogFragment;
import id.unware.poken.ui.shoppingcart.view.ShoppingCartActivity;

import static id.unware.poken.R.id.tvPrice;


public class ProductDetailActivity extends AppCompatActivity
        implements IProductDetailView, ViewPager.OnPageChangeListener {

    private static final String TAG = "ProductDetailActivity";

    @BindView(R.id.progressBarDetailProduct) ProgressBar progressBarDetailProduct;

    @BindView(R.id.ivProduct) ImageView ivProduct;
    @BindView(R.id.viewPagerProductImages) ViewPager viewPagerProductImages;
    @BindView(R.id.inkPageIndicator) com.pixelcan.inkpageindicator.InkPageIndicator inkPageIndicator;

    @BindView(R.id.tvProductName) TextView tvProductName;
    @BindView(R.id.tvProductPrice) TextView tvProductPrice;
    @BindView(R.id.tvProductStock) TextView tvProductStock;
    @BindView(R.id.tvProductSold) TextView tvProductSold;
    @BindView(R.id.tvProductLeft) TextView tvProductLeft;
    @BindView(R.id.tvProductDescription) TextView tvProductDescription;
    @BindView(R.id.ivSellerAvatar) ImageView ivSellerAvatar;
    @BindView(R.id.tvSellerName) TextView tvSellerName;
    @BindView(R.id.tvSellerAddress) TextView tvSellerAddress;

    // Sale item
    @BindView(R.id.tvPrice2) TextView tvPrice2;
    @BindView(R.id.tvDiscountedPrice) TextView tvDiscountedPrice;
    @BindView(R.id.tvDiscountAmount) TextView tvDiscountAmount;
    @BindView(R.id.viewFlipperProductPrice) ViewFlipper viewFlipperProductPrice;

    // BUY OR ADD TO CART
    @BindView(R.id.btnAddCart) Button btnAddCart;
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
        this.setTitle(null);

        // Set initial price view to prevent view flashing
        viewFlipperProductPrice.setDisplayedChild(Constants.VIEWFLIPPER_CHILD_DEFAULT);

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    long shippingId = selectedShipping != null ? selectedShipping.id : 3;
                    presenter.onBuyNow(shippingId, productId, false);
                }
            }
        });

        // Add product to Shopping Cart and open Shopping cart screen
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    long shippingId = selectedShipping != null ? selectedShipping.id : 3;
                    presenter.onBuyNow(shippingId, productId, true);
                } else {
                    Utils.Logs('e', TAG, "Presenter is not ready.");
                }
            }
        });

        productDetailIbMoreShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.startShippingOptionsScreen();
                } else {
                    Utils.Logs('e', TAG, "Presenter is not ready.");
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
//        Picasso.with(this)
//                .load(String.valueOf(productImages.get(0).path))
//                .into(ivProduct);

        if (!productImages.isEmpty()) {
            ivProduct.setVisibility(View.GONE);
            viewPagerProductImages.setAdapter(new ProductImagesPagerAdapter(viewPagerProductImages, productImages, null));
            viewPagerProductImages.addOnPageChangeListener(this);
            viewPagerProductImages.setPageMargin(getResources().getDimensionPixelSize(R.dimen.item_gap_m));

            inkPageIndicator.setViewPager(viewPagerProductImages);
        } else {
            ivProduct.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void populateProductGeneralInfo(Product product) {

        tvProductName.setText(String.valueOf(product.name));
        // noinspection deprecation
        tvProductDescription.setText(Html.fromHtml(String.valueOf(product.description)));
        tvProductStock.setText(String.valueOf(product.stock));

        tvProductSold.setText(String.valueOf(0));
        tvProductLeft.setText(String.valueOf(product.stock));

        // Store info
        tvSellerName.setText(product.seller.store_name);
        tvSellerAddress.setText(product.seller.location);
    }

    /**
     * Default price view for non SALE product.
     * @param formattedPrice Product price with ID format.
     */
    @Override
    public void updateProductPrice(String formattedPrice) {
        tvProductPrice.setText(formattedPrice);

        viewFlipperProductPrice.setDisplayedChild(Constants.VIEWFLIPPER_CHILD_DEFAULT);
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
        Utils.Logs('i', TAG, "Populate shipping options: " + shippings.size());
    }

    @Override
    public void showAddedShoppingCartItem(ShoppingCart newlyAddedShoppingCart) {
        Utils.Log(TAG, "Show newly created shopping cart item.");

        String strAddedShoppingCartTag = "dialog-added-shopping-cart";
        FragmentTransaction ft = hideDialog(strAddedShoppingCartTag);
        ft.addToBackStack(null);

        NewlyShoppingCartDialogFragment f = new NewlyShoppingCartDialogFragment(
                newlyAddedShoppingCart,
                presenter);
        f.show(ft, strAddedShoppingCartTag);

    }

    @Override
    public void showSaleProduct(Product product) {

        tvProductPrice.setText(StringUtils.formatCurrency(String.valueOf(product.price)));

        // tvPrice2 to show SALE item
        tvPrice2.setText(StringUtils.formatCurrency(String.valueOf(product.price)));
        tvPrice2.setPaintFlags(tvProductPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);  // Strike
        tvDiscountedPrice.setText(StringUtils.formatCurrency(String.valueOf(product.getDiscountedPrice())));
        tvDiscountAmount.setText((int) product.discount_amount + "%");

        // Discount view
        if (product.discount_amount > 0D) {
            viewFlipperProductPrice.setDisplayedChild(Constants.VIEWFLIPPER_CHILD_SALE);
        } else {
            viewFlipperProductPrice.setDisplayedChild(Constants.VIEWFLIPPER_CHILD_DEFAULT);
        }
    }

    @Override
    public void showLoginScreen() {
        Intent accountIntent = new Intent(this, LoginActivity.class);
        accountIntent.putExtra(Constants.EXTRA_REQUESTED_PAGE, Constants.TAG_ADD_SHOPPING_CART);
        this.startActivityForResult(accountIntent, Constants.TAG_LOGIN);
    }

    @Override
    public void showViewState(UIState uiState) {
        Utils.Logs('i', TAG, "View state: " + String.valueOf(uiState));
        switch (uiState) {
            case LOADING:
                showLoadingIndicator(true);
                break;
            case FINISHED:
                showLoadingIndicator(false);
                break;
        }
    }

    private void showLoadingIndicator(boolean isLoading) {
        if (isLoading) {

            btnAddCart.setEnabled(false);
            btnBuy.setEnabled(false);
            progressBarDetailProduct.animate().alpha(1F);

        } else {

            btnAddCart.setEnabled(true);
            btnBuy.setEnabled(true);
            progressBarDetailProduct.animate().alpha(0F);
        }

    }

    @Override
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log(TAG, "Activity result. Req: " + requestCode + ", res: " + resultCode + ", data: " + data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.TAG_LOGIN) {

                int requestedPageTag = data.getIntExtra(Constants.EXTRA_REQUESTED_PAGE, -1);

                if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
                    if (requestedPageTag == Constants.TAG_ADD_SHOPPING_CART
                            || requestedPageTag == Constants.TAG_BUY_NOW) {
                        // Login success, continue add product to shopping cart
                        if (presenter != null) {
                            long shippingId = selectedShipping != null ? selectedShipping.id : 3;
                            presenter.onBuyNow(shippingId, productId, false);
                        }
                    }
                }
            }
        } else {
            Utils.Logs('w', TAG, "Result not OK.");
            showLoadingIndicator(false);
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
        if (id == R.id.action_refresh) {
            if (presenter != null) {
                presenter.getProductData(productId);
            }
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

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Utils.Logs('i', TAG, "Page selected: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Utils.Logs('v', TAG, "Page scroll state change: " + state);

    }

}
