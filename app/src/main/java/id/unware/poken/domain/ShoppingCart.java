package id.unware.poken.domain;

import java.util.Date;

/**
 * Represent "/poken_rest/cart/"
 *
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ShoppingCart {
    public long id;
    public Product product;
    public Date date;
    public int quantity;

    public static String KEY_SHOPPING_CART_ID = "id";


    public ShoppingCart() {
    }
}
/*
{
      "id": 1,
      "product": {
        "name": "Baju Pria",
        "images": [
          {
            "path": "http://192.168.1.100:8000/media/product_image/1496421469_66_2014-08-25_Matt_Look_094_2048x2048.jpeg"
          }
        ],
        "size": "L",
        "stock": 7,
        "price": 350000,
        "weight": 250,
        "seller": {
          "id": 1,
          "store_name": "Kejora Busana",
          "tag_line": "Harga paling murah, terpercaya",
          "phone_number": "087884155479",
          "location": "Jakarta Barat"
        }
      },
      "date": "2017-06-01T13:18:17.071000Z",
      "quantity": 3
    }
*/