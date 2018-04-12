package id.unware.poken.ui.pageseller.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.MemoryCategory;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Seller;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideApp;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.browse.view.adapter.EndlessRecyclerViewScrollListener;
import id.unware.poken.ui.pokenaccount.LoginActivity;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;
import id.unware.poken.ui.pageseller.model.SellerPageModel;
import id.unware.poken.ui.pageseller.presenter.SellerPagePresenter;
import id.unware.poken.ui.pageseller.view.adapter.SellerProductAdapter;

public class SellerActivity extends BaseActivity implements ISellerPageView {

    private static final String TAG = "SellerActivity";

    // Parent on seller content
    @BindView(R.id.sellerParentView) RelativeLayout sellerParentView;

    @BindView(R.id.ivUserAvatar) ImageView ivUserAvatar;

    @BindView(R.id.tvSellerUser) TextView tvSellerUser;

    @BindView(R.id.sellerIconLocation) ImageView sellerIconLocation;
    @BindView(R.id.tvSellerIdetifier) TextView tvSellerIdetifier;

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvSellerProduct) RecyclerView rvSellerProduct;

    @BindDimen(R.dimen.profile_avatar_m) int profile_avatar_m;

    private MenuItem itemSubscriptionStatus;

    private Unbinder unbinder;

    private SellerPagePresenter presenter;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    private SellerProductAdapter adapter;
    private ArrayList<Product> listItem = new ArrayList<>();

    private boolean isSubscribe = false;
    private long sellerId;

    private Handler handler = new Handler();
    private Runnable reqSellerPageRun = new Runnable() {
        @Override
        public void run() {
            Utils.Log(TAG, "Begin gitting seller page data.");
            presenter.getSellerPageProductData(sellerId);
        }
    };

    private GlideRequests glideRequests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        GlideApp.get(this).setMemoryCategory(MemoryCategory.HIGH);

        glideRequests = GlideApp.with(this);

        unbinder = ButterKnife.bind(this);

        presenter = new SellerPagePresenter(new SellerPageModel(), this);

        if (getIntent().getExtras() != null) {
            sellerId = getIntent().getExtras().getLong(Constants.KEY_DOMAIN_ITEM_ID, -1L);
            Utils.Logs('i', TAG, "Seller id to show: " + sellerId);
        }

        handler.postDelayed(reqSellerPageRun, this.getResources().getInteger(android.R.integer.config_mediumAnimTime));

        initView();

    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getSellerPageProductData(sellerId);
            }
        });

        initSellerPageList();

    }

    private void initSellerPageList() {
        adapter = new SellerProductAdapter(listItem, presenter, glideRequests);
        rvSellerProduct.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvSellerProduct.setLayoutManager(gridLayoutManager);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvSellerProduct.addOnScrollListener(scrollListener);

    }

    private void loadNextDataFromApi(int page) {
        if (presenter != null) {
            presenter.getMoreSellerPageProductData(sellerId, page);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        itemSubscriptionStatus = menu.findItem(R.id.action_toggle_subscribe);
        Utils.Logs('i', TAG, "Subscribe item: " + itemSubscriptionStatus);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_seller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Utils.Log(TAG, "Home navigation clicked.");
            this.onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_toggle_subscribe) {

            if (itemSubscriptionStatus == null) {
                itemSubscriptionStatus = item;
            }

            Utils.Log(TAG, "Toggle berlangganan: " + item.getTitle());
            if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
                if (presenter != null) {
                    presenter.subscribeOnSeller(this.sellerId, this.isSubscribe);
                }
            } else {
                ControllerDialog.getInstance().showYesNoDialog(
                        getString(R.string.msg_login_required_for_subscription),
                        this,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == DialogInterface.BUTTON_POSITIVE) {
                                    openPokenAccountScreen();
                                }

                                dialogInterface.dismiss();
                            }
                        },
                        getString(R.string.btn_positive_join_poken),
                        getString(R.string.btn_negative_later)
                );
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void openPokenAccountScreen() {
        Intent accountIntent = new Intent(this, LoginActivity.class);
        accountIntent.putExtra(Constants.EXTRA_REQUESTED_PAGE, Constants.TAG_FEATURE_SUBSCRIPTION);  // Request for subcription
        this.startActivityForResult(accountIntent, Constants.TAG_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log(TAG, "Activity result. Req: " + requestCode + ", res: " + resultCode + ", data: " + data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.TAG_LOGIN) {

                int requestedPageTag = data.getIntExtra(Constants.EXTRA_REQUESTED_PAGE, -1);

                if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
                    if (requestedPageTag == Constants.TAG_FEATURE_SUBSCRIPTION) {
                        // Continue pending subscription
                        if (presenter != null) {
                            presenter.subscribeOnSeller(this.sellerId, this.isSubscribe);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(reqSellerPageRun);
        unbinder.unbind();

    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                showLoadingIndicator(true);
                break;
            case FINISHED:
                showLoadingIndicator(false);
                break;
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }

    @Override
    public void pupolateSellerProductList(ArrayList<Product> products) {
        Utils.Log(TAG, "Product response size: " + products.size());
        listItem.clear();
        listItem.addAll(products);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void appendSellerProductList(ArrayList<Product> products) {
        int moreProductsSize = products.size();
        int currentProductsSize = listItem.size();
        Utils.Logs('i', TAG, "More product list size: " + moreProductsSize);
        Utils.Logs('i', TAG, "Current size " + currentProductsSize);

        listItem.addAll(products);
        adapter.notifyItemRangeInserted(currentProductsSize, moreProductsSize);
    }

    @Override
    public void showProductDetail(Product product) {
        Utils.Log(TAG, "Open collection detail ID: " + product.id);
        Intent productDetailIntent = new Intent(this, ProductDetailActivity.class);
        productDetailIntent.putExtra(Product.KEY_PRODUCT_ID, product.id);
        this.startActivityForResult(productDetailIntent, Constants.TAG_PRODUCT_DETAIL);
    }

    @Override
    public void showSellerInfo(Seller seller) {

        tvSellerUser.setText(seller.store_name);

        tvSellerIdetifier.setText(seller.location.city);  // Alpha still 0
        sellerIconLocation.animate().alpha(1F).withEndAction(new Runnable() {
            @Override
            public void run() {
                tvSellerIdetifier.animate().alpha(1F);
            }
        });


        Utils.Log(TAG, "Seller img url: " + seller.store_avatar);

        if (!StringUtils.isEmpty(seller.store_avatar)) {
            glideRequests.asDrawable()
                    .clone()
                    .load(seller.store_avatar)
                    .circleCrop()
                    .placeholder(R.drawable.ic_circle_24dp)
                    .into(ivUserAvatar);
        }
    }

    @Override
    public void showSubscriptionStatus(boolean isSubscribe) {
        this.isSubscribe = isSubscribe;
        try {
            if (isSubscribe) {
                itemSubscriptionStatus.setTitle(R.string.action_toggle_subscribe_on);
                itemSubscriptionStatus.setIcon(R.drawable.ic_notifications_black_24dp);
            } else {
                itemSubscriptionStatus.setTitle(R.string.action_toggle_subscribe_off);
                itemSubscriptionStatus.setIcon(R.drawable.ic_notifications_none_black_24dp);
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    @Override
    public void showSubscriptionStatusMessage(boolean isSubscribe) {
        if (isSubscribe) {
            Utils.snackBar(sellerParentView, this.getString(R.string.msg_seller_subscribed), Log.INFO);
        } else {
            Utils.snackBar(sellerParentView, this.getString(R.string.msg_seller_unsubscribed), Log.WARN);
        }
    }

    private void showLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }

            // Disable menu item
            if (itemSubscriptionStatus != null) {
                itemSubscriptionStatus.setEnabled(false);
                itemSubscriptionStatus.getIcon().setColorFilter(ContextCompat.getColor(this, R.color.separator_view), PorterDuff.Mode.SRC_ATOP);
            }
        } else {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            // Enable menu item
            if (itemSubscriptionStatus != null) {
                itemSubscriptionStatus.setEnabled(true);
                itemSubscriptionStatus.getIcon().setColorFilter(ContextCompat.getColor(this, android.R.color.black), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
