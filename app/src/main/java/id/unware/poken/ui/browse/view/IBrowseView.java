package id.unware.poken.ui.browse.view;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 17 2017
 */

public interface IBrowseView extends BaseView {
    void pupolateSellerProductList(ArrayList<Product> products);

    void showProductDetail(Product product);
}
