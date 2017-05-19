package id.unware.poken.ui.wallet.addBankAccount.view;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import id.unware.poken.pojo.PojoBank;

/**
 * Created by marzellaalfamega on 2/13/17.
 */
public interface IDialogBankAccountView {
    Bundle getMyArguments();

    void setupBankList(ArrayList<PojoBank> listBank);

    void setupBankListString();

    void refreshBankList();

    String getBankId();

    String getUserBankAccountName();

    String getUserBankAccountNumber();

    String getPassword();

    View getParentView();

    void showLoading();

    void successAddBank();

    void dismissLoading();
}
