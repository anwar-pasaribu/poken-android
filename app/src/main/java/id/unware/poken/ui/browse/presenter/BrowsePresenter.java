package id.unware.poken.ui.browse.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.pojo.UIState;
import id.unware.poken.ui.browse.model.IBrowseModel;
import id.unware.poken.ui.browse.view.IBrowseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 18 2017
 */

public class BrowsePresenter implements IBrowsePresenter, IBrowseModelPresenter {

    private static final String TAG = "BrowsePresenter";

    private final IBrowseModel model;
    private final IBrowseView view;

    public BrowsePresenter(IBrowseModel model, IBrowseView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void getProductDataByIntentId(int actionId) {
        model.requestSellerData(this, actionId);
    }

    @Override
    public void startProductDetail(Product product) {
        view.showProductDetail(product);
    }

    @Override
    public void onProductsResponse(ArrayList<Product> products) {
        view.pupolateSellerProductList(products);
    }

    @Override
    public void updateViewState(UIState uiState) {
        view.showViewState(uiState);
    }
}
