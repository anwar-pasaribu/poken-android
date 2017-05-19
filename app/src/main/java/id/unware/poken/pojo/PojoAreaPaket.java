package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Represent {@code area_info} data from {@code get_data} which contains:
 * <pre>
 * {
 * "area_id": "79796",
 * "subdistrict": "",
 * "district": "",
 * "area": "Aceh Barat",
 * "province": "NANGROE ACEH DARUSSALAM",
 * "zip_code": "23681",
 * "updated_on": "2016-11-25 16:49:56"
 * }
 * </pre>
 *
 * @author Anwar Pasaribu
 * @since Nov 28 2016 - Add more field to met new {@code get_data} response. <br />
 * Nov 23 2016 - NEW!
 */
public class PojoAreaPaket extends RealmObject {

    // KEYS
    public static final String KEY_AREA_ID = "areaId";
    public static final String KEY_AREA = "area";

    @PrimaryKey
    @SerializedName("area_id") private String areaId;
    @SerializedName("subdistrict") private String subdistrict;
    @SerializedName("district") private String district;
    @SerializedName("area") private String area;
    @SerializedName("province") private String province;
    @SerializedName("zip_code") private String zipCode;
    @SerializedName("updated_on") private String updatedOn;

    /**
     * Indicate this item is expanded or no
     * on "More" item text case.
     */
    @Ignore private boolean isExpanded;

    /**
     * Indicate this item is a header
     */
    @Ignore private boolean isHeader;

    public PojoAreaPaket() {
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }
}
