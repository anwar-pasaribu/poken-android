package id.unware.poken.ui.seller.model;

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
import id.unware.poken.ui.seller.presenter.ISellerPageModelPresenter;
import okhttp3.Credentials;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 17 2017
 */

public class SellerPageModel extends MyCallback implements ISellerPageModel {

    private static final String TAG = "SellerPageModel";

    final private PokenRequest req;

    private ISellerPageModelPresenter presenter;

    public SellerPageModel() {
        this.req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestSellerData(ISellerPageModelPresenter presenter) {
        this.presenter = presenter;

        String credential = Credentials.basic("anwar", "anwar_poken17");
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", credential);

        this.req.reqProductContent(headerMap).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {
        presenter.updateViewState(UIState.FINISHED);
        ArrayList<Product> products = new ArrayList<>();
        products.addAll(((ProductDataRes) response.body()).results);
        presenter.onSellerPageContentResponse(products);

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
