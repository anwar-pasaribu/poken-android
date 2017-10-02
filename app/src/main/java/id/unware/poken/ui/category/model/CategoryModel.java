package id.unware.poken.ui.category.model;

import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.FeaturedCategoryProductDataRes;
import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.category.presenter.ICategoryModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Aug 13 2017
 */

public class CategoryModel extends MyCallback implements ICategoryModel {

    private static final String TAG = "CategoryModel";

    private final PokenRequest req;
    private ICategoryModelPresenter presenter;

    public CategoryModel() {
        this.req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void reqFeaturedCategory(ICategoryModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        this.req.reqFeaturedProductCategoriesContent(new HashMap<String, String>()).enqueue(this);
    }

    @Override
    public void requestMoreFeaturedProductCategory(ICategoryModelPresenter presenter, int page) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(page));

        this.req.reqFeaturedProductCategoriesContent(
                queryParams
        ).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {

        this.presenter.onNextProductPage(((FeaturedCategoryProductDataRes) response.body()).next);

        this.presenter.updateViewState(UIState.FINISHED);

        if (response.body() instanceof FeaturedCategoryProductDataRes) {

            Utils.Log(TAG, "Featured product per category found.");
            this.presenter.onFeaturedProductPerCategoryResponse((FeaturedCategoryProductDataRes) response.body());

        }
    }

    @Override
    public void onMessage(String msg, int status) {
        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            this.presenter.updateViewState(UIState.ERROR);
        }

        this.presenter.onMessageResponse(msg, status);
    }

    @Override
    public void onFinish() {
        this.presenter.updateViewState(UIState.FINISHED);
    }
}
