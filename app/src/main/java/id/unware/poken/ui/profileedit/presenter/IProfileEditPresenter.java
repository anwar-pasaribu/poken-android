package id.unware.poken.ui.profileedit.presenter;

import id.unware.poken.domain.Customer;

public interface IProfileEditPresenter {
    void startEditProfile(long currentCustId, Customer customer);

    /**
     * Load Customer detail.
     * @param currentCustId Customer ID or Token
     */
    void loadPokenCustomerInfo(String currentCustId);

    void startEditProfilePassword(long custId, Customer customerData, String currentPassword, String newPassword);
}
