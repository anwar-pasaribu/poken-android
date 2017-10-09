package id.unware.poken.ui.shoppingcartnew.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.ShippingRates;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * Created by PID-T420S on 10/5/2017.
 */
public interface INewlyShoppingCartModelPresenter extends BasePresenter {
    void onRateEstimateResponse(ArrayList<ShippingRates> shippingArrayList);

    void onAddressBookListResponse(ArrayList<AddressBook> addressBookArrayList);

    void onNewShoppingCartAdded(ShoppingCart shoppingCart);
}
