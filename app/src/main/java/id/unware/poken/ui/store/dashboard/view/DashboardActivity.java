package id.unware.poken.ui.store.dashboard.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.OrderCredit;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;
import id.unware.poken.ui.shoppingorder.view.OrderActivity;
import id.unware.poken.ui.store.credits.view.StoreCreditsFragment;
import id.unware.poken.ui.store.manageproduct.view.ManageProductActivity;
import id.unware.poken.ui.store.nonregisteredseller.view.NonSellerFragment;
import id.unware.poken.ui.store.product.view.StoreProductListFragment;
import id.unware.poken.ui.store.summary.view.StoreSummaryFragment;

public class DashboardActivity extends BaseActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,
        NonSellerFragment.OnNonSellerFragmentInteractionListener,
        StoreSummaryFragment.OnFragmentInteractionListener,
        StoreProductListFragment.OnStoreProductListener,
        StoreCreditsFragment.OnStoreCreditListener{

    private static final String TAG = "DashboardActivity";

    @BindView(R.id.navigation) BottomNavigationView navigation;
    @BindView(R.id.frameDashboard) FrameLayout frameDashboard;
    @BindView(R.id.fabDashboardAddProduct) FloatingActionButton fabDashboardAddProduct;

    /** Idicate whether user is Seller */
    private boolean isNonSeller = false;

    private StoreSummaryFragment storeSummaryFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        navigation.setOnNavigationItemSelectedListener(this);

        initView();

    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fabDashboardAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                openManageProductScreen();
            }
        });

        navigation.setSelectedItemId(R.id.navigation_home);

    }

    private void openManageProductScreen() {
        Intent manageProductIntent = new Intent(this, ManageProductActivity.class);
        startActivityFromFragment(storeSummaryFragment, manageProductIntent, Constants.TAG_STORE_SUMMARY);
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Utils.Logs('i', TAG + " - On act result. Req: " + requestCode + ", res: " + resultCode + ", data: " + data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override public void onFragmentInteraction(@NotNull Uri uri) {
        Utils.Log(TAG, "Selected URI: " + uri);
    }

    @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (this.isNonSeller) {
            // Show non seller page
            openNonSellerScreen();
            return true;
        }

        switch (item.getItemId()) {
            case R.id.navigation_home:
                toggleFloatingActionButtonVisibility(false);
                openStoreSummary();
                return true;
            case R.id.navigation_dashboard:
                toggleFloatingActionButtonVisibility(true);
                openStoreProductScreen();
                return true;
            case R.id.navigation_notifications:
                toggleFloatingActionButtonVisibility(false);
                openStoreCreditsScreen();
                return true;
        }
        return false;
    }

    private void openNonSellerScreen() {
        Utils.changeFragment(this, frameDashboard.getId(), NonSellerFragment.Companion.newInstance("", ""));
    }

    private void openStoreCreditsScreen() {
        Utils.changeFragment(this, frameDashboard.getId(), StoreCreditsFragment.Companion.newInstance(1));
    }

    private void openStoreSummary() {

        if (storeSummaryFragment == null) {
            storeSummaryFragment = StoreSummaryFragment.Companion.newInstance("s", "s");
        }

        Utils.changeFragment(this, frameDashboard.getId(), storeSummaryFragment);
    }

    private void openStoreProductScreen() {
        Utils.changeFragment(this, frameDashboard.getId(), StoreProductListFragment.Companion.newInstance(1));
    }

    private void toggleFloatingActionButtonVisibility(boolean visible) {
        if (visible)
            fabDashboardAddProduct.show();
        else
            fabDashboardAddProduct.hide();
    }

    @Override public void onListFragmentInteraction(@NotNull Product product) {
        Utils.Logs('i', String.format("Clicked product : %s", product));
        Intent productDetailIntent = new Intent(this, ProductDetailActivity.class);
        productDetailIntent.putExtra(Product.KEY_PRODUCT_ID, product.id);
        productDetailIntent.putExtra(Constants.EXTRA_PRODUCT_DETAIL_IS_EDIT, true);
        this.startActivityForResult(productDetailIntent, Constants.TAG_PRODUCT_DETAIL);
    }

    @Override public void openAllProductListScreen() {

        Utils.Logs('i', "Open product list screen");

        navigation.setSelectedItemId(R.id.navigation_dashboard);
    }

    @Override public void onCreditItemInteraction(@NotNull OrderCredit item) {
        Utils.Logs('i', "Click credit item: " + item);
        // Create SHopping Order Model
        ShoppingOrder shoppingOrder = new ShoppingOrder();
        shoppingOrder.id = item.getId();

        openOrderScreen(shoppingOrder);
    }

    private void openOrderScreen(ShoppingOrder item) {
        Intent orderScreenIntent = new Intent(this, OrderActivity.class);
        orderScreenIntent.putExtra(Constants.EXTRA_ORDER_ID, item.id);
        orderScreenIntent.putExtra(Constants.EXTRA_IS_SELLER_MODE_ORDER_PAGE, true);
        this.startActivity(orderScreenIntent);
    }

    @Override public void showSellerNotRegisteredView() {
        MyLog.FabricLog(Log.WARN, "User not registered as Seller");
        isNonSeller = true;
        navigation.setSelectedItemId(R.id.navigation_home);
        navigation.setVisibility(View.GONE);
    }
}
