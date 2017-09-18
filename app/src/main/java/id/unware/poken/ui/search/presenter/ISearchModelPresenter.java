package id.unware.poken.ui.search.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 17 2017
 */

public interface ISearchModelPresenter extends BasePresenter{
    void onProductsResponse(ArrayList<Product> products);

    void onNextProductPage(String next);
}
