package id.unware.poken.ui.shoppingcart.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingCartDataRes;
import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.PokenRequest;
import id.unware.poken.pojo.UIState;
import id.unware.poken.ui.shoppingcart.presenter.IShoppingCartModelPresenter;
import okhttp3.Credentials;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ShoppingCartModel extends MyCallback implements IShoppingCartModel {

    final private PokenRequest req;

    private IShoppingCartModelPresenter presenter;

    public ShoppingCartModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestShoppingCartData(IShoppingCartModelPresenter presenter) {
        this.presenter = presenter;

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        String credential = Credentials.basic("anwar", "anwar_poken17");
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", credential);

        req.reqShoppingCartContent(headerMap).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {

        presenter.updateViewState(UIState.FINISHED);

        ArrayList<ShoppingCart> shoppingCarts = new ArrayList<>();
        shoppingCarts.addAll(((ShoppingCartDataRes) response.body()).results);
        presenter.onShoppingCartDataResponse(shoppingCarts);
    }

    @Override
    public void onMessage(String msg) {

    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
