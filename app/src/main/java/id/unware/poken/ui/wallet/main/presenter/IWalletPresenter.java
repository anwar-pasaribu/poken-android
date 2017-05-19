package id.unware.poken.ui.wallet.main.presenter;

import java.util.ArrayList;

import id.unware.poken.pojo.PojoUserBank;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jan 30 2017
 */

public interface IWalletPresenter extends BasePresenter {
    void loadWalletData();

    ArrayList<PojoUserBank> getUserBankAccount();

}
