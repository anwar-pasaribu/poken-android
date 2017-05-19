package id.unware.poken.ui.newPackage.presenter;
import java.util.ArrayList;

import id.unware.poken.pojo.PojoAddressBook;
import id.unware.poken.pojo.PojoNewPackage;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.newPackage.model.INewPackageModel;
import id.unware.poken.ui.newPackage.model.NewPackageModel;
import id.unware.poken.ui.newPackage.view.INewPackageView;

/**
 * @author Anwar Pasaribu
 * @since Feb 07 2017
 */

public class NewPackagePresenter implements
        INewPackagePresenter,  /* Presenter-View interaction*/
        INewPackageModelPresenter /* Presenter-Model interaction*/ {

    private static final String TAG = "NewPackagePresenter";
    private final INewPackageView view;
    private final INewPackageModel model;

    public NewPackagePresenter(INewPackageView view) {
        this.view = view;
        this.model = new NewPackageModel(this);
    }

    @Override
    public void updateViewState(UIState uiState) {
        view.showViewState(uiState);
    }

    @Override
    public void requestPackageServices() {
        Utils.Log(TAG, "Request Package services list");
        model.requestPackageServices(view.getParentView());
    }

    @Override
    public void onPackageServicesResponse(ArrayList<Object> packageServices) {
        Utils.Log(TAG, "Package services list response size: " + packageServices.size());
        view.initPackageServiceList(packageServices);
    }

    @Override
    public void requestAddressBook(boolean isForSender) {
        Utils.Log(TAG, "Request address book for sender --> " + isForSender);
        model.getAllAddressBook(isForSender);
    }

    @Override
    public void onAddressBookResponse(ArrayList<PojoAddressBook> pojoAddressBookArrayList) {
        Utils.Log(TAG, "Show Address book list size: " + pojoAddressBookArrayList.size());
        view.populateAutoComplete(pojoAddressBookArrayList);
    }

    @Override
    public void bookNewPackage() {
        if (view.isFormReady()) {
            Utils.Log(TAG, "Book new package: " + view.getFormData());
            model.requestNewPackageBooking(
                    view.getParentView(),
                    view.getFormData()
            );
        }
    }

    //TODO PojoNewPackage terdapat android package. Unit test akan menjalankan android.
    @Override
    public void onNewPackageCreated(PojoNewPackage newPackage) {
        Utils.Log(TAG, "Newly created package: " + newPackage);
        view.saveLastSender(newPackage);
        view.showNewPackageSummaryScreen(newPackage);
        view.skipAllTutorial();
    }
}
