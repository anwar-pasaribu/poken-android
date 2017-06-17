package id.unware.poken.ui.customercollection.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.CustomerCollection;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public interface ICustomerCollectionModelPresenter extends BasePresenter {
    void onCustomerCollectionDataResponse(ArrayList<CustomerCollection> customerCollections);
}
