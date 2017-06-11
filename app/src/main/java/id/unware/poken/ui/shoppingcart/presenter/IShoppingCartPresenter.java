package id.unware.poken.ui.shoppingcart.presenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public interface IShoppingCartPresenter {
    void getShoppingCartData();
    void deleteShoppingCartItem(long shoppingCartId);

    void startShoppingOrderScreen();
}
