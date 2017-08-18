package id.unware.poken.tools.glide;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Glide application. Will recognize as GlideApp.with(Context) instead of Glide.with(Context)
 *
 * @author Anwar Pasaribu
 * @since Aug 17 2017
 */
@GlideModule
public final class MyGlideApp extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setLogLevel(Log.VERBOSE);
        super.applyOptions(context, builder);
    }
}
