package id.unware.poken.ui.wallet.withdrawal.view;

import android.view.View;

import java.util.ArrayList;

import id.unware.poken.pojo.PojoBank;
import id.unware.poken.pojo.PojoUserBank;
import id.unware.poken.pojo.PojoWithdraw;

/**
 *
 * Created by marzellaalfamega on 2/7/17.
 */

public interface IWithdrawalView {

    void refreshBankList();

    void addUserAddBank();

    void setParentView(View parentView);

    View getParentView();

    void showAddBankAccount(ArrayList<PojoBank> listBank);

    /**
     * Show minimum and maximum withdrawal amount
     */
    void showWithdrawalAmount(String strMaxAmount, String strMinAmount);

    /**
     * Display withdrawal transfer summary.
     *
     * @param pojoWithdraw Withdrawal info to show.
     */
    void showTransferSummary(PojoWithdraw pojoWithdraw);

    /**
     * Show withdrawal input amount.
     */
    void showInputWithdrawal();

    void refreshBankListRemote();

    void setUserBanks(ArrayList<PojoUserBank> listUserBanks);

    boolean isWithdrawReady();

    String getAmount();

    String getSelectedUserBankId();

    String getPassword();

    void checkWithdrawStatus();
}
