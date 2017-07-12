package id.unware.poken.domain;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public class Shipping {
    public long id;
    public String name;
    public double fee;

    public Shipping(long id, String name, double fee) {
        this.id = id;
        this.name = name;
        this.fee = fee;
    }
}
