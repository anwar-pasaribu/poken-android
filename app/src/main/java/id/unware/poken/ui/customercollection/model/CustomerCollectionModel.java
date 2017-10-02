package id.unware.poken.ui.customercollection.model;

import java.util.ArrayList;

import id.unware.poken.domain.CustomerCollection;
import id.unware.poken.domain.CustomerCollectionDataRes;
import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.customercollection.presenter.ICustomerCollectionModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 12 2017
 */

public class CustomerCollectionModel extends MyCallback implements ICustomerCollectionModel {

    private static final String TAG = "CustomerCollectionModel";
    final private PokenRequest req;

    private ICustomerCollectionModelPresenter presenter;

    public CustomerCollectionModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestCustomerCollectionData(ICustomerCollectionModelPresenter presenter) {

        this.presenter = presenter;

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // This req. response: ShoppingOrderDataRes
        req.reqCustomerCollectionContent(
                PokenCredentials.getInstance().getCredentialHashMap()
        ).enqueue(this);

    }

    @Override
    public void onSuccess(Response response) {

        Utils.Log(TAG, "Success response: " + response.toString());

        presenter.updateViewState(UIState.FINISHED);

        ArrayList<CustomerCollection> items = new ArrayList<>();
        items.addAll(((CustomerCollectionDataRes) response.body()).results);
        presenter.onCustomerCollectionDataResponse(items);
    }

    @Override
    public void onMessage(String msg, int status) {
        Utils.Logs('v', TAG, "Message from network req: " + msg);
        Utils.Logs('v', TAG, "Status from network req: " + status);

        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            presenter.updateViewState(UIState.ERROR);
        }
    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
