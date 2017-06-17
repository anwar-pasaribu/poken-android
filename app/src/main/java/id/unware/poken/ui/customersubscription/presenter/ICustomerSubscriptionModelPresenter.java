package id.unware.poken.ui.customersubscription.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.CustomerSubscription;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 13 2017
 */

public interface ICustomerSubscriptionModelPresenter extends BasePresenter {
    void onCustomerSubscriptionDataResponse(ArrayList<CustomerSubscription> customerSubscriptions);
}
