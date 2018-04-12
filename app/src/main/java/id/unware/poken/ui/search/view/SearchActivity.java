package id.unware.poken.ui.search.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideApp;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.browse.view.adapter.EndlessRecyclerViewScrollListener;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;
import id.unware.poken.ui.search.model.SearchModel;
import id.unware.poken.ui.search.presenter.SearchPresenter;
import id.unware.poken.ui.search.view.adapter.SearchProductAdapter;

public class SearchActivity extends BaseActivity
        implements ISearchView, SearchView.OnQueryTextListener {

    private static final String TAG = "SearchActivity";

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvProducts) RecyclerView rvProducts;

    private SearchPresenter presenter;

    private GlideRequests glideRequests;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    private ArrayList<Product> listItem = new ArrayList<>();
    private SearchProductAdapter adapter;

    private String submittedQuery = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        presenter = new SearchPresenter(
                new SearchModel(),
                this
        );

        glideRequests = GlideApp.with(this);

        initView();

    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initSearchResultView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    presenter.beginSearch(submittedQuery);
                }
            }
        });

    }

    private void initSearchResultView() {
        adapter = new SearchProductAdapter(listItem, presenter, glideRequests);
        rvProducts.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(gridLayoutManager);

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
        rvProducts.addOnScrollListener(scrollListener);

    }

    private void loadNextDataFromApi(int nextPage) {
        Utils.Logs('i', TAG, "Next page: " + nextPage);
        if (presenter != null) {
            presenter.loadMoreSearchResult(submittedQuery, nextPage);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        initSearchView(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            Utils.Log(TAG, "Home navigation clicked.");
            this.onBackPressed();
            return true; // To make sure no more
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isSearchViewFocus = true;
    private void initSearchView(final Menu menu) {

        //Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            // MenuItemCompat.expandActionView(searchItem);
            android.support.v4.view.MenuItemCompat.setOnActionExpandListener(
                    searchItem, new android.support.v4.view.MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) {
                            Utils.Log(TAG, "Search view expanded. Then begin search mode!");
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            Utils.Log(TAG, "Search view collapsed. Then end search mode.");
                            SearchActivity.this.finish();
                            return true;
                        }
                    });

            searchItem.expandActionView();
            SearchView mSearchView = (SearchView) searchItem.getActionView();
            mSearchView.setSubmitButtonEnabled(false);
            mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean isFocus) {
                    Utils.Logs('v', TAG, "View : " + String.valueOf(view) + ", is focus: " + isFocus);
                    if (!isFocus) {
                        isSearchViewFocus = false;
                    }
                }
            });
            mSearchView.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
            mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH | EditorInfo.IME_FLAG_NO_FULLSCREEN);
            mSearchView.setQueryHint("Cari nama barang");
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
            mSearchView.setOnQueryTextListener(this);

        } else {
            Utils.Logs('e', TAG, "No search view.");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.Logs('e', TAG, "No search view. Is search focus --> " + isSearchViewFocus);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Utils.Logs('i', TAG, "Search query: \"".concat(query).concat("\""));

        // Track user search
        MyLog.FabricTrackSearchQuery(query);

        submittedQuery = query;
        if (presenter != null) {
            presenter.beginSearch(query);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void showViewState(UIState uiState) {

        Utils.Logs('i', TAG, "Show on view; UI state: " + uiState);

        switch (uiState) {
            case LOADING:
                showLoadingIndicator(true);
                break;
            case FINISHED:
                showLoadingIndicator(false);
                break;
            case ERROR:
                showLoadingIndicator(false);
                break;
            case NODATA:
                showNoDateView(true);
                break;
        }
    }

    private void showNoDateView(boolean isShow) {
        if (isShow) {
            scrollListener.resetState();
            listItem.clear();
            adapter.notifyDataSetChanged();

            // Show no data
            Utils.snackBar(swipeRefreshLayout, "Tidak ada data untuk pencarian \"" + submittedQuery + "\". Silahkan coba kata kunci lainnya.", Log.WARN);
        }
    }

    private void showLoadingIndicator(boolean isShow) {
        if (isShow) {
            progressBar.animate().alpha(1F);

            // Hide SwipeRefreshLayout refreshing indicator
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        } else {
            progressBar.animate().alpha(0F);
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }

    @Override
    public void pupulateProductSearchRes(ArrayList<Product> products) {
        Utils.Logs('i', TAG, "Search result size: " + products.size());

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
}
