package id.unware.poken.ui.address.presenter;

import id.unware.poken.domain.AddressBook;

/**
 * Created by PID-T420S on 9/26/2017.
 */

public interface IAddressPresenter {
    void addNewAddressBook(AddressBook addressBook);

    void getAddressBookData();

    void onAddressItemSelected(int position, AddressBook addressBook);

    void startDeleteAddressBookItem(int position, AddressBook addressBook);

    void startEditAddressBook(int position, AddressBook addressBook);

    void updateAddressBook(int currentAddressBookPos, AddressBook currentAddressBook);
}
