package id.unware.poken.ui.shoppingorder.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingcart.model.IShoppingCartModel;
import id.unware.poken.ui.shoppingcart.presenter.IShoppingCartModelPresenter;
import id.unware.poken.ui.shoppingcart.presenter.IShoppingCartPresenter;
import id.unware.poken.ui.shoppingcart.view.IShoppingCartView;
import id.unware.poken.ui.shoppingorder.model.IShoppingOrderModel;
import id.unware.poken.ui.shoppingorder.view.IShoppingOrderView;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ShoppingOrderPresenter implements IShoppingOrderPresenter, IShoppingOrderModelPresenter {

    private final static String TAG = "ShoppingOrderPresenter";

    private final IShoppingOrderModel model;
    private final IShoppingOrderView view;

    public ShoppingOrderPresenter(IShoppingOrderModel model, IShoppingOrderView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void getShoppingOrderData() {
        model.requestShoppingOrderData(this);
    }

    @Override
    public void startPaymentScreen() {
        Utils.Logs('i', TAG, "Start Payment screen");
        view.openPaymentScreen();
    }

    @Override
    public void updateViewState(UIState uiState) {
        Utils.Logs('w', TAG, "Update view state: " + uiState);
        view.showViewState(uiState);
    }

    @Override
    public void onShoppingOrderDataResponse(ArrayList<ShoppingOrder> shoppingOrders) {
        Utils.Logs('i', TAG, "onShoppingOrderDataResponse. Size: " + shoppingOrders.size());

        if (shoppingOrders.size() > 0) {

            ShoppingOrder shoppingOrder = shoppingOrders.get(0);
            OrderDetail orderDetail = shoppingOrder.order_details;
            AddressBook addressBook = orderDetail.address_book;
            Shipping shipping = orderDetail.shipping;
            Product product = shoppingOrder.shopping_carts.get(0).product;

            view.setupShippingReceiver(addressBook);

            view.setupSelectedProduct(product);

            view.setupShippingMethod(shipping);
        }
    }
}
