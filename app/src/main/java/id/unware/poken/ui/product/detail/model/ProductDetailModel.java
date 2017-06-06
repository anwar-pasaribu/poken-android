package id.unware.poken.ui.product.detail.model;

import id.unware.poken.domain.Product;
import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.PokenRequest;
import id.unware.poken.pojo.UIState;
import id.unware.poken.ui.product.detail.presenter.IProductDetailModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public class ProductDetailModel extends MyCallback implements IProductDetailModel {

    final private PokenRequest req;

    private IProductDetailModelPresenter presenter;

    public ProductDetailModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }


    @Override
    public void requestProductData(long productId, IProductDetailModelPresenter presenter) {
        this.presenter = presenter;

        // Loading state to view
        presenter.updateViewState(UIState.LOADING);

        req.reqSingleProductDetail(productId).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {
        presenter.onProductDetailDataResponse((Product) response.body());
    }

    @Override
    public void onMessage(String msg) {

    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
