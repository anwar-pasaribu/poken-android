package id.unware.poken.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marzellaalfamega on 7/29/15.<br/>
 * <p>
 * Handle server response when create new booking successfully.
 * This object is part of <code>PojoBooking</code> object.
 *
 * @see PojoBooking
 * @since Sep 12, 2016 - Add <code>booking_id</code> for list refresh in Package page.
 */
public class PojoNewPackage implements Parcelable {

    @SerializedName("created_by_user_id") private String createdByUserId;  // NEW [V49]

    @SerializedName("booking_id") private String bookingId;
    @SerializedName("booking_code") private String bookingCode;

    // Full data format
    @SerializedName("from_full") private String fromFull;
    @SerializedName("to_full") private String toFull;

    @SerializedName("from_name") private String fromName;
    @SerializedName("from_phone") private String fromPhone;
    @SerializedName("from_address") private String fromAddress;
    @SerializedName("from_zip_code") private String fromZipCode;

    @SerializedName("to_name") private String toName;
    @SerializedName("to_phone") private String toPhone;
    @SerializedName("to_address") private String toAddress;
    @SerializedName("to_zip_code") private String toZipCode;
    @SerializedName("to_area_id") private String toAreaId;
    @SerializedName("to_email") private String toEmail;

    @SerializedName("note") private String note;
    @SerializedName("content") private String content;  // NEW [V49]
    @SerializedName("message") private String message;

    @SerializedName("booking_date") private String bookingDate;  // NEW [V49]
    @SerializedName("expiry_date") private String expiryDate;  // NEW [V49]
    @SerializedName("created_on") private String createdOn;  // NEW [V49]
    @SerializedName("updated_on") private String updatedOn;  // NEW [V49]

    @SerializedName("insured_value") private String insuredValue;  // NEW [V49]
    @SerializedName("service") private String service;  // NEW [V49]

    @SerializedName("reference_code") private String referenceCode;

    @SerializedName("valid_zip") private PojoValidZip validZip;


    public PojoNewPackage() {
    }

    public boolean getValidZipIsSuccess() {
        return validZip.getSuccess() == 1;
    }

    public String getValidZipMsg() {
        return validZip.getMsg() != null ? validZip.getMsg() : "";
    }

    public String getValidZipAreaList() {
        return validZip.getAreaList() != null ? validZip.getAreaList() : "";
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getFromFull() {
        return fromFull;
    }

    public void setFromFull(String fromFull) {
        this.fromFull = fromFull;
    }

    public String getToFull() {
        return toFull;
    }

    public void setToFull(String toFull) {
        this.toFull = toFull;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromPhone() {
        return fromPhone;
    }

    public void setFromPhone(String fromPhone) {
        this.fromPhone = fromPhone;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getFromZipCode() {
        return fromZipCode;
    }

    public void setFromZipCode(String fromZipCode) {
        this.fromZipCode = fromZipCode;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToPhone() {
        return toPhone;
    }

    public void setToPhone(String toPhone) {
        this.toPhone = toPhone;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getToZipCode() {
        return toZipCode;
    }

    public void setToZipCode(String toZipCode) {
        this.toZipCode = toZipCode;
    }

    public String getToAreaId() {
        return toAreaId;
    }

    public void setToAreaId(String toAreaId) {
        this.toAreaId = toAreaId;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getInsuredValue() {
        return insuredValue;
    }

    public void setInsuredValue(String insuredValue) {
        this.insuredValue = insuredValue;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }

    public PojoValidZip getValidZip() {
        return validZip;
    }

    public void setValidZip(PojoValidZip validZip) {
        this.validZip = validZip;
    }

    protected PojoNewPackage(Parcel in) {
        createdByUserId = in.readString();
        bookingId = in.readString();
        bookingCode = in.readString();
        fromName = in.readString();
        fromPhone = in.readString();
        fromAddress = in.readString();
        fromZipCode = in.readString();
        toName = in.readString();
        toPhone = in.readString();
        toAddress = in.readString();
        toZipCode = in.readString();
        toAreaId = in.readString();
        toEmail = in.readString();
        note = in.readString();
        content = in.readString();
        message = in.readString();
        bookingDate = in.readString();
        expiryDate = in.readString();
        createdOn = in.readString();
        updatedOn = in.readString();
        insuredValue = in.readString();
        service = in.readString();
        referenceCode = in.readString();
        validZip = in.readParcelable(PojoValidZip.class.getClassLoader());
    }

    public static final Creator<PojoNewPackage> CREATOR = new Creator<PojoNewPackage>() {
        @Override
        public PojoNewPackage createFromParcel(Parcel in) {
            return new PojoNewPackage(in);
        }

        @Override
        public PojoNewPackage[] newArray(int size) {
            return new PojoNewPackage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdByUserId);
        dest.writeString(bookingId);
        dest.writeString(bookingCode);
        dest.writeString(fromName);
        dest.writeString(fromPhone);
        dest.writeString(fromAddress);
        dest.writeString(fromZipCode);
        dest.writeString(toName);
        dest.writeString(toPhone);
        dest.writeString(toAddress);
        dest.writeString(toZipCode);
        dest.writeString(toAreaId);
        dest.writeString(toEmail);
        dest.writeString(note);
        dest.writeString(content);
        dest.writeString(message);
        dest.writeString(bookingDate);
        dest.writeString(expiryDate);
        dest.writeString(createdOn);
        dest.writeString(updatedOn);
        dest.writeString(insuredValue);
        dest.writeString(service);
        dest.writeString(referenceCode);
        dest.writeParcelable(validZip, flags);
    }

    @Override
    public String toString() {
        return String.format("%s to %s", String.valueOf(getFromFull()), String.valueOf(getToFull()));
    }
}

/*
// NEW Package
{
  "detail": {
    "booking_id": "53484",
    "created_by_user_id": "450",
    "from_full": "DIka Maheswara 5123145123 Rawa Kepa Raya no 41, 11223",
    "from_email": "alfa.marzella@gmail.com",
    "from_name": "DIka Maheswara",
    "from_phone": "5123145123",
    "from_address": "Rawa Kepa Raya no 41, 11223",
    "from_zip_code": "11223",
    "from_country_code": "ID",
    "to_full": "Doni 265464213564 Rawa Belong no 21, 11212",
    "to_email": null,
    "to_name": "Doni",
    "to_phone": "265464213564",
    "to_address": "Rawa Belong no 21, 11212",
    "to_zip_code": "11212",
    "to_country_code": "ID",
    "note": "",
    "content": "",
    "message": "",
    "insured_value": "0.00",
    "service": "REG",
    "max_tariff": "0.00",
    "booking_code": "UAZRB",
    "reference_code": "ID190217UAZRB",
    "booking_date": "2017-02-19",
    "expiry_date": "2017-03-21",
    "status": "0",
    "created_from": "PaketApp",
    "created_on": "2017-02-19 14:46:06",
    "updated_on": "2017-02-19 14:46:06",
    "valid_zip": {
      "success": 1,
      "msg": "Zipcode not found"
    }
  },
  "success": 1
}
 */