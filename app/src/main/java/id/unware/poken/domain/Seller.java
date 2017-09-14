package id.unware.poken.domain;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class Seller {
    public long id;
    public String store_avatar;
    public String store_name;
    public String tag_line;
    public String phone_number;
    public String location;

    // Wether customer subscribe to Seller
    public boolean is_subscribed;
}
