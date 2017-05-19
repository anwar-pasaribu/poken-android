package id.unware.poken.ui.newPackage.model;

import android.view.View;

import id.unware.poken.pojo.PojoNewPackage;

/**
 * @author Anwar Pasaribu
 * @since Feb 07 2017
 */

public interface INewPackageModel {

    public void requestPackageServices(View parentView);

    /**
     * Get Address Book for Sender or Receiver.
     * For AutoComplete purpose.
     *
     * @param isForSender Decide whether autocomplete for Sender or Receiver.
     */
    public void getAllAddressBook(boolean isForSender);

    /**
     * Request to book new package.
     */
    public void requestNewPackageBooking(View parentView, PojoNewPackage newPackage);
}
