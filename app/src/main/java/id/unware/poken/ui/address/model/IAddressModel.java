package id.unware.poken.ui.address.model;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.ui.address.presenter.AddressPresenter;
import id.unware.poken.ui.address.presenter.IAddressModelPresenter;

/**
 * Created by PID-T420S on 9/26/2017.
 */

public interface IAddressModel {
    void getAddressBookData(IAddressModelPresenter presenter);

    void postNewAddressBook(IAddressModelPresenter presenter, AddressBook addressBook);

    void deleteAddressBookItem(IAddressModelPresenter presenter, AddressBook addressBook);

    void patchAddressBookChanges(IAddressModelPresenter presenter, AddressBook addressBook);
}
