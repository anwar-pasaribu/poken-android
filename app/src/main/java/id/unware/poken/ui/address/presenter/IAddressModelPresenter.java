package id.unware.poken.ui.address.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * Created by PID-T420S on 9/26/2017.
 */

public interface IAddressModelPresenter extends BasePresenter {
    void onAddressBookContentResponse(ArrayList<AddressBook> addressBookArrayList);
    void onNetworkMessage(String msg, int messageStatus);

    void onAddressBookCreated(AddressBook addressBook);

    void onAddressBookDeleted();

    void onAddressBookUpdated(AddressBook addressBook);
}
