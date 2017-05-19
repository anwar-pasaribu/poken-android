package id.unware.poken.ui.nearby.model;

import id.unware.poken.httpConnection.MyCallback;

/**
 * @author Anwar Pasaribu
 * @since Mar 22 2017
 */

public interface INearbyModel {
    void getNearby(double lat, double lon, MyCallback callback);
}
