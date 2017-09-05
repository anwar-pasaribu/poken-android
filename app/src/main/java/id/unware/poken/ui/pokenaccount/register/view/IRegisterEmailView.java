package id.unware.poken.ui.pokenaccount.register.view;

import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public interface IRegisterEmailView extends BaseView {
    void onRegisterSuccess();

    void showMessage(String msg, int status);
}
