package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public class AddressBook implements Parcelable {

    @Expose
    @SerializedName("id")
    public long id;

    @Expose
    @SerializedName("customer")
    public long customer;

    @Expose
    @SerializedName("location")
    public Location location;

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("address")
    public String address;

    @Expose
    @SerializedName("phone")
    public String phone;

    public AddressBook() {
    }

    protected AddressBook(Parcel in) {
        id = in.readLong();
        customer = in.readLong();
        location = in.readParcelable(Location.class.getClassLoader());
        name = in.readString();
        address = in.readString();
        phone = in.readString();
    }

    public static final Creator<AddressBook> CREATOR = new Creator<AddressBook>() {
        @Override
        public AddressBook createFromParcel(Parcel in) {
            return new AddressBook(in);
        }

        @Override
        public AddressBook[] newArray(int size) {
            return new AddressBook[size];
        }
    };

    @Override
    public String toString() {
        return name
                .concat(" (")
                .concat(String.valueOf(phone))
                .concat(") - ")
                .concat(String.valueOf(address));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(customer);
        parcel.writeParcelable(location, i);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(phone);
    }
}