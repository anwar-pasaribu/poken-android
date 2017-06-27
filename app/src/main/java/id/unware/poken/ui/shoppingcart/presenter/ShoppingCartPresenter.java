package id.unware.poken.ui.shoppingcart.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingcart.model.IShoppingCartModel;
import id.unware.poken.ui.shoppingcart.view.IShoppingCartView;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ShoppingCartPresenter implements IShoppingCartPresenter, IShoppingCartModelPresenter {

    private final static String TAG = "ShoppingCartPresenter";

    private final IShoppingCartModel model;
    private final IShoppingCartView view;

    public ShoppingCartPresenter(IShoppingCartModel model, IShoppingCartView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void calculateSelectedShoppingCarts(ArrayList<ShoppingCart> shoppingCarts) {
        Utils.Log(TAG, "Selected item size: " + shoppingCarts.size());

        int totalQuantity = 0;
        double totalPrice = 0D;
        for (ShoppingCart item : shoppingCarts) {
            totalQuantity = totalQuantity + item.quantity;
            totalPrice = totalPrice + (item.product.price * item.quantity);
        }

        Utils.Log(TAG, "Total price: " + StringUtils.formatCurrency(String.valueOf(totalPrice)));
        Utils.Log(TAG, "Total quantity: " + totalQuantity);

        view.updatePriceGrandTotal(totalPrice);

        view.toggleContinueOrderButton(totalPrice > 0D);
    }

    @Override
    public void getShoppingCartData() {
        model.requestShoppingCartData(this);
    }

    @Override
    public void deleteShoppingCartItem(int itemPos, long shoppingCartId) {
        Utils.Logs('w', TAG, "Delete shopping cart ID: " + shoppingCartId + " pos: " + itemPos);
        // Remove shopping cart item
        model.deleteShoppingCartData(itemPos, shoppingCartId, this);
    }

    @Override
    public void onItemChecked(int itemPos, boolean isChecked, long shoppingCartId, int quantity, double price, ShoppingCart shoppingCart) {

        Utils.Logs('i', TAG, "Shopping cart item " + (isChecked? "checked" : "unchecked") +
                ". ID: " + shoppingCartId + " pos: " + itemPos + ", quantity: " + quantity +
                ", price: " + price);

        view.onShoppingCartItemSelected(itemPos, isChecked, shoppingCart);
    }

    @Override
    public void onItemQuantityChanges(int itemPos, long shoppingCartId, int quantity, double price, ShoppingCart shoppingCart) {

        Utils.Logs('i', TAG, "Shopping cart item quantity change. ID: " + shoppingCartId +
                " pos: " + itemPos + ", quantity: " + quantity + ", price: " + price);

        view.onShoppingCartItemQuantityChanges(itemPos, shoppingCart);
    }

    @Override
    public void startShoppingOrderScreen() {
        Utils.Logs('i', TAG, "Start shopping order screen");
        view.openShoppingOrder();
    }

    @Override
    public void updateViewState(UIState uiState) {
        Utils.Logs('w', TAG, "Update view state: " + uiState);
        view.showViewState(uiState);
    }

    @Override
    public void onShoppingCartDataResponse(ArrayList<ShoppingCart> shoppingCarts) {
        view.populateShoppingCarts(shoppingCarts);
        view.updatePriceGrandTotal(0D);
        view.toggleContinueOrderButton(false);
    }

    @Override
    public void onShoppingCartDeleted(int deletedItemPos) {
        view.deleteShoppingCartItem(deletedItemPos);
    }
}
