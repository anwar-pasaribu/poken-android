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
    public int status;
}

/*{
            "id": 1,
            "order_details": {
                "id": 1,
                "customer": 1,
                "address": {
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
            },
            "shopping_carts": [
                {*/