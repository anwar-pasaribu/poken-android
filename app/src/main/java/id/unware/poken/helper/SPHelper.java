package id.unware.poken.helper;

import android.content.Context;
import android.content.SharedPreferences;

import id.unware.poken.BuildConfig;
import id.unware.poken.PokenApp;

public class SPHelper {

    private final String preferenceName = BuildConfig.APPLICATION_ID + ".pref";
    private static SPHelper instance;
    private static Context context;

    public static SPHelper getInstance() {
        if (instance == null) {
            instance = new SPHelper();
        }
        context = PokenApp.getInstance().getApplicationContext();
        return instance;
    }

    @SuppressWarnings("unused")
    public int getSharedPreferences(String key, int defValue) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return p.getInt(key, defValue);
    }

    public long getSharedPreferences(String key, long defValue) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return p.getLong(key, defValue);
    }

    public String getSharedPreferences(String key, String defValue) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return p.getString(key, defValue);
    }

    @SuppressWarnings("unused")
    public boolean getSharedPreferences(String key, boolean defValue) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        return p.getBoolean(key, defValue);
    }

    @SuppressWarnings("unused")
    public void setPreferences(String key, boolean value) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putBoolean(key, value);
        e.apply();
    }

    @SuppressWarnings("unused")
    public void setPreferences(String key, CharSequence value) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putString(key, value.toString());
        e.apply();
    }

    @SuppressWarnings("unused")
    public void setPreferences(String key, int value) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putInt(key, value);
        e.apply();
    }

    public void setPreferences(String key, long value) {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit();
        e.putLong(key, value);
        e.apply();
    }

    /**
     * [IMPORTANT] Removing all Shared Preference data.
     */
    @SuppressWarnings("unused")
    public void clearData() {
        SharedPreferences p = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = p.edit().clear();
        e.apply();
    }
}
