package id.unware.poken.ui.shoppingcart.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public interface IShoppingCartModelPresenter extends BasePresenter {
    void onShoppingCartDataResponse(ArrayList<ShoppingCart> shoppingCarts);
    void onShoppingCartDeleted(int deletedItemPos);

    void onShoppingCartItemUpdated(ShoppingCart shoppingCart);

}
