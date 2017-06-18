package id.unware.poken.ui.seller.view;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.Seller;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 17 2017
 */

public interface ISellerPageView extends BaseView {
    void pupolateSellerProductList(ArrayList<Product> products);

    void showProductDetail(Product product);

    void showSellerInfo(Seller seller);
}
