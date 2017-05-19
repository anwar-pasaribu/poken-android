package id.unware.poken.ui.wallet.transactions.presenter;

import java.util.ArrayList;

import id.unware.poken.pojo.PojoUserBank;
import id.unware.poken.pojo.PojoUserTransaction;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.wallet.transactions.model.ITransactionsModel;
import id.unware.poken.ui.wallet.transactions.view.FragmentWalletTransactionHistory;

/**
 * @author Anwar Pasaribu
 * @since Feb 06 2017
 */

public class TransactionsPresenter implements
        ITransactionsPresenter {

    private final String TAG = "TransactionsPresenter";

    private FragmentWalletTransactionHistory view;
    private ITransactionsModel model;

    public TransactionsPresenter(FragmentWalletTransactionHistory view, ITransactionsModel model) {
        this.view = view;
        this.model = model;
    }

    public void loadWalletData() {
        Utils.Log(TAG, "Load wallet data arguments null --> " + (view.getArguments() == null));

        model.loadWalletData(view.getArguments(), this);
    }

    public ArrayList<PojoUserBank> getUserBankAccount() {
        return null;
    }

    public void updateViewState(UIState uiState) {
        Utils.Log(TAG, "Update view state: " + uiState);
        view.showViewState(uiState);

    }

    @Override
    public void successGetTransactionHistory(ArrayList<PojoUserTransaction> listUserTransaction) {
        Utils.Log(TAG, "Success get trans history size: " + listUserTransaction.size());
        view.showTransactionList(listUserTransaction);
    }

//    //////
//    // S: Volley listener
//    @Override
//    public void onStart(PojoBase clazz) {
//        updateViewState(UIState.LOADING);
//    }
//
//    @Override
//    public void onSuccess(PojoBase clazz) {
//        if (clazz instanceof PojoWalletData) {
//            PojoWalletData pojoWalletData = (PojoWalletData) clazz;
//
//            // Sent transaction histories to view.
//            List<PojoUserTransaction> pojoUserTransactionList = new ArrayList<>(pojoWalletData.getUserTransaction());
//            view.showTransactionList(pojoUserTransactionList);
//
//        }
//    }
//
//    @Override
//    public void onFinish(PojoBase clazz) {
//
//        // Only set FINISHED state when pojo response is non-null
//        if (clazz != null) {
//            updateViewState(UIState.FINISHED);
//        }
//    }
//
//    @Override
//    public boolean onError(PojoBase clazz) {
//        updateViewState(UIState.ERROR);
//        return false;
//    }
    // E: Volley listener
    //////
}
