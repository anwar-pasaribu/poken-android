package id.unware.poken.ui.featured.presenter;

import id.unware.poken.domain.Featured;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Aug 12 2017
 */

public interface IFeaturedModelPresenter extends BasePresenter {
    void onFeaturedItemDetailRes(Featured featured);
    void showMessage(String msg, int msgStatus);
}
