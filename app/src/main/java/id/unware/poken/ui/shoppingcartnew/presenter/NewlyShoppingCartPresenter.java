package id.unware.poken.ui.shoppingcartnew.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ShippingRates;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingcartnew.model.INewlyShoppingCartModel;
import id.unware.poken.ui.shoppingcartnew.view.INewlyShoppingCartView;

/**
 * Created by PID-T420S on 10/5/2017.
 */
public class NewlyShoppingCartPresenter implements INewlyShoppingCartPresenter, INewlyShoppingCartModelPresenter {

    private static final String TAG = "NewlyShoppingCartPresenter";

    private AddressBook selectedAddressBook;
    private Product currentProduct;

    private boolean isContinueToPayment = false;

    private final INewlyShoppingCartModel model;
    private final INewlyShoppingCartView view;


    public NewlyShoppingCartPresenter(INewlyShoppingCartModel model, INewlyShoppingCartView view, Product product) {
        this.model = model;
        this.view = view;
        this.currentProduct = product;
    }

    @Override
    public void loadLastUsedAddressBook() {
        model.requestLastUsedAddressBook(this);
    }

    @Override
    public void startAddNewShoppingCart(long productId, int quantity, long shippingOptionId,
                                        double shippingFee, String shippingService, boolean continueToPayment) {

        // Validate
        if (shippingOptionId == -1) {
            view.showMessage(Constants.STATE_ERROR, "Tidak ada metode pengiriman yg dipilih");
            return;
        }

        if (StringUtils.isEmpty(shippingService)) {
            view.showMessage(Constants.STATE_ERROR, "Tidak ada metode pengiriman yg dipilih");
            return;
        }

        this.isContinueToPayment = continueToPayment;
        model.postProductToShoppingCart(productId, quantity, shippingOptionId, shippingFee, shippingService, this);
    }

    @Override
    public void loadRates(long productId, int productQuantity, AddressBook selectedAddressBook) {
        if (productId > 1 && productQuantity >= 1 && selectedAddressBook != null) {
            model.requestRatesEstimation(this, this.currentProduct.id, productQuantity, this.selectedAddressBook.id);
        } else {
            view.showMessage(Constants.STATE_ERROR, "Masalah terjadi saat cek ongkir.");
        }
    }

    @Override
    public void updateViewState(UIState uiState) {
        if (view.isActivityFinishing()) return;
        view.showViewState(uiState);
    }

    @Override
    public void onRateEstimateResponse(ArrayList<ShippingRates> shippingRatesList) {
        if (view.isActivityFinishing()) return;

        view.populateShippingRates(shippingRatesList);

        // No shipping method when address book is incomplete
        view.showNoShippingMethodAvailable(false);
    }

    @Override
    public void onAddressBookListResponse(ArrayList<AddressBook> addressBookArrayList) {

        if (view.isActivityFinishing()) return;

        if (addressBookArrayList.size() > 0 && addressBookArrayList.get(0).location != null) {
            view.showFirstShippingAddress(addressBookArrayList.get(0));
            this.selectedAddressBook = addressBookArrayList.get(0);

            model.requestRatesEstimation(this, this.currentProduct.id, 1, this.selectedAddressBook.id);
        } else {
            Utils.Logs('w', TAG, "Skip rates check due address inclomplete");

            // Stop loading indicator
            view.showViewState(UIState.NODATA);
        }

        if (addressBookArrayList.size() > 0) {
            if (addressBookArrayList.get(0).location == null) {
                // Address book incomplete
                view.showAddressBookIncomplete(true);

                // No shipping method when address book is incomplete
                view.showNoShippingMethodAvailable(true);
            }

            view.showNoShippingAddressIndicator(false);

        } else {
            view.showNoShippingAddressIndicator(true);
        }

    }

    @Override
    public void onNewShoppingCartAdded(ShoppingCart shoppingCart) {

        if (view.isActivityFinishing()) return;

        if (isContinueToPayment) {
            this.view.showShoppingPaymentScreen(shoppingCart);
        } else {
            this.view.showPreviousScreen(shoppingCart);

        }
    }
}
