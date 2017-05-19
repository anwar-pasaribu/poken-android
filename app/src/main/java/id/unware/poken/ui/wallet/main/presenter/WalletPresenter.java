package id.unware.poken.ui.wallet.main.presenter;

import java.util.ArrayList;

import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.pojo.PojoUserBank;
import id.unware.poken.pojo.PojoWalletData;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.wallet.main.model.IWalletModel;
import id.unware.poken.ui.wallet.main.view.FragmentWallet;

/**
 * @author Anwar Pasaribu
 * @since Feb 02 2017
 */

public class WalletPresenter implements IWalletPresenter, VolleyResultListener {

    private final String TAG = "FragmentDialogTransferSummary";

    private FragmentWallet view;
    private IWalletModel model;

    public WalletPresenter(FragmentWallet view, IWalletModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void loadWalletData() {
        Utils.Log(TAG, "Load wallet data");
        model.loadWalletData(view.getParentView(), this);
    }

    @Override
    public ArrayList<PojoUserBank> getUserBankAccount() {
        return model.getUserBankAccount();
    }

    @Override
    public void updateViewState(UIState uiState) {
        Utils.Log(TAG, "Update view state: " + uiState);
        view.showViewState(uiState);
    }

    //////
    // S: Volley listener
    @Override
    public void onStart(PojoBase clazz) {
        updateViewState(UIState.LOADING);
    }

    @Override
    public void onSuccess(PojoBase clazz) {
        if (clazz instanceof PojoWalletData) {
            PojoWalletData pojoWalletData = (PojoWalletData) clazz;
            model.saveUpdatedOn(pojoWalletData.getUpdatedOn());
            model.saveUserBankAccount(pojoWalletData.getUserBank());

            // Sent balance amount to Main Wallet view
            view.setBalance(pojoWalletData.getUserBalance());

            // Setup tabs: History, Top up, and Withdrawal
            view.setupViewPager(pojoWalletData);
        }
    }

    @Override
    public void onFinish(PojoBase clazz) {
        updateViewState(UIState.FINISHED);
    }

    @Override
    public boolean onError(PojoBase clazz) {

        return false;
    }
    // E: Volley listener
    //////
}
