package id.unware.poken.ui.wallet.topup.model;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;


import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoDepositPending;
import id.unware.poken.ui.wallet.topup.presenter.IWalletTopupPresenter;

/**
 * Created by marzellaalfamega on 2/2/17.
 */

public class WalletTopupModel implements IWalletTopupModel {
    @Override
    public void copyStringToClipboard(String strStringToCopy, ClipboardManager clipboard,
                                      IWalletTopupPresenter listener) {

        ClipData clip = ClipData.newPlainText("copied_string_paket_id", strStringToCopy);
        clipboard.setPrimaryClip(clip);

        listener.finishCopyToClipboard(strStringToCopy);
    }

    @Override
    public void requestTopup(View snackContainer, String amountString, VolleyResultListener listener) {
        ControllerPaket.getInstance().walletTopup(snackContainer, amountString, listener);
    }

    @Override
    public void checkStatus(Bundle arguments, IWalletTopupPresenter listener) {
        PojoDepositPending pojoDepositPending = arguments.getParcelable(Constants.EXTRA_TOPUP_STATUS);
        listener.successCheckStatus(pojoDepositPending);
    }

    @Override
    public void cancelTopup(View snackContainer, VolleyResultListener listener) {
        ControllerPaket.getInstance().walletCancelTopup(snackContainer,listener);
    }
}
