package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Represent data from {@code mobile/apis/zip/PARAMS}
 * @author Anwar Pasaribu
 * @since Nov 21 2016
 */

public class PojoPostcodeData extends PojoBase {
    @SerializedName("area_info") private PojoPostcode[] areaInfos;

    public PojoPostcodeData(PojoPostcode[] areaInfos) {
        this.areaInfos = areaInfos;
    }

    public PojoPostcode[] getAreaInfos() {
        return areaInfos;
    }

    public void setAreaInfos(PojoPostcode[] areaInfos) {
        this.areaInfos = areaInfos;
    }
}

/*
SAMPLE DATA:

{
  "area_info": [
    {
      "subdistrict": "PANGARONGAN",
      "district": "MARANCAR",
      "area": "TAPANULI SELATAN",
      "province": "SUMATERA UTARA",
      "zip_code": "22738"
    }
  ],
  "success": 1
}
*/
