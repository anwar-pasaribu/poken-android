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
    public boolean is_new;
    public Date date_created;
    public ProductBrand brand;
    public String category;
    public ArrayList<ProductImage> images;
    public String size;
    public int stock;
    public double price;
    public double weight;

    public Product() {
    }
}
