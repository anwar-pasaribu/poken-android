package id.unware.poken.domain;

import java.io.Serializable;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public class Shipping implements Serializable {
    public long id;
    public String name;
    public double fee;

    public Shipping() {
    }

    public Shipping(long id, String name, double fee) {
        this.id = id;
        this.name = name;
        this.fee = fee;
    }
}
