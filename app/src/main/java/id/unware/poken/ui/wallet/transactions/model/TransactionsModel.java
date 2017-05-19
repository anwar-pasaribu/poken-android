package id.unware.poken.ui.wallet.transactions.model;

import android.os.Bundle;

import java.util.ArrayList;

import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoUserTransaction;
import id.unware.poken.ui.wallet.transactions.presenter.ITransactionsPresenter;

/**
 * @author Anwar Pasaribu
 * @since Feb 02 2017
 */

public class TransactionsModel implements ITransactionsModel {

    @Override
    public void loadWalletData(Bundle argument, ITransactionsPresenter listener) {
        ArrayList<PojoUserTransaction> pojoUserTransactions = argument.getParcelableArrayList(Constants.EXTRA_LIST_WALLET_TRANSACTION_HISTORY);
        listener.successGetTransactionHistory(pojoUserTransactions);
    }
}