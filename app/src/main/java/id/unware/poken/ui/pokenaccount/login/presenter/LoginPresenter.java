package id.unware.poken.ui.pokenaccount.login.presenter;

import id.unware.poken.domain.Customer;
import id.unware.poken.domain.User;
import id.unware.poken.models.UIState;
import id.unware.poken.ui.pokenaccount.login.model.ILoginModel;
import id.unware.poken.ui.pokenaccount.login.view.ILoginView;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public class LoginPresenter implements ILoginPresenter, ILoginModelPresenter {

    private final ILoginModel model;
    private final ILoginView view;

    public LoginPresenter(ILoginModel model, ILoginView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void startLogin(User user) {
        model.postLogin(user, this);
    }

    @Override
    public void updateViewState(UIState uiState) {

        if (view.isActivityFinishing()) return;

        view.showViewState(uiState);
    }

    @Override
    public void onLoginTokenResponse(String userToken) {
        model.getCustomerDataByToken(userToken, this);
    }

    @Override
    public void onLoginSuccess(Customer customer) {

        if (view.isActivityFinishing()) return;

        view.onLoginSuccess();
    }

    @Override
    public void onLoginError(String msg, int status) {

        if (view.isActivityFinishing()) return;

        view.showMessage(msg, status);
    }
}
