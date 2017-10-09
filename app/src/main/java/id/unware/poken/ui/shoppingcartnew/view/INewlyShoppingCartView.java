package id.unware.poken.ui.shoppingcartnew.view;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ShippingRates;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.ui.view.BaseView;

/**
 * Created by PID-T420S on 10/5/2017.
 */

public interface INewlyShoppingCartView extends BaseView {
    void showProductData(Product product);

    void startAddressBookScreen(long addressBookId);


    void populateShippingRates(ArrayList<ShippingRates> shippingRatesArrayList);

    void showFirstShippingAddress(AddressBook addressBook);

    void showNoShippingAddressIndicator(boolean isShippingAddressEmpty);

    void showShoppingPaymentScreen(ShoppingCart shoppingCart);

    void showPreviousScreen(ShoppingCart shoppingCart);
}
