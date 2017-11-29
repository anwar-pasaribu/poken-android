package id.unware.poken.ui.shoppingorder.model;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.AddressBookDataRes;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.domain.ShoppingOrderDataRes;
import id.unware.poken.domain.ShoppingOrderInserted;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingorder.presenter.IShoppingOrderModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ShoppingOrderModel extends MyCallback implements IShoppingOrderModel {

    private static final String TAG = "ShoppingOrderModel";

    final private PokenRequest req;

    private IShoppingOrderModelPresenter presenter;

    public ShoppingOrderModel() {
        req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void requestShoppingOrderData(IShoppingOrderModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // This req. response: ShoppingOrderDataRes
        // noinspection unchecked
        req.reqShoppingOrderContent(
                PokenCredentials.getInstance().getCredentialHashMap()
        ).enqueue(this);
    }

    @Override
    public void postNewAddressBook(IShoppingOrderModelPresenter presenter, AddressBook addressBook) {

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // HashMap<String, String> newAddressMap = new HashMap<>();
        // newAddressMap.put()
        // noinspection unchecked
        req.postNewAddressBook(
                "application/json",
                PokenCredentials.getInstance().getCredentialHashMap(),
                addressBook
        ).enqueue(this);
    }

    @Override
    public void getAddressBookData(IShoppingOrderModelPresenter presenter) {

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // noinspection unchecked
        req.reqAddressBookContent(
                PokenCredentials.getInstance().getCredentialHashMap()
        ).enqueue(this);
    }

    @Override
    public void postOrUpdateOrderDetails(IShoppingOrderModelPresenter presenter, AddressBook addressBook, long previousOrderDetailId) {

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading
        this.presenter.updateViewState(UIState.LOADING);

        HashMap<String, String> postData = new HashMap<>();
        postData.put("address_book_id", String.valueOf(addressBook.id));
        postData.put("order_details_id", String.valueOf(previousOrderDetailId));

        // noinspection unchecked
        req.postNewOrUpdateOrderDetails(
                PokenCredentials.getInstance().getCredentialHashMap(),
                postData
        ).enqueue(this);
    }

    @Override
    public void postOrUpdateOrderedProduct(IShoppingOrderModelPresenter presenter, OrderDetail orderDetail, long[] shoppingCartIds) {

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading
        this.presenter.updateViewState(UIState.LOADING);

        HashMap<String, String> postData = new HashMap<>();
        postData.put("order_details_id", String.valueOf(orderDetail.id));

        // noinspection unchecked
        req.postNewOrderedProduct(
                PokenCredentials.getInstance().getCredentialHashMap(),
                postData,
                shoppingCartIds
        ).enqueue(this);

    }

    @Override
    public void requestShoppingOrderDataById(IShoppingOrderModelPresenter presenter, long orderedProductId) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        // This req. response: ShoppingOrderDataRes
        // noinspection unchecked
        req.reqShoppingOrderDetail(
                PokenCredentials.getInstance().getCredentialHashMap(),
                orderedProductId
        ).enqueue(this);
    }

    @Override
    public void parseSelectedShoppingCarts(IShoppingOrderModelPresenter presenter, String shoppingCartArrayListJsonString) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        ArrayList<ShoppingCart> shoppingCarts = new ArrayList<>();

        Gson gson = new Gson();
        try {
            shoppingCarts =
                    gson.fromJson(
                            shoppingCartArrayListJsonString,
                            new TypeToken<ArrayList<ShoppingCart>>() {}.getType()
                    );

            Utils.Log(TAG, "ArrayList of ShoppingCart size: " + shoppingCarts.size());

        } catch (JsonParseException e) {
            e.printStackTrace();
        }

        this.presenter.onShoppingCartsParseResponse(shoppingCarts);
    }

    @Override
    public void patchOrderDetailsStatus(IShoppingOrderModelPresenter presenter, long orderDetailsId, int orderStatus) {

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.presenter.updateViewState(UIState.LOADING);

        Map<String, String> postBody = new HashMap<>();
        postBody.put("order_status", String.valueOf(orderStatus));

        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
            // noinspection unchecked
            req.patchOrderDetailsStatus(
                    orderDetailsId,
                    PokenCredentials.getInstance().getCredentialHashMap(),
                    postBody)
                    .enqueue(this);
        }

    }

    @Override
    public void onSuccess(Response response) {

        Utils.Log(TAG, "Success response: " + response.toString());

        Object o = response.body();

        presenter.updateViewState(UIState.FINISHED);

        if (o instanceof ShoppingOrderDataRes) {

            ArrayList<ShoppingOrder> shoppingOrders = new ArrayList<>();
            shoppingOrders.addAll(((ShoppingOrderDataRes) response.body()).results);
            presenter.onShoppingOrderDataResponse(shoppingOrders);

        } else if (o instanceof ShoppingOrder) {

            ShoppingOrder shoppingOrder = (ShoppingOrder) o;

            Utils.Log(TAG, "Order detail (single) : " + shoppingOrder);

            presenter.onOrderDetailResponse(shoppingOrder);

        } else if (o instanceof AddressBook) {

            AddressBook addressBook = (AddressBook) o;
            Utils.Logs('i', TAG, "Created address book: " + addressBook.toString());
            presenter.onAddressBookCreated(addressBook);

        } else if (o instanceof AddressBookDataRes) {

            AddressBookDataRes addressBookDataRes = (AddressBookDataRes) o;
            Utils.Logs('v', TAG, "All address book res size: " + addressBookDataRes.results.size());
            presenter.onAddressBookContentResponse(addressBookDataRes.results);

        } else if (o instanceof OrderDetail) {

            OrderDetail orderDetail = (OrderDetail) o;
            Utils.Logs('v', TAG, "New/Updated order details. Address book id: " + orderDetail.address_book_id);
            presenter.onOrderDetailCreatedOrUpdated(orderDetail);

        } else if (o instanceof ShoppingOrderInserted) {
            ShoppingOrderInserted shoppingOrderInserted = (ShoppingOrderInserted) o;
            Utils.Logs('v', TAG, "New ordered product: " + shoppingOrderInserted);

            // Load actual ordered data
            presenter.onOrderedProductInserted(shoppingOrderInserted);
        }
    }

    @Override
    public void onMessage(String msg, int status) {
        Utils.Logs('v', TAG, "Message from network req: " + msg);
        Utils.Logs('v', TAG, "Status from network req: " + status);
        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            presenter.updateViewState(UIState.ERROR);
        }
        presenter.onNetworkMessage(msg, status);
    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
