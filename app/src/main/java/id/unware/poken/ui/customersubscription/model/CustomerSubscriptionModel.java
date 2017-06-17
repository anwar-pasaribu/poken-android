package id.unware.poken.ui.customersubscription.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.CustomerCollection;
import id.unware.poken.domain.CustomerCollectionDataRes;
import id.unware.poken.domain.CustomerSubscription;
import id.unware.poken.domain.CustomerSubscriptionDataRes;
import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.PokenRequest;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.customersubscription.presenter.ICustomerSubscriptionModelPresenter;
import okhttp3.Credentials;
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

        String credential = Credentials.basic("anwar", "anwar_poken17");
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", credential);

        // This req. response: CustomerSubscriptionDataRes
        req.reqCustomerSubscriptionContent(headerMap).enqueue(this);
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
