package id.unware.poken.ui.TrackPackage.TrackingMain.view;

import android.view.View;

import java.util.List;

/**
 * Created by marzellaalfamega on 3/20/17.
 */

public interface ITrackingMainView {

    void showProgressDialog();

    void dismissProgressDialog();

    void setVerticalLineVisible();

    void showMultipleTrackings(boolean isShow);

    List getListTracking();

    View getVerticalLine();

    void refreshList();

    String getResi();

    void showMessage(String message);
}
