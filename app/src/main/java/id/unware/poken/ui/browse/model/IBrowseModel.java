package id.unware.poken.ui.browse.model;

import id.unware.poken.domain.Category;
import id.unware.poken.ui.browse.presenter.IBrowseModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public interface IBrowseModel {
    void requestSellerData(IBrowseModelPresenter presenter, int actionId);
    void requestSellerDataByCategory(IBrowseModelPresenter presenter, Category category);
}
