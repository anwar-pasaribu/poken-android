package id.unware.poken.ui.wallet.main.model;

import android.view.View;


import java.util.ArrayList;

import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoUserBank;

/**
 * @author Anwar Pasaribu
 * @since Feb 02 2017
 */

public class WalletModel implements IWalletModel {

    @Override
    public void loadWalletData(View snackContainer, VolleyResultListener listener) {
        ControllerPaket.getInstance().loadWalletData(snackContainer, listener);
    }

    @Override
    public void saveUserBankAccount(ArrayList<PojoUserBank> userBank) {
        ControllerRealm.getInstance().saveUserBankAccount(userBank);
    }

    @Override
    public ArrayList<PojoUserBank> getUserBankAccount() {
        return ControllerRealm.getInstance().getUserBankAccount();
    }

    @Override
    public void saveUpdatedOn(String updatedOn) {
        SPHelper.getInstance().setPreferences(Constants.SHARED_UPDATED_ON_WALLET, updatedOn);
    }
}
