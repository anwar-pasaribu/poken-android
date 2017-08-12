package id.unware.poken.ui.featured.presenter;

import id.unware.poken.domain.Product;

/**
 * @author Anwar Pasaribu
 * @since Aug 12 2017
 */

public interface IFeaturedPresenter {
    void reqFeaturedDetail(long featuredId);

    void startProductDetail(Product product);
}
