package id.unware.poken.ui.product.detail.presenter;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
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
    public void onBuyNow(long shippingOptionId, long productId) {
        Utils.Log(TAG, "Buy now on product id:" + String.valueOf(productId));
        model.postProductToShoppingCart(shippingOptionId, productId, this);
    }

    @Override
    public void startShippingOptionsScreen() {
        Utils.Log(TAG, "Start shipping options screen");
        boolean isCod = model.isCodAvailable();
        view.showShippingOptionsScreen(isCod, model.getShippingOptions());
    }

    @Override
    public void updateViewState(UIState uiState) {
        view.showViewState(uiState);
    }

    @Override
    public void onProductDetailDataResponse(Product product) {
        view.populateProductImage(product.images);

        view.updateProductPrice(
                StringUtils.formatCurrency(String.valueOf(product.price))
        );

        view.populateProductGeneralInfo(product);

    }

    @Override
    public void onShoppingCartCreateOrUpdateResponse(ShoppingCart cart) {
        view.showShoppingCartScreen(cart);
    }

    @Override
    public void onShippingOptionListResponse(ArrayList<Shipping> shippings) {
        view.showDefaultShippingOption(shippings.get(0));
    }
}
