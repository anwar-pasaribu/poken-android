package id.unware.poken.ui.shoppingcart.model;

import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.ui.shoppingcart.presenter.IShoppingCartModelPresenter;
import id.unware.poken.ui.shoppingcart.presenter.ShoppingCartPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public interface IShoppingCartModel {
    void requestShoppingCartData(IShoppingCartModelPresenter presenter);
    void deleteShoppingCartData(int deleteItemPos, long shoppingCartId, IShoppingCartModelPresenter presenter);

    void patchExtraNote(IShoppingCartModelPresenter presenter, ShoppingCart item);

    void updateItemQuantity(IShoppingCartModelPresenter presenter, ShoppingCart shoppingCart);
}
