package id.unware.poken.domain;

import java.util.Date;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public class OrderDetail {
    public long id;
    /** Customer ID*/
    public long customer;
    public AddressBook address_book;
    public Date date;
    public Shipping shipping;

}

/*
"order_details": {
    "id": 1,
    "customer": 1,
    "address_book": {
        "id": 2,
        "customer": 1,
        "location": {
            "city": "Jakarta Barat",
            "district": "Kembangan",
            "zip": "11620",
            "state": "Indonesia"
        },
        "name": "Kantor Anwar",
        "address": "Jl. Meruya Ilir No. 88, Kembangan",
        "phone": "082362588301"
    },
    "date": "2017-06-08T13:39:04.883000Z",
    "shipping": {
        "name": "POS Indonesia",
        "fee": 9000
    }
}
*/