package id.unware.poken.ui.newPackage.presenter;

import java.util.ArrayList;

import id.unware.poken.pojo.PojoAddressBook;
import id.unware.poken.pojo.PojoNewPackage;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * Presenter to interact with Model
 *
 * @author Anwar Pasaribu
 * @since Feb 22 2017
 */

public interface INewPackageModelPresenter extends BasePresenter {
    void onPackageServicesResponse(ArrayList<Object> packageServices);
    void onAddressBookResponse(ArrayList<PojoAddressBook> pojoAddressBookArrayList);
    /**
     * New Package successfully created.
     */
    void onNewPackageCreated(PojoNewPackage newPackage);
}
