package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class ProductBrand implements Parcelable {
    public String name;
    public String logo;

    protected ProductBrand(Parcel in) {
        name = in.readString();
        logo = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(logo);
    }
}
