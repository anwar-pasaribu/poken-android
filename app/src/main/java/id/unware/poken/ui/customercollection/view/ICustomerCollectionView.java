package id.unware.poken.ui.customercollection.view;

import java.util.ArrayList;

import id.unware.poken.domain.CustomerCollection;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public interface ICustomerCollectionView extends BaseView {

    void populateCollectionList(ArrayList<CustomerCollection> collections);

    void openDetail(CustomerCollection collection);
}
