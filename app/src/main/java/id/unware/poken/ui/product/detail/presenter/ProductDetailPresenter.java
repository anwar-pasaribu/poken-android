package id.unware.poken.ui.product.detail.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.product.detail.model.IProductDetailModel;
import id.unware.poken.ui.product.detail.view.IProductDetailView;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public class ProductDetailPresenter implements IProductDetailPresenter, IProductDetailModelPresenter {

    private final String TAG = "ProductDetailPresenter";

    private final IProductDetailModel model;
    private final IProductDetailView view;

    private boolean isContinueShopping = true;
    private boolean isSoldOut = false;


    public ProductDetailPresenter(IProductDetailModel model, IProductDetailView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void getProductData(long productId) {
        model.requestProductData(productId, this);
    }

    @Override
    public void getShippingOptionData(long productId) {
        model.loadShippingOptions();
    }

    @Override
    public void onBuyNow(long shippingOptionId, long productId, boolean continueShopping) {
        Utils.Log(TAG, "Buy now on product id:" + String.valueOf(productId));
        this.isContinueShopping = continueShopping;

        model.postProductToShoppingCart(shippingOptionId, productId, this, true /* TRUE Continue shopping*/);
    }

    @Override
    public void startShippingOptionsScreen() {
        Utils.Log(TAG, "Start shipping options screen");
        boolean isCod = model.isCodAvailable();
        view.showShippingOptionsScreen(isCod, model.getShippingOptions());
    }

    @Override
    public void startShoppingCartScreen(ShoppingCart shoppingCart) {
        view.showShoppingCartScreen(shoppingCart);
    }

    @Override
    public void startNewShoppingCartItemScreen(Product product) {
        if (PokenCredentials.getInstance().getCredentialHashMap() !=  null) {
            view.showAddNewShoppingCartItem(product);
        } else {
            startLogin();
        }
    }

    @Override
    public void startSellerScreen() {
        view.openSellerScreen();
    }

    @Override
    public void onShopMoreClicked() {
        view.openHomePage();
    }

    @Override public void prepareEditModePage() {
        view.showEditProductButton();

    }

    @Override
    public void updateViewState(UIState uiState) {

        if (view.isActivityFinishing()) return;

        view.showViewState(uiState);

        // Make sure sold out view is visible
        // after network calls finished.
        view.showSoldOutView(this.isSoldOut);
    }

    @Override
    public void onProductDetailDataResponse(Product product) {

        if (view.isActivityFinishing()) return;

        view.setCurrentProduct(product);

        view.populateProductImage(product.images);

        view.updateProductPrice(
                StringUtils.formatCurrency(String.valueOf(product.price))
        );

        if (product.is_discount && product.discount_amount > 0D) {
            view.showSaleProduct(product);
        }

        view.populateProductGeneralInfo(product);

        // Show out of stock info
        this.isSoldOut = (product.stock <= 0);
        view.showSoldOutView(this.isSoldOut);
    }

    @Override
    public void onShoppingCartCreateOrUpdateResponse(ShoppingCart cart) {

        if (view.isActivityFinishing()) return;

        if (isContinueShopping) {

            view.showShoppingCartScreen(cart);

        }
    }

    @Override
    public void onShippingOptionListResponse(ArrayList<Shipping> shippings) {

        if (view.isActivityFinishing()) return;

        view.showDefaultShippingOption(shippings.get(0));
    }

    @Override
    public void startLogin() {
        view.showLoginScreen();
    }
}
