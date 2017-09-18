package id.unware.poken.ui.search.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.search.model.ISearchModel;
import id.unware.poken.ui.search.view.ISearchView;

/**
 * @author Anwar Pasaribu
 * @since Aug 06 2017
 */

public class SearchPresenter implements ISearchPresenter, ISearchModelPresenter {

    private static final String TAG = "SearchPresenter";

    final private ISearchModel model;
    final private ISearchView view;
    private boolean isMoreContentAvailable = false;
    private boolean isLoadMore = false;

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
    public void loadMoreSearchResult(String submittedQuery, int nextPage) {

        this.isLoadMore = true;

        if (isMoreContentAvailable) {
            model.loadMoreSearchedProductQuery(submittedQuery, nextPage, this);
        } else {
            Utils.Log(TAG, "Last page reached...");
        }
    }

    @Override
    public void updateViewState(UIState uiState) {

        if (view.isActivityFinishing()) return;

        view.showViewState(uiState);
    }

    @Override
    public void onProductsResponse(ArrayList<Product> products) {

        if (view.isActivityFinishing()) return;

        if (!isLoadMore) {
            view.pupulateProductSearchRes(products);
        } else {
            view.appendProductList(products);
        }
    }

    @Override
    public void onNextProductPage(String nextPage) {
        Utils.Log(TAG, "Next page URL: " + String.valueOf(nextPage));
        this.isMoreContentAvailable = !StringUtils.isEmpty(nextPage);
    }
}
