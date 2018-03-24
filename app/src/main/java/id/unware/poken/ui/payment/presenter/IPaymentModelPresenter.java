package id.unware.poken.ui.payment.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.UserBank;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * Created by PID-T420S on 10/20/2017.
 */

public interface IPaymentModelPresenter extends BasePresenter {
    void onOrderDetailCreatedOrUpdated(OrderDetail orderDetail);

    void onBankListResponse(ArrayList<UserBank> userBankArrayList);
}
