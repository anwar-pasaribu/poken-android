package id.unware.poken.ui.address.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.address.model.IAddressModel;
import id.unware.poken.ui.address.view.IAddressView;


public class AddressPresenter implements IAddressPresenter, IAddressModelPresenter {

    private final static String TAG = "AddressPresenter";

    private final IAddressModel model;
    private final IAddressView view;


    private int itemPosToDelete;
    private AddressBook addressBookItemToDelete;

    private int updatedAddressBookPos;
    private AddressBook addressBookBeforeEdit;

    public AddressPresenter(IAddressModel model, IAddressView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void addNewAddressBook(AddressBook addressBook) {
        model.postNewAddressBook(this, addressBook);
    }

    @Override
    public void getAddressBookData() {
        model.getAddressBookData(this);
    }

    @Override
    public void onAddressItemSelected(int position, AddressBook addressBook) {
        view.showSelectedAddressBook(position, addressBook);
    }

    @Override
    public void startDeleteAddressBookItem(int position, AddressBook addressBook) {
        this.itemPosToDelete = position;
        this.addressBookItemToDelete = addressBook;
        model.deleteAddressBookItem(this, addressBook);
    }

    @Override
    public void startEditAddressBook(int position, AddressBook addressBook) {

        // Save original condition of Address Book before edit
        this.updatedAddressBookPos = position;
        this.addressBookBeforeEdit = addressBook;

        view.showEditModeScreen(true);

        // Populate edit text with address book data
        view.setAddressBookData(position, addressBook);

        view.setShippingAddressPic(addressBook.name);
        view.setShippingPhoneNumber(addressBook.phone);
        view.setShippingDetailAddress(addressBook.address);
    }

    @Override
    public void updateAddressBook(int updatedPos, AddressBook updatedAddressBook) {

        this.updatedAddressBookPos = updatedPos;

        model.patchAddressBookChanges(this, updatedAddressBook);
    }

    @Override
    public void updateViewState(UIState uiState) {
        if (view.isActivityFinishing()) return;

        Utils.Logs('w', TAG, "Update view state: " + uiState);
        view.showViewState(uiState);
    }

    @Override
    public void onAddressBookContentResponse(ArrayList<AddressBook> addressBookArrayList) {

        if (view.isActivityFinishing()) return;

        if (addressBookArrayList.isEmpty()) {
            view.showNoReceiverAddressView(true);
        } else {
            view.showNoReceiverAddressView(false);
            view.populateAddressBookList(addressBookArrayList);
        }

    }

    @Override
    public void onNetworkMessage(String msg, int messageStatus) {
        if (view.isActivityFinishing()) return;

        view.showMessage(msg, messageStatus);
    }

    @Override
    public void onAddressBookCreated(AddressBook addressBook) {
        view.showNewAddressBook(addressBook);
    }

    @Override
    public void onAddressBookDeleted() {
        view.updateDeletedItemView(this.itemPosToDelete, this.addressBookItemToDelete);
    }

    @Override
    public void onAddressBookUpdated(AddressBook addressBook) {

        // Show list when editing complete
        view.showEditModeScreen(false);

        view.showUpdatedItem(this.updatedAddressBookPos, addressBook);
    }
}
