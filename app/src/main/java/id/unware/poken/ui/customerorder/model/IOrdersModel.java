package id.unware.poken.ui.customerorder.model;

import id.unware.poken.ui.customerorder.presenter.IOrdersModelPresenter;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public interface IOrdersModel {
    void requestOrdersData(IOrdersModelPresenter presenter);
}
