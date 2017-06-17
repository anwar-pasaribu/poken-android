package id.unware.poken.domain;

/**
 * @author Anwar Pasaribu
 * @since Jun 13 2017
 */

public class CustomerSubscription {
    public long id;
    public boolean is_get_notif;
    public long seller_id;
    public String seller_name;
    public String seller_profile_pic;
    public String seller_tag_line;
    public String seller_location;

    public CustomerSubscription() {
    }
}

/*
{
            "id": 2,
            "is_get_notif": false,
            "seller_id": 2,
            "seller_name": "Indah Store",
            "seller_tag_line": "Casing dengan harga termurah se-Poken",
            "seller_location": "Jakarta Barat"
        }
 */
