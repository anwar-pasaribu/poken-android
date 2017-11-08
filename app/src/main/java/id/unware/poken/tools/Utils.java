package id.unware.poken.tools;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import id.unware.poken.BuildConfig;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDate;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.models.Tracking;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoCity;
import id.unware.poken.tools.chromecustomtabs.CustomTabActivityHelper;

import static android.content.Context.CLIPBOARD_SERVICE;

public class Utils {

    public static final String TAG = "poken";
    private static List<String[]> resultList;
    private static final long ANIM_DUR = 500;
    private static final int height = 400;
    private static final LinkedList<PopupWindow> popupSnack = new LinkedList<>();

    public static boolean isEmpty(String str) {
        return str == null || str.trim().equals("") || str.trim().toLowerCase().equals("null");
    }

    public static boolean isNetworkNotConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni == null;
    }

    public static void Log(String msg, String value) {
        if (BuildConfig.DEV_MODE) {
            // Null value state when no value found
            value = value == null ? "NULL_LOG_VAL" : value;
            try {
                if (value.length() > 15000) {
                    value = value.substring(0, 15000);
                }
                int maxLength = 1000;
                if (!StringUtils.isEmpty(value)) {
                    if (value.length() > maxLength) {
                        Log.v(TAG, msg + " " + value.substring(0, maxLength));
                        Log(msg, value.substring(maxLength));
                    } else {
                        Log.v(TAG, msg + " " + value.substring(0, value.length()));
                    }
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Different Logging for INFO, DEBUG, ERROR.
     *
     * @param logAttr : Character to indicate log type.
     * @param msg     : String message to print
     * @param value   : Value string
     */
    public static void Logs(char logAttr, String msg, String value) {
        if (BuildConfig.DEV_MODE) {
            // Null value state when no value found
            value = value == null ? "NULL_LOGS_VAL" : value;
            try {
                if (value.length() > 15000) {
                    value = value.substring(0, 15000);
                }

                int maxLength = 1000;
                if (!StringUtils.isEmpty(value)) {
                    if (value.length() > maxLength) {
                        switch (logAttr) {
                            case 'e':
                                Log.e(TAG, msg + " " + value.substring(0, maxLength));
                                Logs('e', msg, value.substring(maxLength));
                                break;
                            case 'w':
                                Log.w(TAG, msg + " " + value.substring(0, maxLength));
                                Logs('w', msg, value.substring(maxLength));
                                break;
                            case 'i':
                                Log.i(TAG, msg + " " + value.substring(0, maxLength));
                                Logs('i', msg, value.substring(maxLength));
                                break;
                            case 'd':
                                Log.d(TAG, msg + " " + value.substring(0, maxLength));
                                Logs('d', msg, value.substring(maxLength));
                            default:
                                Log.v(TAG, msg + " " + value.substring(0, maxLength));
                                Logs('v', msg, value.substring(maxLength));

                        }
                    } else {
                        switch (logAttr) {
                            case 'e':
                                Log.e(TAG, msg + " " + value.substring(0, value.length()));
                                break;
                            case 'w':
                                Log.w(TAG, msg + " " + value.substring(0, value.length()));
                                break;
                            case 'i':
                                Log.i(TAG, msg + " " + value.substring(0, value.length()));
                                break;
                            case 'd':
                                Log.d(TAG, msg + " " + value.substring(0, value.length()));
                                break;
                            default:
                                Log.v(TAG, msg + " " + value.substring(0, value.length()));
                        }
                    }
                }

            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static boolean contains(String source, String contain) {
        return source.toLowerCase().contains(contain.toLowerCase());
    }

    public static void hideKeyboardFrom(Context context, View view) {
        if (view != null && context != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Display modified Toast with big font.
     *
     * @param context : Context where to display the Toast.
     * @param msg     : String message to show.
     */
    public static void toast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        // toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
        try {
            LinearLayout toastLayout = (LinearLayout) toast.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(20);
            toast.show();
        } catch (ClassCastException cce) {
            cce.printStackTrace();

            ViewGroup toastParent = (ViewGroup) toast.getView();
            if (toastParent.getChildCount() != 0) {
                TextView toastTV = (TextView) toastParent.getChildAt(0);
                toastTV.setTextSize(20);
                toast.show();
            }
        }
    }

    /**
     * Show Toast that only appear on DEV_MODE.
     *
     * @param context : Context where to display the Toast.
     * @param msg     : String message to show.
     */
    public static void devModeToast(Context context, String msg) {
        if (BuildConfig.DEV_MODE) {
            Toast toast = Toast.makeText(context, String.format("[D!] %s", msg), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    /**
     * Create custom Snackbar by inlfating layout {@code snack_custom}
     * (WARNING, process kinda expensive).
     *
     * @param rootView Parent conatiner for views.
     * @param strMessage Message to show on Snacbar.
     * @param intLogLevel Message state whether {@code ERROR, INFO, DEBUG, or WARNING}
     */
    public static void snackBar(@NonNull View rootView, @NonNull String strMessage, int intLogLevel) {
        try {
            PopupWindow popupWindow = new PopupWindow(rootView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            final Context context = rootView.getContext();

            int intColor = ContextCompat.getColor(context, R.color.black_90);
            switch (intLogLevel) {
                case Log.INFO:
                    intColor = ContextCompat.getColor(context, R.color.green);
                    break;
                case Log.DEBUG:
                    intColor = ContextCompat.getColor(context, R.color.black_90);
                    break;
                case Log.WARN:
                    intColor = ContextCompat.getColor(context, R.color.package_status_picked);
                    break;
                case Log.ERROR:
                    intColor = Color.RED;
                    break;
            }

            View view = LayoutInflater.from(context).inflate(R.layout.snack_custom, null, false);

            ViewGroup parentContent = view.findViewById(R.id.parentContent);
            TextView txtmessage = view.findViewById(R.id.txtMessage);
            ImageButton snackBarBtn = view.findViewById(R.id.btnCloseSnackbar);

            // Set snackbar message
            txtmessage.setText(strMessage);
            txtmessage.setTextColor(intColor);

            // Animate content
            parentContent.setTranslationY(height * -1);
            parentContent.animate().translationY(0).setDuration(ANIM_DUR).start();

            popupWindow.setContentView(view);

            int location[] = new int[2];
            rootView.getLocationOnScreen(location);
            popupWindow.showAtLocation(rootView, Gravity.NO_GRAVITY, 0, location[1]);

            popupSnack.add(popupWindow);

            snackBarBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyLog.FabricTrackContentView(Tracking.TRACK_FEATURE_CLOSE_SNACKBAR, Tracking.TRACK_TYPE_FEATURE, Tracking.TRACK_ID_SECONDARY_FUNCTION);

                    Utils.snackbarDismiss();
                }
            });

        } catch (WindowManager.BadTokenException ex){
            MyLog.FabricLog(Log.ERROR, "Custom snackbar exception occour.", ex);
            ex.printStackTrace();
        }
    }

    /**
     * Show custom {@code Snackbar} which is show up from top of the screen.
     *
     * @param rootView Parent conatiner for views.
     * @param s Message to show on Snacbar.
     * @since Nov 21 2016 - In order to variate Snackbar text color, creation now done
     *                      on {@link Utils#snackBar(View, String, int)}.
     */
    public static void snackBar(@NonNull View rootView, @NonNull String s) {
        snackBar(rootView, s, Log.DEBUG);
    }

    public static void snackbarDismissImmediately() {
        PopupWindow popupWindow;
        while ((popupWindow = popupSnack.poll()) != null) {
            try {
                popupWindow.dismiss();
            } catch (IllegalArgumentException ex) {
                MyLog.FabricLog(Log.ERROR, "Snackbar exception occour while dismiss Snackbar.", ex);
                Utils.Log(TAG, "Illegal exception: " + ex.getMessage());
            }
        }
    }

    public static void snackbarDismiss() {
        if (popupSnack.size() > 0) {
            final PopupWindow popupWindow = popupSnack.poll();
            if (popupWindow.isShowing()) {

                ViewGroup parentContent = popupWindow.getContentView().findViewById(R.id.parentContent);

                parentContent.animate().translationY(height * -1).setDuration(ANIM_DUR).start();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            popupWindow.dismiss();
                        } catch (IllegalArgumentException ex) {
                            MyLog.FabricLog(Log.ERROR, "Snackbar exception occour while dismiss Snackbar.", ex);
                            Utils.Log(TAG, "Illegal exception: " + ex.getMessage());
                        }

                        Utils.Log(TAG, "Popup window size " + popupSnack.size());
                    }
                }, ANIM_DUR);
            }
        }
    }

    public static List<String[]> readCSV(InputStream inputStream) {
        if (resultList == null) {
            resultList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            try {
                String csvLine;
                while ((csvLine = reader.readLine()) != null) {
                    String[] row = csvLine.split(",");
                    resultList.add(row);
                }
            } catch (IOException ex) {
                throw new RuntimeException("Error in reading CSV file: " + ex);
            } finally {

                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("[Utils] Error while closing input stream.");
                }

            }
        }
        return resultList;
    }

    public static void changeFragment(FragmentActivity fragmentActivity,
                                      int layoutFragment, Fragment fragment, String tag,
                                      boolean toBackStack) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        ft.replace(layoutFragment, fragment, tag);
        if (toBackStack) {
            ft.addToBackStack(tag);
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        fragmentActivity.supportInvalidateOptionsMenu();
    }

    /**
     * Replace fragment container with selected fragment.
     *
     * @param fragmentActivity : Activity to host the fragment.
     * @param layoutFragment   : Fragment layout to accept the fragment.
     * @param fragment         : Replacement for fragment container.
     */
    public static void changeFragment(FragmentActivity fragmentActivity,
                                      int layoutFragment, Fragment fragment) {

        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager().beginTransaction();
        ft.replace(layoutFragment, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_NONE);
        ft.commit();
        fragmentActivity.supportInvalidateOptionsMenu();
    }


    public static Fragment getCurrentFragment(FragmentActivity fragmentActivity, int layoutFragment) {
        return fragmentActivity.getSupportFragmentManager().findFragmentById(layoutFragment);
    }

    public static void addFragment(FragmentActivity fragmentActivity,
                                   int layoutFragment, Fragment... fragment) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        for (Fragment fragment1 : fragment) {
            ft.add(layoutFragment, fragment1);
        }

        // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public static void showFragment(FragmentActivity fragmentActivity,
                                    Fragment fragmentToShow, Fragment... fragment) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        for (Fragment fragment1 : fragment) {
            ft.hide(fragment1);
        }
        ft.show(fragmentToShow);
        ft.commit();
    }

    public static Fragment findFragmentByTag(FragmentActivity fragmentActivity,
                                             String tag) {

        return fragmentActivity.getSupportFragmentManager().findFragmentByTag(
                tag);
    }

    public static String dateToTimeStamp(Date date) {
        return (date.getTime() / 1000) + "";
    }

    public static String getCurrentTimeStamp() {
        return dateToTimeStamp(new Date());
    }

    public static String CapsFirst(String str) {
        String[] words = str.trim().split(" ");
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            ret.append(Character.toUpperCase(words[i].charAt(0)));
            ret.append(words[i].substring(1).toLowerCase());
            if (i < words.length - 1) {
                ret.append(' ');
            }
        }
        return ret.toString();
    }

    public static boolean isTelephonyEnabled(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    /**
     * Open phone dialer when device support it.
     *
     * @param strPhoneNumber : String phone number.
     *
     * @since Nov 14 2016 - "resolveActivity()" check whether activity is available.
     */
    public static void openDialer(Context context, String strPhoneNumber) {

        MyLog.FabricLog(Log.INFO, "Open dialer for number: " + strPhoneNumber);

        String uri = String.format("tel:%s", strPhoneNumber.trim());
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(uri));

        if (intent.resolveActivity(context.getPackageManager()) != null
                && isTelephonyEnabled(context)) {

            context.startActivity(intent);

        } else {

            // Copy phone number when phone feature not available
            String strToCopy = context.getString(R.string.msg_info_string_copied, strPhoneNumber);
            Utils.toast(context, strToCopy);

            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("copied_string_paket_id_phone", strToCopy);
            clipboard.setPrimaryClip(clip);
        }

    }


    /**
     * Compose email with place holder text (App version).
     *
     * @param context  : Context to call this feature
     * @param msgTitle : Mail subject.
     */
    public static void composeEmail(Context context, String msgTitle) {
        final String strHtmlContent = context.getString(R.string.email_support_compose_contents,
                Utils.getAppVersionString(context));

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", context.getString(R.string.support_email), null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(strHtmlContent));
        // emailIntent.putExtra(Intent.EXTRA_TEXT, Utils.getAppVersionString(context));
        context.startActivity(Intent.createChooser(emailIntent, msgTitle));
    }

    /**
     * Provide formatted (APP_NAME \n APP_VERSION) app version.
     *
     * @param context : Context of the app
     * @return : Formatted string.
     */
    public static String getAppVersionString(Context context) {
        // Show app version
        Resources appR = context.getResources();
        CharSequence txt = appR.getText(appR.getIdentifier("app_name",
                "string", context.getPackageName()));

        //String appName =
        String appVersionName = BuildConfig.VERSION_NAME;
        int appVersionCode = BuildConfig.VERSION_CODE;

        return context.getString(R.string.lbl_app_version, txt.toString(), appVersionName, appVersionCode);
    }

    /**
     * Open PLay Store to rate Paket ID
     * <p/>
     * Source: http://stackoverflow.com/questions/10816757/rate-this-app-link-in-google-play-store-app-on-the-phone
     */
    public static void rateOnPlayStore(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);

        // New document flag when on API >= 20
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        }

        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public static double getParsedDouble(String strNumber) {

        if (Utils.isEmpty(strNumber)) return 0;

        NumberFormat format = NumberFormat.getInstance(ControllerDate.getInstance().getDefLocale());
        Number number = null;

        double doubleResult = 0;

        try {
            number = format.parse(strNumber);
        } catch (ParseException e) {
            MyLog.FabricLog(Log.ERROR, "[1st] Double parsing for \"" + strNumber + "\" error.", e);
            e.printStackTrace();

            doubleResult = Double.parseDouble(strNumber.replaceAll(",", "."));

        } catch (NumberFormatException e) {
            MyLog.FabricLog(Log.ERROR, "[2st] Double parsing for \"" + strNumber + "\" error.", e);
            e.printStackTrace();
        }

        return number != null ? number.doubleValue() : doubleResult;
    }

    public static float getParsedFloat(String strNumber) {

        if (Utils.isEmpty(strNumber)) return 0;

        NumberFormat format = NumberFormat.getInstance(ControllerDate.getInstance().getDefLocale());
        Number number = null;

        float floatResult = 0F;

        try {
            number = format.parse(strNumber);
        } catch (ParseException e) {
            MyLog.FabricLog(Log.ERROR, "[1st] Float parsing for \"" + strNumber + "\" error.", e);
            e.printStackTrace();

            floatResult = Float.parseFloat(strNumber.replaceAll(",", "."));

        } catch (NumberFormatException e) {
            MyLog.FabricLog(Log.ERROR, "[2st] Float parsing for \"" + strNumber + "\" error.", e);
            e.printStackTrace();
        }

        return number != null ? number.floatValue() : floatResult;
    }

    public static long getParsedLong(String strNumber) {

        if (Utils.isEmpty(strNumber)) return 0;

        NumberFormat format = NumberFormat.getInstance(ControllerDate.getInstance().getDefLocale());
        Number number = null;
        try {
            number = format.parse(strNumber);
        } catch (ParseException e) {
            MyLog.FabricLog(Log.ERROR, "Long parsing for \"" + strNumber + "\" error.", e);
            e.printStackTrace();
        }

        return number != null? number.longValue() : 0L;
    }

    public static int getParsedInt(String strNumber) {

        if (Utils.isEmpty(strNumber)) return 0;

        NumberFormat format = NumberFormat.getInstance(ControllerDate.getInstance().getDefLocale());
        Number number = null;
        try {
            number = format.parse(strNumber);
        } catch (ParseException e) {
            MyLog.FabricLog(Log.ERROR, "Int parsing for \"" + strNumber + "\" error.", e);
            e.printStackTrace();
        }

        return number != null? number.intValue() : 0;
    }

    public static void openInBrowser(final Activity activity, final String strUrl) {

        if (Utils.isEmpty(strUrl)) return;

        MyLog.FabricLog(Log.INFO, "Open in browser: " + strUrl);
        try {
            final Context ctx = activity.getApplicationContext();
            // For custom tabs purpose
            final String CUSTOM_TAB_PACKAGE_NAME = BuildConfig.APPLICATION_ID;

            // This useful for pre-lollipop Android
            CustomTabsServiceConnection mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
                CustomTabsClient mCustomTabsClient;

                @Override
                public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                    Utils.Log(TAG, "[ CustomTabsServiceConnection ] Tab service connected");
                    mCustomTabsClient = customTabsClient;
                    mCustomTabsClient.warmup(0L);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    Utils.Log(TAG, "[ CustomTabsServiceConnection ] Tab service disconnected");
                    mCustomTabsClient = null;
                }
            };
            CustomTabsClient.bindCustomTabsService(ctx, CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);

            CustomTabsIntent.Builder customTabsIntentBuilder = new CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .setToolbarColor(ContextCompat.getColor(ctx, R.color.colorPrimary));

            CustomTabsIntent customTabsIntent = customTabsIntentBuilder.build();

            // Launch Custom tab
            // or launch default browser when customtabs isn't available.
            CustomTabActivityHelper.openCustomTab(
                    activity, customTabsIntent, Uri.parse(strUrl), new CustomTabActivityHelper.CustomTabFallback() {
                        @Override
                        public void openUri(Activity activity, Uri uri) {

                            MyLog.FabricLog(Log.INFO, "Begin to open default browser activity for url : " + uri);

                            try {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(uri);
                                activity.startActivity(intent);
                            } catch (android.content.ActivityNotFoundException ex) {
                                ex.printStackTrace();
                                MyLog.FabricLog(Log.ERROR, "Default browser activity not found to open : " + uri, ex);
                            }

                        }
                    });

        } catch (android.content.ActivityNotFoundException e) {
            e.printStackTrace();
            MyLog.FabricLog(Log.ERROR, "Custom Tabs activity not found to open : " + strUrl, e);
        }
    }

    public static void setLastSenderData(PojoBooking pojoBooking) {

        SPHelper sp = SPHelper.getInstance();

        sp.setPreferences(Constants.SHARED_NAME, pojoBooking.getFrom_name());
        sp.setPreferences(Constants.SHARED_ADDRESS, pojoBooking.getFrom_address());
        sp.setPreferences(Constants.SHARED_POST_CODE, pojoBooking.getFrom_zip_code());
        sp.setPreferences(Constants.SHARED_PHONE, pojoBooking.getFrom_phone());

        Utils.Log(TAG, "Form name: " + pojoBooking.getFrom_name());
    }

    public static PojoCity getCityById(Context context, String id) {
        // area_id,city,state,country,country_code,importance,timestamp
        List<String[]> listArrayCity = readCSV(context.getResources()
                .openRawResource(R.raw.area_detail));
        PojoCity result = new PojoCity();
        // id,kelurahan,kecamatan,kabupaten,provinsi,kodepos
        for (String[] city : listArrayCity) {
            if (city[0].equals(id)) {
                result.setAreaId(Integer.parseInt(city[0]));
                result.setCity(city[1]);
                result.setState(city[2]);
                result.setCountry(city[3]);
                result.setCountry_code(city[4]);
                result.setImportance(Integer.parseInt(city[5]));
                result.setTimestamp(city[6]);
            }
        }
        return result;
    }

    /**
     * Intent to open the official Instagram app to the user's profile. If the Instagram app is not
     * installed then the Web Browser will be used.</p>
     *
     * Example usage:</p> {@code newInstagramProfileIntent(context.getPackageManager(),
     *     "http://instagram.com/jaredrummler");}</p>
     *
     * @param pm
     *            The {@link PackageManager}. You can find this class through
     *            {@link Context#getPackageManager()}.
     * @param url
     *            The URL to the user's Instagram profile.
     * @return The intent to open the Instagram app to the user's profile.
     */
    public static Intent newInstagramProfileIntent(PackageManager pm, String url) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                if (url.endsWith("/")) {
                    url = url.substring(0, url.length() - 1);
                }
                final String username = url.substring(url.lastIndexOf("/") + 1);
                // http://stackoverflow.com/questions/21505941/intent-to-open-instagram-user-profile-on-android
                intent.setData(Uri.parse("http://instagram.com/_u/" + username));
                intent.setPackage("com.instagram.android");
                return intent;
            } else {
                Utils.Logs('e', TAG, "com.instagram.android not found.");
            }
        } catch (PackageManager.NameNotFoundException ignored) {
            ignored.printStackTrace();
        }
        intent.setData(Uri.parse(url));
        return intent;
    }
}
