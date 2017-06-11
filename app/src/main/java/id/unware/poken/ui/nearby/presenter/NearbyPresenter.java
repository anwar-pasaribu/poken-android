package id.unware.poken.ui.nearby.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.pojo.PojoOfficeLocation;
import id.unware.poken.pojo.PojoOfficeLocationData;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.nearby.model.INearbyModel;
import id.unware.poken.ui.nearby.model.NearbyModel;
import id.unware.poken.ui.nearby.view.INearbyView;
import retrofit2.Response;


/**
 * @author Anwar Pasaribu
 * @since Mar 22 2017
 */

public class NearbyPresenter extends MyCallback implements INearbyPresenter {

    private final INearbyView mView;
    private final INearbyModel mModel;

    public NearbyPresenter(INearbyView view) {
        this.mView = view;
        this.mModel = new NearbyModel();
    }

    @Override
    public void requestNearbyBranches(double lat, double lon) {
        mView.showViewState(UIState.LOADING);
        mModel.getNearby(lat, lon, this);
    }

    @Override
    public void onSuccess(Response response) {

        PojoOfficeLocationData locationData = (PojoOfficeLocationData) response.body();
        if (locationData == null) return;

        List<PojoOfficeLocation> officeLocationList = new ArrayList<>();
        officeLocationList.addAll(Arrays.asList(locationData.getOfficeLocations()));

        Utils.Log("NearbyPresenter", "Nearby size: " + officeLocationList.size());

        mView.showNearbyBranches(officeLocationList);
    }

    @Override
    public void onMessage(String msg, int status) {
        Utils.Logs('e', "NearbyPresenter", "Message : " + msg);
    }

    @Override
    public void onFinish() {
        mView.showViewState(UIState.FINISHED);
    }
}
