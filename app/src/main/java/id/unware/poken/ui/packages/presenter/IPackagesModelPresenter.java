package id.unware.poken.ui.packages.presenter;


import java.util.ArrayList;
import java.util.List;

import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.pojo.PojoUser;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Feb 24 2017
 */

public interface IPackagesModelPresenter extends BasePresenter {
    void onPackagesResponse(List<PojoBooking> pojoBookingList);
    void onMorePackagesResponse(List<PojoBooking> pojoBookingList);
    void onNewlyCreatedPackagesResponse(List<PojoBooking> newlyCreatedPackage);

    // NON PACKAGE RELATED METHOD
    // Proceed get_data response
    void onUserDataResponse(PojoUser pojoUser);
    void onAndroidVersionResponse(int intAndroidVersion);
    void onLastUpdateResponse(String strUpdatedOn);
    void onVendorsResponse(ArrayList<PojoCourier> pojoCourierArrayList);
    void onAreaInfoResponse(int intIsDownloadNeede);  // 0: No download necessary, 1: Download necessary
    void onPickupHistoryResponse(ArrayList<PojoPickupHistory> pojoPickupHistoryArrayList);
}
