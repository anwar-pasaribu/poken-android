package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by IT11 on 4/21/2015. <br />
 *
 * Object reflection for json response of {@code mobile/apis/tariffn}.
 * The server response consist of: <br />
 * - Array of {@link PojoVendorTariff}
 *
 * @since Nov 27 2016 - Apply new {@code tariff3} response.
 */
public class PojoTarifData extends PojoBase {
    @SerializedName("vendor_tariff") private PojoVendorTariff[] vendorTariffs;

    public PojoTarifData() {
    }

    public PojoVendorTariff[] getVendorTariffs() {
        return vendorTariffs;
    }

    public void setVendorTariffs(PojoVendorTariff[] vendorTariffs) {
        this.vendorTariffs = vendorTariffs;
    }
}
