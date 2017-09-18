package id.unware.poken.ui.search.presenter;

import id.unware.poken.domain.Product;

/**
 * @author Anwar Pasaribu
 * @since Aug 06 2017
 */

public interface ISearchPresenter {
    void beginSearch(String query);
    void startProductDetail(Product product);

    void loadMoreSearchResult(String submittedQuery, int nextPage);
}
