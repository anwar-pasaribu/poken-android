package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * @author Anwar Pasaribu
 * @since Dec 27 2016
 */

public class PojoTrackingStatus extends RealmObject {

    @SerializedName("entry_date") private String entryDate;
    @SerializedName("status") private String status;
    @SerializedName("entry_name") private String entryName;
    @SerializedName("entry_place") private String entryPlace;
    @SerializedName("noted") private String noted;

    public PojoTrackingStatus() {
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getEntryPlace() {
        return entryPlace;
    }

    public void setEntryPlace(String entryPlace) {
        this.entryPlace = entryPlace;
    }

    public String getNoted() {
        return noted;
    }

    public void setNoted(String noted) {
        this.noted = noted;
    }
}
