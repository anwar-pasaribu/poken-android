package id.unware.poken.ui.home.presenter;

import id.unware.poken.domain.HomeDataRes;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public interface IHomeModelPresenter extends BasePresenter {
    void onHomeDataResponse(HomeDataRes homeDataRes);

    void showMessage(String msg, int msgStatus);
}
