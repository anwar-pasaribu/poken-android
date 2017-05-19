package id.unware.poken.ui.wallet.transactions.view;

import android.view.View;


import java.util.List;

import id.unware.poken.pojo.PojoUserTransaction;

/**
 * @author Anwar Pasaribu
 * @since Feb 06 2017
 */

public interface ITransactionsView {
    View getParentView();
    void showTransactionList(List<PojoUserTransaction> userTransactionList);

    void setParentView(View parentView);
}
