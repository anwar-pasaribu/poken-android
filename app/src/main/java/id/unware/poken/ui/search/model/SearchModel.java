package id.unware.poken.ui.search.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductDataRes;
import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.search.presenter.ISearchModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Aug 06 2017
 */
public class SearchModel extends MyCallback implements ISearchModel {

    private static final String TAG = "SearchModel";

    private ISearchModelPresenter presenter;
    final private PokenRequest req;

    public SearchModel() {
        this.req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }


    @Override
    public void searchProductByQuery(String query, ISearchModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        MyLog.FabricLog(Log.VERBOSE, TAG.concat(" - search query: ").concat(query));

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", String.valueOf(query));
        queryParams.put("category_name", String.valueOf(query));

        this.req.reqSearchProductContent(
                queryParams)
                .enqueue(this);

    }

    @Override
    public void loadMoreSearchedProductQuery(String query, int page, ISearchModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        MyLog.FabricLog(Log.VERBOSE, TAG.concat(" - search query: ").concat(query));

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("name", String.valueOf(query));
        queryParams.put("category_name", String.valueOf(query));
        queryParams.put("page", String.valueOf(page));

        this.req.reqSearchProductContent(
                queryParams)
                .enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {
        presenter.updateViewState(UIState.FINISHED);

        this.presenter.onNextProductPage(((ProductDataRes) response.body()).next);

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
