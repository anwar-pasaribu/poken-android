package id.unware.poken.ui.seller.presenter;

import id.unware.poken.domain.Product;

/**
 * @author Anwar Pasaribu
 * @since Jun 17 2017
 */

public interface ISellerPagePresenter {
    void getSellerPageProductData(long sellerId);

    void startProductDetail(Product product);

    void startDetailScreen(Product product);

    void subscribeOnSeller(long sellerId, boolean isSubscribe);

    void getMoreSellerPageProductData(long sellerId, int page);
}
