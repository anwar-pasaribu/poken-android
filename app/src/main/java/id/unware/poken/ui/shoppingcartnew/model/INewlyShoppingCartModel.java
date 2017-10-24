package id.unware.poken.ui.shoppingcartnew.model;

import id.unware.poken.ui.shoppingcartnew.presenter.INewlyShoppingCartModelPresenter;

/**
 * Created by PID-T420S on 10/5/2017.
 */

public interface INewlyShoppingCartModel {

    void postProductToShoppingCart (
            long productId,
            int quantity,
            long shippingOptionId,
            double shippingFee,
            String shippingService,
            INewlyShoppingCartModelPresenter presenter);

    void requestRatesEstimation(INewlyShoppingCartModelPresenter presenter, long productId, int productQuantity, long addressBookId);

    void requestLastUsedAddressBook(INewlyShoppingCartModelPresenter presenter);
}
