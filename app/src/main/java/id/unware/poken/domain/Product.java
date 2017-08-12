package id.unware.poken.domain;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class Product {
    public long id;
    public String name;
    public String description;
    public Seller seller;
    public boolean is_cod;
    public boolean is_new;
    public boolean is_discount;
    public double discount_amount;
    public Date date_created;
    public ProductBrand brand;
    public String category;
    public ArrayList<ProductImage> images;
    public String size;
    public int stock;
    public double price;
    public double weight;

    public static String KEY_PRODUCT_ID = "id";

    public Product() {
    }

    public double getDiscountedPrice() {
        double originalProductPrice = this.price;
        double discountAmount = this.discount_amount;

        return (originalProductPrice - ((originalProductPrice * discountAmount) / 100));

    }
}
