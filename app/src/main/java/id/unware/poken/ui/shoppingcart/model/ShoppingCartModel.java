package id.unware.poken.ui.shoppingcart.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingCartDataRes;
import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingcart.presenter.IShoppingCartModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ShoppingCartModel extends MyCallback implements IShoppingCartModel {

    private static final String TAG = "ShoppingCartModel";
    final private PokenRequest req;

    private IShoppingCartModelPresenter presenter;

    private int deletedItemPos = -1;

    public ShoppingCartModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestShoppingCartData(IShoppingCartModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // noinspection unchecked
        req.reqShoppingCartContent(PokenCredentials.getInstance().getCredentialHashMap())
                .enqueue(this);
    }

    @Override
    public void deleteShoppingCartData(int deleteItemPos, long shoppingCartId, IShoppingCartModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.deletedItemPos = deleteItemPos;

        // noinspection unchecked
        req.deleteShoppingCartContent(
                PokenCredentials.getInstance().getCredentialHashMap(),
                shoppingCartId
        ).enqueue(this);
    }

    @Override
    public void patchExtraNote(IShoppingCartModelPresenter presenter, ShoppingCart item) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> postBody = new HashMap<>();
        postBody.put("extra_note", String.valueOf(item.extra_note));

        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
            // noinspection unchecked
            req.patchShoppingCartExtraNote(
                    item.id,
                    PokenCredentials.getInstance().getCredentialHashMap(),
                    postBody)
                    .enqueue(this);
        }
    }

    @Override
    public void updateItemQuantity(IShoppingCartModelPresenter presenter, ShoppingCart shoppingCart) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> postBody = new HashMap<>();
        postBody.put("quantity", String.valueOf(shoppingCart.quantity));

        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
            // noinspection unchecked
            req.patchShoppingCartExtraNote(
                    shoppingCart.id,
                    PokenCredentials.getInstance().getCredentialHashMap(),
                    postBody)
                    .enqueue(this);
        }
    }

    @Override
    public void onSuccess(Response response) {
        Utils.Log(TAG, "On network request success: \"" + String.valueOf(response.body()) + "\"");
        if(response.code() == 204) {
            // No body for data DELETE
            Utils.Log(TAG, "No body content. Deleted item pos: " + deletedItemPos);
            presenter.onShoppingCartDeleted(deletedItemPos);
        } else if (response.code() == 200){
            presenter.updateViewState(UIState.FINISHED);

            if (response.body() instanceof ShoppingCartDataRes) {

                ArrayList<ShoppingCart> shoppingCarts = new ArrayList<>();
                shoppingCarts.addAll(((ShoppingCartDataRes) response.body()).results);
                presenter.onShoppingCartDataResponse(shoppingCarts);

            } else if (response.body() instanceof ShoppingCart) {
                Utils.Log(TAG, "Shopping cart updated.");
                presenter.onShoppingCartItemUpdated((ShoppingCart) response.body());
            }
        }
    }

    @Override
    public void onMessage(String msg, int status) {
        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            presenter.updateViewState(UIState.ERROR);
        }
    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
