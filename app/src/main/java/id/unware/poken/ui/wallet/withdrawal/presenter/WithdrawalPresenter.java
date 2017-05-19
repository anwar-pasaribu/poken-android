package id.unware.poken.ui.wallet.withdrawal.presenter;


import java.util.ArrayList;

import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.PojoBank;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.pojo.PojoUserBank;
import id.unware.poken.pojo.PojoWalletConfig;
import id.unware.poken.pojo.PojoWithdrawPending;
import id.unware.poken.pojo.PojowithdrawAdd;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.wallet.withdrawal.model.IWithdrawalModel;
import id.unware.poken.ui.wallet.withdrawal.view.FragmentWalletWithdrawal;

/**
 * Created by marzellaalfamega on 2/7/17.
 */

public class WithdrawalPresenter implements IWithdrawalPresenter, VolleyResultListener {

    private final String TAG = "WithdrawalPresenter";

    private FragmentWalletWithdrawal mView;
    private IWithdrawalModel mModel;

    public WithdrawalPresenter(FragmentWalletWithdrawal mView, IWithdrawalModel model) {
        this.mView = mView;
        this.mModel = model;
    }

    @Override
    public void getWalletInfo() {
        mModel.getWalletInfo(mView.getArguments(), this);
    }

    @Override
    public void successGetWalletInfo(String strUserBalance, PojoWalletConfig walletConfig) {

        Utils.Log(TAG, "Wallet info. User balance: " + strUserBalance);

        mView.showWithdrawalAmount(
                strUserBalance,
                String.valueOf(walletConfig.getWalletMinWithdraw())
        );
    }

    @Override
    public void successGetUserBanks(ArrayList<PojoUserBank> listUserBanks) {
        Utils.Log(TAG, "On succes get user bank list. Size: " + listUserBanks.size());

        mView.setUserBanks(listUserBanks);
        mView.refreshBankList();
    }

    @Override
    public void getBankList() {
        mModel.getBankList(mView.getArguments(), this);
    }

    @Override
    public void successGetBankList(ArrayList<PojoBank> listBank) {
        Utils.Log(TAG, "Bank list size: " + listBank.size());
        mView.showAddBankAccount(listBank);
    }

    @Override
    public void requestWithdrawal() {
        if (mView.isWithdrawReady()) {

            Utils.Log(TAG, "Request withdrawal begins.");

            mModel.requestWithdrawal(mView.getParentView(),
                    mView.getAmount(),
                    mView.getSelectedUserBankId(),
                    mView.getPassword(),
                    this);
        }
    }

    @Override
    public void requestCancelWithdrawal() {
        Utils.Log(TAG, "Request cancel withdrawal.");
        mModel.requestCancelWithdrawal(mView.getParentView(), this);
    }

    @Override
    public void checkWithdrawStatus() {
        mModel.checkWithdrawStatus(mView.getArguments(), this);
    }

    @Override
    public void successGetWithdrawStatus(PojoWithdrawPending pojoWithdrawPending) {

        Utils.Log(TAG, "Withdraw Status --> " + pojoWithdrawPending.isPending());

        if (pojoWithdrawPending.isPending()) {
            mView.showTransferSummary(pojoWithdrawPending.getWithdraw());
        }
    }

    @Override
    public void getUserBanks() {
        mModel.getUserBanks(mView.getArguments(), this);
    }

    @Override
    public void onSuccessAddBank() {
        mView.refreshBankListRemote();
    }

    @Override
    public void onStart(PojoBase clazz) {
        Utils.Log(TAG, "Start volley request");
    }

    @Override
    public void onSuccess(PojoBase clazz) {

        Utils.Logs('i', TAG, "Volley request success. Class: " + clazz);

        // Show pending screen when withdrawal proceed
        if (clazz instanceof PojowithdrawAdd) {

            Utils.Log(TAG, "PojoWithdrawAdd found.");

            PojowithdrawAdd pojowithdrawAdd = (PojowithdrawAdd) clazz;
            mView.showTransferSummary(pojowithdrawAdd.withdraw);


            // Show input screen when withdrawal cancelled
        } else if (clazz.success == 1) {
            Utils.Log(TAG, "Request success with success=1 value. Msg: " + clazz.msg);
            mView.showInputWithdrawal();
        }
    }

    @Override
    public void onFinish(PojoBase clazz) {
        Utils.Logs('i', TAG, "Volley request finished");
    }

    @Override
    public boolean onError(PojoBase clazz) {
        Utils.Logs('e', TAG, "Volley request ERROR");
        return false;
    }
}
