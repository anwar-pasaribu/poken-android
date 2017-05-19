package id.unware.poken.tools;

import id.unware.poken.BuildConfig;

/*
 * Generate {@link com.crashlytics.android.Crashlytics} logging purpose.
 *
 * @author Anwar Pasaribu
 * @since Oct 20 2016 (V 24) - NEW!
 */

public class MyLog extends Utils{
    private static final int MAX_LENGTH = 1000;
    /**
     * Show log on Fabric dashboard. Message should not over 64KB long.
     *
     * @param logPriority : Log priority number, ex. Log.DEBUG
     * @param msg : Message to show. The message limits to 64KB
     */
    public static void FabricLog(int logPriority, String msg) {
        if (BuildConfig.enableCrashlytics) {

            if (!msg.isEmpty() && msg.length() > MAX_LENGTH) {
                msg = String.format("%s [...]", msg.substring(0, 256));
            }

//            Crashlytics.log(logPriority, TAG, String.format("\"%s\"", msg));
        }
    }

    /*
     * Generate Crashlytics logging with {@link Throwable} object.
     * The message on this method contain general info for logging such as when logging occour
     * and {@link Throwable} object generate {@link Crashlytics} stacktrace.
     *
     * @param logPriority   Logging level (ERROR, INFO, etc.)
     * @param msg           Messaeg to show
     * @param exception     {@link Throwable} object occours.
     *
     * @since Oct. 20, 2016 (V 24) - NEW!
     */
    public static void FabricLog(int logPriority, String msg, java.lang.Throwable exception) {

        if (BuildConfig.enableCrashlytics) {

            if (!msg.isEmpty() && msg.length() > MAX_LENGTH) {
                msg = String.format("%s [...]", msg.substring(0, 256));
            }

//            Crashlytics.log(logPriority, TAG, String.format("General msg: \"%s\". \nException msg: \"%s\"", msg, exception.getMessage()));

            // Log stacktrace for Fabric
//            Crashlytics.logException(exception);
        }

    }

    /**
     * Crashlytics user information include :
     * - User ID - Contain phone number
     * - User Email
     * - User Name
     *
     * @since (Oct. 12th, 2016) Version 24
     */
    public static void FabricSetUserInformation() {
        if (BuildConfig.enableCrashlytics) {
//            Util.Log("Util", "Set Fabric user information");
//            SPHelper spHelper = SPHelper.getInstance();
//            Crashlytics.setUserIdentifier(String.format("tel:%s", spHelper.getSharedPreferences(Config.USER_PHONE, "0")));
//            Crashlytics.setUserEmail(spHelper.getSharedPreferences(Config.USER_EMAIL, "NO_EMAIL"));
//            Crashlytics.setUserName(spHelper.getSharedPreferences(Config.USER_NAME, "NO_USERNAME"));
        }
    }
}
