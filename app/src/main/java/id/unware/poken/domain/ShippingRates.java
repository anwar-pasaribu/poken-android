package id.unware.poken.domain;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public class ShippingRates extends Shipping implements Parcelable {

    public ArrayList<Shipping> courier_rates;

    public ShippingRates() {
    }
}
