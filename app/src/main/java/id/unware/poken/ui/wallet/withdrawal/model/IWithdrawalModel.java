package id.unware.poken.ui.wallet.withdrawal.model;

import android.os.Bundle;
import android.view.View;

import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.ui.wallet.withdrawal.presenter.IWithdrawalPresenter;

/**
 * Created by marzellaalfamega on 2/7/17.
 */
public interface IWithdrawalModel {

    void getWalletInfo(Bundle arguments, IWithdrawalPresenter presenter);

    void getBankList(Bundle arguments, IWithdrawalPresenter listener);

    void requestCancelWithdrawal(View v, VolleyResultListener listener);
    void requestWithdrawal(View snackContainer, String amount, String selectedUserBankId, String password, VolleyResultListener listener);

    void checkWithdrawStatus(Bundle arguments, IWithdrawalPresenter listener);

    void getUserBanks(Bundle arguments, IWithdrawalPresenter listener);
}
