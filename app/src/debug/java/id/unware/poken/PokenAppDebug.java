package id.unware.poken;

import com.facebook.stetho.Stetho;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.regex.Pattern;

public class PokenAppDebug extends PokenApp {

    @Override
    public void onCreate() {
        super.onCreate();

        // S : Stetho for app debugging on Google Chrome
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                        .build()
        );

        RealmInspectorModulesProvider.builder(this)
                .withFolder(getCacheDir())
                .withMetaTables()
                .withDescendingOrder()
                .withLimit(1600)
                .databaseNamePattern(Pattern.compile(".+\\.realm"))
                .build();
        // E : Stetho for app debugging on Google Chrome

    }
}