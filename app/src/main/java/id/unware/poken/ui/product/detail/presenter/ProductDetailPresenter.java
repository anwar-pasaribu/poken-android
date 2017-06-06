package id.unware.poken.ui.product.detail.presenter;

import id.unware.poken.domain.Product;
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
    public void onBuyNow(long productId) {
        Utils.Log("Buy now on product id:", String.valueOf(productId));
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
}
