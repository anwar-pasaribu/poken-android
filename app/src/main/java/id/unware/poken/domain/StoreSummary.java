package id.unware.poken.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anwar Pasaribu on 2/21/2018.
 */

public class StoreSummary {

    @Expose
    @SerializedName("id")
    public long id;

    @Expose
    @SerializedName("seller_detail")
    public Seller seller_detail;

    @Expose
    @SerializedName("latest_products")
    public ArrayList<Product> latest_products;

    @Expose
    @SerializedName("total_credits")
    public double total_credits;

    @Expose
    @SerializedName("promotions")
    public ArrayList<SellerPromo> promotions;
}
