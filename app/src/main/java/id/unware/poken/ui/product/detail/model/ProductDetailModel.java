package id.unware.poken.ui.product.detail.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.PokenRequest;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.ui.product.detail.presenter.IProductDetailModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public class ProductDetailModel extends MyCallback implements IProductDetailModel {

    final private PokenRequest req;

    private IProductDetailModelPresenter presenter;

    private ArrayList<Shipping> shippings = new ArrayList<>();
    private boolean isCod = false;

    public ProductDetailModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }


    @Override
    public ArrayList<Shipping> getShippingOptions() {
        return shippings;
    }

    @Override
    public void requestProductData(long productId, IProductDetailModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // noinspection unchecked
        req.reqSingleProductDetail(productId).enqueue(this);

    }

    @Override
    public void postProductToShoppingCart (
            final long shippingOptionId,
            final long productId,
            IProductDetailModelPresenter presenter ) {

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> postBody = new HashMap<>();
        postBody.put("product_id", String.valueOf(productId));
        postBody.put("shipping_id", String.valueOf(shippingOptionId));
        postBody.put("quantity", "1");  // Add one quantity

        // noinspection unchecked
        req.postNewOrUpdateShoppingCart(
                PokenCredentials.getInstance().getCredentialHashMap(),
                postBody)
                .enqueue(ProductDetailModel.this);


    }

    @Override
    public void loadShippingOptions() {

        shippings.add(new Shipping(1, "POS Indonesia", 0));
        shippings.add(new Shipping(2, "COD", 15000));

        this.presenter.onShippingOptionListResponse(shippings);
    }

    @Override
    public boolean isCodAvailable() {
        return isCod;
    }

    @Override
    public void onSuccess(Response response) {

        Object o = response.body();

        if (o instanceof Product) {

            this.isCod = ((Product) o).is_cod;

            presenter.onProductDetailDataResponse((Product) o);

        } else if (o instanceof ShoppingCart) {
            presenter.onShoppingCartCreateOrUpdateResponse((ShoppingCart) o);
        }

    }

    @Override
    public void onMessage(String msg, int status) {

    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
