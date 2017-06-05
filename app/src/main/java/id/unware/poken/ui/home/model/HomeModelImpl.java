package id.unware.poken.ui.home.model;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.PokenRequest;
import id.unware.poken.domain.HomeDataRes;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.home.presenter.IHomeModelPresenter;
import io.realm.Realm;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class HomeModelImpl extends MyCallback implements IHomeModel {

    private static final String TAG = "HomeModelImpl";

    final private PokenRequest req;

    private Realm realm;
    private IHomeModelPresenter presenter;

    public HomeModelImpl(Realm realm) {
        this.realm = realm;
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestHomeData(IHomeModelPresenter presenter) {

        this.presenter = presenter;

        //String credential = Credentials.basic("anwar", "anwar_poken17");
        Map<String, String> headerMap = new HashMap<>();
        //headerMap.put("Authorization", credential);


        req.reqHomeContent(headerMap).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {
        Utils.Logs('i', TAG, "Req. success with response: " + String.valueOf(response.body()));
        presenter.onHomeDataResponse((HomeDataRes) response.body());
    }

    @Override
    public void onMessage(String msg) {
        MyLog.FabricLog(Log.INFO, "Home model message on req: " + msg);
    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
