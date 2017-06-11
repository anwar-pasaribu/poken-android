package id.unware.poken.ui.customerorder.view;

import java.util.ArrayList;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public interface IOrdersView extends BaseView {

    void populateOrdersList(ArrayList<ShoppingOrder> orders);

    void openOrderDetail(ShoppingOrder shoppingOrder);
}
