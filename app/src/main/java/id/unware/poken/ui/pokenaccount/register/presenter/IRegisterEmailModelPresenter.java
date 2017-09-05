package id.unware.poken.ui.pokenaccount.register.presenter;

import id.unware.poken.domain.Customer;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public interface IRegisterEmailModelPresenter extends BasePresenter {
    void onRegisterTokenResponse(String userToken);

    void onRegisterSuccess(Customer customer);

    void onRegisterError(String msg, int status);
}
