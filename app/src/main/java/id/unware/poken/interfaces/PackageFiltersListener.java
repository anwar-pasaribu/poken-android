package id.unware.poken.interfaces;

import java.util.ArrayList;

/**
 * @author Anwar Pasaribu
 * @since Dec 13 2016
 */

public interface PackageFiltersListener {
    void onFilterItemClick(String strBookingStatus);
    void onFilterDone(ArrayList<String> statusTextList);
    void onFilterReset();
}
