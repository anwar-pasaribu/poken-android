package id.unware.poken.ui.browse.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 17 2017
 */

public interface IBrowseModelPresenter extends BasePresenter{
    void onProductsResponse(ArrayList<Product> products);

    void onNextProductPage(String nextPage);
}
