package id.unware.poken.ui.browse.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Category;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Seller;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.browse.model.IBrowseModel;
import id.unware.poken.ui.browse.view.IBrowseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 18 2017
 */
public class BrowsePresenter implements IBrowsePresenter, IBrowseModelPresenter {

    private static final String TAG = "BrowsePresenter";

    private boolean isLoadMore = false;

    private final IBrowseModel model;
    private final IBrowseView view;

    private boolean isMoreContentAvailable = false;
    private int nextPage;

    public BrowsePresenter(IBrowseModel model, IBrowseView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void getProductDataByIntentId(int actionId) {

        // Make false to prevent append data again
        this.isLoadMore = false;

        model.requestSellerData(this, actionId);
    }

    @Override
    public void getSellerList() {

        this.isLoadMore = false;

        model.requestSellerListData(this);

    }

    @Override
    public void getProductByCategory(Category category) {

        // Make false to prevent append data again
        this.isLoadMore = false;

        model.requestSellerDataByCategory(this, category);
    }

    @Override
    public void startProductDetail(Product product) {
        view.showProductDetail(product);
    }

    @Override
    public void getMoreProductByCategory(Category category, int nextPage) {

        this.isLoadMore = true;

        if (this.nextPage != Constants.STATE_NODATA) {
            model.requestMoreProductByCategory(this, category, this.nextPage);
        } else {
            Utils.Log(TAG, "Last page reached...");
        }
    }

    @Override
    public void getMoreSellerData(int page) {
        this.isLoadMore = true;

        if (this.nextPage != Constants.STATE_NODATA) {
            model.requestMoreSellerData(this, this.nextPage);
        } else {
            Utils.Log(TAG, "Last page reached...");
        }
    }

    @Override
    public void getMoreProductDataByIntentId(int actionId, int page) {

        this.isLoadMore = true;

        if (this.nextPage != Constants.STATE_NODATA) {
            model.requestMoreProductsByIntentId(this, actionId, this.nextPage);
        } else {
            Utils.Log(TAG, "Last page reached...");
        }
    }

    @Override
    public void onSellerClick(int position, Seller seller) {
        view.showSellerDetail(position, seller);
    }

    @Override
    public void onProductsResponse(ArrayList<Product> products) {

        if (view.isActivityFinishing()) return;

        if (!isLoadMore) {
            Utils.Log(TAG, "Initial product list response. Size: " + products.size());
            view.pupolateSellerProductList(products);
        } else {
            Utils.Log(TAG, "More item response. Size: " + products.size());
            view.appendProductList(products);
        }
    }

    @Override
    public void onNextProductPage(String nextPageUrl, int nextPage) {
        Utils.Log(TAG, "Next page URL: " + String.valueOf(nextPageUrl) + ", next page num: " + nextPage);
        this.isMoreContentAvailable = !StringUtils.isEmpty(nextPageUrl);

        this.nextPage = nextPage;
    }

    @Override
    public void onNextSellerListPage(String nextPage, int nextPageNumber) {
        Utils.Log(TAG, "Next seller list page URL: " + String.valueOf(nextPage) + ", next page num: " + nextPage);
        this.isMoreContentAvailable = !StringUtils.isEmpty(nextPage);

        this.nextPage = nextPageNumber;
    }

    @Override
    public void onSellerListResponse(ArrayList<Seller> sellers) {
        if (view.isActivityFinishing()) return;

        if (!isLoadMore) {
            Utils.Log(TAG, "Initial sellers list response. Size: " + sellers.size());
            view.pupolateSellerList(sellers);
        } else {
            Utils.Log(TAG, "More sellers response. Size: " + sellers.size());
            view.appendSellerList(sellers);
        }
    }

    @Override
    public void updateViewState(UIState uiState) {

        if (view.isActivityFinishing()) return;

        view.showViewState(uiState);
    }
}
