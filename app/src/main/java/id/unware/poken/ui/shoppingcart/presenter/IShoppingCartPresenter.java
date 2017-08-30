package id.unware.poken.ui.shoppingcart.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.ShoppingCart;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public interface IShoppingCartPresenter {

    void calculateSelectedShoppingCarts(ArrayList<ShoppingCart> shoppingCarts);

    void getShoppingCartData();

    void startShoppingOrderScreen();

    void deleteShoppingCartItem(int itemPos, long shoppingCartId);

    void onItemChecked(int itemPos, boolean isChecked, long shoppingCartId, int quantity, double price, ShoppingCart shoppingCart);

    void onItemQuantityChanges(int itemPos, long shoppingCartId, int quantity, double price, ShoppingCart shoppingCart);

    void addExtraNote(int adapterPosition, ShoppingCart item);

    boolean isItemSelected(ShoppingCart cartItem);
}
