package id.unware.poken.ui.seller.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.pojo.UIState;
import id.unware.poken.ui.seller.model.ISellerPageModel;
import id.unware.poken.ui.seller.view.ISellerPageView;

/**
 * @author Anwar Pasaribu
 * @since Jun 17 2017
 */

public class SellerPagePresenter implements ISellerPagePresenter, ISellerPageModelPresenter {

    private final ISellerPageModel model;
    private final ISellerPageView view;

    public SellerPagePresenter(ISellerPageModel model, ISellerPageView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void getSellerPageProductData() {
        model.requestSellerData(this);
    }

    @Override
    public void startProductDetail(Product product) {
        view.showProductDetail(product);
    }

    @Override
    public void startDetailScreen(Product product) {
        view.showProductDetail(product);
    }

    @Override
    public void onSellerPageContentResponse(ArrayList<Product> products) {
        view.pupolateSellerProductList(products);
    }

    @Override
    public void updateViewState(UIState uiState) {
        view.showViewState(uiState);
    }
}
