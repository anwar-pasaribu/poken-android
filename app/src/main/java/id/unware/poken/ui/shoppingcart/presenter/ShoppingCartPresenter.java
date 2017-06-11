package id.unware.poken.ui.shoppingcart.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
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
    public void getShoppingCartData() {
        model.requestShoppingCartData(this);
    }

    @Override
    public void deleteShoppingCartItem(long shoppingCartId) {
        Utils.Logs('w', TAG, "Delete shopping cart ID: " + shoppingCartId);
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
    }
}
