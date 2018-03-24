package id.unware.poken.ui.payment.presenter;

/**
 * Created by PID-T420S on 10/20/2017.
 */

public interface IPaymentPresenter {

    void beginOrder(long orderId, int orderStatus);

    void loadAvailablePokenBankList();

    void copyBankAccountNumber(String accountNumber);
}
