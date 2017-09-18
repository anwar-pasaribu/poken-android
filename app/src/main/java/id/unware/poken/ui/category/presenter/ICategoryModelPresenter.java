package id.unware.poken.ui.category.presenter;

import id.unware.poken.domain.FeaturedCategoryProductDataRes;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Aug 13 2017
 */

public interface ICategoryModelPresenter extends BasePresenter {

    void onMessageResponse(String msg, int status);

    void onFeaturedProductPerCategoryResponse(FeaturedCategoryProductDataRes categoryDataRes);

    void onNextProductPage(String next);
}
