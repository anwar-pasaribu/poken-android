package id.unware.poken.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Represent "/poken_rest/cart/"
 *
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ShoppingCart implements Serializable {

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
}