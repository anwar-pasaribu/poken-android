package id.unware.poken.ui.featured.model;

import id.unware.poken.ui.featured.presenter.IFeaturedModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Aug 12 2017
 */

public interface IFeaturedModel {
    void requestFeaturedItemDetail(IFeaturedModelPresenter presenter, long featuredId);
}
