package id.unware.poken.ui.category.view;

import java.util.ArrayList;

import id.unware.poken.domain.Category;
import id.unware.poken.domain.FeaturedCategoryProduct;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Aug 13 2017
 */

public interface ICategoryView extends BaseView {
    void pupulateCategories(ArrayList<Category> categories);

    void showCategoryDetail(Category category);

    void showMessage(String msg, int status);

    void pupulateFeaturedCategories(ArrayList<FeaturedCategoryProduct> featuredCategoryProducts);
}
