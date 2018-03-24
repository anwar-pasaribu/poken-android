package id.unware.poken.ui.payment.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.UserBank;
import id.unware.poken.domain.UserBankDataRes;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.payment.presenter.IPaymentModelPresenter;
import id.unware.poken.ui.shoppingorder.presenter.IShoppingOrderModelPresenter;
import retrofit2.Response;

/**
 * Created by PID-T420S on 10/20/2017.
 */

public class PaymentModel extends MyCallback implements IPaymentModel {

    private static final String TAG = "ShoppingOrderModel";

    final private PokenRequest req;

    private IPaymentModelPresenter presenter;

    public PaymentModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }


    @Override
    public void patchOrderDetailsStatus(IPaymentModelPresenter presenter, long orderDetailsId, int orderStatus) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> postBody = new HashMap<>();
        postBody.put("order_status", String.valueOf(orderStatus));

        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
            // noinspection unchecked
            req.patchOrderDetailsStatus(
                    orderDetailsId,
                    PokenCredentials.getInstance().getCredentialHashMap(),
                    postBody)
                    .enqueue(this);
        }
    }

    @Override public void getPokenBankList(IPaymentModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
            // noinspection unchecked
            req.getBankList(
                    PokenCredentials.getInstance().getCredentialHashMap())
                    .enqueue(this);
        }

    }

    @Override
    public void onSuccess(Response response) {
        Object o = response.body();

        if (o instanceof OrderDetail) {
            presenter.updateViewState(UIState.FINISHED);

            OrderDetail orderDetail = (OrderDetail) o;
            Utils.Logs('v', TAG, "New/Updated order details. Address book id: " + orderDetail.address_book_id);
            presenter.onOrderDetailCreatedOrUpdated(orderDetail);

        } else if (o instanceof UserBankDataRes) {

            presenter.updateViewState(UIState.FINISHED);

            ArrayList<UserBank> userBankArrayList = new ArrayList<>(((UserBankDataRes) o).results);
            Utils.Logs('i', TAG, "User bank list size: " + userBankArrayList.size());

            this.presenter.onBankListResponse(userBankArrayList);

        }
    }

    @Override
    public void onMessage(String msg, int status) {

        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            presenter.updateViewState(UIState.ERROR);
        }

    }

    @Override
    public void onFinish() {

    }
}
