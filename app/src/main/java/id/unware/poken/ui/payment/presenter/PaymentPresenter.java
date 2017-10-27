package id.unware.poken.ui.payment.presenter;

import id.unware.poken.domain.OrderDetail;
import id.unware.poken.models.OrderStatus;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.payment.model.IPaymentModel;
import id.unware.poken.ui.payment.view.IPaymentView;

/**
 * Created by PID-T420S on 10/20/2017.
 */

public class PaymentPresenter implements IPaymentPresenter, IPaymentModelPresenter {

    private final static String TAG = "PaymentPresenter";

    private final IPaymentModel model;
    private final IPaymentView view;

    public PaymentPresenter(IPaymentModel model, IPaymentView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void beginOrder(long orderId, int orderStatus) {

        if (orderStatus == OrderStatus.INITIALIZE) {
            model.patchOrderDetailsStatus(
                    this,
                    orderId,
                    OrderStatus.ORDERED
            );
        } else {
            Utils.Logs('w', TAG, "No need to update order.");
            // Open shopping payment summary directly
            view.openShoppingSummaryScreen();
        }
    }

    @Override
    public void updateViewState(UIState uiState) {

        if (view.isActivityFinishing()) return;

        view.showViewState(uiState);
    }

    @Override
    public void onOrderDetailCreatedOrUpdated(OrderDetail orderDetail) {

        if (view.isActivityFinishing()) return;

        Utils.Logs('i', TAG, "Created/updated order detail id: " + orderDetail.id + ", status: " + orderDetail.order_status);
        Utils.Logs('i', TAG, "Created/updated order detsil.address_book_id: " + orderDetail.address_book_id);

        view.openShoppingSummaryScreen();
    }
}
