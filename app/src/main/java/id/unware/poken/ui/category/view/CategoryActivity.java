package id.unware.poken.ui.category.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;

import com.bumptech.glide.MemoryCategory;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Category;
import id.unware.poken.domain.FeaturedCategoryProduct;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideApp;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.BaseActivityWithup;
import id.unware.poken.ui.browse.view.BrowseActivity;
import id.unware.poken.ui.browse.view.adapter.EndlessRecyclerViewScrollListener;
import id.unware.poken.ui.category.model.CategoryModel;
import id.unware.poken.ui.category.presenter.CategoryPresenter;
import id.unware.poken.ui.category.view.adapter.CategoryAdapter;

public class CategoryActivity extends BaseActivityWithup implements ICategoryView {

    private static final String TAG = "CategoryActivity";

    @BindView(R.id.categoryProgress) ProgressBar categoryProgress;
    @BindView(R.id.categoryRvMain) RecyclerView categoryRvMain;

    private CategoryPresenter presenter;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    private CategoryAdapter adapter;
    private ArrayList<FeaturedCategoryProduct> listItem = new ArrayList<>();

    private GlideRequests glideRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        GlideApp.get(this).setMemoryCategory(MemoryCategory.HIGH);
        glideRequests = GlideApp.with(this);

        ButterKnife.bind(this);

        presenter = new CategoryPresenter(new CategoryModel(), this);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.loadCategoryList();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new CategoryAdapter(this, glideRequests, listItem, presenter);
        categoryRvMain.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(
                        this, LinearLayoutManager.VERTICAL, false);

        categoryRvMain.setLayoutManager(linearLayoutManager);

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        categoryRvMain.addOnScrollListener(scrollListener);

    }

    private void loadNextDataFromApi(int page) {
        if (presenter != null) {
            presenter.loadMoreCategoryList(page);
        }
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

    private void showLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            categoryProgress.animate().alpha(1F);
        } else {
            categoryProgress.animate().alpha(0F);
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }

    @Override
    public void showCategoryDetail(Category category) {
        Utils.Log(TAG, "Show category detail: " + category.getName());
        Intent browsePage = new Intent(this, BrowseActivity.class);
        browsePage.putExtra(Constants.EXTRA_GENERAL_INTENT_ID, Constants.INTENT_BROWSE_BY_CATEGORY);
        browsePage.putExtra(Constants.EXTRA_GENERAL_INTENT_VALUE, category.getName());
        browsePage.putExtra(Constants.EXTRA_IS_BROWSE_BY_CATEGORY, true);
        browsePage.putExtra(Constants.EXTRA_CATEGORY_ID, category.getId());
        browsePage.putExtra(Constants.EXTRA_CATEGORY_NAME, category.getName());
        this.startActivity(browsePage);
    }

    @Override
    public void showMessage(String msg, int status) {
        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            Utils.snackBar(categoryRvMain, msg, Log.ERROR);
        } else {
            Utils.snackBar(categoryRvMain, msg);
        }
    }

    @Override
    public void pupulateFeaturedCategories(ArrayList<FeaturedCategoryProduct> featuredCategoryProducts) {
        listItem.clear();
        listItem.addAll(featuredCategoryProducts);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void appendFeaturedCategories(ArrayList<FeaturedCategoryProduct> categories) {
        int moreProductsSize = categories.size();
        int currentProductsSize = listItem.size();
        Utils.Logs('i', TAG, "More product list size: " + moreProductsSize);
        Utils.Logs('i', TAG, "Current size " + currentProductsSize);

        listItem.addAll(categories);
        adapter.notifyItemRangeInserted(currentProductsSize, moreProductsSize);

    }
}
