package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Jul 29 2017
 */

public class Customer implements Parcelable {

    @Expose
    @SerializedName("id")
    public long id;

    @Expose
    @SerializedName("related_user")
    public User related_user;

    @Expose
    @SerializedName("phone_number")
    public String phone_number;

    @Expose
    @SerializedName("location")
    public Location location;

    public Customer() {
    }

    protected Customer(Parcel in) {
        id = in.readLong();
        related_user = in.readParcelable(User.class.getClassLoader());
        phone_number = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeParcelable(related_user, i);
        parcel.writeString(phone_number);
        parcel.writeParcelable(location, i);
    }
}

/*
{
    "id": 3,
    "related_user": {
        "username": "porkot",
        "first_name": "Porkot",
        "last_name": "Sarumpaet",
        "email": "porkot@icloud.com"
    },
    "phone_number": "01234",
    "location": {
        "address": "Jl. Lapangan Bola No. 3, Gg. Mawar No. 43, Kebon Jeruk, Jakarta Barat 11530",
        "city": "Jakarta Barat",
        "district": "Kebon Jeruk",
        "zip": "11530",
        "state": "Indonesia"
    }
}
 */
