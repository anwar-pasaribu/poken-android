package id.unware.poken.ui.wallet.topup.model;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;

import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.ui.wallet.topup.presenter.IWalletTopupPresenter;


/**
 * Created by marzellaalfamega on 2/2/17.
 */
public interface IWalletTopupModel {
    void copyStringToClipboard(String accountNumber, ClipboardManager clipboard, IWalletTopupPresenter listener);

    void requestTopup(View snackContainer, String amountString, VolleyResultListener listener);

    void checkStatus(Bundle arguments, IWalletTopupPresenter listener);

    void cancelTopup(View snackContainer, VolleyResultListener listener);
}
