package id.unware.poken.ui.home.presenter;

import id.unware.poken.domain.Product;
import id.unware.poken.domain.Section;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public interface IHomePresenter {
    void getHomeData();
    void onSectionActionClick(int position, Section section);

    void onProductClick(int position, Product product);
}
