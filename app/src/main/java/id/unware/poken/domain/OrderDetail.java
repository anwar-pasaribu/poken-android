package id.unware.poken.domain;

import java.util.Date;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public class OrderDetail {
    public long id;
    public String order_id;  // Unique order id for presentation purpose
    /** Customer ID*/
    public long customer;
    public AddressBook address_book;
    public Date date;
    public Shipping shipping;

}