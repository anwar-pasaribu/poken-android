package id.unware.poken.ui.pickup.model;


import java.util.ArrayList;

import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoPickUpConfirmation;
import id.unware.poken.pojo.PojoPickupBook;
import id.unware.poken.pojo.PojoRequestPickup;
import id.unware.poken.ui.presenter.BasePresenter;

/**
 * @author Anwar Pasaribu
 * @since Mar 14 2017
 */

public interface IPickupModelPresenter extends BasePresenter {

    void onVendorListResponse(ArrayList<PojoCourier> vendors);

    void onJavaGeocodingFailure(double failedLat, double failedLon);
    void onGeocodingResponse(PojoRequestPickup pojoRequestPickup);
    void onPickupRequestResponse(PojoPickUpConfirmation pickUpConfirmation);
    void onPickupConfirmationResponse(PojoPickupBook pickupBook);
}
