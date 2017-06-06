package id.unware.poken.ui.product.detail.view;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductImage;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public interface IProductDetailView extends BaseView {
    void populateProductImage(ArrayList<ProductImage> productImages);
    void populateProductGeneralInfo(Product product);
    void updateProductPrice(String formattedPrice);
}
