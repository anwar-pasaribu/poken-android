package id.unware.poken.ui.wallet.withdrawalStatus.presenter;


import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.ui.wallet.withdrawalStatus.model.IWithdrawSummaryModel;
import id.unware.poken.ui.wallet.withdrawalStatus.view.IDialogWithdrawSummaryView;

/**
 * Created by marzellaalfamega on 2/16/17.
 */

public class WithdrawSummaryPresenter implements IWithdrawSummaryPresenter, VolleyResultListener {

    private IDialogWithdrawSummaryView view;
    private IWithdrawSummaryModel model;

    public WithdrawSummaryPresenter(IDialogWithdrawSummaryView view, IWithdrawSummaryModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void cancelWithdraw() {
        model.cancelWithdraw(view.getParentView(), this);
    }

    @Override
    public void onStart(PojoBase clazz) {

    }

    @Override
    public void onSuccess(PojoBase clazz) {
        view.closeDialog();
    }

    @Override
    public void onFinish(PojoBase clazz) {

    }

    @Override
    public boolean onError(PojoBase clazz) {
        return false;
    }
}
