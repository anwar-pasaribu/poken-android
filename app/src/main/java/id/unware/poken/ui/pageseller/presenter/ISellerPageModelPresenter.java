package id.unware.poken.ui.pageseller.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.Seller;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 17 2017
 */

public interface ISellerPageModelPresenter extends BasePresenter {
    void onSellerPageContentResponse(ArrayList<Product> products);
    void setupSellerInfo(Seller seller);

    void onSuscriptionSuccess(boolean isSubscribe);

    void onNextProductPage(String next);
}
