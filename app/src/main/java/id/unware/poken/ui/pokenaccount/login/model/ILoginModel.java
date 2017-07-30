package id.unware.poken.ui.pokenaccount.login.model;

import id.unware.poken.domain.User;
import id.unware.poken.ui.pokenaccount.login.presenter.ILoginModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public interface ILoginModel {
    void postLogin(User user, ILoginModelPresenter presenter);
    void getCustomerDataByToken(String userToken, ILoginModelPresenter presenter);
}
