package id.unware.poken.ui.TrackPackage.TrackingMain.model;

import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.TikiRequest;

/**
 * Created by marzellaalfamega on 3/20/17.
 */

public class TrackingMainModel implements ITrackingMainModel {

    private TikiRequest req;

    public TrackingMainModel() {
        req = AdRetrofit.getInstanceTiki().create(TikiRequest.class);
    }

    @Override
    public void track(String strResiKeyword, MyCallback trackingMainPresenter) {
        req.getTracking(strResiKeyword).enqueue(trackingMainPresenter);
    }
}