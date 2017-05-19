package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Represent {@code tariffs } API response.
 *
 * @since 27 Nov 2016 - NEW
 * @author Anwar Pasaribu
 */
public class PojoVendorTariff {

    @SerializedName("vendor_id") private String vendorId;
    @SerializedName("vendor_code") private String vendorCode;
    @SerializedName("vendor_logo_url") private String vendorLogoUrl;
    @SerializedName("tariff") private PojoTarif[] tariffs;


    public PojoVendorTariff() {
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getVendorLogoUrl() {
        return vendorLogoUrl;
    }

    public void setVendorLogoUrl(String vendorLogoUrl) {
        this.vendorLogoUrl = vendorLogoUrl;
    }

    public PojoTarif[] getTariffs() {
        return tariffs;
    }

    public void setTariffs(PojoTarif[] tariffs) {
        this.tariffs = tariffs;
    }
}
