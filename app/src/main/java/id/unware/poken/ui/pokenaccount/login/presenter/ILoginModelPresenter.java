package id.unware.poken.ui.pokenaccount.login.presenter;

import id.unware.poken.domain.Customer;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public interface ILoginModelPresenter extends BasePresenter {
    void onLoginTokenResponse(String userToken);

    void onLoginSuccess(Customer customer);

    void onLoginError(String msg, int status);
}
