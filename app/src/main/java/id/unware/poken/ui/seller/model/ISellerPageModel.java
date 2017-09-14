package id.unware.poken.ui.seller.model;

import id.unware.poken.ui.seller.presenter.ISellerPageModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public interface ISellerPageModel {
    void requestSellerData(ISellerPageModelPresenter presenter, long sellerId);
    void requestSubscription(ISellerPageModelPresenter presenter, long sellerId, boolean isSubscribe);
}
