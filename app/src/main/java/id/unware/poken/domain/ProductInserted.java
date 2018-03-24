package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by Anwar Pasaribu on 16/02/2018.
 * Add new product staructure
 */
public class ProductInserted implements Parcelable {

    @SerializedName("id") public long id;
    @SerializedName("name") public String name;
    @SerializedName("description") public String description;
    @SerializedName("seller") public long seller;
    @SerializedName("is_cod") public boolean is_cod;
    @SerializedName("is_new") public boolean is_new;
    @SerializedName("is_discount") public boolean is_discount;
    @SerializedName("discount_amount") public double discount_amount;
    @SerializedName("date_created") public Date date_created;
    @SerializedName("brand") public long brand;
    @SerializedName("images") public long[] images;
    @SerializedName("size") public long size;
    @SerializedName("category") public long category;
    @SerializedName("stock") public int stock;
    @SerializedName("price") public double price;
    @SerializedName("weight") public double weight;

    public ProductInserted() {
    }

    protected ProductInserted(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        seller = in.readLong();
        is_cod = in.readByte() != 0;
        is_new = in.readByte() != 0;
        is_discount = in.readByte() != 0;
        discount_amount = in.readDouble();
        brand = in.readLong();
        images = in.createLongArray();
        size = in.readLong();
        category = in.readLong();
        stock = in.readInt();
        price = in.readDouble();
        weight = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeLong(seller);
        dest.writeByte((byte) (is_cod ? 1 : 0));
        dest.writeByte((byte) (is_new ? 1 : 0));
        dest.writeByte((byte) (is_discount ? 1 : 0));
        dest.writeDouble(discount_amount);
        dest.writeLong(brand);
        dest.writeLongArray(images);
        dest.writeLong(size);
        dest.writeLong(category);
        dest.writeInt(stock);
        dest.writeDouble(price);
        dest.writeDouble(weight);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductInserted> CREATOR = new Creator<ProductInserted>() {
        @Override
        public ProductInserted createFromParcel(Parcel in) {
            return new ProductInserted(in);
        }

        @Override
        public ProductInserted[] newArray(int size) {
            return new ProductInserted[size];
        }
    };

    @Override public String toString() {
        return "ProductInserted{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", seller=" + seller +
                ", is_cod=" + is_cod +
                ", is_new=" + is_new +
                ", is_discount=" + is_discount +
                ", discount_amount=" + discount_amount +
                ", date_created=" + date_created +
                ", brand=" + brand +
                ", images=" + Arrays.toString(images) +
                ", size=" + size +
                ", category=" + category +
                ", stock=" + stock +
                ", price=" + price +
                ", weight=" + weight +
                '}';
    }
}

/*

{
    "name": "[POSTMAN] ZARA Vertigo Backpack",
    "description": "[POSTMAN] Seri : 70828<br/>\r\nQuality : Like Ori<br/>\r\nMaterial : Kulit<br/>\r\nBag Size : 30x15x36cm<br/>\r\nWeight : 8 ons<br/>\r\nReady 2 Colour : Black, Brown<br/>\r\n\r\nNB: 1 seri { 2 Black,2 Brown }",
    "seller": 24,
    "is_new": true,
    "date_created": "2018-02-16T14:25:11.757000Z",
    "brand": 1,
    "category": 5,
    "images": [
        1120,
        1119,
        1118,
        1117,
        1116
    ],
    "size": 5,
    "stock": 10,
    "price": 267750,
    "weight": 250
}

* */