package id.unware.poken.ui.nearby.model;

import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.TikiRequest;

/**
 * @author Anwar Pasaribu
 * @since Mar 22 2017
 */

public class NearbyModel implements INearbyModel {

    private final TikiRequest mTikiRequest;

    public NearbyModel() {
        mTikiRequest = AdRetrofit.getInstanceTiki().create(TikiRequest.class);
    }

    @Override
    public void getNearby(double lat, double lon, MyCallback callback) {
        mTikiRequest.fetchNearbyBranchOffices(lat, lon).enqueue(callback);
    }
}
