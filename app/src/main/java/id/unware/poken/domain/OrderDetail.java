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

    /** Related address book id when insert new order detail sucess */
    public long address_book_id;

    public AddressBook address_book;
    public Shipping shipping;

    public Date date;
    public Date payment_expiration_date;
    public Date order_expiration_date;

    /**
     * Refer to {@link id.unware.poken.models.OrderStatus}
     */
    public int order_status;

    public String shipping_tracking_id;

    public OrderDetail() {
    }

    @Override public String toString() {
        return "OrderDetail{" +
                "id=" + id +
                ", order_id='" + order_id + '\'' +
                ", customer=" + customer +
                ", address_book_id=" + address_book_id +
                ", address_book=" + address_book +
                ", shipping=" + shipping +
                ", date=" + date +
                ", payment_expiration_date=" + payment_expiration_date +
                ", order_expiration_date=" + order_expiration_date +
                ", order_status=" + order_status +
                ", shipping_tracking_id='" + shipping_tracking_id + '\'' +
                '}';
    }
}