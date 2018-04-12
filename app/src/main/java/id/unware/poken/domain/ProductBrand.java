package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class ProductBrand implements Parcelable {
    public long id;
    public String name;
    public String logo;

    protected ProductBrand(Parcel in) {
        id = in.readLong();
        name = in.readString();
        logo = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(logo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductBrand> CREATOR = new Creator<ProductBrand>() {
        @Override
        public ProductBrand createFromParcel(Parcel in) {
            return new ProductBrand(in);
        }

        @Override
        public ProductBrand[] newArray(int size) {
            return new ProductBrand[size];
        }
    };
}
