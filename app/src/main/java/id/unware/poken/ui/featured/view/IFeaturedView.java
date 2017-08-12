package id.unware.poken.ui.featured.view;

import java.util.ArrayList;

import id.unware.poken.domain.Featured;
import id.unware.poken.domain.Product;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Aug 12 2017
 */

public interface IFeaturedView extends BaseView {
    void setupFeaturedView(Featured featured);
    void populateFeaturedRelatedProducts(ArrayList<Product> related_products);
    void showProductDetail(Product product);
}
