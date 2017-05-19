package id.unware.poken.ui.packages.presenter;

import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.pojo.PojoUser;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.packages.model.PackagesInteractor;
import id.unware.poken.ui.packages.view.PackagesView;
import id.unware.poken.ui.presenter.SharedPreferencesRepositoryImpl;

/**
 * @author Anwar Pasaribu
 * @since Dec 21 2016
 */

public class PackagesPresenterImpl implements
        IPackagesModelPresenter,
        PackagesPresenter {

    private final String TAG = "PackagesPresenterImpl";

    private final PackagesInteractor interactor;
    private final PackagesView view;

    /** Shared Preferences interface impl.*/
    private final SharedPreferencesRepositoryImpl mSharedPrefRepo;


    public PackagesPresenterImpl(PackagesView packagesView) {
        this.interactor = new PackagesInteractor(this);
        this.view = packagesView;

        this.mSharedPrefRepo = new SharedPreferencesRepositoryImpl();
    }

    @Override
    public void requestPackagesOnline(boolean reloadAll) {
        interactor.requestPackages(reloadAll);
    }

    @Override
    public void requestMorePackagesOnline() {
        // After network calls finished, onMorePackagesResponse called
        interactor.requestMorePackages();
    }

    @Override
    public void onPackagesResponse(List<PojoBooking> pojoBookingList) {

        ArrayList<PojoBooking> savedPackages = interactor.savePackages(pojoBookingList);

        if (!savedPackages.isEmpty()) {
            // Replace old and change with new data
            view.showAllPackages(pojoBookingList);
        } // else all data is available.
    }

    @Override
    public void onMorePackagesResponse(List<PojoBooking> pojoBookingList) {
        interactor.savePackages(pojoBookingList);

        Utils.Log(TAG, "More item found: " + pojoBookingList.size());

        if (pojoBookingList.size() >= 50) {
            pojoBookingList.add(createLoadMoreItem());
        }

        view.showMorePackages(pojoBookingList);
    }

    @Override
    public void onNewlyCreatedPackagesResponse(List<PojoBooking> pojoBookingList) {
        interactor.savePackages(pojoBookingList);

        // When this the first time to create new package
        // replace list with new data, or append new data on top.
        List<PojoBooking> pojoBookings = interactor.getAllPackages();

        Utils.Log(TAG, "All package list size: " + pojoBookings.size());
        Utils.Log(TAG, "Newly Created Packages size: " + pojoBookingList.size());

        if (pojoBookings.size() == 1) {
            // [V49] Assume this is the first booking ever with status "Booked"

            // Replace list item ("empty state item") with booking item
            view.showAllPackages(pojoBookingList);

            Utils.Log(TAG, "Replace all item with new data, with size: " + pojoBookingList.size());

        } else {
            // Append new booking to to of the list
            view.showNewPackages(pojoBookingList);

            Utils.Log(TAG, "Append to top for item size: " + pojoBookingList.size());
        }

        // [V49] Save the #1 first item info for "latest sender" purpose
        if (!pojoBookingList.isEmpty()) {
            Utils.Logs('i', TAG, "Save latest sender #1 item to SP.");
            mSharedPrefRepo.setLastSender(pojoBookingList.get(0));
        }
    }

    @Override
    public void getLocalPackageList() {
        Utils.Log(TAG, "Prepare to get package list");

        ArrayList<PojoBooking> filters = interactor.getFilters();

        if (filters.isEmpty()) {

            Utils.Log(TAG, "Get all data cuz filters is empty");

            List<PojoBooking> pojoBookings = new ArrayList<>(interactor.getAllPackages());
            if (pojoBookings.size() > 0) {

                // Check, if local data contain at least one "Booked"
                // then show "request pickup" section
                // long bookedItemCount = interactor.getBookedPackageAvailability();
                // if (bookedItemCount > 0L) {
                    // view.showRequestPickupItem(createRequestPickupItem(bookedItemCount));
                    // pojoBookings.add(0, createRequestPickupItem(bookedItemCount));
                // }

                // Save last Sender info for "New Package" purpose
                // Assume first index on list is latest Sender
                mSharedPrefRepo.setLastSender(pojoBookings.get(0));

                // Show all packages on view
                view.showViewState(UIState.FINISHED);
                view.showAllPackages(pojoBookings);

                // Show load more item when all booking size is more than or equals to MAX_CAPACITY
                if (pojoBookings.size() >= Constants.MAX_PACKAGES_ON_DB) {
                    view.showLoadMoreItem(createLoadMoreItem());
                }

            } else {

                // Version 3.0.0
                // User has updated the app
                // Check case NO_DATA because update the app
                if (this.mSharedPrefRepo.isUserLoggedIn()) {

                    this.view.showPrepareAllData(true);
                    this.requestPackagesOnline(true);


                } else {
                    MyLog.FabricLog(Log.WARN, TAG + " - Package is empty.");
                    // Update filter to met no-data state
                    view.showFilteredPackages(new ArrayList<PojoBooking>(), new ArrayList<String>());
                    view.showViewState(UIState.NODATA);
                }
            }
        } else {

            Utils.Log(TAG, "Get filtered data cuz filters is available");

            ArrayList<String> filterBookingTexts = new ArrayList<>();
            for(PojoBooking filterItem : filters) {
                filterBookingTexts.add(filterItem.getBooking_status_text());
            }

            getLocalFilteredList(filterBookingTexts);
        }
    }

    @Override
    public void getLocalFilteredList(ArrayList<String> filterBookingTexts) {

        Utils.Log(TAG, "Get filtered list. Filters: " + filterBookingTexts);

        List<PojoBooking> filteredPackages = interactor.getFilteredPackages(filterBookingTexts);
        if (filteredPackages != null && filteredPackages.size() > 0) {

            // Save last Sender info for "New Package" purpose
            // Assume first index on list is latest Sender
            mSharedPrefRepo.setLastSender(filteredPackages.get(0));

            // Make sure list is visible
            view.showViewState(UIState.FINISHED);

            view.showFilteredPackages(filteredPackages, filterBookingTexts);
        } else {
            view.showViewState(UIState.NODATA);
        }
    }

    @Override
    public void refreshPackageList() {
        view.refreshRecyclerView();
    }

    @Override
    public void updateViewState(UIState uiState) {
        view.showViewState(uiState);
    }

    @Override
    public void deletePackage(long bookingId, int position) {

        if (position == 0) {

            // Delete data from database
            position = interactor.deletePackageByBookingId(bookingId);

            if (position != -1) {
                Utils.Logs('w', TAG, "Deleted package id: " + bookingId + " in pos: " + position);

                // [V49]
                // Show proper view based on Package count
                // Empty state when no data found.
                List<PojoBooking> pojoBookings = interactor.getAllPackages();
                Utils.Logs('w', TAG, "[DELETE SECTION] All package size:" + pojoBookings.size());
                if (pojoBookings.isEmpty()) {

                    // Update filter to met no-data state
                    view.showFilteredPackages(new ArrayList<PojoBooking>(), new ArrayList<String>());

                    view.showViewState(UIState.NODATA);
                } else {
                    view.showViewState(UIState.FINISHED);

                    // Animate item deletion on list view
                    view.deleteRecyclerItemAt(position);
                }
            }

        } else {
            // [V54] Item id and item position defined.
            interactor.deletePackageByBookingId(bookingId);

            List<PojoBooking> pojoBookings = interactor.getAllPackages();
            Utils.Logs('w', TAG, "[DELETE SECTION] All package size:" + pojoBookings.size());
            if (pojoBookings.isEmpty()) {

                // Update filter to met no-data state
                view.showFilteredPackages(new ArrayList<PojoBooking>(), new ArrayList<String>());

                view.showViewState(UIState.NODATA);
            } else {
                view.showViewState(UIState.FINISHED);

                // Animate item deletion on list view
                view.deleteRecyclerItemAt(position);
            }
        }
    }

    @Override
    public void startPackageDetail(long bookingId, int itemPosition) {
        view.showPackageDetail(bookingId, itemPosition);
    }

    @Override
    public void startNewPackage() {
        view.showNewPackageScreen();
    }

    @Override
    public void startPickupMap() {
        view.showPickupMapScreen();
    }

    @Override
    public void startTutorial() {
        view.showTutorial();
    }

    /**
     * Save PojoUser data to Shared Preferences.
     *
     * @param pojoUser PojoUser data.
     */
    @Override
    public void onUserDataResponse(PojoUser pojoUser) {
        mSharedPrefRepo.setUserInfo(pojoUser);

    }

    @Override
    public void onAndroidVersionResponse(int intAndroidVersion) {
        // TODO For next, save 1: when app is out dated, 0: app is up to date

        // [V48] For now save app version
        mSharedPrefRepo.setAppVersionOutDated(intAndroidVersion);
    }

    @Override
    public void onLastUpdateResponse(String strUpdatedOn) {
        mSharedPrefRepo.setUpdatedOn(strUpdatedOn);
    }

    @Override
    public void onVendorsResponse(ArrayList<PojoCourier> pojoCourierArrayList) {
        // Save Vendor data to realm
        // [v49] Always replace all offline vendor with online response.
        interactor.saveVendors(pojoCourierArrayList);
    }

    @Override
    public void onAreaInfoResponse(int intIsDownloadNeeded) {
        mSharedPrefRepo.setIsLoadAreaNeeded(intIsDownloadNeeded);
    }

    @Override
    public void onPickupHistoryResponse(ArrayList<PojoPickupHistory> pojoPickupHistoryArrayList) {
        int totalOtwDriver = 0;
        ArrayList<PojoPickupHistory> pojoPickupHistories = new ArrayList<>();
        for (PojoPickupHistory pojoPickupHistory : pojoPickupHistoryArrayList) {
            int statusNumber = Utils.getParsedInt(pojoPickupHistory.getStatus());

            // Add number of OTW driver
            totalOtwDriver = statusNumber == PojoPickupHistory.STATUS_OTW ?
                    totalOtwDriver + 1 : totalOtwDriver;

            pojoPickupHistories.add(pojoPickupHistory);
        }

        Utils.Log(TAG, "OTW driver number: " + totalOtwDriver);

        // Save number of OTW driver to SP
        mSharedPrefRepo.setNumberOfOtwDriver(totalOtwDriver);

        interactor.savePickupHistory(pojoPickupHistories);
    }

    /**
     * Create {@link PojoBooking} for "Request Pickup" item. The "request pickup" item indicated
     * with {@link PojoBooking#content}'s value, {@code booked_package_number}.
     *
     * @return PojoBooking for header purpose.
     */
    private PojoBooking createRequestPickupItem(long bookedItemCount) {

        Utils.Log(TAG, "Create request pickup");

        final PojoBooking pickupNowItem = new PojoBooking();
        final String strBookedNumber = String.valueOf(bookedItemCount);

        pickupNowItem.setBooking_id(Constants.HEADER_ITEM_ID);
        pickupNowItem.setContent(strBookedNumber);
        return pickupNowItem;
    }

    /*
     * Create {@link PojoBooking} as "Load more" button on the bottom of the list. <br />
     * The load more item recognized by looking into package {@link PojoBooking#content}
     * with value {@code * (asterisk)}.
     *
     * @return PojoBooking as "load more" item.
     *
     * @since Dec 20 2016 - V47: Set {@link AppClass#FOOTER_LOAD_MORE_ITEM_ID} as booking id.
     */
    private PojoBooking createLoadMoreItem() {

        Utils.Log(TAG, "Create load more item");

        final PojoBooking loadMoreItem = new PojoBooking();
        loadMoreItem.setBooking_id(Constants.FOOTER_LOAD_MORE_ITEM_ID);
        loadMoreItem.setContent("*");
        return loadMoreItem;
    }
}
