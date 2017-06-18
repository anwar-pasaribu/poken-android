package id.unware.poken.ui.home.view;

import java.util.ArrayList;

import id.unware.poken.domain.Featured;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Section;
import id.unware.poken.domain.Seller;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public interface IHomeView extends BaseView {
    void populateHomeView(ArrayList<Featured> featured_items, ArrayList<Section> sections);

    void startProductCategoryScreen(Section sectionItem);

    void startProductDetailScreen(Product product);

    void startSellerDetailScreen(int position, Seller seller);
}
