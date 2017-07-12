package id.unware.poken.ui.product.detail.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public interface IProductDetailModelPresenter extends BasePresenter {
    void onProductDetailDataResponse(Product product);

    void onShoppingCartCreateOrUpdateResponse(ShoppingCart cart);

    void onShippingOptionListResponse(ArrayList<Shipping> shippings);
}
