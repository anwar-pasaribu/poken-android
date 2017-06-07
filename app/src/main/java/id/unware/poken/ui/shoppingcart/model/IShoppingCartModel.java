package id.unware.poken.ui.shoppingcart.model;

import id.unware.poken.ui.shoppingcart.presenter.IShoppingCartModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public interface IShoppingCartModel {
    void requestShoppingCartData(IShoppingCartModelPresenter presenter);
}
