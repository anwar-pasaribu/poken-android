package id.unware.poken.ui.wallet.addBankAccount.presenter;

import java.util.ArrayList;

import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.PojoBank;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.ui.wallet.addBankAccount.model.IDialogBankAccountModel;
import id.unware.poken.ui.wallet.addBankAccount.view.IDialogBankAccountView;

/**
 * Created by marzellaalfamega on 2/13/17.
 *
 */

public class DialogBankAccountPresenter implements IDialogBankAccountPresenter, VolleyResultListener {

    private IDialogBankAccountView view;
    private IDialogBankAccountModel model;

    public DialogBankAccountPresenter(IDialogBankAccountView view, IDialogBankAccountModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void getBankList() {
        model.getBankList(view.getMyArguments(), this);
    }

    @Override
    public void successGetBankList(ArrayList<PojoBank> listBank) {
        view.setupBankList(listBank);
        view.setupBankListString();
        view.refreshBankList();
    }

    @Override
    public void createBankAccount() {
        model.createBankAccount(view.getParentView(),
                view.getBankId(),
                view.getUserBankAccountName(),
                view.getUserBankAccountNumber(),
                view.getPassword(),
                this);
    }

    @Override
    public void onStart(PojoBase clazz) {
        view.showLoading();
    }

    @Override
    public void onSuccess(PojoBase clazz) {
        view.successAddBank();
    }

    @Override
    public void onFinish(PojoBase clazz) {
        view.dismissLoading();
    }

    @Override
    public boolean onError(PojoBase clazz) {
        return false;
    }
}
