package id.unware.poken.ui.wallet.transactions.model;

import android.os.Bundle;

import id.unware.poken.ui.wallet.transactions.presenter.ITransactionsPresenter;


/**
 * @author Anwar Pasaribu
 * @since Feb 06 2017
 */

public interface ITransactionsModel {

    void loadWalletData(Bundle argument, ITransactionsPresenter listener);
}
