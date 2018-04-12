package id.unware.poken.ui.home.model;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.domain.HomeDataRes;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.home.presenter.IHomeModelPresenter;
import io.realm.Realm;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class HomeModel extends MyCallback implements IHomeModel {

    private static final String TAG = "HomeModelImpl";

    final private PokenRequest req;

    private Realm realm;
    private IHomeModelPresenter presenter;

    public HomeModel(Realm realm) {
        this.realm = realm;
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestHomeData(IHomeModelPresenter presenter) {

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> headerMap = new HashMap<>();

        req.reqHomeContent(headerMap).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {

        presenter.updateViewState(UIState.FINISHED);

        Utils.Logs('i', TAG, "Req. success with response: " + String.valueOf(response.body()));
        presenter.onHomeDataResponse((HomeDataRes) response.body());
    }

    @Override
    public void onMessage(String msg, int status) {
        MyLog.FabricLog(Log.INFO, "Home model message on req: " + msg);
        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            presenter.updateViewState(UIState.ERROR);
        }

        presenter.showMessage(msg, status);
    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
