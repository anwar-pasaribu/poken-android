package id.unware.poken.ui.shoppingorder.view;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public interface IShoppingOrderView extends BaseView {
    void openPaymentScreen();

    void setupShippingReceiver(AddressBook addressBook);
    void setupSelectedProduct(Product product);
    void setupSelectedProducts(ArrayList<Product> products);
    void setupShippingMethod(Shipping shipping);
    void showTotalAmount(double grandTotal);

    void showAddressBookScreen();
    void showNoReceiverAddressView(boolean isShow);
    void showOrderId(String orderId);

    void pupulateAddressBookList(ArrayList<AddressBook> addressBookArrayList);
}
