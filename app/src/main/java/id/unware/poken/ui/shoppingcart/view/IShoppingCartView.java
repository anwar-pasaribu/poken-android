package id.unware.poken.ui.shoppingcart.view;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductImage;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public interface IShoppingCartView extends BaseView {
    void deleteShoppingCartItem(int deletedItemPos);

    void populateShoppingCarts(ArrayList<ShoppingCart> shoppingCarts);

    void onShoppingCartItemSelected(int itemPos, boolean isChecked, ShoppingCart shoppingCart);
    void onShoppingCartItemQuantityChanges(int itemPos, ShoppingCart shoppingCart);
    void openShoppingOrder();

    void toggleContinueOrderButton(boolean isActive);

    void updatePriceGrandTotal(double totalPrice);
}
