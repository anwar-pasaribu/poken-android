package id.unware.poken.ui.browse.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.Category;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductDataRes;
import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.domain.SellerDataRes;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.StringUtils;
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

        this.presenter.updateViewState(UIState.LOADING);

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
    public void requestSellerListData(IBrowseModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(1));

        this.req.reqSellerList(queryParams).enqueue(this);
    }

    @Override
    public void requestMoreProductsByIntentId(IBrowseModelPresenter presenter, int actionId, int page) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("action_id", String.valueOf(actionId));
        queryParams.put("page", String.valueOf(page));

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

        this.presenter.updateViewState(UIState.LOADING);

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
    public void requestMoreProductByCategory(IBrowseModelPresenter presenter, Category category, int nextPage) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("category_id", String.valueOf(category.getId()));
        queryParams.put("category_name", category.getName());
        queryParams.put("page", String.valueOf(nextPage));

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
    public void requestMoreSellerData(IBrowseModelPresenter presenter, int page) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(page));

        this.req.reqSellerList(queryParams).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {

        presenter.updateViewState(UIState.FINISHED);

        if (response.body() instanceof ProductDataRes) {

            int nextPage = Constants.STATE_NODATA;
            if (!StringUtils.isEmpty(((ProductDataRes) response.body()).next)) {
                Uri uri = Uri.parse(((ProductDataRes) response.body()).next);
                nextPage = Integer.valueOf(uri.getQueryParameter("page"));

                Utils.Logs('i', TAG, "Next URL parts: " + String.valueOf(uri));
                Utils.Logs('i', TAG, "Next page: " + String.valueOf(nextPage));

            }

            this.presenter.onNextProductPage(((ProductDataRes) response.body()).next, nextPage);

            ArrayList<Product> products = new ArrayList<>();
            products.addAll(((ProductDataRes) response.body()).results);
            if (products.size() > 0) {
                presenter.onProductsResponse(products);
            } else {
                presenter.updateViewState(UIState.NODATA);
            }

        } else if (response.body() instanceof SellerDataRes) {

            int nextPage = Constants.STATE_NODATA;
            if (!StringUtils.isEmpty(((SellerDataRes) response.body()).next)) {
                Uri uri = Uri.parse(((SellerDataRes) response.body()).next);
                nextPage = Integer.valueOf(uri.getQueryParameter("page"));

                Utils.Logs('i', TAG, "Next URL parts: " + String.valueOf(uri));
                Utils.Logs('i', TAG, "Next page: " + String.valueOf(nextPage));

            }
            this.presenter.onNextSellerListPage(((SellerDataRes) response.body()).next, nextPage);

            this.presenter.onSellerListResponse(((SellerDataRes) response.body()).results);
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
