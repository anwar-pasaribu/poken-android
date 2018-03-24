package id.unware.poken.ui.product.detail.presenter;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.ShoppingCart;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public interface IProductDetailPresenter {
    void getProductData(long productId);
    void getShippingOptionData(long productId);

    void onBuyNow(long shippingOptionId, long productId, boolean continueShopping);

    void startShippingOptionsScreen();

    void startShoppingCartScreen(ShoppingCart shoppingCart);

    void startNewShoppingCartItemScreen(Product product);

    void startSellerScreen();

    void onShopMoreClicked();

    void prepareEditModePage();
}
