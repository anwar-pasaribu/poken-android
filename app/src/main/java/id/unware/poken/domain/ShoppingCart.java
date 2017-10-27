package id.unware.poken.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Represent "/poken_rest/cart/"
 *
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ShoppingCart implements Parcelable {

    public long id;

    /** INSERT ONLY - API response when inserting new shopping cart*/
    public long product_id;

    public Product product;
    public Shipping shipping;
    public Date date;
    public int quantity;

    /** Shopping item price without additional fee */
    public double total_price;

    /** Shopping item price include all additional fee (ex. shipping, tax, etc.) */
    public double grand_total_price;

    /** Selected shipping fee - Courier specific fee*/
    public double shipping_fee;

    public String extra_note;

    public static String KEY_SHOPPING_CART_ID = "id";


    public ShoppingCart() {
    }

    protected ShoppingCart(Parcel in) {
        id = in.readLong();
        product_id = in.readLong();
        product = in.readParcelable(Product.class.getClassLoader());
        shipping = in.readParcelable(Shipping.class.getClassLoader());
        quantity = in.readInt();
        total_price = in.readDouble();
        grand_total_price = in.readDouble();
        shipping_fee = in.readDouble();
        extra_note = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(product_id);
        dest.writeParcelable(product, flags);
        dest.writeParcelable(shipping, flags);
        dest.writeInt(quantity);
        dest.writeDouble(total_price);
        dest.writeDouble(grand_total_price);
        dest.writeDouble(shipping_fee);
        dest.writeString(extra_note);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ShoppingCart> CREATOR = new Creator<ShoppingCart>() {
        @Override
        public ShoppingCart createFromParcel(Parcel in) {
            return new ShoppingCart(in);
        }

        @Override
        public ShoppingCart[] newArray(int size) {
            return new ShoppingCart[size];
        }
    };
}