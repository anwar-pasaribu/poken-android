package id.unware.poken.ui.wallet.addBankAccount.model;

import android.os.Bundle;
import android.view.View;

import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.ui.wallet.addBankAccount.presenter.IDialogBankAccountPresenter;

/**
 * Created by marzellaalfamega on 2/13/17.
 */
public interface IDialogBankAccountModel {
    void getBankList(Bundle arguments, IDialogBankAccountPresenter listener);

    void createBankAccount(View snackContainer,
                           String bankId,
                           String userBankAccountName,
                           String userBankAccountNumber,
                           String password,
                           VolleyResultListener listener);
}
