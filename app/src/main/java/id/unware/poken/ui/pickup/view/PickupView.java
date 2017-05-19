package id.unware.poken.ui.pickup.view;

import android.view.View;

import java.util.ArrayList;

import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoPickUpConfirmation;
import id.unware.poken.pojo.PojoPickupBook;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.pojo.PojoRequestPickup;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Jan 25 2017
 */

public interface PickupView extends BaseView {

    void setupCourierOption(ArrayList<PojoCourier> vendors);

    void showAddress(PojoRequestPickup pojoRequestPickup);
    void showNoAddressFound();

    void showPickupConfirmation(PojoPickUpConfirmation pickUpConfirmation);
    void showPickupConfirmationResult(PojoPickupBook pickupConfirmationResult);

    void showAddressLoadingIndicator(boolean isShow);

    // Show or hide Address info (bottom sheet)
    void toggleAddressInfo(boolean showOrHide);

    View getParentView();

    PojoPickupHistory getPickupRequestData();
    boolean isPickupReady();

}
