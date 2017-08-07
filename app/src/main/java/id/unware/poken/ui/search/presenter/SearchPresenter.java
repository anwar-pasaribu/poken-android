package id.unware.poken.ui.search.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.pojo.UIState;
import id.unware.poken.ui.search.model.ISearchModel;
import id.unware.poken.ui.search.view.ISearchView;

/**
 * @author Anwar Pasaribu
 * @since Aug 06 2017
 */

public class SearchPresenter implements ISearchPresenter, ISearchModelPresenter {

    final private ISearchModel model;
    final private ISearchView view;

    public SearchPresenter(ISearchModel model, ISearchView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void beginSearch(String query) {
        model.searchProductByQuery(query, this);
    }

    @Override
    public void startProductDetail(Product product) {
        view.showProductDetail(product);
    }

    @Override
    public void updateViewState(UIState uiState) {
        view.showViewState(uiState);
    }

    @Override
    public void onProductsResponse(ArrayList<Product> products) {
        view.pupulateProductSearchRes(products);
    }
}
