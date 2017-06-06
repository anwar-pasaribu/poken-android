package id.unware.poken.ui.product.detail.model;

import id.unware.poken.ui.product.detail.presenter.IProductDetailModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public interface IProductDetailModel {
    void requestProductData(long productId, IProductDetailModelPresenter presenter);
}
