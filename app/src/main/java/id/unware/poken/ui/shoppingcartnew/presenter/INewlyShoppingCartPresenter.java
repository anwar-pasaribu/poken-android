package id.unware.poken.ui.shoppingcartnew.presenter;

import id.unware.poken.domain.AddressBook;

/**
 * Created by PID-T420S on 10/5/2017.
 */
public interface INewlyShoppingCartPresenter {
    void loadLastUsedAddressBook();

    void startAddNewShoppingCart(long productId,
                                 int quantity,
                                 long shippingOptionId,
                                 double shippingFee,
                                 String shippingService,
                                 boolean continueToPayment);

    void loadRates(long productId, int productQuantity, AddressBook selectedAddressBook);
}
