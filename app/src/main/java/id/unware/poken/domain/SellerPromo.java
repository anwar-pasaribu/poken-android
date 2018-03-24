package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Anwar Pasaribu on 2/21/2018.
 */

public class SellerPromo implements Parcelable {

    @Expose
    @SerializedName("seller")
    public Seller seller;

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

    protected SellerPromo(Parcel in) {
        seller = in.readParcelable(Seller.class.getClassLoader());
        id = in.readLong();
        name = in.readString();
        image = in.readString();
        thumbnail = in.readString();
        target_id = in.readInt();
        featured_text = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(seller, flags);
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

    public static final Creator<SellerPromo> CREATOR = new Creator<SellerPromo>() {
        @Override
        public SellerPromo createFromParcel(Parcel in) {
            return new SellerPromo(in);
        }

        @Override
        public SellerPromo[] newArray(int size) {
            return new SellerPromo[size];
        }
    };
}
