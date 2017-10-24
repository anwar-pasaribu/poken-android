package id.unware.poken.ui.home.view;

import java.util.ArrayList;

import id.unware.poken.domain.Category;
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

    void showMessage(String msg, int status);

    void populateHomeView(ArrayList<Featured> featured_items, ArrayList<Section> sections);

    void showBrowseSeller(Section section);

    void startProductCategoryScreen(Section sectionItem);

    void showCategoryDetailScreen(Category category);

    void showProductDetailScreen(Product product);

    void showSellerDetailScreen(int position, Seller seller);

    void showFeaturedScreen(int position, Featured featured);

    void showPokenInstagram();

    void showPokenFacebookPage();

    void showPokenPhoneContact();

}
