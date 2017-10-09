package id.unware.poken.ui.shoppingcartnew.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ShippingRates;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
import id.unware.poken.ui.shoppingcartnew.model.INewlyShoppingCartModel;
import id.unware.poken.ui.shoppingcartnew.view.INewlyShoppingCartView;

/**
 * Created by PID-T420S on 10/5/2017.
 */
public class NewlyShoppingCartPresenter implements INewlyShoppingCartPresenter, INewlyShoppingCartModelPresenter {

    private AddressBook selectedAddressBook;

    private boolean isContinueToPayment = false;

    private final INewlyShoppingCartModel model;
    private final INewlyShoppingCartView view;


    public NewlyShoppingCartPresenter(INewlyShoppingCartModel model, INewlyShoppingCartView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void loadLastUsedAddressBook() {
        model.requestLastUsedAddressBook(this);
    }

    @Override
    public void loadShippingRates(String destinationZipCode) {

    }

    @Override
    public void postNewShoppingCart(Product product, int quantity) {

    }

    @Override
    public void loadRatesEstimation(long productId, long addressBookId) {
        model.requestRatesEstimation(this, productId, addressBookId);
    }

    @Override
    public void startAddNewShoppingCart(long productId, int quantity, long shippingOptionId,
                                        double shippingFee, String shippingService, boolean continueToPayment) {
        this.isContinueToPayment = continueToPayment;
        model.postProductToShoppingCart(productId, quantity, shippingOptionId, shippingFee, shippingService, this);
    }

    @Override
    public void updateViewState(UIState uiState) {
        if (view.isActivityFinishing()) return;
        view.showViewState(uiState);
    }

    @Override
    public void onRateEstimateResponse(ArrayList<ShippingRates> shippingRatesList) {
        if (view.isActivityFinishing()) return;

        if (shippingRatesList.size() > 0) {

            view.populateShippingRates(shippingRatesList);

        }
    }

    @Override
    public void onAddressBookListResponse(ArrayList<AddressBook> addressBookArrayList) {

        if (view.isActivityFinishing()) return;

        if (addressBookArrayList.size() > 0) {
            view.showFirstShippingAddress(addressBookArrayList.get(0));
            this.selectedAddressBook = addressBookArrayList.get(0);
        }

        view.showNoShippingAddressIndicator(addressBookArrayList.isEmpty());
    }

    @Override
    public void onNewShoppingCartAdded(ShoppingCart shoppingCart) {
        if (isContinueToPayment) {
            this.view.showShoppingPaymentScreen(shoppingCart);
        } else {
            this.view.showPreviousScreen(shoppingCart);

        }
    }
}
