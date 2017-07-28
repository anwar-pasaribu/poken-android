package id.unware.poken;

import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.squareup.picasso.Picasso;

import java.util.Map;

import id.unware.poken.helper.SPHelper;
import id.unware.poken.httpConnection.MyRequest;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import com.facebook.FacebookSdk;


public class PokenApp extends Application {

    private final String TAG = "AppClass";


    private String lastTag;

    /**
     * Selected item position on Navigation Menu. <br /> Default value set 0 (Menu Package)
     */
    public int lastNavDrawerItemId = 0;

    public long idDetail = -1;
    public static int toolbarHeight = 0;
    public static int statusBarHeight = 0;

    // Flag for New Package edit text (focus or not)
    public boolean focusOnSender = false;

    /**
     * Extra detail var on App level.
     * The var. changed from onActivityResult in FragmentPickupForm.
     */
    public String strExtraDetailFromActivityResult = "";

    // Version code
    private int appVersionCode = 0;

    private static PokenApp instance;
    private RequestQueue mRequestQueue;
    private Bundle bndl;


    public static PokenApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Realm on top
        Realm.init(this);

        instance = this;
        // use version code to handle database migration.
        int databaseVersion = 0;
        try {
            databaseVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            this.setAppVersionCode(databaseVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // Configure Realm for the application
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(databaseVersion)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default

        int currentDatabaseVersion = SPHelper.getInstance().getSharedPreferences(Constants.SHARED_DATABSE_VERSION, 0);
        if (databaseVersion > currentDatabaseVersion) {
            /*
              When current db version is less than new db version,
              indicate the app should reload data from server.
             */
            SPHelper.getInstance().setPreferences(Constants.SHARED_HAS_UPDATED, true);

            SPHelper.getInstance().setPreferences(Constants.SHARED_DATABSE_VERSION, databaseVersion);
            SPHelper.getInstance().setPreferences(Constants.SHARED_LAST_UPDATE, "");
        }

        Picasso.with(this.getApplicationContext()).areIndicatorsEnabled();
        Picasso.with(this).setLoggingEnabled(true);

        Fabric.with(this, new Crashlytics(), new Answers());
    }

    public <T> void addToRequestQueue(final View snackContainer, String url, Map<String, String> params,
                                      Class<T> clazz, final VolleyResultListener listener, Object tag,
                                      boolean removeChaceFirst) {

        Utils.Log(TAG, "Result JSON Class : " + clazz);

        listener.onStart(null);

        MyRequest sr = new MyRequest<>(url, clazz, params, new Response.Listener<T>() {
            @Override
            public void onResponse(T s) {

                // Server success to response
                if (s instanceof PojoBase) {

                    Utils.Log(TAG, "Response: " + ((PojoBase) s).success + ", Reason msg: " + ((PojoBase) s).msg);

                    PojoBase sBase = (PojoBase) s;

                    Utils.Log(TAG, "Server success status: " + sBase.success);
                    if (sBase.success == 1) {
                        listener.onSuccess(sBase);
                    } else {
                        listener.onError(sBase);
                    }

                    listener.onFinish(sBase);

                    if (!StringUtils.isEmpty(sBase.msg) && snackContainer != null) {
                        // Show message as snackbar (Info Message, or Error message)
                        Utils.snackBar(
                                snackContainer,
                                sBase.msg,
                                (sBase.success == 1
                                        ? Log.INFO
                                        : Log.ERROR
                                ));


                    } else {
                        MyLog.FabricLog(Log.WARN, TAG + " - Snack container is null");
                    }

                } else {
                    MyLog.FabricLog(Log.WARN, TAG + " - Parsing, model should instance of pojo base");
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Utils.Log(TAG, "Response volley error: " + volleyError.getMessage());
                listener.onError(null);
                listener.onFinish(null);

                volleyError.printStackTrace();
                handleErrorResponse(snackContainer, volleyError);
            }
        });

        if (removeChaceFirst) {
            getRequestQueue().getCache().remove(url);
        }

        // Set time out
        sr.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 4,  // Default ... * 2
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        sr.setTag(tag);

        getRequestQueue().add(sr);
    }

    public <T> void addToRequestQueue(
            View snackContainer,
            String url,
            Map<String, String> params,
            Class<T> clazz,
            VolleyResultListener listener,
            Object tag) {

        // (11/11/2016) Fabric logging for requested URL
        MyLog.FabricLog(Log.INFO, String.format("request?url=%s&params=%s", url, params));

        // Add request to queue
        addToRequestQueue(snackContainer, url, params, clazz, listener, tag, true);
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            mRequestQueue.start();
        }
        return mRequestQueue;
    }

    private void handleErrorResponse(View snackContainer, VolleyError volleyError) {
        // Sometimes snackContainer is null
        if (snackContainer == null || volleyError == null) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {

            if (volleyError.networkResponse != null) {
                MyLog.FabricLog(Log.ERROR,
                        TAG + " - Dari error TimeoutError || NoConnectionError", volleyError);
            }

            Utils.snackBar(snackContainer, "Request timeout, please check your internet connection.", Log.ERROR);

        } else if (volleyError instanceof AuthFailureError) {

            MyLog.FabricLog(Log.ERROR, TAG + " - Dari error, dari error auth failure", volleyError);

            Utils.snackBar(snackContainer, "Auth failure, try to restart your internet connection.", Log.ERROR);

        } else if (volleyError instanceof ServerError) {

            MyLog.FabricLog(Log.ERROR, TAG + " - Dari error, Server Error.", volleyError);

            Utils.snackBar(snackContainer, "Server error try again later", Log.ERROR);

        } else if (volleyError instanceof NetworkError) {

            MyLog.FabricLog(Log.ERROR, TAG + " - Dari error, Network Error.", volleyError);

            Utils.snackBar(snackContainer, "Network error please check your internet connection.", Log.ERROR);

        } else if (volleyError instanceof ParseError) {

            MyLog.FabricLog(Log.ERROR, TAG + " - Dari error, Parse Error", volleyError);

            Utils.snackBar(snackContainer, "Parse Error please try again.", Log.ERROR);

        } else {

            if (volleyError.networkResponse != null) {

                MyLog.FabricLog(Log.ERROR, TAG + " - Error code " + volleyError.networkResponse.statusCode);

                switch (volleyError.networkResponse.statusCode) {
                    case 500:
                        sb.append("Bad Response 500.");
                        break;
                    default:
                        sb.append("Network Error.");
                        break;
                }

                Utils.snackBar(snackContainer, "Error " + volleyError.networkResponse.statusCode + " please check your " +
                        "internet connection", Log.ERROR);
            }

            MyLog.FabricLog(Log.ERROR, TAG + " - Handle Error Response else string: " + sb.toString());
        }
    }

    /**
     * Cancel all pending requests
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            Utils.Logs('w', TAG, "CANCEL NETWORK REQUEST. TAG: " + String.valueOf(tag));
            mRequestQueue.cancelAll(tag);
        }
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }
}
