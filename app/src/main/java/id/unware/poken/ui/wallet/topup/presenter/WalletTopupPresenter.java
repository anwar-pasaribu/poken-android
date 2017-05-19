package id.unware.poken.ui.wallet.topup.presenter;


import id.unware.poken.R;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.pojo.PojoDepositPending;
import id.unware.poken.pojo.PojoWalletTopup;
import id.unware.poken.ui.wallet.topup.model.IWalletTopupModel;
import id.unware.poken.ui.wallet.topup.view.FragmentWalletTopup;

/**
 * Created by marzellaalfamega on 2/2/17.
 */

public class WalletTopupPresenter implements IWalletTopupPresenter, VolleyResultListener {

    private FragmentWalletTopup view;
    private IWalletTopupModel model;

    public WalletTopupPresenter(FragmentWalletTopup view, IWalletTopupModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void copyAccountNumberToClipboard() {
        model.copyStringToClipboard(view.getAccountNumber(), view.getClipboardManager(), this);
    }

    @Override
    public void copyAmountToClipboard() {
        model.copyStringToClipboard(view.getAmountString(), view.getClipboardManager(), this);
    }

    @Override
    public void clearAmountText() {
        view.setAmount("");
    }

    @Override
    public void finishCopyToClipboard(String strStringToCopy) {
        view.showToast(view.getParentActivity().getString(R.string.msg_info_string_copied, strStringToCopy));
    }

    @Override
    public void checkStatus() {
        model.checkStatus(view.getArguments(), this);
    }

    @Override
    public void requestTopUp() {
        if (view.isValidAmount()) {
            view.setLoadingState();
            model.requestTopup(view.getParentView(), view.getAmountString(), this);
        } else {
            view.showMessage("Nominal yang dimasukkan tidak benar.");
        }
    }

    @Override
    public void successCheckStatus(PojoDepositPending pojoDepositPending) {
        if (pojoDepositPending.isPending()) {
            view.showPendingPage(pojoDepositPending.getDeposit());
        } else {

        }
    }

    @Override
    public void requestCancelTopUp() {
        model.cancelTopup(view.getParentView(), this);
    }

    @Override
    public void onStart(PojoBase clazz) {

    }

    @Override
    public void onSuccess(PojoBase clazz) {
        if (clazz instanceof PojoWalletTopup) {
            PojoWalletTopup pojoWalletTopup = (PojoWalletTopup) clazz;
            view.showPendingPage(pojoWalletTopup.deposit);
        } else {
            view.setupInputAmountState();
        }
    }

    @Override
    public void onFinish(PojoBase clazz) {

    }

    @Override
    public boolean onError(PojoBase clazz) {
        view.setupInputAmountState();
        return false;
    }
}
