package id.unware.poken.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;


public class PojoTarif extends RealmObject {

    /** [Dec/05/16] For estimate tariff check after create new package*/
    @SerializedName("vendor_name") private String vendorName;

    @SerializedName("service_name") private String serviceName;
    @SerializedName("service_code") private String serviceCode;
    @SerializedName("tariff") private double tariff;
    @SerializedName("etd") private String estDay;


    // For list view purpose
    @Expose private String vendorId;
    @Expose private String vendorCode;
    @Expose private String vendorLogoUrl;


    public PojoTarif() {
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public double getTariff() {
        return tariff;
    }

    public void setTariff(double tariff) {
        this.tariff = tariff;
    }

    public String getEstDay() {
        return estDay;
    }

    public void setEstDay(String estDay) {
        this.estDay = estDay;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
