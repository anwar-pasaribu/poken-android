package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Dec 28 2016
 */
public class PojoOfficeLocation {

    @SerializedName("branch_code") private String branchCode;
    @SerializedName("branch_lat") private double branchLat;
    @SerializedName("branch_lon") private double branchLon;
    @SerializedName("branch_name") private String branchName;
    @SerializedName("branch_phone") private String branchPhone;
    @SerializedName("branch_open_time") private String branchOpenTime;
    @SerializedName("branch_close_time") private String branchCloseTime;
    @SerializedName("branch_type") private String branchType;
    @SerializedName("branch_address") private String branchAddress;
    @SerializedName("branch_zip_code") private String branchZipCode;
    @SerializedName("book") private int book;
    @SerializedName("distance") private String distance;

    public PojoOfficeLocation() {
    }

    public PojoOfficeLocation(String branchCode, String branchLat, String branchLon, String branchName, String branchPhone, String branchOpenTime, String branchCloseTime, String branchType, String branchAddress, String branchZipCode, int book, String distance) {
        this.branchCode = branchCode;
        this.branchLat = Double.parseDouble(branchLat);
        this.branchLon = Double.parseDouble(branchLon);
        this.branchName = branchName;
        this.branchPhone = branchPhone;
        this.branchOpenTime = branchOpenTime;
        this.branchCloseTime = branchCloseTime;
        this.branchType = branchType;
        this.branchAddress = branchAddress;
        this.branchZipCode = branchZipCode;
        this.book = book;
        this.distance = distance;
    }

    public int getBook() {
        return book;
    }

    public void setBook(int book) {
        this.book = book;
    }

    public String getBranchZipCode() {
        return branchZipCode;
    }

    public void setBranchZipCode(String branchZipCode) {
        this.branchZipCode = branchZipCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public double getBranchLat() {
        return branchLat;
    }

    public void setBranchLat(double branchLat) {
        this.branchLat = branchLat;
    }

    public double getBranchLon() {
        return branchLon;
    }

    public void setBranchLon(double branchLon) {
        this.branchLon = branchLon;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchPhone() {
        return branchPhone;
    }

    public void setBranchPhone(String branchPhone) {
        this.branchPhone = branchPhone;
    }

    public String getBranchOpenTime() {
        return branchOpenTime;
    }

    public void setBranchOpenTime(String branchOpenTime) {
        this.branchOpenTime = branchOpenTime;
    }

    public String getBranchCloseTime() {
        return branchCloseTime;
    }

    public void setBranchCloseTime(String branchCloseTime) {
        this.branchCloseTime = branchCloseTime;
    }

    public String getBranchType() {
        return branchType;
    }

    public void setBranchType(String branchType) {
        this.branchType = branchType;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
