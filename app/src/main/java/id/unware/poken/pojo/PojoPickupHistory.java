package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by marzellaalfamega on 2/12/16. <br />
 * Represent <code>/get_data/</code> on key <code>"pickup"</code>.
 *
 * @since Nov 8 2016 - Add <code>vendorBranch</code> and <code>vendorBranchPhone</code>.
 */
public class PojoPickupHistory extends RealmObject {

    @PrimaryKey
    private String pickup_id;

    private String address;

    // [V54] Administrative Lv 3 area
    @SerializedName("administrative_area") private String administrativeArea;

    private String area_id;
    private String created_on;
    private String distance;
    private String driver_id;
    private String driver_name;
    private String driver_phone;
    private String plate_number;
    private String updated_on;
    private String lat;
    private String lon;
    private String location_id;
    private String memo;
    private String user_name;
    private String nearest_vendor_location_id;
    private String package_count;
    private String pickup_time;
    private String process_time;
    private String received_time;
    private String selected_courier_id;
    private String selected_vendor_id;
    private String status;
    private String status_number;
    private String status_text;
    private String tariff;
    private String user_phone;
    private String time_range_from;
    private String time_range_to;
    private String user_id;
    private String vehicle_type;
    private String zip_code;
    private long vendor_id;
    private String image;
    private String extra_detail;

    /**
     * Field to hold list of picked packages, separete by comma.
     * Null when no data.
     */
    private String bookings;

    /**
     * Vendor branch name and phone number.
     */
    @SerializedName("vendor_branch") private String vendorBranch;
    @SerializedName("vendor_branch_phone") private String vendorBranchPhone;

    // Field names
    public static final String KEY_STATUS_NUMBER = "status_number";
    public static final String KEY_PICKUP_ID = "pickup_id";

    // Keys for status number
    public static final int STATUS_OTW = 3;


    public PojoPickupHistory() {}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getNearest_vendor_location_id() {
        return nearest_vendor_location_id;
    }

    public void setNearest_vendor_location_id(String nearest_vendor_location_id) {
        this.nearest_vendor_location_id = nearest_vendor_location_id;
    }

    public String getPickup_id() {
        return pickup_id;
    }

    public void setPickup_id(String pickup_id) {
        this.pickup_id = pickup_id;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getProcess_time() {
        return process_time;
    }

    public void setProcess_time(String process_time) {
        this.process_time = process_time;
    }

    public String getReceived_time() {
        return received_time;
    }

    public void setReceived_time(String received_time) {
        this.received_time = received_time;
    }

    public String getSelected_courier_id() {
        return selected_courier_id;
    }

    public void setSelected_courier_id(String selected_courier_id) {
        this.selected_courier_id = selected_courier_id;
    }

    public String getSelected_vendor_id() {
        return selected_vendor_id;
    }

    public void setSelected_vendor_id(String selected_vendor_id) {
        this.selected_vendor_id = selected_vendor_id;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Get integer version from status.
     *
     * @return Status in integer.
     */
    public int getIntStatus() {
        try {
            return Integer.parseInt(status);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return 99;
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public String getTime_range_from() {
        return time_range_from;
    }

    public void setTime_range_from(String time_range_from) {
        this.time_range_from = time_range_from;
    }

    public String getTime_range_to() {
        return time_range_to;
    }

    public void setTime_range_to(String time_range_to) {
        this.time_range_to = time_range_to;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getZip_code() {
        return zip_code;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public String getDriver_phone() {
        return driver_phone;
    }

    public void setDriver_phone(String driver_phone) {
        this.driver_phone = driver_phone;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getStatus_number() {
        return status_number;
    }

    public void setStatus_number(String status_number) {
        this.status_number = status_number;
    }

    public long getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(long vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getPackage_count() {
        return package_count;
    }

    public void setPackage_count(String package_count) {
        this.package_count = package_count;
    }

    public String getExtra_detail() {
        return extra_detail;
    }

    public void setExtra_detail(String extra_detail) {
        this.extra_detail = extra_detail;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getBookings() {
        return bookings;
    }

    public void setBookings(String bookings) {
        this.bookings = bookings;
    }

    public String getVendorBranch() {
        return vendorBranch;
    }

    public void setVendorBranch(String vendorBranch) {
        this.vendorBranch = vendorBranch;
    }

    public String getVendorBranchPhone() {
        return vendorBranchPhone;
    }

    public void setVendorBranchPhone(String vendorBranchPhone) {
        this.vendorBranchPhone = vendorBranchPhone;
    }

    public String getAdministrativeArea() {
        return administrativeArea;
    }

    public void setAdministrativeArea(String administrativeArea) {
        this.administrativeArea = administrativeArea;
    }
}
