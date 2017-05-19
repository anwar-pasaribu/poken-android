package id.unware.poken.ui.wallet.topup.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;

import id.unware.poken.pojo.PojoDeposit;


/**
 * Created by marzellaalfamega on 2/2/17.
 */
public interface IWalletTopupView {

    String getAccountNumber();

    ClipboardManager getClipboardManager();

    void setAmount(String amount);

    Context getParentActivity();

    void showToast(String string);

    String getAmountString();

    boolean isValidAmount();

    void showMessage(String msg);

    void setLoadingState();

    View getParentView();

    void setParentView(View parentView);

    void showPendingPage(PojoDeposit deposit);

    void setupInputAmountState();
}
