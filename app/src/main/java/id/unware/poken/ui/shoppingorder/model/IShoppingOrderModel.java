package id.unware.poken.ui.shoppingorder.model;

import id.unware.poken.ui.shoppingorder.presenter.IShoppingOrderModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public interface IShoppingOrderModel {
    void requestShoppingOrderData(IShoppingOrderModelPresenter presenter);
}
