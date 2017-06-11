package id.unware.poken.ui.customerorder.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public interface IOrdersModelPresenter extends BasePresenter {
    void onOrdersDataResponse(ArrayList<ShoppingOrder> shoppingOrders);
}
