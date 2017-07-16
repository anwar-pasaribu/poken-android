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
    public void addNewAddressBook(AddressBook addressBook) {
        model.postNewAddressBook(this, addressBook);
    }

    @Override
    public void getAddressBookData() {
        model.getAddressBookData(this);
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
    public void startAddressBookScreen() {
        view.showAddressBookScreen();
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

            view.showNoReceiverAddressView(false);

            ShoppingOrder shoppingOrder = shoppingOrders.get(0);
            OrderDetail orderDetail = shoppingOrder.order_details;
            AddressBook addressBook = orderDetail.address_book;
            Shipping shipping = shoppingOrder.shopping_carts.get(0).shipping;
            Product product = shoppingOrder.shopping_carts.get(0).product;

            double grandTotal = 0D;
            ArrayList<Product> productArrayList = new ArrayList<>();
            for (ShoppingCart item : shoppingOrder.shopping_carts) {
                productArrayList.add(item.product);

                // Count Product Grand total + shipping fee
                grandTotal += (item.product.price * item.quantity) + item.shipping.fee;
            }

            view.showOrderId(orderDetail.order_id);

            view.showTotalAmount(grandTotal);

            view.setupShippingReceiver(addressBook);

            if (productArrayList.size() <= 1) {
                view.setupSelectedProduct(product);
            } else {
                view.setupSelectedProducts(productArrayList);
            }

            view.setupShippingMethod(shipping);
        } else {
            view.showNoReceiverAddressView(true);
        }
    }

    @Override
    public void onAddressBookCreated(AddressBook newlyCreatedAddressBook) {
        view.setupShippingReceiver(newlyCreatedAddressBook);
    }

    @Override
    public void onAddressBookContentResponse(ArrayList<AddressBook> addressBookArrayList) {
        view.pupulateAddressBookList(addressBookArrayList);
    }
}
