package id.unware.poken.ui.newPackage.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.unware.poken.R;
import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.GeneralListItem;
import id.unware.poken.pojo.PojoAddressBook;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoNewPackage;
import id.unware.poken.pojo.PojoNewPackageData;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.newPackage.presenter.INewPackageModelPresenter;
import id.unware.poken.ui.newPackage.presenter.NewPackagePresenter;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author Anwar Pasaribu
 * @since Feb 07 2017
 */

public class NewPackageModel implements INewPackageModel,
        VolleyResultListener {

    private final INewPackageModelPresenter mPresenter;
    private Realm mRealm;

    public NewPackageModel(NewPackagePresenter presenter) {
        this.mRealm = Realm.getDefaultInstance();
        this.mPresenter = presenter;
    }

    @Override
    public void requestPackageServices(View parentView) {

        // TODO Mas? Apakah ini cara yang baik dan benar?
        Context parentViewContext = parentView.getContext();

        ArrayList<Object> packagesServices = new ArrayList<>();

        // [V49] Harcoded Package service items
        GeneralListItem item1 = new GeneralListItem();
        item1.setId(0);
        item1.setSelected(true);
        item1.setTitle(parentViewContext.getString(R.string.lbl_package_service_reg));
        item1.setContent(parentViewContext.getString(R.string.lbl_package_service_reg_desc));

        GeneralListItem item2 = new GeneralListItem();
        item2.setId(1);
        item2.setTitle(parentViewContext.getString(R.string.lbl_package_service_exp));
        item2.setContent(parentViewContext.getString(R.string.lbl_package_service_exp_desc));

        GeneralListItem item3 = new GeneralListItem();
        item3.setId(2);
        item3.setTitle(parentViewContext.getString(R.string.lbl_package_service_eco));
        item3.setContent(parentViewContext.getString(R.string.lbl_package_service_eco_desc));

        packagesServices.add(item1);
        packagesServices.add(item2);
        packagesServices.add(item3);

        mPresenter.onPackageServicesResponse(packagesServices);
    }

    /**
     * Get Address Book for Sender or Receiver.
     * For AutoComplete purpose.
     *
     * @param isForSender Decide whether autocomplete for Sender or Receiver.
     */
    @Override
    public void getAllAddressBook(boolean isForSender) {

        Utils.Log("NewPackageModel", "Begin init async for sender? --> " + isForSender);
        ProceedAddressBookAsync addressBookAsync = new ProceedAddressBookAsync();
        addressBookAsync.execute(isForSender);

    }

    @Override
    public void requestNewPackageBooking(
            View parentView,
            PojoNewPackage newPackage
            ) {
        ControllerPaket.getInstance().newPackage(
                parentView,
                newPackage.getFromFull(),
                newPackage.getToFull(),
                newPackage.getService(),
                newPackage.getContent(),
                newPackage.getInsuredValue(),
                newPackage.getNote(),
                this
        );
    }

    //////
    // S: Volley Listener
    @Override
    public void onStart(PojoBase clazz) {
        mPresenter.updateViewState(UIState.LOADING);
        MyLog.FabricLog(Log.INFO, "Start creating new package.");

    }

    @Override
    public void onSuccess(PojoBase clazz) {
        final PojoNewPackageData pojoNewPackageData = (PojoNewPackageData) clazz;
        final PojoNewPackage pojoNewPackage = pojoNewPackageData.detail;

        if (pojoNewPackage != null) {
            mPresenter.onNewPackageCreated(pojoNewPackage);
        }
    }

    @Override
    public void onFinish(PojoBase clazz) {
        if (clazz != null) {
            mPresenter.updateViewState(UIState.FINISHED);
        }
    }

    @Override
    public boolean onError(PojoBase clazz) {
        mPresenter.updateViewState(UIState.ERROR);
        return false;
    }
    // E: Volley listener
    //////


    /**
     * Generate auto complete list for New Package Sender/Receiver.
     */
    private class ProceedAddressBookAsync extends AsyncTask<Boolean, Integer, ArrayList<PojoAddressBook>> {

        @Override
        protected void onPreExecute() {
            Utils.Log("ProceedAddressBookAsync", "onPreExecute begins");
        }

        @Override
        protected ArrayList<PojoAddressBook> doInBackground(Boolean... params) {

            boolean isForSender = params[0];

            ArrayList<PojoAddressBook> listFilteredAddressBook = new ArrayList<>();
            HashMap<String, PojoAddressBook> uniqueAddressBookSet = new HashMap<>();

            Realm realm = Realm.getDefaultInstance();
            try {
                String strAutocompleteTarget = isForSender
                        ? "to_name"
                        : "from_name";

                RealmResults<PojoBooking> results = realm.where(PojoBooking.class)
                        .findAllSorted(strAutocompleteTarget, Sort.ASCENDING)
                        .distinct(strAutocompleteTarget);

                Utils.Log("ProceedAddressBookAsync", "Result on thread: " + results.size());

                List<PojoAddressBook> pojoAddressBookList = new ArrayList<>();
                for (PojoBooking bookingItem : results) {
                    pojoAddressBookList.add(new PojoAddressBook(
                            bookingItem.getFrom_name(),
                            bookingItem.getFrom_phone(),
                            bookingItem.getFrom_address(),
                            PojoAddressBook.STATE_ADDRESS_NORMAL,
                            true));  // True means Sender data

                    pojoAddressBookList.add(new PojoAddressBook(
                            bookingItem.getTo_name(),
                            bookingItem.getTo_phone(),
                            bookingItem.getTo_address(),
                            PojoAddressBook.STATE_ADDRESS_NORMAL,
                            false));  // False means Receiver data
                }

                Utils.Log("ProceedAddressBookAsync", "Pojo address book size: " + pojoAddressBookList.size());

                // Filtering by name and phone. Any duplicates will be ignored on HashMap
                StringBuilder sb = new StringBuilder();  // For faster string processing
                for (PojoAddressBook item : pojoAddressBookList) {
                    sb.delete(0, sb.length());
                    uniqueAddressBookSet.put(
                            sb.append(
                                    item.getName().trim())
                                    .append(item.getPhoneNumber().trim())
                                    .toString(),
                            item);
                }

                // Populate final list for presentations
                for (Object o : uniqueAddressBookSet.entrySet()) {
                    Map.Entry pair = (Map.Entry) o;
                    listFilteredAddressBook.add((PojoAddressBook) pair.getValue());
                }

                // Sorting by to_name (Implement comparable)
                Collections.sort(listFilteredAddressBook);

                Utils.Log("ProceedAddressBookAsync", "Address book list size: " + listFilteredAddressBook.size());

                return listFilteredAddressBook;

            } finally {
                realm.close();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Utils.Log("ProceedAddressBookAsync", "Proceed success for booking ID: " + values[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<PojoAddressBook> addressBooks) {
            Utils.Log("ProceedAddressBookAsync", "onPostExecute :" + addressBooks.size());
            mPresenter.onAddressBookResponse(addressBooks);
        }


    }
}
