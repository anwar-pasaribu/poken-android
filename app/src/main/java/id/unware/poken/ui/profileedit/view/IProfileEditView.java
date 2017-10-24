package id.unware.poken.ui.profileedit.view;

import id.unware.poken.domain.Customer;
import id.unware.poken.ui.view.BaseView;

/**
 * Created by PID-T420S on 10/13/2017.
 */

public interface IProfileEditView extends BaseView {
    void showMessage(int msgState, String msg);

    void showCustomerData(Customer customer);
}
