package id.unware.poken.ui.shoppingorder.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public interface IShoppingOrderModelPresenter extends BasePresenter {
    void onShoppingOrderDataResponse(ArrayList<ShoppingOrder> shoppingOrders);
}
