package id.unware.poken.tools.glide;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import id.unware.poken.R;

import static android.content.Context.ACTIVITY_SERVICE;

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
        DecodeFormat decodeFormat = com.bumptech.glide.load.DecodeFormat.PREFER_RGB_565;
        // Prefer higher quality images unless we're on a low RAM device
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && activityManager != null) {
            decodeFormat = com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888;
            // defaultOptions.format(activityManager.isLowRamDevice() ? PREFER_RGB_565 : PREFER_ARGB_8888);
        }

        final RequestOptions defaultOptions = new RequestOptions()
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_broken_image_black_24dp)
                .format(decodeFormat)
                .disallowHardwareConfig();

        builder.setLogLevel(2); // Log.VERBOSE
        builder.setDefaultRequestOptions(defaultOptions);
    }
}
