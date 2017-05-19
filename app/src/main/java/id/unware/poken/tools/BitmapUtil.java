package id.unware.poken.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import id.unware.poken.R;


/**
 * Manage all drawable related function.
 */
public class BitmapUtil {
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // We ask for the bounds if they have been set as they would be most
        // correct, then we check we are  > 0
        final int width = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ?
                drawable.getBounds().height() : drawable.getIntrinsicHeight();

        // Now we check we are > 0
        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width, height <= 0 ? 1 : height,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Generate filter targeted drawable.
     * @param context : Context where filter will be used.
     * @param colorRes : Color to overlay the drawable.
     * @return PorterDuffColorFilter filter.
     */
    public static PorterDuffColorFilter getDrawableFilter(Context context, int colorRes) {

        return new PorterDuffColorFilter(
                ContextCompat.getColor(context, colorRes),
                PorterDuff.Mode.SRC_ATOP);

    }

    public static PorterDuffColorFilter getDisabledColor(Context context) {
        return new PorterDuffColorFilter(
                ContextCompat.getColor(context, R.color.style_overlay_grey),
                PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Return grey color overlay
     * @param context : Context to implement filter.
     * @return
     */
    public static PorterDuffColorFilter getEnabledColor(Context context) {
        return new PorterDuffColorFilter(
                ContextCompat.getColor(context, R.color.colorAccent),
                PorterDuff.Mode.SRC_ATOP);
    }
}