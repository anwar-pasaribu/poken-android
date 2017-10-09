package id.unware.poken.ui.shoppingcartnew.presenter;

import id.unware.poken.domain.Product;

/**
 * Created by PID-T420S on 10/5/2017.
 */
public interface INewlyShoppingCartPresenter {
    void loadLastUsedAddressBook();

    void loadShippingRates(String destinationZipCode);

    void postNewShoppingCart(Product product, int quantity);

    void loadRatesEstimation(long productId, long addressBookId);

    void startAddNewShoppingCart(long productId,
                                 int quantity,
                                 long shippingOptionId,
                                 double shippingFee,
                                 String shippingService,
                                 boolean continueToPayment);
}
