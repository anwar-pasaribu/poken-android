package id.unware.poken.ui.TrackPackage.TrackingMain.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;

import id.unware.poken.R;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.pojo.PojoTracking;
import id.unware.poken.pojo.PojoTrackingData;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.ui.TrackPackage.TrackingMain.model.ITrackingMainModel;
import id.unware.poken.ui.TrackPackage.TrackingMain.view.TrackingActivity;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by marzellaalfamega on 3/20/17.
 */

public class TrackingMainPresenter extends MyCallback implements ITrackingMainPresenter {

    private TrackingActivity view;
    private ITrackingMainModel model;

    public TrackingMainPresenter(TrackingActivity view, ITrackingMainModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void track() {
        view.showProgressDialog();
        model.track(view.getResi(), this);
    }

    @Override
    public void onSuccess(Response response) {
        if (response.body() instanceof PojoTrackingData) {
            PojoTrackingData data = (PojoTrackingData) response.body();
            if (view.isFinishing()) return;

            int trackingSize = data.getTrackings().length;

            if (trackingSize > 0 && data.success == 1) {

                // Make sure vertical line is empty
                view.getVerticalLine().setVisibility(View.VISIBLE);

                // First tracking data to check whether tracking success or no
                PojoTracking firstPojoTracking = data.getTrackings()[0];

                MyLog.FabricLog(Log.INFO, TAG + " - [success] Tracking size: " + trackingSize);

                // [V30] Some case cnno key not available, the abort process
                // Check whether "msg" / "error" available.
                if (TextUtils.isEmpty(firstPojoTracking.getCnno())) {

                    if (!TextUtils.isEmpty(firstPojoTracking.getMsg())) {

//                        MyLog.FabricLog(Log.ERROR, TAG + " - Tracking CNNO not available with msg: " + firstPojoTracking.getMsg());
//                        failed(firstPojoTracking.getMsg());
                        onMessage(firstPojoTracking.getMsg());

                    } else if (!TextUtils.isEmpty(firstPojoTracking.getError())) {

                        MyLog.FabricLog(Log.ERROR, TAG + " - Tracking CNNO not available with error field: " + firstPojoTracking.getError());
//                        failed(firstPojoTracking.getError());
                        onMessage(firstPojoTracking.getError());
                    }

                    return;
                }

                // Add data to db
                ControllerRealm.getInstance().addAllTrackingData(data);

                // Collect RESI
                ArrayList<String> cnnoArrayList = new ArrayList<>();
                for (PojoTracking pojoTracking : data.getTrackings()) {

                    cnnoArrayList.add(pojoTracking.getCnno());
                }

                // Show multi RESI
                if (cnnoArrayList.size() > 1) {

                    view.showMultipleTrackings(true);
                    view.setupViewPager(cnnoArrayList);

                    // Show single RESI
                } else if (cnnoArrayList.size() == 1) {

                    view.showMultipleTrackings(false);

                    PojoTracking pojoTracking = data.getTrackings()[0];

                    view.getListTracking().clear();
                    view.getListTracking().add(pojoTracking);  // For header purpose

                    if (pojoTracking.getStatuses().length > 0) {
                        Collections.addAll(view.getListTracking(), pojoTracking.getStatuses());  // Tracking item
                    } else {
                        // Show "Belum ada data tracking untuk saat ini." message
                        view.getListTracking().add(pojoTracking.getMsg());

                        // Hide vertical line when no data available
                        view.getVerticalLine().setVisibility(View.GONE);
                    }

                    view.refreshList();
                }

                // [V29] Save last tracking keyword.
                if (!cnnoArrayList.isEmpty()) {
                    String strKeyword = view.getResi().trim() + " (" + data.getTrackings()[0].getConsigneeName().trim() + ")";

                    // Save recent tracking
                    ControllerRealm.getInstance().addRecentKeyword(strKeyword, Constants.TAG_TRACKING);
                }
            } else {
                view.showMessage(view.getString(R.string.msg_tracking_not_found));
            }
        }
    }

    @Override
    public void onMessage(String msg) {
        view.showMessage(msg);
    }

    @Override
    public void onFinish() {
        view.dismissProgressDialog();
    }
}