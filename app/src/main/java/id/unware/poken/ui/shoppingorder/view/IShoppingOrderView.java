package id.unware.poken.ui.shoppingorder.view;

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

    void setupShippingMethod(Shipping shipping);
}
