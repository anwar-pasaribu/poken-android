package id.unware.poken.ui.browse.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.Category;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Seller;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideApp;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.browse.model.BrowseModel;
import id.unware.poken.ui.browse.presenter.BrowsePresenter;
import id.unware.poken.ui.browse.view.adapter.BrowseProductAdapter;
import id.unware.poken.ui.browse.view.adapter.EndlessRecyclerViewScrollListener;
import id.unware.poken.ui.browse.view.adapter.SellerListAdapter;
import id.unware.poken.ui.pageseller.view.SellerActivity;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;

public class BrowseActivity extends AppCompatActivity implements IBrowseView {

    private static final String TAG = "BrowseActivity";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvProductBrowsing) RecyclerView rvProductBrowsing;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    private Unbinder unbinder;

    private GlideRequests glideRequests;

    private ArrayList<Product> listItem = new ArrayList<>();
    private BrowseProductAdapter adapter;

    private ArrayList<Seller> sellerArrayList = new ArrayList<>();
    private SellerListAdapter sellerSectionAdapter;

    private BrowsePresenter presenter;

    private int actionId = -1;
    private String actionName = "";

    // CASE BROWSE BY CATEGORY
    private boolean isBrowseByCategory = false;
    private int categoryId = -1;
    private String categoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        unbinder = ButterKnife.bind(this);

        glideRequests = GlideApp.with(this);

        if (getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();

            actionId = extras.getInt(Constants.EXTRA_GENERAL_INTENT_ID, -1);
            actionName = extras.getString(Constants.EXTRA_GENERAL_INTENT_VALUE, "");

            // Browse by category
            if (extras.containsKey(Constants.EXTRA_IS_BROWSE_BY_CATEGORY)) {
                isBrowseByCategory = extras.getBoolean(Constants.EXTRA_IS_BROWSE_BY_CATEGORY, false);
                categoryId = (int) extras.getLong(Constants.EXTRA_CATEGORY_ID, -1);
                categoryName = extras.getString(Constants.EXTRA_CATEGORY_NAME, "");

                Utils.Log(TAG, " Browse by category. ID: " + categoryId + ", name: " + categoryName);
            }

            Utils.Log(TAG, " Browse by action ID: " + actionId);
        }

        presenter = new BrowsePresenter(new BrowseModel(), this);

        initView();
    }

    private void initView() {

        setupToolbarView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestContent();

                scrollListener.resetState();
            }
        });

        initBrowseListView();

        requestContent();
    }

    private void requestContent() {

        if (!isBrowseByCategory) {

            if (actionId == Constants.HOME_SECTION_SALE_PRODUCT) {
                presenter.getProductDataByIntentId(actionId);
            } else if (actionId == Constants.HOME_SECTION_TOP_SELLER){
                presenter.getSellerList();
            }

        } else {

            Utils.Logs('i', TAG, "Request product data by category. " +
                    "Name: " + categoryName + ", ID: " + categoryId);

            Category category = new Category();
            category.setId(categoryId);
            category.setName(categoryName);
            presenter.getProductByCategory(category);
        }
    }

    private void setupToolbarView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(actionName);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initBrowseListView() {

        int gridColumnCount = 1;

        if (actionId == Constants.HOME_SECTION_SALE_PRODUCT) {
            adapter = new BrowseProductAdapter(listItem, presenter, glideRequests);
            rvProductBrowsing.setAdapter(adapter);

            gridColumnCount = 2;
        } else if (actionId == Constants.HOME_SECTION_TOP_SELLER) {
            sellerSectionAdapter = new SellerListAdapter(this, sellerArrayList, presenter, glideRequests);
            rvProductBrowsing.setAdapter(sellerSectionAdapter);

            gridColumnCount = 1;
        } else {

            // DEFAULT
            adapter = new BrowseProductAdapter(listItem, presenter, glideRequests);
            rvProductBrowsing.setAdapter(adapter);

            gridColumnCount = 2;
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, gridColumnCount);
        rvProductBrowsing.setLayoutManager(gridLayoutManager);

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
        rvProductBrowsing.addOnScrollListener(scrollListener);
    }

    private void loadNextDataFromApi(int page) {
        Utils.Logs('i', TAG, "Next page: " + page);

        if (presenter != null) {

            if (actionId == Constants.HOME_SECTION_TOP_SELLER) {
                // Load more seller
                presenter.getMoreSellerData(page);
            } else {
                if (!isBrowseByCategory) {
                    presenter.getMoreProductDataByIntentId(actionId, page);
                } else {
                    Category category = new Category();
                    category.setId(categoryId);
                    category.setName(categoryName);
                    presenter.getMoreProductByCategory(category, page);
                }
            }

        }
    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                swipeRefreshLayout.setRefreshing(true);
                break;
            case FINISHED:
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }

    @Override
    public void pupolateSellerProductList(ArrayList<Product> products) {
        listItem.clear();
        listItem.addAll(products);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProductDetail(Product product) {
        Utils.Logs('i', TAG, "Show product detail with id: " + product.id);
        Intent productDetailIntent = new Intent(this, ProductDetailActivity.class);
        productDetailIntent.putExtra(Product.KEY_PRODUCT_ID, product.id);
        this.startActivityForResult(productDetailIntent, Constants.TAG_PRODUCT_DETAIL);
    }

    @Override
    public void appendProductList(ArrayList<Product> products) {
        int moreProductsSize = products.size();
        int currentProductsSize = listItem.size();
        Utils.Logs('i', TAG, "More product list size: " + moreProductsSize);
        Utils.Logs('i', TAG, "Current size " + currentProductsSize);

        listItem.addAll(products);
        adapter.notifyItemRangeInserted(currentProductsSize, moreProductsSize);

    }

    @Override
    public void pupolateSellerList(ArrayList<Seller> sellers) {
        sellerArrayList.clear();
        sellerArrayList.addAll(sellers);
        sellerSectionAdapter.notifyDataSetChanged();
    }

    @Override
    public void appendSellerList(ArrayList<Seller> newSellerList) {
        int moreSellerSize = newSellerList.size();
        int currectSellerListSize = sellerArrayList.size();
        Utils.Logs('i', TAG, "More seller list size: " + moreSellerSize);
        Utils.Logs('i', TAG, "Current seller size " + currectSellerListSize);

        sellerArrayList.addAll(newSellerList);
        sellerSectionAdapter.notifyItemRangeInserted(currectSellerListSize, moreSellerSize);
    }

    @Override
    public void showSellerDetail(int position, Seller seller) {
        Intent sellerIntent = new Intent(this, SellerActivity.class);
        sellerIntent.putExtra(Constants.KEY_DOMAIN_ITEM_ID, seller.id);
        this.startActivity(sellerIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        Utils.Log(TAG, "Endless scroll listener: " + String.valueOf(scrollListener));
        super.onDestroy();
        unbinder.unbind();
    }
}
