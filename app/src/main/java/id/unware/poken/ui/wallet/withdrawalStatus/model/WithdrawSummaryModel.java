package id.unware.poken.ui.wallet.withdrawalStatus.model;

import android.view.View;

import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.interfaces.VolleyResultListener;


/**
 * Created by marzellaalfamega on 2/16/17.
 */

public class WithdrawSummaryModel implements IWithdrawSummaryModel {
    @Override
    public void cancelWithdraw(View snackContainer, VolleyResultListener listener) {
        ControllerPaket.getInstance().cancelWithdraw(snackContainer, listener);
    }
}
