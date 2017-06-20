package id.unware.poken.ui.browse.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductDataRes;
import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.PokenRequest;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.browse.presenter.IBrowseModelPresenter;
import okhttp3.Credentials;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 18 2017
 */

public class BrowseModel extends MyCallback implements IBrowseModel {

    private static final String TAG = "BrowseModel";

    final private PokenRequest req;

    private IBrowseModelPresenter presenter;

    public BrowseModel() {
        this.req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestSellerData(IBrowseModelPresenter presenter, int actionId) {
        this.presenter = presenter;

        String credential = Credentials.basic("anwar", "anwar_poken17");
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", credential);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("action_id", String.valueOf(actionId));

        this.req.reqProductContent(headerMap, queryParams).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {
        presenter.updateViewState(UIState.FINISHED);
        ArrayList<Product> products = new ArrayList<>();
        products.addAll(((ProductDataRes) response.body()).results);
        if (products.size() > 0) {
            presenter.onProductsResponse(products);
        }

    }

    @Override
    public void onMessage(String msg, int status) {
        Utils.Logs('v', TAG, "Message from network req: " + msg);
        Utils.Logs('v', TAG, "Status from network req: " + status);

        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            presenter.updateViewState(UIState.ERROR);
        }
    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
