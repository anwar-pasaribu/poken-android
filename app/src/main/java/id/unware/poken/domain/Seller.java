package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */
public class Seller implements Parcelable {

    public long id;
    public String store_avatar;
    public String store_name;
    public String tag_line;
    public String phone_number;
    public Location location;

    // Wether customer subscribe to Seller
    public boolean is_subscribed;

    protected Seller(Parcel in) {
        id = in.readLong();
        store_avatar = in.readString();
        store_name = in.readString();
        tag_line = in.readString();
        phone_number = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        is_subscribed = in.readByte() != 0;
    }

    public static final Creator<Seller> CREATOR = new Creator<Seller>() {
        @Override
        public Seller createFromParcel(Parcel in) {
            return new Seller(in);
        }

        @Override
        public Seller[] newArray(int size) {
            return new Seller[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(store_avatar);
        parcel.writeString(store_name);
        parcel.writeString(tag_line);
        parcel.writeString(phone_number);
        parcel.writeParcelable(location, i);
        parcel.writeByte((byte) (is_subscribed ? 1 : 0));
    }
}
