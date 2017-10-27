package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public class Shipping implements Parcelable {
    public long id;
    public String name;
    public double fee;

    public Shipping() {
    }

    public Shipping(long id, String name, double fee) {
        this.id = id;
        this.name = name;
        this.fee = fee;
    }

    protected Shipping(Parcel in) {
        id = in.readLong();
        name = in.readString();
        fee = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeDouble(fee);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Shipping> CREATOR = new Creator<Shipping>() {
        @Override
        public Shipping createFromParcel(Parcel in) {
            return new Shipping(in);
        }

        @Override
        public Shipping[] newArray(int size) {
            return new Shipping[size];
        }
    };
}
