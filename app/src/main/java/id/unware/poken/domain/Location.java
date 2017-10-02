package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Jun 05 2017
 */

public class Location implements Parcelable {

    @Expose
    @SerializedName("address")
    public String address;

    @Expose
    @SerializedName("city")
    public String city;

    @Expose
    @SerializedName("subdistrict")
    public String subdistrict;

    @Expose
    @SerializedName("district")
    public String district;

    @Expose
    @SerializedName("province")
    public String province;

    @Expose
    @SerializedName("state")
    public String state;

    @Expose
    @SerializedName("zip")
    public String zip;

    public Location() {
    }

    public Location(String city, String subdistrict, String district, String province, String zip) {
        this.city = city;
        this.subdistrict = subdistrict;
        this.district = district;
        this.province = province;
        this.zip = zip;
    }

    protected Location(Parcel in) {
        address = in.readString();
        city = in.readString();
        subdistrict = in.readString();
        district = in.readString();
        province = in.readString();
        state = in.readString();
        zip = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(address);
        parcel.writeString(city);
        parcel.writeString(subdistrict);
        parcel.writeString(district);
        parcel.writeString(province);
        parcel.writeString(state);
        parcel.writeString(zip);
    }
}
