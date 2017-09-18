package id.unware.poken.ui.category.model;

import id.unware.poken.ui.category.presenter.ICategoryModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Aug 13 2017
 */

public interface ICategoryModel {

    void reqFeaturedCategory(ICategoryModelPresenter presenter);

    void requestMoreFeaturedProductCategory(ICategoryModelPresenter presenter, int page);
}
