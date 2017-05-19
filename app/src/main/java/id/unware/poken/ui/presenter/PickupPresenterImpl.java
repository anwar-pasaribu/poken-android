package id.unware.poken.ui.presenter;
import java.util.ArrayList;

import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoPickUpConfirmation;
import id.unware.poken.pojo.PojoPickupBook;
import id.unware.poken.pojo.PojoRequestPickup;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.pickup.model.GeocodingModel;
import id.unware.poken.ui.pickup.model.IPickupModelPresenter;
import id.unware.poken.ui.pickup.view.PickupView;

/**
 * @author Anwar Pasaribu
 * @since Jan 25 2017
 */

public class PickupPresenterImpl implements PickupPresenter, IPickupModelPresenter {

    private final String TAG = "PickupPresenterImpl";

    private final PickupInteractor mInteractor;
    private final PickupView mView;

    public PickupPresenterImpl(PickupView mView, GeocodingModel geocodingModelInstance) {
        this.mInteractor = new PickupInteractor(this, geocodingModelInstance);
        this.mView = mView;
    }

    @Override
    public void requestAddress(double lat, double lon) {
        Utils.Log(TAG, "Request Geocoding address for lat: " + lat + ", lon: " + lon);

        // Request java geocoding
        this.mView.showAddressLoadingIndicator(true);  // Trigger loading indicator on view
        this.mInteractor.reverseGeocoding(lat, lon);

    }

    @Override
    public void requestPickup() {
        Utils.Log(TAG, "Request pickup.");
        if (mView.isPickupReady()) {
            mInteractor.beginRequestPickup(mView.getParentView(), mView.getPickupRequestData());
        }
    }

    @Override
    public void confirmPickupRequest(PojoPickUpConfirmation confirmation, boolean isConfirm) {
        Utils.Log(TAG, "Confirm request pickup.");
        mInteractor.beginPickupConfirmation(mView.getParentView(), confirmation, isConfirm);
    }

    @Override
    public void requestVendorList() {
        mInteractor.requestLocalVendorData();
    }

    @Override
    public void onVendorListResponse(ArrayList<PojoCourier> vendors) {
        Utils.Log(TAG, "Vendor list size: " + vendors.size());
        mView.setupCourierOption(vendors);
    }

    @Override
    public void onJavaGeocodingFailure(double failedLat, double failedLon) {
        // Re-request Http Geocoding when java geocoding failed
        mInteractor.reverseGeocodingHttp(mView.getParentView(), failedLat, failedLon);
    }

    @Override
    public void onGeocodingResponse(PojoRequestPickup pojoRequestPickup) {

        String strAddress = pojoRequestPickup.getFormattedAddress();

        Utils.Log(TAG, "Response address: \"" + strAddress + "\"");

        if (!Utils.isEmpty(strAddress)) {
            // Show address on view when address available
            this.mView.showAddress(pojoRequestPickup);
        } else {
            this.mView.showNoAddressFound();
        }
    }

    @Override
    public void onPickupRequestResponse(PojoPickUpConfirmation pickUpConfirmation) {
        mView.showPickupConfirmation(pickUpConfirmation);
    }

    @Override
    public void onPickupConfirmationResponse(PojoPickupBook pickupBook) {
        mView.showPickupConfirmationResult(pickupBook);
    }

    @Override
    public void restoreAddress(double lat, double lon) {
        Utils.Log(TAG, "Restore map and move camera to pos. lat: " + lat + ", lon: " + lon);
    }

    @Override
    public void updateViewState(UIState uiState) {
        this.mView.showViewState(uiState);
    }
}
