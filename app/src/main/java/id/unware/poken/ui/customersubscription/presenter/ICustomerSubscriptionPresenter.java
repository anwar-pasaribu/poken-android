package id.unware.poken.ui.customersubscription.presenter;

import id.unware.poken.domain.CustomerSubscription;

/**
 * @author Anwar Pasaribu
 * @since Jun 13 2017
 */

public interface ICustomerSubscriptionPresenter {
    void getCustomerSubscriptionData();

    void startDetailScreen(CustomerSubscription customerSubscription);

    void unsubscribe(CustomerSubscription customerSubscription);
}
