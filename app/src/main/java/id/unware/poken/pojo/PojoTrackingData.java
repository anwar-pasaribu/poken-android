package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Dec 27 2016
 */

public class PojoTrackingData extends PojoBase {

    @SerializedName("tracked") private PojoTracking[] trackings;

    public static String KEY_ROOT_NAME = "tracked";

    public PojoTrackingData() {
    }

    public PojoTracking[] getTrackings() {
        return trackings;
    }

    public void setTrackings(PojoTracking[] trackings) {
        this.trackings = trackings;
    }
}
