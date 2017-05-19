package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * @author Anwar Pasaribu
 * @since Dec 27 2016
 */

public class PojoTracking extends RealmObject {

    @PrimaryKey
    @SerializedName("cnno") private String cnno;
    @SerializedName("seq_no") private String seqNo;
    @SerializedName("sender_reference") private String senderReference;
    @SerializedName("origin_tariff") private String origin_tariff;
    @SerializedName("destination_tariff_code") private String destinationTariffCode;
    @SerializedName("product") private String product;
    @SerializedName("sys_created_on") private String sysCreatedOn;

    @Index
    @SerializedName("consignee_name") private String consigneeName;
    @SerializedName("consignee_address") private String consigneeAddress;
    @SerializedName("consignor_name") private String consignorName;
    @SerializedName("consignor_address") private String consignorAddress;

    /**
     * For Gson serialization purpose only.
     */
    @Ignore
    @SerializedName("history") private PojoTrackingStatus[] statuses;

    // V29 : Specialize message when no history
    @SerializedName("msg") private String msg;

    // V30 : Sometime track3 return error
    @SerializedName("error") private String error;


    /**
     * Tracking status on Realm.
     */
    private RealmList<PojoTrackingStatus> statusList;

    // Frequent used keys
    @Ignore public static String KEY_CNNO = "cnno";
    @Ignore public static String KEY_STATUSES = "status";

    public PojoTracking() {
    }

    public String getCnno() {
        return cnno;
    }

    public void setCnno(String cnno) {
        this.cnno = cnno;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getSenderReference() {
        return senderReference;
    }

    public void setSenderReference(String senderReference) {
        this.senderReference = senderReference;
    }

    public String getOrigin_tariff() {
        return origin_tariff;
    }

    public void setOrigin_tariff(String origin_tariff) {
        this.origin_tariff = origin_tariff;
    }

    public String getDestinationTariffCode() {
        return destinationTariffCode;
    }

    public void setDestinationTariffCode(String destinationTariffCode) {
        this.destinationTariffCode = destinationTariffCode;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSysCreatedOn() {
        return sysCreatedOn;
    }

    public void setSysCreatedOn(String sysCreatedOn) {
        this.sysCreatedOn = sysCreatedOn;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public String getConsignorName() {
        return consignorName;
    }

    public void setConsignorName(String consignorName) {
        this.consignorName = consignorName;
    }

    public String getConsignorAddress() {
        return consignorAddress;
    }

    public void setConsignorAddress(String consignorAddress) {
        this.consignorAddress = consignorAddress;
    }

    public RealmList<PojoTrackingStatus> getStatusList() {
        return statusList;
    }

    public void setStatusList(RealmList<PojoTrackingStatus> statusList) {
        this.statusList = statusList;
    }

    public PojoTrackingStatus[] getStatuses() {
        return statuses;
    }

    public void setStatuses(PojoTrackingStatus[] statuses) {
        this.statuses = statuses;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
/*
    "cnno": "020103975850",
    "seq_no": 0,
    "sender_reference": "",
    "origin_tariff": null,
    "destination_tariff_code": null,
    "product": "REG",
    "sys_created_on": "2016-12-09 21:55:09",
    "consignee_name": "M HADZIG",
    "consignee_address": "JL LINTAS SAROLANGUN - MUARA TEMBESI",
    "consignor_name": "SUMATERA AGRO MANDIRI",
    "consignor_address": "DEUTSCHE BANK BUILDING LT.19 SUITE 1201",
    "statuses": [
*/
