package id.unware.poken.ui.payment.model;

import id.unware.poken.ui.payment.presenter.IPaymentModelPresenter;

/**
 * Created by PID-T420S on 10/20/2017.
 */

public interface IPaymentModel {
    void patchOrderDetailsStatus(IPaymentModelPresenter presenter, long orderDetailsId, int orderStatus);
}
