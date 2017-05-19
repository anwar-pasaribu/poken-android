package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Represent shipping vendor location info. Mostly map location. <br />
 * Example json string:
 * <pre>
 * {
 *      "location_id": "1",
 *      "vendor_id": "17",
 *      "user_id": "405",
 *      "name": "Tiki Hayam Wuruk",
 *      "address": "Jl. Hayam Wuruk Raya No. 100 C Jakarta Pusat",
 *      "phone": "02162302643",
 *      "zip_code": "10120",
 *      "area_id": "187",
 *      "from_tariff_code": "CGK01.00",
 *      "lat": "-6.153748",
 *      "lon": "106.817926",
 *      "created_on": "2016-05-29 22:41:05",
 *      "updated_on": "2016-11-20 21:13:55"
 * }
 * </pre>
 */
@SuppressWarnings("unused")
public class PojoCourierLocation extends RealmObject {

    @PrimaryKey
    @SerializedName("location_id") private long location_id;
    @SerializedName("vendor_id") private long vendor_id;
    @SerializedName("user_id") private long userId;
    @SerializedName("name") private String name;
    @SerializedName("address") private String address;
    @SerializedName("zip_code") private String zip_code;
    @SerializedName("area_id") private String area_id;
    @SerializedName("from_tariff_code") private String fromTariffCode;
    @SerializedName("lat") private String lat;
    @SerializedName("lon") private String lon;

    // Phone number
    private String phone = "00";

    public PojoCourierLocation() {
    }

    public long getLocation_id() {
        return location_id;
    }

    public void setLocation_id(long location_id) {
        this.location_id = location_id;
    }

    public long getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(long vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFromTariffCode() {
        return fromTariffCode;
    }

    public void setFromTariffCode(String fromTariffCode) {
        this.fromTariffCode = fromTariffCode;
    }
}
