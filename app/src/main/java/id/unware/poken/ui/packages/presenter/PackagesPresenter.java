package id.unware.poken.ui.packages.presenter;

import java.util.ArrayList;

import id.unware.poken.pojo.UIState;

/**
 * @author Anwar Pasaribu
 * @since Dec 21 2016
 */

public interface PackagesPresenter {

    void requestPackagesOnline(boolean reloadAll);
    void requestMorePackagesOnline();

    void getLocalPackageList();
    void getLocalFilteredList(ArrayList<String> bookingStatus);

    void deletePackage(long bookingId, int position);
    void refreshPackageList();
    void updateViewState(UIState uiState);

    void startPackageDetail(long bookingId, int itemPosition);
    void startNewPackage();
    void startPickupMap();
    void startTutorial();  // Trigger Create new package tutorial
}
