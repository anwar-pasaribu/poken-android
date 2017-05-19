package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marzellaalfamega on 2/12/16.
 *
 * @since Dec 1 2016 - Change var to private and add {@link SerializedName}
 */
public class PojoCourierLocationData extends PojoBase {

    @SerializedName("location") private PojoCourierLocation[] location;

    public PojoCourierLocation[] getLocation() {
        return location;
    }

    public void setLocation(PojoCourierLocation[] location) {
        this.location = location;
    }
}
