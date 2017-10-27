package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import id.unware.poken.tools.StringUtils;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public class User implements Parcelable {

    @Expose
    @SerializedName("username")
    public String username;

    @Expose
    @SerializedName("first_name")
    public String first_name;

    @Expose
    @SerializedName("last_name")
    public String last_name;

    @Expose
    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("token")
    public String token;

    public User() {
    }

    protected User(Parcel in) {
        username = in.readString();
        first_name = in.readString();
        last_name = in.readString();
        email = in.readString();
        password = in.readString();
        token = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getFullName() {
        if (!StringUtils.isEmpty(first_name) && !StringUtils.isEmpty(last_name)) {
            return String.valueOf(first_name).concat(" ").concat(String.valueOf(last_name));
        } else {
            return String.valueOf(username);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(token);
    }
}
