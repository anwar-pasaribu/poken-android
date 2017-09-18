package id.unware.poken.ui.search.model;

import id.unware.poken.ui.search.presenter.ISearchModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Aug 06 2017
 */

public interface ISearchModel {
    void searchProductByQuery(String query, ISearchModelPresenter presenter);
    void loadMoreSearchedProductQuery(String query, int page, ISearchModelPresenter presenter);
}
