package id.unware.poken.ui.shoppingorder.view;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public interface IShoppingOrderView extends BaseView {
    void openPaymentScreen();

    void setupShippingReceiver(AddressBook addressBook);
    void setupSelectedProduct(ShoppingCart shoppingCart);
    void setupSelectedProducts(ArrayList<ShoppingCart> shoppingCarts);
    void setupShippingMethod(Shipping shipping);
    void showTotalAmount(double grandTotal);

    void showAddressBookScreen(boolean isAddressBookAvailable);
    void showNoReceiverAddressView(boolean isShow);
    void showOrderId(String orderDetailUniqueId, long orderedProductId);

    void populateAddressBookList(ArrayList<AddressBook> addressBookArrayList);

    void showMessage(String msg, int messageStatus);

    long[] getSelectedShoppingCartIds();

    void showSelectedProductDialog();

    void showMultiSelectedProduct(boolean isMultiSelectedProduct);
}
