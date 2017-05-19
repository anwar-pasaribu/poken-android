package id.unware.poken.ui.wallet.withdrawal.model;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoBank;
import id.unware.poken.pojo.PojoUserBank;
import id.unware.poken.pojo.PojoWalletConfig;
import id.unware.poken.pojo.PojoWithdrawPending;
import id.unware.poken.tools.MyLog;
import id.unware.poken.ui.wallet.withdrawal.presenter.IWithdrawalPresenter;


/**
 * Created by marzellaalfamega on 2/7/17.
 */

public class WithdrawalModel implements IWithdrawalModel {

    @Override
    public void getWalletInfo(Bundle args, IWithdrawalPresenter presenter) {

        // Trigger presenter to show withdrawal min and max amount.
        PojoWalletConfig pojoWalletConfig = args.getParcelable(Constants.EXTRA_WALLET_CONFIG);
        presenter.successGetWalletInfo(
                args.getString(Constants.EXTRA_WALLET_USER_BALANCE),
                pojoWalletConfig
        );
        Constants.MIN_WITHDRAW_VALUE = pojoWalletConfig.getWalletMinWithdraw();
    }

    @Override
    public void getBankList(Bundle args, IWithdrawalPresenter listener) {
        listener.successGetBankList(
                args.<PojoBank>getParcelableArrayList(Constants.EXTRA_WITHDRAWAL_BANK_LIST));
    }

    @Override
    public void requestCancelWithdrawal(View v, VolleyResultListener listener) {
        ControllerPaket.getInstance().cancelWithdraw(v, listener);
    }

    @Override
    public void requestWithdrawal(
            View v,
            String amount,
            String selectedUserBankId,
            String password,
            VolleyResultListener listener) {

        MyLog.FabricLog(Log.INFO, "Request withdrawal. Amount: " + amount + ", user bank id: " + selectedUserBankId);

        ControllerPaket.getInstance().requestWithdrawal(v, amount, selectedUserBankId, password, listener);
    }

    @Override
    public void checkWithdrawStatus(Bundle arguments, IWithdrawalPresenter listener) {
        listener.successGetWithdrawStatus(arguments.<PojoWithdrawPending>getParcelable(Constants.EXTRA_WITHDRAWAL_STATUS));
    }

    @Override
    public void getUserBanks(Bundle arguments, IWithdrawalPresenter listener) {
        listener.successGetUserBanks(
                arguments.<PojoUserBank>getParcelableArrayList(Constants.EXTRA_WITHDRAWAL_USER_BANK_LIST));

    }

}