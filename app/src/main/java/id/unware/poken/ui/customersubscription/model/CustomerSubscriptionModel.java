package id.unware.poken.ui.customersubscription.model;

import java.util.ArrayList;

import id.unware.poken.domain.CustomerSubscription;
import id.unware.poken.domain.CustomerSubscriptionDataRes;
import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.customersubscription.presenter.ICustomerSubscriptionModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 13 2017
 */

public class CustomerSubscriptionModel extends MyCallback implements ICustomerSubscriptionModel {

    private static final String TAG = "CustomerSubscriptionModel";
    final private PokenRequest req;

    private ICustomerSubscriptionModelPresenter presenter;

    public CustomerSubscriptionModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestCustomerSubscriptionData(ICustomerSubscriptionModelPresenter presenter) {
        this.presenter = presenter;

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // This req. response: CustomerSubscriptionDataRes
        req.reqCustomerSubscriptionContent(
                PokenCredentials.getInstance().getCredentialHashMap()
        ).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {
        Utils.Log(TAG, "Success response: " + response.toString());

        presenter.updateViewState(UIState.FINISHED);

        ArrayList<CustomerSubscription> items = new ArrayList<>();
        items.addAll(((CustomerSubscriptionDataRes) response.body()).results);
        presenter.onCustomerSubscriptionDataResponse(items);
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
