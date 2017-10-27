package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class ProductImage implements Parcelable {
    public long id;
    public String path;
    public String thumbnail;
    public String title;
    public String description;

    protected ProductImage(Parcel in) {
        id = in.readLong();
        path = in.readString();
        thumbnail = in.readString();
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<ProductImage> CREATOR = new Creator<ProductImage>() {
        @Override
        public ProductImage createFromParcel(Parcel in) {
            return new ProductImage(in);
        }

        @Override
        public ProductImage[] newArray(int size) {
            return new ProductImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(path);
        parcel.writeString(thumbnail);
        parcel.writeString(title);
        parcel.writeString(description);
    }
}
