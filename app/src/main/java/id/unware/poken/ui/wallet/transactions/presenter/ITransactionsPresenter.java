package id.unware.poken.ui.wallet.transactions.presenter;


import java.util.ArrayList;

import id.unware.poken.pojo.PojoUserTransaction;

/**
 * @author Anwar Pasaribu
 * @since Feb 06 2017
 */

public interface ITransactionsPresenter {
    void successGetTransactionHistory(ArrayList<PojoUserTransaction> listUserTransaction);
}
