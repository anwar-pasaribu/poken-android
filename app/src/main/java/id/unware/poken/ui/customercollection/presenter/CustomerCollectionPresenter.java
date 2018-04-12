package id.unware.poken.ui.customercollection.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.CustomerCollection;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.customercollection.model.ICustomerCollectionModel;
import id.unware.poken.ui.customercollection.view.ICustomerCollectionView;

/**
 * @author Anwar Pasaribu
 * @since Jun 12 2017
 */

public class CustomerCollectionPresenter implements ICustomerCollectionPresenter, ICustomerCollectionModelPresenter {

    private static final String TAG = "CustomerCollectionPresenter";

    private final ICustomerCollectionModel model;
    private final ICustomerCollectionView view;

    public CustomerCollectionPresenter(ICustomerCollectionModel model, ICustomerCollectionView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void getCustomerCollectionData() {
        this.model.requestCustomerCollectionData(this);
    }

    @Override
    public void startDetailScreen(CustomerCollection customerCollection) {
        Utils.Log(TAG, "Show collection detail. ID: " + customerCollection.id);
        view.openDetail(customerCollection);
    }

    @Override
    public void removeCollection(CustomerCollection customerCollection) {
        // TODO Add user confirmation
    }

    @Override
    public void updateViewState(UIState uiState) {
        view.showViewState(uiState);
    }

    @Override
    public void onCustomerCollectionDataResponse(ArrayList<CustomerCollection> customerCollections) {
        view.populateCollectionList(customerCollections);
    }
}
