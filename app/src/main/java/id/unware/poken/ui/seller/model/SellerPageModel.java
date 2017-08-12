package id.unware.poken.ui.seller.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductDataRes;
import id.unware.poken.domain.Seller;
import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.PokenRequest;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
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
    public void requestSellerData(ISellerPageModelPresenter presenter, long sellerId) {
        this.presenter = presenter;

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("seller_id", String.valueOf(sellerId));

        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
            this.req.reqProductContent(
                    PokenCredentials.getInstance().getCredentialHashMap(),
                    queryParams
            ).enqueue(this);
        } else {
            this.req.reqProductContentByActionId(queryParams).enqueue(this);
        }
    }

    @Override
    public void onSuccess(Response response) {
        presenter.updateViewState(UIState.FINISHED);
        ArrayList<Product> products = new ArrayList<>();
        products.addAll(((ProductDataRes) response.body()).results);
        if (products.size() > 0) {
            presenter.onSellerPageContentResponse(products);

            Seller seller = products.get(0).seller;

            presenter.setupSellerInfo(seller);
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
