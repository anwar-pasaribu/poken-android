package id.unware.poken.ui.category.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Category;
import id.unware.poken.domain.FeaturedCategoryProduct;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivityWithup;
import id.unware.poken.ui.browse.view.BrowseActivity;
import id.unware.poken.ui.category.model.CategoryModel;
import id.unware.poken.ui.category.presenter.CategoryPresenter;
import id.unware.poken.ui.category.view.adapter.CategoryAdapter;

public class CategoryActivity extends BaseActivityWithup implements ICategoryView {

    private static final String TAG = "CategoryActivity";

    @BindView(R.id.categoryProgress) ProgressBar categoryProgress;
    @BindView(R.id.categoryRvMain) RecyclerView categoryRvMain;

    private CategoryPresenter presenter;

    private CategoryAdapter adapter;
    private ArrayList<FeaturedCategoryProduct> listItem = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new CategoryAdapter(this, listItem, presenter);
        categoryRvMain.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        categoryRvMain.setAdapter(adapter);

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
    public void pupulateCategories(ArrayList<Category> categories) {
        Utils.Log(TAG, "Category list size: " + categories.size());
        // listItem.clear();
        // listItem.addAll(categories);
        // adapter.notifyDataSetChanged();
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
}
