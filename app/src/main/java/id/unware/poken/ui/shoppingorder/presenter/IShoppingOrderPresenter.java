package id.unware.poken.ui.shoppingorder.presenter;

import id.unware.poken.domain.AddressBook;

/**
 * @author Anwar Pasaribu
 * @since Jun 06 2017
 */

public interface IShoppingOrderPresenter {
    void addNewAddressBook(AddressBook addressBook);
    void getAddressBookData();  // Started when user click "UBAH"
    void getShoppingOrderData(long orderedProductId);

    void startPaymentScreen();

    void startAddressBookScreen(boolean isAddressBookAvailable);

    void startSelectedProductScreen();

    void setOrderDetailsTrackingId(long orderDetailsId, String trackingId);

    void createOrUpdateOrderDetail(long[] selectedShoppingCartIds, AddressBook addressBook);

    void prepareOrderFromShoppingCart(String shoppingCartArrayListJsonString);

    void confirmOrderReceived(long orderDetailsId);

    void retureOrder(long orderDetailsId);

    void beginOrder();

    void setupSellerMode(boolean isSellerMode);

    void sendPackage(long orderDetailsId);
}
