package id.unware.poken.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class Featured {

    @Expose
    @SerializedName("id")
    public long id;

    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("image")
    public String image;

    @Expose
    @SerializedName("thumbnail")
    public String thumbnail;

    @Expose
    @SerializedName("expiry_date")
    public Date expiry_date;

    @Expose
    @SerializedName("target_id")
    public int target_id;

    @SerializedName("related_products")
    public ArrayList<Product> related_products;


    public Featured() {
    }

    public enum Type {
        IMAGE,
        VIDEO
    }
}
