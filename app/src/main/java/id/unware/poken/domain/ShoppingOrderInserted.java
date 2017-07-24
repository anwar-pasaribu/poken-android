package id.unware.poken.domain;

/**
 * @author Anwar Pasaribu
 * @since Jul 21 2017
 */

public class ShoppingOrderInserted {
    public long id;
    public long order_details_id;
    public long[] shopping_carts;
    public int status;
}

/*

{
    "id": 2,
    "order_details_id": 12,
    "shopping_carts": [
        7
    ],
    "status": 0
}

*/