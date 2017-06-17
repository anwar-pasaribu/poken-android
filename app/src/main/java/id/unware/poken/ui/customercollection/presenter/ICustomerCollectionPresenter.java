package id.unware.poken.ui.customercollection.presenter;

import id.unware.poken.domain.CustomerCollection;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public interface ICustomerCollectionPresenter {
    void getCustomerCollectionData();

    void startDetailScreen(CustomerCollection customerCollection);

    void removeCollection(CustomerCollection customerCollection);
}
