package id.unware.poken.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public class ShippingRates extends Shipping implements Serializable {

    public ArrayList<Shipping> courier_rates;

    public ShippingRates() {
    }
}
