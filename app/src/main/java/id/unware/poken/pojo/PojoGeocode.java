package id.unware.poken.pojo;

/**
 * Created by marzellaalfamega on 9/6/16.
 */
public class PojoGeocode extends PojoBase {
    public String status;
    public PojoGeocodeResult[] results;

    public PojoGeocode() {
        success = 1;
    }
}
