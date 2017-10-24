package id.unware.poken.ui.shoppingorder.view;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public interface IShoppingOrderView extends BaseView {

    boolean isOrderReady();

    void openPaymentScreen(OrderDetail currentOrderDetails);

    void setupShippingReceiver(AddressBook addressBook);

    void setupSelectedProduct(ShoppingCart shoppingCart);

    void setupSelectedProducts(ArrayList<ShoppingCart> shoppingCarts);

    void setupShippingMethod(Shipping shipping);

    void showTotalAmount(double grandTotal);

    void showAddressBookScreen(boolean isAddressBookAvailable);

    void showNoReceiverAddressView(boolean isShow);

    void showOrderId(String orderDetailsRef, long orderDetailsId, long orderedProductId);

    void populateAddressBookList(ArrayList<AddressBook> addressBookArrayList);

    void showMessage(String msg, int messageStatus);

    long[] getSelectedShoppingCartIds();

    void showSelectedProductDialog();

    void showMultiSelectedProduct(int selectedProductSize);

    void setupPaymentView(OrderDetail orderDetail);

    void showPayNowView(boolean isShow);

    void showViewStatusBooked();

    void showViewStatusPaid();

    void showViewStatusSent();

    void showViewStatusReceived();

    void showViewStatusSuccess();

    void showViewStatusReturn();

}
