package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Java object represent <code>.../get_data/</code> key <code>"vendors"</code>.
 *
 * @author Anwar Pasaribu
 * @since Nov 03 2016 - NEW!
 */

public class PojoCourier extends RealmObject {

    @PrimaryKey
    @SerializedName("vendor_id") private long vendorId;

    @SerializedName("ShipmentFee") private String shipmentFee;
    @SerializedName("VendorCode") private String vendorCode;
    @SerializedName("VendorName") private String vendorName;
    @SerializedName("VendorPickup") private String vendorPickup;
    @SerializedName("VendorPickupRadius") private String vendorPickupRadius;
    @SerializedName("VendorPickupTerms") private String vendorPickupTerms;
    @SerializedName("VendorPriceURL") private String vendorPriceUrl;
    @SerializedName("VendorStatusDetailURL") private String vendorStatusDetailUrl;
    @SerializedName("VendorSupportEmail") private String vendorSupportEmail;
    @SerializedName("VendorSupportPhone") private String vendorSupportPhone;
    @SerializedName("VendorWebsite") private String vendorWebsite;
    @SerializedName("VendorLogoURL") private String vendorLogoUrl;

    public static final String KEY_COURIER_ID = "vendorId";

    public PojoCourier() {
    }

    public long getVendorId() {
        return vendorId;
    }

    public String getShipmentFee() {
        return shipmentFee;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getVendorPickup() {
        return vendorPickup;
    }

    public String getVendorPickupRadius() {
        return vendorPickupRadius;
    }

    public String getVendorPickupTerms() {
        return vendorPickupTerms;
    }

    public String getVendorPriceUrl() {
        return vendorPriceUrl;
    }

    public String getVendorStatusDetailUrl() {
        return vendorStatusDetailUrl;
    }

    public String getVendorSupportEmail() {
        return vendorSupportEmail;
    }

    public String getVendorSupportPhone() {
        return vendorSupportPhone;
    }

    public String getVendorWebsite() {
        return vendorWebsite;
    }

    public void setVendorId(long vendorId) {
        this.vendorId = vendorId;
    }

    public void setShipmentFee(String shipmentFee) {
        this.shipmentFee = shipmentFee;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public void setVendorPickup(String vendorPickup) {
        this.vendorPickup = vendorPickup;
    }

    public void setVendorPickupRadius(String vendorPickupRadius) {
        this.vendorPickupRadius = vendorPickupRadius;
    }

    public void setVendorPickupTerms(String vendorPickupTerms) {
        this.vendorPickupTerms = vendorPickupTerms;
    }

    public void setVendorPriceUrl(String vendorPriceUrl) {
        this.vendorPriceUrl = vendorPriceUrl;
    }

    public void setVendorStatusDetailUrl(String vendorStatusDetailUrl) {
        this.vendorStatusDetailUrl = vendorStatusDetailUrl;
    }

    public void setVendorSupportEmail(String vendorSupportEmail) {
        this.vendorSupportEmail = vendorSupportEmail;
    }

    public void setVendorSupportPhone(String vendorSupportPhone) {
        this.vendorSupportPhone = vendorSupportPhone;
    }

    public void setVendorWebsite(String vendorWebsite) {
        this.vendorWebsite = vendorWebsite;
    }

    public String getVendorLogoUrl() {
        return vendorLogoUrl;
    }

    public void setVendorLogoUrl(String vendorLogoUrl) {
        this.vendorLogoUrl = vendorLogoUrl;
    }
}
