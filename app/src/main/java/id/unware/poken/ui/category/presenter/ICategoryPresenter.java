package id.unware.poken.ui.category.presenter;

import id.unware.poken.domain.Category;

/**
 * @author Anwar Pasaribu
 * @since Aug 13 2017
 */

public interface ICategoryPresenter {
    void loadCategoryList();
    void onCategoryClick(int position, Category category);
}
