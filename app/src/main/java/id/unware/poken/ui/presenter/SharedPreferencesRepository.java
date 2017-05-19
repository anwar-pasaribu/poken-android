package id.unware.poken.ui.presenter;

import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoUser;

/**
 * @author Anwar Pasaribu
 * @since Jan 20 2017
 */

public interface SharedPreferencesRepository {

    boolean isUserLoggedIn();

    void setUserInfo(PojoUser user);
    void setUpdatedOn(String strUpdatedOn);
    void setIsLoadAreaNeeded(int intSaveOrno);
    void setNumberOfOtwDriver(int intNumberOfOtw);
    void setAppVersionOutDated(int intAppVersion);

    // Save last Sender data
    void setLastSender(PojoBooking pojoBooking);
}
