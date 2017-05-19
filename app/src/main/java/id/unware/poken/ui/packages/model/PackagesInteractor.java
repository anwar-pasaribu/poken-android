package id.unware.poken.ui.packages.model;

import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.BookingStatus;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoBookingData;
import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoLogin;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.packages.presenter.IPackagesModelPresenter;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * @author Anwar Pasaribu
 * @since Dec 21 2016
 */

@SuppressWarnings("WeakerAccess")
public class PackagesInteractor implements VolleyResultListener {

    private final String TAG = "PackagesInteractor";

    private final IPackagesModelPresenter packagesPresenter;
    private final ControllerPaket mControllerPaket;
    private Realm mRealm;

    /** [V48] NOT Reload all indicate append data on top*/
    private boolean mIsReloadAll = false;

    public PackagesInteractor(IPackagesModelPresenter packagesPresenter) {
        this.packagesPresenter = packagesPresenter;
        this.mControllerPaket = ControllerPaket.getInstance();
        this.mRealm = Realm.getDefaultInstance();
    }

    public void requestPackages(boolean reloadAll) {

        // [V48] Save whether reload all or no
        this.mIsReloadAll = reloadAll;

        mControllerPaket.loginWithSession(
                null,                   /* View to hold custom snackbar*/
                reloadAll,              /* Reload whole data*/
                PackagesInteractor.this /* VolleyResultListener*/
        );
    }

    public void requestMorePackages() {
        //noinspection unused
        Long longMax = mRealm.where(PojoBooking.class).max(PojoBooking.KEY_BOOKING_ID) != null
                ? mRealm.where(PojoBooking.class).max(PojoBooking.KEY_BOOKING_ID).longValue()
                : 0L;
        Long longMin = mRealm.where(PojoBooking.class).min(PojoBooking.KEY_BOOKING_ID) != null
                ? mRealm.where(PojoBooking.class).min(PojoBooking.KEY_BOOKING_ID).longValue()
                : 0L;

        mControllerPaket.getPagedPackages(null, longMin, 0, PackagesInteractor.this);
    }

    /**
     * Save or update  packages (pojoBooking) to Realm.
     *
     * @param pojoBookingList List of PojoBooking to save.
     *
     * @return Successfully saved packages.
     */
    public ArrayList<PojoBooking> savePackages(final List<PojoBooking> pojoBookingList) {
        Utils.Logs('i', TAG, "Insert "+pojoBookingList.size()+" PojoBooking to Realm");

        final ArrayList<PojoBooking> savedPojoBooking = new ArrayList<>();

        try {

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm mRealm) {

                    // Realm return saved data
                    savedPojoBooking.addAll(mRealm.copyToRealmOrUpdate(pojoBookingList));

                }
            });

        } catch (IllegalArgumentException e){
            MyLog.FabricLog(Log.ERROR, TAG + " - Can not save Packages.", e);

            return new ArrayList<>();
        }

        return savedPojoBooking;
    }

    /**
     * Save vendors after delete previous data.
     * @param pojoCouriers Array list of PojoCourier
     * @return Saved PojoCourier
     *
     * @since V48 - NEW
     */
    public ArrayList<PojoCourier> saveVendors(final ArrayList<PojoCourier> pojoCouriers) {
        final ArrayList<PojoCourier> savedPojoCourier = new ArrayList<>();

        try {

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm mRealm) {

                    // Realm return saved data
                    mRealm.where(PojoCourier.class).findAll().deleteAllFromRealm();
                    savedPojoCourier.addAll(mRealm.copyToRealmOrUpdate(pojoCouriers));
                }
            });

        } catch (IllegalArgumentException e){
            MyLog.FabricLog(Log.ERROR, TAG + " - Can not save Packages.", e);

            return new ArrayList<>();
        }

        return savedPojoCourier;
    }

    /**
     * Save Pickup History when access "get_data" API.
     *
     * @param PojoPickupHistories ArrayList of PojoPickupHistory
     * @return Saved Pickup History.
     *
     * @since V48 - NEW
     */
    public ArrayList<PojoPickupHistory> savePickupHistory(final ArrayList<PojoPickupHistory> PojoPickupHistories) {
        final ArrayList<PojoPickupHistory> savedPojoPickupHistory = new ArrayList<>();

        try {

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm mRealm) {

                    // Realm return saved data
                    savedPojoPickupHistory.addAll(mRealm.copyToRealmOrUpdate(PojoPickupHistories));
                }
            });

        } catch (IllegalArgumentException e){
            MyLog.FabricLog(Log.ERROR, TAG + " - Can not save Packages.", e);

            return new ArrayList<>();
        }

        return savedPojoPickupHistory;
    }

    public List<PojoBooking> getAllPackages() {

        ArrayList<PojoBooking> pojoBookingList = new ArrayList<>();

        // Query again after package is deleted
        RealmResults<PojoBooking> pojoBookingRealmResults = mRealm
                .where(PojoBooking.class)
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.RETURNED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.BOOKED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.SENT.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.DELIVERED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.PICKED.getStrVal())
                .findAllSorted(PojoBooking.KEY_BOOKING_ID, Sort.DESCENDING);

        int resultSize = pojoBookingRealmResults.size();

        int packageLimit =  Math.min(Constants.MAX_PACKAGES_ON_DB, resultSize);

        for (PojoBooking pojoBooking : pojoBookingRealmResults.subList(0, packageLimit)) {
            pojoBookingList.add(pojoBooking);
        }

        Utils.Log(TAG, "Realm result ALL PACKAGES size: " + resultSize);

        return pojoBookingRealmResults;
    }

    public List<PojoBooking> getFilteredPackages(ArrayList<String> bookingStatusText) {

        List<PojoBooking> pojoBookingList = new ArrayList<>();
        RealmResults<PojoBooking> pojoBookingRealmResults;

        if (bookingStatusText.size() != 0) {

            // Begin Realm query to get the data
            RealmQuery<PojoBooking> pojoBookingRealmQuery = mRealm.where(PojoBooking.class);

            int i = 0;
            for (String statusText : bookingStatusText) {

                if (i != 0) {
                    pojoBookingRealmQuery = pojoBookingRealmQuery.or();
                }

                pojoBookingRealmQuery = pojoBookingRealmQuery
                        .equalTo(PojoBooking.KEY_BOOKING_STATUS_TEXT, statusText);
                i++;
            }

            pojoBookingRealmResults = pojoBookingRealmQuery
                    .findAllSorted(PojoBooking.KEY_BOOKING_ID, Sort.DESCENDING);

            /*
            .findAllSorted(
                    new String[]{ PojoBooking.KEY_BOOKING_STATUS_TEXT, PojoBooking.KEY_BOOKING_ID, PojoBooking.KEY_BOOKING_DATE},
                    new Sort[]{ Sort.ASCENDING, Sort.DESCENDING, Sort.DESCENDING}
            );
            */


        } else {

            // Query again after package is deleted
            pojoBookingRealmResults = mRealm
                    .where(PojoBooking.class)
                    .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.RETURNED.getStrVal())
                    .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.BOOKED.getStrVal())
                    .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.SENT.getStrVal())
                    .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.DELIVERED.getStrVal())
                    .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.PICKED.getStrVal())
                    .findAllSorted(PojoBooking.KEY_BOOKING_ID, Sort.DESCENDING);

                    /*.findAllSorted(
                            new String[]{PojoBooking.KEY_BOOKING_STATUS_TEXT, PojoBooking.KEY_BOOKING_ID, PojoBooking.KEY_BOOKING_DATE},
                            new Sort[]{Sort.ASCENDING, Sort.DESCENDING, Sort.DESCENDING}
                    );*/
        }

        // Limit items to show on list view
        int resultSize = pojoBookingRealmResults.size();
        int packageLimit =  resultSize > Constants.MAX_PACKAGES_ON_DB
                ? Constants.MAX_PACKAGES_ON_DB
                : resultSize;

        for (PojoBooking pojoBooking : pojoBookingRealmResults.subList(0, packageLimit)) {
            pojoBookingList.add(pojoBooking);
        }

        Utils.Log(TAG, "Realm result by status size: " + resultSize);

        return pojoBookingRealmResults;
    }

    public long getBookedPackageAvailability() {
        long count = mRealm
                .where(PojoBooking.class)
                .equalTo(PojoBooking.KEY_STATUS, BookingStatus.BOOKED.getStrVal())
                .count();

        Utils.Logs('i', TAG, "Booked package size: " + count);

        return count;
    }

    public ArrayList<PojoBooking> getFilters() {

        RealmResults<PojoBooking> pojoBookingRealmResults = mRealm
                .where(PojoBooking.class)
                .distinct(PojoBooking.KEY_BOOKING_STATUS_TEXT);

        // Collect filters (Booking status text)
        ArrayList<PojoBooking> filters = new ArrayList<>();
        String strBookingStatus;
        boolean isSelected;
        SPHelper sp = SPHelper.getInstance();
        for (PojoBooking pojoBooking: pojoBookingRealmResults) {

            strBookingStatus = pojoBooking.getBooking_status_text();
            isSelected = sp.getSharedPreferences(strBookingStatus, false);
            if (isSelected) {
                filters.add(pojoBooking);
            }
        }

        return filters;
    }

    /**
     * Delete packahe with desire booking id.
     *
     * @param bookingId Booking id to delete
     * @return Index of deleted package, -1 if failed.
     */
    public int deletePackageByBookingId(long bookingId) {

        // Get filters
        ArrayList<PojoBooking> filters = getFilters();

        final List<PojoBooking> pojoBookingRealmResults;
        final PojoBooking pojoBookingToDelete = mRealm
                .where(PojoBooking.class)
                .equalTo(PojoBooking.KEY_BOOKING_ID, bookingId)
                .findFirst();

        if (filters.size() != 0 && filters.get(0).getStatus().equals(BookingStatus.BOOKED.getStrVal())) {
            Utils.Logs('i', TAG, "Booked group is choose");
            pojoBookingRealmResults = mRealm
                    .where(PojoBooking.class)
                    .equalTo(PojoBooking.KEY_STATUS, BookingStatus.BOOKED.getStrVal())
                    .findAllSorted(PojoBooking.KEY_BOOKING_ID, Sort.DESCENDING);

        } else {
            Utils.Logs('i', TAG, "No filter applied");
            pojoBookingRealmResults = getAllPackages();
        }

        Utils.Log(TAG, "Realm results size: " + pojoBookingRealmResults.size());
        Utils.Log(TAG, "Package to delete: " + (pojoBookingToDelete == null? " NULL " : " AVAILABLE"));

        // Package index to delete
        int packageIndex = -1;
        if (pojoBookingRealmResults.size() > 0 && pojoBookingToDelete != null) {
            final int foundIndex = pojoBookingRealmResults.indexOf(pojoBookingToDelete);
            packageIndex = foundIndex;

            Utils.Log(TAG, "Package index to delete: " + foundIndex);

            if (packageIndex != -1) {
                Utils.Logs('i', TAG, "Item to delete is found. Booking code: " + pojoBookingToDelete.getBooking_code());

                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        pojoBookingRealmResults.get(foundIndex).deleteFromRealm();
                    }
                });
            }
        } else {
            Utils.Log(TAG, "No package found. DELETED process aborted.");
        }

        return packageIndex;
    }

    //////
    // S: Volley listener
    @Override
    public void onStart(PojoBase clazz) {
        if (packagesPresenter != null) {
            packagesPresenter.updateViewState(UIState.LOADING);
        }
    }

    @Override
    public void onSuccess(PojoBase clazz) {

        if (clazz == null) {
            MyLog.FabricLog(Log.ERROR, TAG + " - Get packages success, but clazz is null");
            return;
        }

        if (clazz instanceof PojoLogin) {

            // GET_DATA RESPONSE

            PojoLogin pojoLogin = (PojoLogin) clazz;

            MyLog.FabricLog(Log.INFO, TAG + " - Booking size on PojoLogin: " + pojoLogin.booking.length);

            List<PojoBooking> pojoBookingList = new ArrayList<>(Arrays.asList(pojoLogin.booking));
            ArrayList<PojoCourier> pojoCourierArrayList = new ArrayList<>(Arrays.asList(pojoLogin.vendors));
            ArrayList<PojoPickupHistory> pojoPickupHistoryArrayList = new ArrayList<>(Arrays.asList(pojoLogin.pickup));

            if (packagesPresenter != null) {
                // Proceed booking data
                // [V48] Assume "updated_on":
                // - NO updated_on : Reload all, recreate all list
                // - THERE updated_on : Append new downloaded data.
                if (mIsReloadAll) {
                    packagesPresenter.onPackagesResponse(pojoBookingList);
                } else {
                    packagesPresenter.onNewlyCreatedPackagesResponse(pojoBookingList);
                }

                // PROCESS NOT NECESSARY FOR "booking_history"
                // PROCESS NOT NECESSARY FOR "location"

                // Proceed area_info; 1: download are, 0: no download
                packagesPresenter.onAreaInfoResponse(pojoLogin.areaInfo);

                // Proceed android_version response
                packagesPresenter.onAndroidVersionResponse(pojoLogin.android_version);

                // Proceed Last Update (updated_on)
                packagesPresenter.onLastUpdateResponse(pojoLogin.updated_on);

                // Proceed vendors
                packagesPresenter.onVendorsResponse(pojoCourierArrayList);

                // Proceed user data
                packagesPresenter.onUserDataResponse(pojoLogin.user);

                // Proceed Pickup History
                packagesPresenter.onPickupHistoryResponse(pojoPickupHistoryArrayList);
            }

        } else if (clazz instanceof PojoBookingData) {

            // PAGED PACKAGES RESPONSE

            PojoBookingData pojoBookingData = (PojoBookingData) clazz;

            MyLog.FabricLog(Log.INFO, TAG + " - Paged packages found with size: " + pojoBookingData.getBookings().length);

            List<PojoBooking> pojoBookingList = new ArrayList<>(Arrays.asList(pojoBookingData.getBookings()));

            if (packagesPresenter != null) {
                packagesPresenter.onMorePackagesResponse(pojoBookingList);
            }
        }
    }

    @Override
    public void onFinish(PojoBase clazz) {
        if (packagesPresenter != null) {
            packagesPresenter.updateViewState(UIState.FINISHED);
        }
    }

    @Override
    public boolean onError(PojoBase clazz) {
        if (packagesPresenter != null) {
            packagesPresenter.updateViewState(UIState.ERROR);
        }

        return false;
    }
    // E: Volley listener
    //////
}
