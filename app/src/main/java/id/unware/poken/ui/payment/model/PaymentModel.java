package id.unware.poken.ui.payment.model;

import java.util.HashMap;
import java.util.Map;

import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.pojo.UIState;
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

    @Override
    public void onSuccess(Response response) {
        Object o = response.body();

        presenter.updateViewState(UIState.FINISHED);

        if (o instanceof OrderDetail) {

            OrderDetail orderDetail = (OrderDetail) o;
            Utils.Logs('v', TAG, "New/Updated order details. Address book id: " + orderDetail.address_book_id);
            presenter.onOrderDetailCreatedOrUpdated(orderDetail);

        }
    }

    @Override
    public void onMessage(String msg, int status) {

    }

    @Override
    public void onFinish() {

    }
}
