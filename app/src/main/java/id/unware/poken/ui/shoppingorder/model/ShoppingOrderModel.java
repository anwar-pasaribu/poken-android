package id.unware.poken.ui.shoppingorder.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.AddressBookDataRes;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.domain.ShoppingOrderDataRes;
import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.PokenRequest;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingorder.presenter.IShoppingOrderModelPresenter;
import okhttp3.Credentials;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ShoppingOrderModel extends MyCallback implements IShoppingOrderModel {

    private static final String TAG = "ShoppingOrderModel";

    final private PokenRequest req;

    private IShoppingOrderModelPresenter presenter;

    public ShoppingOrderModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestShoppingOrderData(IShoppingOrderModelPresenter presenter) {
        this.presenter = presenter;

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // This req. response: ShoppingOrderDataRes
        req.reqShoppingOrderContent(PokenCredentials.getInstance().getCredentialHashMap()).enqueue(this);
    }

    @Override
    public void postNewAddressBook(IShoppingOrderModelPresenter presenter, AddressBook addressBook) {

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // HashMap<String, String> newAddressMap = new HashMap<>();
        // newAddressMap.put()
        req.postNewAddressBook(
                "application/json",
                PokenCredentials.getInstance().getCredentialHashMap(),
                addressBook
        ).enqueue(this);
    }

    @Override
    public void getAddressBookData(IShoppingOrderModelPresenter presenter) {
        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        req.reqAddressBookContent(
                PokenCredentials.getInstance().getCredentialHashMap()
        ).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {

        Utils.Log(TAG, "Success response: " + response.toString());

        Object o = response.body();

        if (o instanceof ShoppingOrderDataRes) {

            presenter.updateViewState(UIState.FINISHED);

            ArrayList<ShoppingOrder> shoppingOrders = new ArrayList<>();
            shoppingOrders.addAll(((ShoppingOrderDataRes) response.body()).results);
            presenter.onShoppingOrderDataResponse(shoppingOrders);

        } else if (o instanceof AddressBook){

            AddressBook addressBook = (AddressBook) o;
            Utils.Logs('i', TAG, "Created address book: " + addressBook.toString());
            presenter.onAddressBookCreated(addressBook);

        } else if (o instanceof AddressBookDataRes) {

            AddressBookDataRes addressBookDataRes = (AddressBookDataRes) o;
            Utils.Logs('v', TAG, "All address book res size: " + addressBookDataRes.results.size());
            presenter.onAddressBookContentResponse(addressBookDataRes.results);

        }
    }

    @Override
    public void onMessage(String msg, int status) {
        Utils.Logs('v', TAG, "Message from network req: " + msg);
        Utils.Logs('v', TAG, "Status from network req: " + status);
    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
