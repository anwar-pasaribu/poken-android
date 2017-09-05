package id.unware.poken.ui.pokenaccount.register.model;

import id.unware.poken.domain.User;
import id.unware.poken.ui.pokenaccount.login.presenter.ILoginModelPresenter;
import id.unware.poken.ui.pokenaccount.register.presenter.IRegisterEmailModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public interface IRegisterEmailModel {
    void postRegister(User user, IRegisterEmailModelPresenter presenter);
    void getCustomerDataByToken(String userToken, IRegisterEmailModelPresenter presenter);
}
