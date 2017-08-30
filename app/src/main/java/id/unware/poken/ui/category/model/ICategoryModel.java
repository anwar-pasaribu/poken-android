package id.unware.poken.ui.category.model;

import id.unware.poken.ui.category.presenter.CategoryPresenter;
import id.unware.poken.ui.category.presenter.ICategoryModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Aug 13 2017
 */

public interface ICategoryModel {

    void reqProductCategory(ICategoryModelPresenter presenter);

    void reqFeaturedCategory(ICategoryModelPresenter presenter);
}
