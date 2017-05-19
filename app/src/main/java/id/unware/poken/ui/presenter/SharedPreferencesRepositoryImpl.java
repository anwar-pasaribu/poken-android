package id.unware.poken.ui.presenter;

import android.util.Log;

import id.unware.poken.helper.SPHelper;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoUser;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;

/**
 * Controll saving data to Shared Preferences.
 *
 * @author Anwar Pasaribu
 * @since Jan 20 2017
 */

public class SharedPreferencesRepositoryImpl implements SharedPreferencesRepository {

    private final String TAG = "SharedPreferencesRepositoryImpl";

    private SPHelper mSp;

    public SharedPreferencesRepositoryImpl() {
        mSp = SPHelper.getInstance();
    }

    @Override
    public boolean isUserLoggedIn() {
        // Check if user auto login
        String session = mSp.getSharedPreferences(Constants.SHARED_COOKIE, "");
        String userEmail = mSp.getSharedPreferences(Constants.SHARED_EMAIL, "");

        MyLog.FabricLog(Log.INFO, TAG + " - Auto Login with session: \"" + session + "\", email: \"" + userEmail + "\"");

        return (!StringUtils.isEmpty(session) && !StringUtils.isEmpty(userEmail));
    }

    @Override
    public void setUserInfo(PojoUser user) {

        Utils.Log(TAG, "Save user data: " + user);

        mSp.setPreferences(Constants.SHARED_EMAIL, user.getUserEmail());
        mSp.setPreferences(Constants.SHARED_PROFILE_NAME,
                StringUtils.isEmpty(user.getUserName()) ? "" : user.getUserName());
        mSp.setPreferences(Constants.SHARED_PROFILE_PHONE,
                StringUtils.isEmpty(user.getUserPhone()) ? "" : user.getUserPhone());
        mSp.setPreferences(Constants.SHARED_PROFILE_PHONE_VERIFY, user.getPhoneVerified());

        mSp.setPreferences(Constants.USER_ID, user.getUserId());

    }

    @Override
    public void setUpdatedOn(String strUpdatedOn) {
        Utils.Log(TAG, "Save updated on: " + strUpdatedOn);

        mSp.setPreferences(Constants.SHARED_LAST_UPDATE, strUpdatedOn);
    }

    @Override
    public void setIsLoadAreaNeeded(int intSaveOrno) {
        Utils.Log(TAG, "Download Indonesia's area (0/1) ? --> " + intSaveOrno);

        mSp.setPreferences(Constants.SHARED_LOAD_AREA_LV0, intSaveOrno);
    }

    @Override
    public void setNumberOfOtwDriver(int intNumberOfOtw) {
        Utils.Log(TAG, "Save number of OTW driver: " + intNumberOfOtw);

        mSp.setPreferences(Constants.SHARED_OTW_DRIVER, intNumberOfOtw);
    }

    @Override
    public void setAppVersionOutDated(int intAppVersion) {
        Utils.Log(TAG, "Save app version: " + intAppVersion);

    }

    @Override
    public void setLastSender(PojoBooking pojoBooking) {

        Utils.Log(TAG, "Save last Sender: " + pojoBooking.getFrom_name());

        mSp.setPreferences(Constants.SHARED_NAME, pojoBooking.getFrom_name());
        mSp.setPreferences(Constants.SHARED_ADDRESS, pojoBooking.getFrom_address());
        mSp.setPreferences(Constants.SHARED_POST_CODE, pojoBooking.getFrom_zip_code());
        mSp.setPreferences(Constants.SHARED_PHONE, pojoBooking.getFrom_phone());
    }
}
