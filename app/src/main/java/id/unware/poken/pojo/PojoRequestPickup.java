package id.unware.poken.pojo;

/**
 * Parameter structure when request pickup.
 *
 * @author Anwar Pasaribu
 * @since Jan 25 2017
 */
public class PojoRequestPickup {
    private double lat;
    private double lon;
    private String mFormattedAddress;
    private String mZipCode;
    private String mExtraDetail;
    private int mIntVehicleType;
    private String mLocality;
    private String mAdminArea;
    private String mStrVendorId;


    public PojoRequestPickup() {
    }

    public String getLocality() {
        return mLocality;
    }

    public void setLocality(String mLocality) {
        this.mLocality = mLocality;
    }

    public double getLatitude() {
        return lat;
    }

    public void setLatitude(double lat) {
        this.lat = lat;
    }

    public double getLongitude() {
        return lon;
    }

    public void setLongitude(double lon) {
        this.lon = lon;
    }

    public String getFormattedAddress() {
        return mFormattedAddress;
    }

    public void setFormattedAddress(String mFormattedAddress) {
        this.mFormattedAddress = mFormattedAddress;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public void setZipCode(String mZipCode) {
        this.mZipCode = mZipCode;
    }

    public String getExtraDetail() {
        return mExtraDetail;
    }

    public void setExtraDetail(String mExtraDetail) {
        this.mExtraDetail = mExtraDetail;
    }

    public int getIntVehicleType() {
        return mIntVehicleType;
    }

    public void setIntVehicleType(int mIntVehicleType) {
        this.mIntVehicleType = mIntVehicleType;
    }

    public String getAdminArea() {
        return mAdminArea;
    }

    public void setAdminArea(String mAdminArea) {
        this.mAdminArea = mAdminArea;
    }

    public String getStrVendorId() {
        return mStrVendorId;
    }

    public void setStrVendorId(String mStrVendorId) {
        this.mStrVendorId = mStrVendorId;
    }
}
