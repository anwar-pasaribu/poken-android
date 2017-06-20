package id.unware.poken.ui.browse.presenter;

import id.unware.poken.domain.Product;

/**
 * @author Anwar Pasaribu
 * @since Jun 17 2017
 */

public interface IBrowsePresenter {
    /**
     * Intent ID means id to handle which item to show.
     *
     * @param intentId intention to show data.
     */
    void getProductDataByIntentId(int intentId);

    void startProductDetail(Product product);
}