package id.unware.poken.ui.customersubscription.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.CustomerSubscription;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.customersubscription.model.ICustomerSubscriptionModel;
import id.unware.poken.ui.customersubscription.view.ICustomerSubscriptionView;

/**
 * @author Anwar Pasaribu
 * @since Jun 13 2017
 */

public class CustomerSubscriptionPresenter implements ICustomerSubscriptionPresenter,
        ICustomerSubscriptionModelPresenter {

    private static final String TAG = "CustomerSubscriptionPresenter";

    private final ICustomerSubscriptionModel model;
    private final ICustomerSubscriptionView view;

    public CustomerSubscriptionPresenter(ICustomerSubscriptionModel model, ICustomerSubscriptionView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void getCustomerSubscriptionData() {
        this.model.requestCustomerSubscriptionData(this);
    }

    @Override
    public void startDetailScreen(CustomerSubscription customerSubscription) {
        Utils.Log(TAG, "Show Subscription detail. ID: " + customerSubscription.id);
        view.openDetail(customerSubscription);
    }

    @Override
    public void unsubscribe(CustomerSubscription customerSubscription) {
        Utils.Log(TAG, "Unsubscribe. ID: " + customerSubscription.id);
        view.unsubscribe(customerSubscription);
    }

    @Override
    public void updateViewState(UIState uiState) {
        view.showViewState(uiState);
    }

    @Override
    public void onCustomerSubscriptionDataResponse(ArrayList<CustomerSubscription> customerSubscriptions) {
        view.populateSubscriptionList(customerSubscriptions);
    }
}
