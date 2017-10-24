package id.unware.poken.ui.profileedit.model;

import id.unware.poken.domain.Customer;
import id.unware.poken.ui.profileedit.presenter.IProfileEditModelPresenter;
import id.unware.poken.ui.profileedit.presenter.ProfileEditPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public interface IProfileEditModel {
    void patchProfileInfo(long customerId, Customer customer, IProfileEditModelPresenter presenter);

    void patchProfileInfo(long custId, Customer customerData, String currentPassword, String newPassword, IProfileEditModelPresenter presenter);

    void getPokenCustomerDataById(IProfileEditModelPresenter presenter, String custIdentifier);

}
