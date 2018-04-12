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

    public String owner_name;

    // Wether customer subscribe to Seller
    public boolean is_subscribed;

    public Seller() {
    }

    protected Seller(Parcel in) {
        id = in.readLong();
        store_avatar = in.readString();
        store_name = in.readString();
        tag_line = in.readString();
        phone_number = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        owner_name = in.readString();
        is_subscribed = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(store_avatar);
        dest.writeString(store_name);
        dest.writeString(tag_line);
        dest.writeString(phone_number);
        dest.writeParcelable(location, flags);
        dest.writeString(owner_name);
        dest.writeByte((byte) (is_subscribed ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
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

    @Override public String toString() {
        return "Seller{" +
                "id=" + id +
                ", store_avatar='" + store_avatar + '\'' +
                ", store_name='" + store_name + '\'' +
                ", tag_line='" + tag_line + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", location=" + location +
                ", is_subscribed=" + is_subscribed +
                '}';
    }
}
