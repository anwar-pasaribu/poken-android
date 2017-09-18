package id.unware.poken.ui.search.view;

import java.util.ArrayList;

import id.unware.poken.domain.Product;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Aug 06 2017
 */

public interface ISearchView extends BaseView {
    void pupulateProductSearchRes(ArrayList<Product> products);

    void showProductDetail(Product product);

    void appendProductList(ArrayList<Product> products);
}
