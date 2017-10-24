package id.unware.poken.ui.browse.presenter;

import id.unware.poken.domain.Category;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Seller;

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

    void getSellerList();

    void getMoreProductDataByIntentId(int actionId, int page);

    void getMoreProductByCategory(Category category, int nextPage);

    void getMoreSellerData(int page);

    void getProductByCategory(Category category);

    void startProductDetail(Product product);


    void onSellerClick(int position, Seller seller);

}
