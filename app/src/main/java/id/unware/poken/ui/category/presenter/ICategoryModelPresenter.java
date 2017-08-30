package id.unware.poken.ui.category.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Category;
import id.unware.poken.domain.FeaturedCategoryProductDataRes;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Aug 13 2017
 */

public interface ICategoryModelPresenter extends BasePresenter {

    void onCategoryListResponse(ArrayList<Category> categories);

    void onMessageResponse(String msg, int status);

    void onFeaturedProductPerCategoryResponse(FeaturedCategoryProductDataRes categoryDataRes);
}
