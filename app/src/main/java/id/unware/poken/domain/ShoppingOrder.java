package id.unware.poken.domain;

import java.util.ArrayList;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public class ShoppingOrder {
    public long id;
    public OrderDetail order_details;
    public ArrayList<ShoppingCart> shopping_carts;

    /**
     * User {@link OrderDetail#order_status} instead.
     */
    @Deprecated
    public int status;

    /**
     * Server generated total shopping.
     */
    public double total_shopping;

    public ShoppingOrder() {
    }
}