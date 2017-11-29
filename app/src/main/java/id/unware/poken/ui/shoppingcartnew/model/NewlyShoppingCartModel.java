package id.unware.poken.ui.shoppingcartnew.model;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.domain.AddressBookDataRes;
import id.unware.poken.domain.ShippingRatesDataRes;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.ui.shoppingcartnew.presenter.INewlyShoppingCartModelPresenter;
import retrofit2.Response;

/**
 * Created by PID-T420S on 10/5/2017.
 */

public class NewlyShoppingCartModel extends MyCallback implements INewlyShoppingCartModel {

    final private PokenRequest req;
    private INewlyShoppingCartModelPresenter presenter;

    public NewlyShoppingCartModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void postProductToShoppingCart (
            long productId,
            int quantity,
            long shippingOptionId,
            double shippingFee,
            String shippingService,
            INewlyShoppingCartModelPresenter presenter) {

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> postBody = new HashMap<>();
        postBody.put("product_id", String.valueOf(productId));
        postBody.put("quantity", String.valueOf(quantity));  // Add one quantity
        postBody.put("shipping_id", String.valueOf(shippingOptionId));
        postBody.put("shipping_fee", String.valueOf((int) shippingFee));  // Add one quantity
        postBody.put("shipping_service", String.valueOf(shippingService));  // Add one quantity
        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
            // noinspection unchecked
            req.postNewOrUpdateShoppingCart(
                    PokenCredentials.getInstance().getCredentialHashMap(),
                    postBody)
                    .enqueue(this);
        }
    }

    @Override
    public void requestRatesEstimation(INewlyShoppingCartModelPresenter presenter,
                                       long productId, int productQuantity, long addressBookId) {

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("product_id", String.valueOf(productId));
        queryMap.put("product_quantity", String.valueOf(productQuantity));
        queryMap.put("address_book_id", String.valueOf(addressBookId));

        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
            // noinspection unchecked
            req.reqShippingRates(
                    PokenCredentials.getInstance().getCredentialHashMap(),
                    queryMap
            ).enqueue(this);
        }
    }

    @Override
    public void requestLastUsedAddressBook(INewlyShoppingCartModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
            // noinspection unchecked
            req.reqAddressBookContent(
                    PokenCredentials.getInstance().getCredentialHashMap()
            ).enqueue(this);
        }
    }

    @Override
    public void onSuccess(Response response) {
        Object o = response.body();
        if (o instanceof AddressBookDataRes) {

            this.presenter.onAddressBookListResponse(((AddressBookDataRes) o).results);

        } else if (o instanceof ShippingRatesDataRes) {

            // Finish specific initial data
            // and loading address book
            this.presenter.updateViewState(UIState.FINISHED);

            this.presenter.onRateEstimateResponse(((ShippingRatesDataRes) o).results);

        } else if (o instanceof ShoppingCart) {

            // Finish specific for shopping cart successfully added
            this.presenter.updateViewState(UIState.FINISHED);

            this.presenter.onNewShoppingCartAdded((ShoppingCart) o);
        }
    }

    @Override
    public void onMessage(String msg, int status) {
        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            MyLog.FabricLog(Log.ERROR, "NewlyShoppingCartModel request failed.");
            presenter.updateViewState(UIState.ERROR);
        }
    }

    @Override
    public void onFinish() {

    }
}
