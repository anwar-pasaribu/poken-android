package id.unware.poken.ui.pickup.model;

import android.content.Context;

/**
 * Hide context from PickupPresenter while loading Geocoding data.
 *
 * @author Anwar Pasaribu
 * @since Jan 25 2017
 */

public class GeocodingModel {

    private Context mContext;

    public GeocodingModel(Context mContext) {
        this.mContext = mContext;
    }

    public Context getContext() {
        return this.mContext;
    }
}
