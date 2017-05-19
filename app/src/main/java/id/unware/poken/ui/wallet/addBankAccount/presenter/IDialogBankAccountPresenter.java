package id.unware.poken.ui.wallet.addBankAccount.presenter;

import java.util.ArrayList;

import id.unware.poken.pojo.PojoBank;

/**
 * Created by marzellaalfamega on 2/13/17.
 */
public interface IDialogBankAccountPresenter {
    void getBankList();

    void successGetBankList(ArrayList<PojoBank> listBank);

    void createBankAccount();
}
