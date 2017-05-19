package id.unware.poken.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Feb 19 2017
 */

public class PojoValidZip implements Parcelable {

    @SerializedName("success") private int success;
    @SerializedName("msg") private String msg;
    @SerializedName("area_list") private String areaList;


    public PojoValidZip() {
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAreaList() {
        return areaList;
    }

    public void setAreaList(String areaList) {
        this.areaList = areaList;
    }

    protected PojoValidZip(Parcel in) {
        success = in.readInt();
        msg = in.readString();
        areaList = in.readString();
    }

    public static final Creator<PojoValidZip> CREATOR = new Creator<PojoValidZip>() {
        @Override
        public PojoValidZip createFromParcel(Parcel in) {
            return new PojoValidZip(in);
        }

        @Override
        public PojoValidZip[] newArray(int size) {
            return new PojoValidZip[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(success);
        dest.writeString(msg);
        dest.writeString(areaList);
    }
}