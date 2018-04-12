package id.unware.poken.ui.featured.model;

import android.util.Log;

import id.unware.poken.domain.Featured;
import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.featured.presenter.IFeaturedModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Aug 12 2017
 */

public class FeaturedModel extends MyCallback implements IFeaturedModel {

    private static final String TAG = "FeaturedModel";

    final private PokenRequest req;

    private IFeaturedModelPresenter presenter;

    public FeaturedModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestFeaturedItemDetail(IFeaturedModelPresenter presenter, long featuredId) {

        Utils.Log(TAG, "Req featured detail for: " + featuredId);

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        //noinspection unchecked
        this.req.reqSingleFeaturedItemDetail(String.valueOf(featuredId))
                .enqueue(this);

    }

    @Override
    public void onSuccess(Response response) {

        presenter.updateViewState(UIState.FINISHED);

        if (response.body() instanceof Featured) {

            Featured featured = (Featured) response.body();

            Utils.Log(TAG, "Featured item detail found. Featured name: " + featured.name);

            presenter.onFeaturedItemDetailRes(featured);

        }

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
