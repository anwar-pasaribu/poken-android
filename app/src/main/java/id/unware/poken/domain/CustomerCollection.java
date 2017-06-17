package id.unware.poken.domain;

/**
 * @author Anwar Pasaribu
 * @since Jun 12 2017
 */

public class CustomerCollection {
    public long id;
    public long product_id;
    public String product_name;
    public double product_price;
    public String product_image;
    public int status;

    public CustomerCollection() {
    }
}

/*
{
    "id": 1,
    "product_id": 2,
    "product_name": "Baju PriaLace trim accentuates the neckline of this lightweight Camisole by Only Hearts",
    "product_price": 699000,
    "product_image": "http://192.168.1.100:8000/media/product_image/1496418489_47_2014_10_18_Addis_Look1008_2048x2048.jpeg",
    "status": 1
}
*/
