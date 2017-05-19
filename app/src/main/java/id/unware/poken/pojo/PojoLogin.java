package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marzellaalfamega on 6/23/15.<br />
 * Represent /apis/mobile/get_data/ response structure.
 *
 * @since   Nov 28 2016 - add {@link PojoArea} based on new {@code get_data} response<br />
 *          Nov 03 2016 - NEW! {@link PojoCourier}
 */
public class PojoLogin extends PojoBase {
    public PojoUser user;
    public int android_version;
    public PojoBooking[] booking;
    public PojoHistory[][] booking_history;
    public String updated_on;
    public PojoCourierLocation[] location;
    public PojoPickupHistory[] pickup;

    /**
     * Contains array of Courier information.
     */
    public PojoCourier[] vendors;

    /** Indonesia location from raja ongkir.
     * 1 : Load data from server <br />
     * 0 : Do nothing.
     */
    @SerializedName("area_info") public int areaInfo;
}
