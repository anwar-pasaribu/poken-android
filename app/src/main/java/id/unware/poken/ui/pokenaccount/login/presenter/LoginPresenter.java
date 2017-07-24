package id.unware.poken.ui.pokenaccount.login.presenter;

import id.unware.poken.domain.User;
import id.unware.poken.pojo.UIState;
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
        view.showViewState(uiState);
    }

    @Override
    public void onLoginResponse(String userToken) {
        view.onLoginSuccess();
    }
}
