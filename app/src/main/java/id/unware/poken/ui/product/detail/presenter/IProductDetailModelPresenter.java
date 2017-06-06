package id.unware.poken.ui.product.detail.presenter;

import id.unware.poken.domain.Product;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public interface IProductDetailModelPresenter extends BasePresenter {
    void onProductDetailDataResponse(Product product);
}
