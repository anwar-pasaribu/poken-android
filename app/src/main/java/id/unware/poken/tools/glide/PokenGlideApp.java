package id.unware.poken.tools.glide;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;
import static com.bumptech.glide.load.DecodeFormat.PREFER_RGB_565;

/**
 * Glide application. Will recognize as GlideApp.with(Context) instead of Glide.with(Context)
 *
 * @author Anwar Pasaribu
 * @since Aug 17 2017
 */
@GlideModule
public class PokenGlideApp extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        final RequestOptions defaultOptions = new RequestOptions();
        // Prefer higher quality images unless we're on a low RAM device
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            defaultOptions.format(activityManager.isLowRamDevice() ? PREFER_RGB_565 : PREFER_ARGB_8888);
        }
        // Disable hardware bitmaps as they don't play nicely with Palette
        defaultOptions.disallowHardwareConfig();
        builder.setLogLevel(2); // Log.VERBOSE
        builder.setDefaultRequestOptions(defaultOptions);
    }
}
