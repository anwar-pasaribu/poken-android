package id.unware.poken.ui.category.presenter;

import id.unware.poken.domain.Category;
import id.unware.poken.domain.FeaturedCategoryProductDataRes;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.category.model.ICategoryModel;
import id.unware.poken.ui.category.view.ICategoryView;

/**
 * @author Anwar Pasaribu
 * @since Aug 14 2017
 */

public class CategoryPresenter implements ICategoryPresenter, ICategoryModelPresenter {

    private static final String TAG = "CategoryPresenter";

    final private ICategoryModel model;
    final private ICategoryView view;
    private boolean isLoadMore = false;
    private boolean isMoreContentAvailable = false;

    public CategoryPresenter(ICategoryModel model, ICategoryView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void loadCategoryList() {

        // Make false to prevent append data again
        this.isLoadMore = false;

        model.reqFeaturedCategory(this);
    }

    @Override
    public void onCategoryClick(int position, Category category) {
        view.showCategoryDetail(category);
    }

    @Override
    public void loadMoreCategoryList(int page) {
        this.isLoadMore = true;

        if (this.isMoreContentAvailable) {
            model.requestMoreFeaturedProductCategory(this, page);
        } else {
            Utils.Log(TAG, "Last page reached...");
        }
    }

    @Override
    public void updateViewState(UIState uiState) {

        if (view.isActivityFinishing()) return;

        view.showViewState(uiState);
    }

    @Override
    public void onMessageResponse(String msg, int status) {

        if (view.isActivityFinishing()) return;

        view.showMessage(msg, status);
    }

    @Override
    public void onFeaturedProductPerCategoryResponse(FeaturedCategoryProductDataRes categoryDataRes) {

        if (view.isActivityFinishing()) return;

        if (!isLoadMore) {
            Utils.Log(TAG, "Initial list response. Size: " + categoryDataRes.results.size());
            view.pupulateFeaturedCategories(categoryDataRes.results);
        } else {
            Utils.Log(TAG, "More item response. Size: " + categoryDataRes.results.size());
            view.appendFeaturedCategories(categoryDataRes.results);
        }
    }

    @Override
    public void onNextProductPage(String nextPage) {
        Utils.Log(TAG, "Next page URL: " + String.valueOf(nextPage));
        this.isMoreContentAvailable = !StringUtils.isEmpty(nextPage);
    }
}
