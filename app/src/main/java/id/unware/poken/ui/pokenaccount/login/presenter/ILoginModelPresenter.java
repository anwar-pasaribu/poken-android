package id.unware.poken.ui.pokenaccount.login.presenter;

import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public interface ILoginModelPresenter extends BasePresenter {
    void onLoginResponse(String userToken);
}
