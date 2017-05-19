package id.unware.poken.ui.packages.view;
import java.util.ArrayList;
import java.util.List;

import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.UIState;

/**
 * @author Anwar Pasaribu
 * @since Dec 21 2016
 */

public interface PackagesView {

    void showAllPackages(List<PojoBooking> pojoBookingList);
    void showFilteredPackages(List<PojoBooking> pojoBookingList, ArrayList<String> filterStrings);

    void showPackageDetail(long bookingId, int itemPosition);
    void showNewPackageScreen();
    void showNewPackages(List<PojoBooking> newBookingList);
    void showMorePackages(List<PojoBooking> moreBookingList);

    void deleteRecyclerItemAt(int position);
    void refreshRecyclerViewItem(int position);
    void refreshRecyclerView();

    void showRequestPickupItem(PojoBooking requestPickupItem);
    void showLoadMoreItem(PojoBooking loadMoreItem);
    void showPickupMapScreen();

    void showViewState(UIState UIState);

    void showTutorial();

    void showPrepareAllData(boolean isShow);
}
