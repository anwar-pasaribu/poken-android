package id.unware.poken.ui.TrackPackage.TrackingMain.model;

import id.unware.poken.httpConnection.MyCallback;

/**
 * Created by marzellaalfamega on 3/20/17.
 */

public interface ITrackingMainModel {
    void track(String strResiKeyword, MyCallback trackingMainPresenter);
}
