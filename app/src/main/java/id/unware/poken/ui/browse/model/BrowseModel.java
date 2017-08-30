package id.unware.poken.ui.browse.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.Category;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductDataRes;
import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.PokenRequest;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.browse.presenter.IBrowseModelPresenter;
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
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("action_id", String.valueOf(actionId));

        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
            this.req.reqProductContent(
                    PokenCredentials.getInstance().getCredentialHashMap(),
                    queryParams)
                    .enqueue(this);
        } else {
            this.req.reqProductContentByActionId(queryParams).enqueue(this);
        }
    }

    @Override
    public void requestSellerDataByCategory(IBrowseModelPresenter presenter, Category category) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("category_id", String.valueOf(category.getId()));
        queryParams.put("category_name", category.getName());

        if (PokenCredentials.getInstance().getCredentialHashMap() == null) {
            this.req.reqProductContentByCategory(
                    queryParams)
                    .enqueue(this);
        } else {
            this.req.reqProductContentByCategory(
                    PokenCredentials.getInstance().getCredentialHashMap(),
                    queryParams)
                    .enqueue(this);
        }

    }

    @Override
    public void onSuccess(Response response) {
        presenter.updateViewState(UIState.FINISHED);
        ArrayList<Product> products = new ArrayList<>();
        products.addAll(((ProductDataRes) response.body()).results);
        if (products.size() > 0) {
            presenter.onProductsResponse(products);
        } else {
            presenter.updateViewState(UIState.NODATA);
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
