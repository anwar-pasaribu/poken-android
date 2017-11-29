package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class Featured implements Parcelable {

    @Expose
    @SerializedName("id")
    public long id;

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("image")
    public String image;

    @Expose
    @SerializedName("thumbnail")
    public String thumbnail;

    @Expose
    @SerializedName("expiry_date")
    public Date expiry_date;

    @Expose
    @SerializedName("target_id")
    public int target_id;

    @SerializedName("featured_text")
    public String featured_text;


    public Featured() {
    }

    public enum Type {
        IMAGE,
        VIDEO
    }

    protected Featured(Parcel in) {
        id = in.readLong();
        name = in.readString();
        image = in.readString();
        thumbnail = in.readString();
        target_id = in.readInt();
        featured_text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(thumbnail);
        dest.writeInt(target_id);
        dest.writeString(featured_text);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Featured> CREATOR = new Creator<Featured>() {
        @Override
        public Featured createFromParcel(Parcel in) {
            return new Featured(in);
        }

        @Override
        public Featured[] newArray(int size) {
            return new Featured[size];
        }
    };
}
