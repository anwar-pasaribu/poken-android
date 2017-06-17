package id.unware.poken.ui.customersubscription.view;

import java.util.ArrayList;

import id.unware.poken.domain.CustomerSubscription;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 13 2017
 */

public interface ICustomerSubscriptionView extends BaseView {
    void openDetail(CustomerSubscription subscription);
    void populateSubscriptionList(ArrayList<CustomerSubscription> subscriptions);

    void unsubscribe(CustomerSubscription subscription);
}
