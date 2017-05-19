package id.unware.poken.ui.wallet.addBankAccount.model;

import android.os.Bundle;
import android.view.View;

import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoBank;
import id.unware.poken.ui.wallet.addBankAccount.presenter.IDialogBankAccountPresenter;

/**
 * Created by marzellaalfamega on 2/13/17.
 */

public class DialogBankAccountModel implements IDialogBankAccountModel {
    @Override
    public void getBankList(Bundle arguments, IDialogBankAccountPresenter listener) {
        listener.successGetBankList(arguments.<PojoBank>getParcelableArrayList(Constants.EXTRA_WITHDRAWAL_BANK_LIST));
    }

    @Override
    public void createBankAccount(View snackContainer,
                                  String bankId,
                                  String userBankAccountName,
                                  String userBankAccountNumber,
                                  String password,
                                  VolleyResultListener listener) {
        ControllerPaket.getInstance().createBankAccount(snackContainer, bankId, userBankAccountNumber, userBankAccountName,
                password, listener);
    }
}
