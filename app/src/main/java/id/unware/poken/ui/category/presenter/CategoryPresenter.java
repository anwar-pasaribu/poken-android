package id.unware.poken.ui.category.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Category;
import id.unware.poken.domain.FeaturedCategoryProductDataRes;
import id.unware.poken.pojo.UIState;
import id.unware.poken.ui.category.model.ICategoryModel;
import id.unware.poken.ui.category.view.ICategoryView;

/**
 * @author Anwar Pasaribu
 * @since Aug 14 2017
 */

public class CategoryPresenter implements ICategoryPresenter, ICategoryModelPresenter {

    final private ICategoryModel model;
    final private ICategoryView view;

    public CategoryPresenter(ICategoryModel model, ICategoryView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void loadCategoryList() {
        // model.reqProductCategory(this);
        model.reqFeaturedCategory(this);
    }

    @Override
    public void onCategoryClick(int position, Category category) {
        view.showCategoryDetail(category);
    }

    @Override
    public void updateViewState(UIState uiState) {

        if (view.isActivityFinishing()) return;

        view.showViewState(uiState);
    }

    @Override
    public void onCategoryListResponse(ArrayList<Category> categories) {

        if (view.isActivityFinishing()) return;

        view.pupulateCategories(categories);
    }

    @Override
    public void onMessageResponse(String msg, int status) {

        if (view.isActivityFinishing()) return;

        view.showMessage(msg, status);

    }

    @Override
    public void onFeaturedProductPerCategoryResponse(FeaturedCategoryProductDataRes categoryDataRes) {
        view.pupulateFeaturedCategories(categoryDataRes.results);
    }
}
