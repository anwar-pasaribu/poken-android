package id.unware.poken.ui.customerorder.presenter;

import id.unware.poken.domain.ShoppingOrder;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public interface IOrdersPresenter {
    void getOrdersData();

    void startOrderDetailScreen(ShoppingOrder shoppingOrder);
}
