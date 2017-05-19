package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Mar 22 2017
 */

public class PojoOfficeLocationData extends PojoBase {
    @SerializedName("location") private PojoOfficeLocation[] officeLocations;

    public PojoOfficeLocationData() {
    }

    public PojoOfficeLocation[] getOfficeLocations() {
        return officeLocations;
    }

    public void setOfficeLocations(PojoOfficeLocation[] officeLocations) {
        this.officeLocations = officeLocations;
    }
}
