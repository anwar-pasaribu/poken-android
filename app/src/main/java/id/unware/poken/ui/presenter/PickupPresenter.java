package id.unware.poken.ui.presenter;

import id.unware.poken.pojo.PojoPickUpConfirmation;

/**
 * @author Anwar Pasaribu
 * @since Jan 25 2017
 */

public interface PickupPresenter {
    void confirmPickupRequest(PojoPickUpConfirmation confirmation, boolean isConfirm);

    void requestVendorList();

    void requestAddress(double lat, double lon);
    void requestPickup();

    void restoreAddress(double lat, double lon);  // Restore address to previously picked.
}
