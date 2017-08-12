package id.unware.poken;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.squareup.picasso.Picasso;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;


public class PokenApp extends Application {

    private final String TAG = "AppClass";

    private static PokenApp instance;

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
        int databaseVersion = BuildConfig.DATABASE_VERSION;

        // Configure Realm for the application
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(databaseVersion)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration); // Make this Realm the default

        Picasso.with(this.getApplicationContext()).areIndicatorsEnabled();
        Picasso.with(this).setLoggingEnabled(true);

        // Debug fabric
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics(), new Answers())
                .debuggable(BuildConfig.DEV_MODE)
                .build();

        Fabric.with(fabric);
    }
}
