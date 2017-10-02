package id.unware.poken.ui.address.view;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.ui.view.BaseView;

/**
 * Created by PID-T420S on 9/26/2017.
 */

public interface IAddressView extends BaseView {
    void populateAddressBookList(ArrayList<AddressBook> addressBookArrayList);

    void showNoReceiverAddressView(boolean isShow);

    void showMessage(String msg, int messageStatus);

    void showSelectedAddressBook(int position, AddressBook addressBook);

    void showNewAddressBook(AddressBook addressBook);

    void updateDeletedItemView(int posToDelete, AddressBook addressBookItemToDelete);

    void setShippingAddressPic(String name);

    void setShippingPhoneNumber(String phone);

    void setShippingDetailAddress(String address);

    void showEditModeScreen(boolean isEditMode);

    void setAddressBookData(int position, AddressBook addressBook);

    void showUpdatedItem(int updatedAddressBookPos, AddressBook updatedAddressBook);
}
