package id.unware.poken.ui.customerorder.model;

import java.util.ArrayList;

import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.domain.ShoppingOrderDataRes;
import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.customerorder.presenter.IOrdersModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class OrdersModel extends MyCallback implements IOrdersModel {

    private static final String TAG = "ShoppingOrderModel";

    final private PokenRequest req;

    private IOrdersModelPresenter presenter;

    public OrdersModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestOrdersData(IOrdersModelPresenter presenter) {
        this.presenter = presenter;

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // This req. response: ShoppingOrderDataRes
        req.reqShoppingOrderContent(
                PokenCredentials.getInstance().getCredentialHashMap()
        ).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {

        Utils.Log(TAG, "Success response: " + response.toString());

        presenter.updateViewState(UIState.FINISHED);

        ArrayList<ShoppingOrder> shoppingOrders = new ArrayList<>();
        shoppingOrders.addAll(((ShoppingOrderDataRes) response.body()).results);
        presenter.onOrdersDataResponse(shoppingOrders);
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
