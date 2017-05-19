package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

import io.realm.annotations.Ignore;

/**
 * @author Anwar Pasaribu
 * @since Oct 20 2016
 */
public class PojoArea extends PojoBase {
    @SerializedName("daerah")       public String daerah;
    @SerializedName("tariff_code")  public String tariffCode;

    /**
     * Indicate this item is expanded or no
     * on "More" item text case.
     */
    @Ignore private boolean isExpanded;

    public PojoArea() {
    }

    public PojoArea(String daerah, String tariffCode, boolean isExpanded) {
        this.daerah = daerah;
        this.tariffCode = tariffCode;
        this.isExpanded = isExpanded;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
