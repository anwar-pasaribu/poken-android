package id.unware.poken.ui.wallet.withdrawalStatus.model;

import android.view.View;

import id.unware.poken.interfaces.VolleyResultListener;


/**
 * Created by marzellaalfamega on 2/16/17.
 */
public interface IWithdrawSummaryModel {
    void cancelWithdraw(View snackContainer, VolleyResultListener listener);
}
