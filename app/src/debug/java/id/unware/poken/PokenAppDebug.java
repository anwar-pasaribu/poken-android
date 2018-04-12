package id.unware.poken;

import com.facebook.stetho.Stetho;

public class PokenAppDebug extends PokenApp {

    @Override
    public void onCreate() {
        super.onCreate();

        // S : Stetho for app debugging on Google Chrome
        Stetho.initializeWithDefaults(this);
        // E : Stetho for app debugging on Google Chrome

    }
}