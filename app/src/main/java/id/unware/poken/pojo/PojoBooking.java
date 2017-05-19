package id.unware.poken.pojo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.google.gson.annotations.SerializedName;

import id.unware.poken.R;
import id.unware.poken.tools.StringUtils;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Package / Booking class.
 *
 * @author Alfa
 * @since (Sep 20, 2016) Version 41 - Add enum for status (<code>BookingStatus</code>)
 * @see BookingStatus
 */
@SuppressWarnings("unused")
public class PojoBooking extends RealmObject implements Comparable<PojoBooking> {

    @PrimaryKey
    private long booking_id;

    private String from_email;

    @Index
    private String from_name;
    private String from_phone,
            from_address,
            from_area_id,
            from_state,
            from_zip_code,
            from_country,
            from_country_code;

    private String to_email;

    @Index
    private String to_name;  // To disctinct
    private String to_phone,
            to_address,
            to_area_id,
            to_state,
            to_zip_code,
            to_country,
            to_country_code;

    @SerializedName("content") private String content;

    /**
     * [V49]
     * Selected service: REG - EXP - ECO
     */
    @SerializedName("service") private String paketIdService;

    /**
     * [V49]
     * Shipping insurance value. Money.
     */
    @SerializedName("insured_value") private long insuredValue;

    private String note,
            message,
            booking_code,
            reference_code,
            booking_date,
            expiry_date,
            update_date,
            status;

    // Add text for status
    @Index
    private String booking_status_text;  // Distinct to get available status text

    private String creator_user_id;
    private String currency_unit_code;
    private String currency_unit_name;
    private String currency_unit_symbol;
    private String duration_max_code;
    private String duration_max_name;
    private String duration_min_name;
    private String duration_min_code;
    private String email_receiver;
    private String email_sender;
    private String tariff;
    private String value;
    private String vendor_name;
    private String service_code;
    private String service_name;
    private String volume;
    private String volume_unit_code;
    private String volume_unit_name;
    private String volumetric_weight;
    private String eta;
    private String weight;
    private String weight_unit_code;
    private String weight_unit_name;
    private String vendor_airway_bill;

    /** Frequent used fields on app */
    public static final String KEY_BOOKING_ID = "booking_id";
    public static final String KEY_BOOKING_DATE = "booking_date";
    public static final String KEY_STATUS = "status";  // String status number
    public static final String KEY_BOOKING_STATUS_TEXT = "booking_status_text";

    /** Constant value like Booked: "0", Sent: "1", and so forth.*/
    // NOW on enum BookingStatus.java

    public PojoBooking() {}

    /**
     * Return associated color for certain status number.
     *
     * @param mContext : Context to get Color resource.
     * @return Integer - Color identifier number.
     */
    public int getStatusTextColor(Context mContext) {

        int intColor;

        switch (this.getStatus()) {
            case "0":  // Booked
                intColor = ContextCompat.getColor(mContext, R.color.package_status_booked);
                break;
            case "1":  // Sent
                intColor = ContextCompat.getColor(mContext, R.color.package_status_sent);
                break;
            case "2":  // Delivered
                intColor = ContextCompat.getColor(mContext, R.color.package_status_delivered);
                break;
            case "3": // Picked
                intColor = ContextCompat.getColor(mContext, R.color.package_status_picked);
                break;
            default: // Expired
                intColor = ContextCompat.getColor(mContext, R.color.package_status_deleted);

        }

        return intColor;
    }

    public long getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(long booking_id) {
        this.booking_id = booking_id;
    }

    public String getFrom_email() {
        return from_email;
    }

    public void setFrom_email(String from_email) {
        this.from_email = from_email;
    }

    public String getFrom_name() {
        return StringUtils.getAsciiString(from_name);
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getFrom_phone() {
        return from_phone;
    }

    public void setFrom_phone(String from_phone) {
        this.from_phone = from_phone;
    }

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getFrom_area_id() {
        return from_area_id;
    }

    public void setFrom_area_id(String from_area_id) {
        this.from_area_id = from_area_id;
    }

    public String getFrom_state() {
        return from_state;
    }

    public void setFrom_state(String from_state) {
        this.from_state = from_state;
    }

    public String getFrom_zip_code() {
        return from_zip_code;
    }

    public void setFrom_zip_code(String from_zip_code) {
        this.from_zip_code = from_zip_code;
    }

    public String getFrom_country() {
        return from_country;
    }

    public void setFrom_country(String from_country) {
        this.from_country = from_country;
    }

    public String getFrom_country_code() {
        return from_country_code;
    }

    public void setFrom_country_code(String from_country_code) {
        this.from_country_code = from_country_code;
    }

    public String getTo_email() {
        return to_email;
    }

    public void setTo_email(String to_email) {
        this.to_email = to_email;
    }

    public String getTo_name() {

        return StringUtils.getAsciiString(to_name);
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getTo_phone() {
        return to_phone;
    }

    public void setTo_phone(String to_phone) {
        this.to_phone = to_phone;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getTo_area_id() {
        return to_area_id;
    }

    public void setTo_area_id(String to_area_id) {
        this.to_area_id = to_area_id;
    }

    public String getTo_state() {
        return to_state;
    }

    public void setTo_state(String to_state) {
        this.to_state = to_state;
    }

    public String getTo_zip_code() {
        return to_zip_code;
    }

    public void setTo_zip_code(String to_zip_code) {
        this.to_zip_code = to_zip_code;
    }

    public String getTo_country() {
        return to_country;
    }

    public void setTo_country(String to_country) {
        this.to_country = to_country;
    }

    public String getTo_country_code() {
        return to_country_code;
    }

    public void setTo_country_code(String to_country_code) {
        this.to_country_code = to_country_code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBooking_code() {
        return booking_code == null ? "" : booking_code;
    }

    public void setBooking_code(String booking_code) {
        this.booking_code = booking_code;
    }

    public String getReference_code() {
        return reference_code;
    }

    public void setReference_code(String reference_code) {
        this.reference_code = reference_code;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getStatus() {
        return status;
    }

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

    public String getCreator_user_id() {
        return creator_user_id;
    }

    public void setCreator_user_id(String creator_user_id) {
        this.creator_user_id = creator_user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCurrency_unit_code() {
        return currency_unit_code;
    }

    public void setCurrency_unit_code(String currency_unit_code) {
        this.currency_unit_code = currency_unit_code;
    }

    public String getCurrency_unit_name() {
        return currency_unit_name;
    }

    public void setCurrency_unit_name(String currency_unit_name) {
        this.currency_unit_name = currency_unit_name;
    }

    public String getCurrency_unit_symbol() {
        return currency_unit_symbol;
    }

    public void setCurrency_unit_symbol(String currency_unit_symbol) {
        this.currency_unit_symbol = currency_unit_symbol;
    }

    public String getDuration_max_code() {
        return duration_max_code;
    }

    public void setDuration_max_code(String duration_max_code) {
        this.duration_max_code = duration_max_code;
    }

    public String getDuration_max_name() {
        return duration_max_name;
    }

    public void setDuration_max_name(String duration_max_name) {
        this.duration_max_name = duration_max_name;
    }

    public String getDuration_min_name() {
        return duration_min_name;
    }

    public void setDuration_min_name(String duration_min_name) {
        this.duration_min_name = duration_min_name;
    }

    public String getDuration_min_code() {
        return duration_min_code;
    }

    public void setDuration_min_code(String duration_min_code) {
        this.duration_min_code = duration_min_code;
    }

    public String getEmail_receiver() {
        return email_receiver;
    }

    public void setEmail_receiver(String email_receiver) {
        this.email_receiver = email_receiver;
    }

    public String getEmail_sender() {
        return email_sender;
    }

    public void setEmail_sender(String email_sender) {
        this.email_sender = email_sender;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getVolume_unit_code() {
        return volume_unit_code;
    }

    public void setVolume_unit_code(String volume_unit_code) {
        this.volume_unit_code = volume_unit_code;
    }

    public String getVolume_unit_name() {
        return volume_unit_name;
    }

    public void setVolume_unit_name(String volume_unit_name) {
        this.volume_unit_name = volume_unit_name;
    }

    public String getVolumetric_weight() {
        return volumetric_weight;
    }

    public void setVolumetric_weight(String volumetric_weight) {
        this.volumetric_weight = volumetric_weight;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeight_unit_code() {
        return weight_unit_code;
    }

    public void setWeight_unit_code(String weight_unit_code) {
        this.weight_unit_code = weight_unit_code;
    }

    public String getWeight_unit_name() {
        return weight_unit_name;
    }

    public void setWeight_unit_name(String weight_unit_name) {
        this.weight_unit_name = weight_unit_name;
    }

    public String getVendor_airway_bill() {
        return vendor_airway_bill;
    }

    public void setVendor_airway_bill(String vendor_airway_bill) {
        this.vendor_airway_bill = vendor_airway_bill;
    }

    public String getBooking_status_text() {
        return booking_status_text;
    }

    public void setBooking_status_text(String booking_status_text) {
        this.booking_status_text = booking_status_text;
    }

    public String getPaketIdService() {
        return paketIdService;
    }

    public void setPaketIdService(String paketIdService) {
        this.paketIdService = paketIdService;
    }

    public long getInsuredValue() {
        return insuredValue;
    }

    public void setInsuredValue(long insuredValue) {
        this.insuredValue = insuredValue;
    }

    @Override
    public int compareTo(@NonNull PojoBooking another) {
        return this.to_name.compareTo(another.getTo_name());
    }

}
