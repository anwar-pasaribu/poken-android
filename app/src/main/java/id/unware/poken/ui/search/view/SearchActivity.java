package id.unware.poken.ui.search.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;
import id.unware.poken.ui.search.model.SearchModel;
import id.unware.poken.ui.search.presenter.SearchPresenter;
import id.unware.poken.ui.search.view.adapter.SearchProductAdapter;

public class SearchActivity extends AppCompatActivity
        implements ISearchView, SearchView.OnQueryTextListener {

    private static final String TAG = "SearchActivity";

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvProducts) RecyclerView rvProducts;

    private SearchPresenter presenter;

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

        initView();

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        adapter = new SearchProductAdapter(listItem, presenter);
        adapter.setHasStableIds(true);
        rvProducts.setHasFixedSize(true);
        rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
        rvProducts.setAdapter(adapter);

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

    private void initSearchView(final Menu menu) {

        //Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            MenuItemCompat.expandActionView(searchItem);
            MenuItemCompat.setOnActionExpandListener(
                    searchItem, new MenuItemCompat.OnActionExpandListener() {
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

            SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
    public boolean onQueryTextSubmit(String query) {
        Utils.Logs('i', TAG, "Search query: \"".concat(query).concat("\""));
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
}
