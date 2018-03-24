package id.unware.poken.ui.payment.view;

import java.util.ArrayList;

import id.unware.poken.domain.UserBank;
import id.unware.poken.ui.view.BaseView;

/**
 * Created by PID-T420S on 10/20/2017.
 */

public interface IPaymentView extends BaseView {
    void openShoppingSummaryScreen();

    void populateBankList(ArrayList<UserBank> userBankArrayList);

    void copyBankAccountNumber(String accountNumber);

}
