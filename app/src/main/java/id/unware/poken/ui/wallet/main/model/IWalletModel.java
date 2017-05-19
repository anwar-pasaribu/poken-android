package id.unware.poken.ui.wallet.main.model;

import android.view.View;

import java.util.ArrayList;

import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.PojoUserBank;

/**
 * @author Anwar Pasaribu
 * @since Feb 02 2017
 */

public interface IWalletModel {
    void loadWalletData(View snackContainer, VolleyResultListener listener);

    void saveUserBankAccount(ArrayList<PojoUserBank> userBank);

    ArrayList<PojoUserBank> getUserBankAccount();

    void saveUpdatedOn(String updatedOn);
}
