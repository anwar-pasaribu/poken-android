package id.unware.poken.ui.pokenaccount.register.presenter;

import id.unware.poken.domain.Customer;
import id.unware.poken.domain.User;
import id.unware.poken.models.UIState;
import id.unware.poken.ui.pokenaccount.register.model.IRegisterEmailModel;
import id.unware.poken.ui.pokenaccount.register.view.IRegisterEmailView;

/**
 * @author Anwar Pasaribu
 * @since Sep 01 2017
 */

public class RegisterEmailPresenter implements IRegisterEmailPresenter, IRegisterEmailModelPresenter {

    private final IRegisterEmailModel model;
    private final IRegisterEmailView view;

    public RegisterEmailPresenter(IRegisterEmailModel model, IRegisterEmailView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void startRegister(User user) {

        model.postRegister(user, this);

    }

    @Override
    public void updateViewState(UIState uiState) {
        if (view.isActivityFinishing()) return;

        view.showViewState(uiState);
    }

    @Override
    public void onRegisterTokenResponse(String userToken) {

    }

    @Override
    public void onRegisterSuccess(Customer customer) {

        if (view.isActivityFinishing()) return;

        view.onRegisterSuccess();

    }

    @Override
    public void onRegisterError(String msg, int status) {

        if (view.isActivityFinishing()) return;

        view.showMessage(msg, status);
    }
}
