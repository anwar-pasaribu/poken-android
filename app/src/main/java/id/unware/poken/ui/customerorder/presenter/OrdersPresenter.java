package id.unware.poken.ui.customerorder.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.customerorder.model.IOrdersModel;
import id.unware.poken.ui.customerorder.view.IOrdersView;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class OrdersPresenter implements IOrdersPresenter, IOrdersModelPresenter {

    private final static String TAG = "OrdersPresenter";

    private final IOrdersModel model;
    private final IOrdersView view;

    public OrdersPresenter(IOrdersModel model, IOrdersView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void getOrdersData() {
        model.requestOrdersData(this);
    }

    @Override
    public void startOrderDetailScreen(ShoppingOrder shoppingOrder) {
        view.openOrderDetail(shoppingOrder);
    }

    @Override
    public void updateViewState(UIState uiState) {
        Utils.Logs('w', TAG, "Update view state: " + uiState);
        view.showViewState(uiState);
    }

    @Override
    public void onOrdersDataResponse(ArrayList<ShoppingOrder> shoppingOrders) {
        Utils.Logs('i', TAG, "onShoppingOrderDataResponse. Size: " + shoppingOrders.size());

        if (shoppingOrders.size() > 0) {

            view.populateOrdersList(shoppingOrders);
        }
    }
}
