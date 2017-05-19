package id.unware.poken.ui.wallet.withdrawal.presenter;

import java.util.ArrayList;

import id.unware.poken.pojo.PojoBank;
import id.unware.poken.pojo.PojoUserBank;
import id.unware.poken.pojo.PojoWalletConfig;
import id.unware.poken.pojo.PojoWithdrawPending;
import id.unware.poken.ui.wallet.addBankAccount.view.FragmentDialogBankAccount;

/**
 * Created by marzellaalfamega on 2/7/17.
 */
public interface IWithdrawalPresenter extends FragmentDialogBankAccount.DialogBankAccountListener {

    void getWalletInfo();
    void getBankList();
    void getUserBanks();

    void successGetBankList(ArrayList<PojoBank> listBank);

    /**
     * Trigger to display wallet info include: UserAmount, UserBanks, WalletConfig.
     */
    void successGetWalletInfo(String strUserBalance, PojoWalletConfig walletConfig);

    void successGetUserBanks(ArrayList<PojoUserBank> listUserBanks);

    void successGetWithdrawStatus(PojoWithdrawPending pojoWithdrawPending);

    void requestCancelWithdrawal();

    void requestWithdrawal();

    void checkWithdrawStatus();


}
