package id.unware.poken.ui.wallet.topup.presenter;


import id.unware.poken.pojo.PojoDepositPending;

/**
 * Created by marzellaalfamega on 2/2/17.
 *
 */
public interface IWalletTopupPresenter {
    void copyAccountNumberToClipboard();

    void copyAmountToClipboard();

    void clearAmountText();

    void finishCopyToClipboard(String strStringToCopy);

    void checkStatus();

    void requestTopUp();

    void successCheckStatus(PojoDepositPending pojoDepositPending);

    void requestCancelTopUp();
}
