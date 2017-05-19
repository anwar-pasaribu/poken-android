package id.unware.poken.helper;

import android.content.Context;
import android.content.SharedPreferences;

import id.unware.poken.PokenApp;

/**
 * Created by marzellaalfamega on 6/23/15.
 */
public class SPHelper {
    private static SPHelper instance;
    private static Context context;

    public static SPHelper getInstance() {
        if (instance == null) {
            instance = new SPHelper();
        }
        context = PokenApp.getInstance().getApplicationContext();
        return instance;
    }

    //
    private final String preferenceName = "myJNE_app_preference";

    public int getSharedPreferences(String key, int defValue) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return p.getInt(key, defValue);
    }

    public String getSharedPreferences(String key, String defValue) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return p.getString(key, defValue);
    }

    public boolean getSharedPreferences(String key, boolean defValue) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return p.getBoolean(key, defValue);
    }

    public void setPreferences(String key, boolean value) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean(key, value);
        e.commit();
    }

    public void setPreferences(String key, CharSequence value) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putString(key, value.toString());
        e.commit();
    }

    public void setPreferences(String key, int value) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putInt(key, value);
        e.commit();
    }

    /**
     * [IMPORTANT] Removing all Shared Preference data.
     */
    public void clearData() {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit().clear();
        e.commit();
    }
}
