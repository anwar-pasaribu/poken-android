package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Represent <code>apis/mobile/zip/"keyword"</code>
 *
 * @author Anwar Pasaribu
 * @since Nov 18 2016
 */

public class PojoPostcode extends PojoBase{
    @SerializedName("zip_code") private String zipCode;
    @SerializedName("subdistrict") private String subDistrict;
    @SerializedName("district") private String district;
    @SerializedName("province") private String province;
    @SerializedName("area") private String area;

    public PojoPostcode(String zipCode, String subDistrict, String district, String province, String area) {
        this.zipCode = zipCode;
        this.subDistrict = subDistrict;
        this.district = district;
        this.province = province;
        this.area = area;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict) {
        this.subDistrict = subDistrict;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
