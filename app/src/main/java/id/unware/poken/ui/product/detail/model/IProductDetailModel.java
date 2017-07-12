package id.unware.poken.ui.product.detail.model;

import java.util.ArrayList;

import id.unware.poken.domain.Shipping;
import id.unware.poken.ui.product.detail.presenter.IProductDetailModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public interface IProductDetailModel {

    ArrayList<Shipping> getShippingOptions();

    void requestProductData(long productId, IProductDetailModelPresenter presenter);

    void postProductToShoppingCart(long shippingOptionId, long productId, IProductDetailModelPresenter presenter);

    void loadShippingOptions();

    boolean isCodAvailable();
}
