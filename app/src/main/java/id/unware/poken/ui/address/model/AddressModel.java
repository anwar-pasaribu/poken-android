package id.unware.poken.ui.address.model;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.AddressBookDataRes;
import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.address.presenter.IAddressModelPresenter;
import retrofit2.Response;

/**
 * Created by PID-T420S on 9/26/2017.
 */

public class AddressModel extends MyCallback implements IAddressModel {

    private static final String TAG = "AddressModel";

    final private PokenRequest req;

    private IAddressModelPresenter presenter;
    private boolean isUpdating = false;

    public AddressModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void getAddressBookData(IAddressModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // noinspection unchecked
        req.reqAddressBookContent(
                PokenCredentials.getInstance().getCredentialHashMap()
        ).enqueue(this);
    }

    @Override
    public void postNewAddressBook(IAddressModelPresenter presenter, AddressBook addressBook) {

        this.isUpdating = false;

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // HashMap<String, String> newAddressMap = new HashMap<>();
        // newAddressMap.put()
        // noinspection unchecked
        req.postNewAddressBook(
                "application/json",
                PokenCredentials.getInstance().getCredentialHashMap(),
                addressBook
        ).enqueue(this);
    }

    @Override
    public void deleteAddressBookItem(IAddressModelPresenter presenter, AddressBook addressBook) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // HashMap<String, String> newAddressMap = new HashMap<>();
        // newAddressMap.put()
        // noinspection unchecked
        req.deleteAddressBookContent(
                PokenCredentials.getInstance().getCredentialHashMap(),
                addressBook.id
        ).enqueue(this);
    }

    @Override
    public void patchAddressBookChanges(IAddressModelPresenter presenter, AddressBook addressBook) {

        this.isUpdating = true;

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // HashMap<String, String> newAddressMap = new HashMap<>();
        // newAddressMap.put()
        // noinspection unchecked
        req.patchAddressBookChanges(
                "application/json",
                addressBook.id,
                PokenCredentials.getInstance().getCredentialHashMap(),
                addressBook
        ).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {
        Utils.Log(TAG, "Success response: " + response.toString());

        Object o = response.body();

        presenter.updateViewState(UIState.FINISHED);

        if (o instanceof AddressBookDataRes) {

            AddressBookDataRes addressBookDataRes = (AddressBookDataRes) o;
            Utils.Logs('v', TAG, "All address book res size: " + addressBookDataRes.results.size());
            presenter.onAddressBookContentResponse(addressBookDataRes.results);

        } else if (o instanceof AddressBook) {

            AddressBook addressBook = (AddressBook) o;
            Utils.Logs('i', TAG, "Created/updated address book: " + addressBook.toString());

            if (this.isUpdating) {
                presenter.onAddressBookUpdated(addressBook);
            } else {
                presenter.onAddressBookCreated(addressBook);
            }

        } else {
            presenter.onAddressBookDeleted();
        }
    }

    @Override
    public void onMessage(String msg, int status) {
        Utils.Logs('v', TAG, "Message from network req: " + msg);
        Utils.Logs('v', TAG, "Status from network req: " + status);
        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            presenter.updateViewState(UIState.ERROR);
        }
        presenter.onNetworkMessage(msg, status);
    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
