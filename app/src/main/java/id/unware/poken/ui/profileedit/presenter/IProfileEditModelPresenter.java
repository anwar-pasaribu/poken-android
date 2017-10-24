package id.unware.poken.ui.profileedit.presenter;

import id.unware.poken.domain.Customer;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public interface IProfileEditModelPresenter extends BasePresenter {

    void onEditProfileSuccess(Customer customer);

    void startMessage(String msg, int msgState);

}
