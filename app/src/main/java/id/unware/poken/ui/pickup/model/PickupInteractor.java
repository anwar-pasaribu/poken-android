package id.unware.poken.ui.pickup.model;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Locale;

import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoAddressComponent;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoGeocode;
import id.unware.poken.pojo.PojoGeocodeResult;
import id.unware.poken.pojo.PojoPickUpConfirmation;
import id.unware.poken.pojo.PojoPickupBook;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.pojo.PojoRequestPickup;
import id.unware.poken.pojo.UIState;
import id.unware.poken.services.FetchAddressIntentService;
import id.unware.poken.tools.Utils;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Anwar Pasaribu
 * @since Jan 25 2017
 *
 * @since V49 - NEW MVP
 */

public class PickupInteractor implements VolleyResultListener {
    private final String TAG = "PickupInteractor";

    private final IPickupModelPresenter mPickupPresenter;
    private final ControllerPaket mControllerPaket;

    /**
     * Tracks whether the user has requested an address. Becomes true when the user requests an
     * address and false when the address (or an error message) is delivered.
     * The user requests an address by pressing the Fetch Address button. This may happen
     * before GoogleApiClient connects. This activity uses this boolean to keep track of the
     * user's intent. If the value is true, the activity tries to fetch the address as soon as
     * GoogleApiClient connects.
     */
    private boolean mAddressRequested;

    private double mLat, mLon;

    /**
     * Receiver registered with this activity to get the response from FetchAddressIntentService.
     */
    private AddressResultReceiver mResultReceiver;

    private Context mContext;
    private Realm mRealm;


    public PickupInteractor(IPickupModelPresenter pickupModelPresenter, GeocodingModel geocodingModelInstance) {
        this.mPickupPresenter = pickupModelPresenter;
        this.mControllerPaket = ControllerPaket.getInstance();

        this.mContext = geocodingModelInstance.getContext();

        this.mRealm = Realm.getDefaultInstance();

        // Set defaults, then update using values stored in the Bundle.
        mAddressRequested = false;

        mResultReceiver = new AddressResultReceiver(new Handler());
    }

    public void requestLocalVendorData() {
        ArrayList<PojoCourier> pojoCouriers = new ArrayList<>();
        RealmResults<PojoCourier> pojoCourierRealmResults = mRealm.where(PojoCourier.class).findAll();
        for (PojoCourier pojoCourier: pojoCourierRealmResults) {
            pojoCouriers.add(pojoCourier);
        }

        Utils.Logs('i', TAG, "Pojo couriers result size: " + pojoCourierRealmResults.size());

        mPickupPresenter.onVendorListResponse(pojoCouriers);
    }

    public void beginRequestPickup(View parentView, PojoPickupHistory pickupData) {
        this.mControllerPaket.pickUp(
                parentView,
                pickupData.getLat(),
                pickupData.getLon(),
                pickupData.getAddress(),
                pickupData.getExtra_detail(),
                pickupData.getZip_code(),
                Utils.getParsedInt(pickupData.getVehicle_type()),
                pickupData.getAdministrativeArea(),
                String.valueOf(pickupData.getVendor_id()),
                this
        );
    }

    public void beginPickupConfirmation (
            View parentView,
            PojoPickUpConfirmation pickUpConfirmation,
            boolean isConfirm) {

        this.mControllerPaket.pickUpConfirm(
                parentView,
                String.valueOf(pickUpConfirmation.pickup_id),
                isConfirm? "1" : "0",
                this);
    }

    public void reverseGeocoding(double lat, double lon) {

        if (!mAddressRequested) {
            LatLng latLng = new LatLng(lat, lon);
            startIntentService(latLng);
        }

        // Save lat lon to class scope in case java client Geocoding doesn't make it
        this.mLat = lat;
        this.mLon = lon;
    }

    /**
     * Request address from http Geocoding.
     *
     * @param lat Latitude
     * @param lon Longitude
     * @since V49 - NEW MVP
     */
    public void reverseGeocodingHttp(View parentView, double lat, double lon) {
        this.mControllerPaket.geocode(parentView, lat, lon, PickupInteractor.this);
    }

    /**
     * Convert HTTP Reverse Geocoding respose to PojoRequestPickup.
     *
     * @param pojoGeocodeResult Http Geocoding response
     * @return PojoRequestPickup data.
     */
    private PojoRequestPickup convertToRequestPickupData(PojoGeocodeResult pojoGeocodeResult) {
        Address address = new Address(Locale.ENGLISH);

        String[] splitedAddress = pojoGeocodeResult.formatted_address.split(",");

        for (int i = 0; i < splitedAddress.length; i++) {
            Utils.Log(TAG, "Address log: " + splitedAddress[i]);
            address.setAddressLine(i, splitedAddress[i]);
        }

        address.setLatitude(pojoGeocodeResult.geometry.location.lat);
        address.setLongitude(pojoGeocodeResult.geometry.location.lng);
        //find zip code
        for (PojoAddressComponent address_component : pojoGeocodeResult.address_components) {
            if (address_component.types.contains("postal_code")) {
                address.setPostalCode(address_component.long_name);
            }
            if (address_component.types.contains("administrative_area_level_3")) {
                address.setLocality(address_component.long_name);
            }
            if (address_component.types.contains("administrative_area_level_2")) {
                address.setSubAdminArea(address_component.long_name);
            }
        }

        String strAddress = (address.getAddressLine(0) != null
            ? address.getAddressLine(0)
            : ""),
        strAdminArea = (address.getAddressLine(3) != null
                ? address.getAddressLine(3)
                : ""),
        strLocality = (address.getLocality() != null
            ? address.getLocality()
            : ""),
        strZipCode = (address.getPostalCode() != null
            ? address.getPostalCode()
            : "");

        PojoRequestPickup pojoRequestPickup = new PojoRequestPickup();
        pojoRequestPickup.setLatitude(address.getLatitude());
        pojoRequestPickup.setLongitude(address.getLongitude());
        pojoRequestPickup.setFormattedAddress(strAddress);
        pojoRequestPickup.setAdminArea(strAdminArea);
        pojoRequestPickup.setZipCode(strZipCode);
        pojoRequestPickup.setLocality(strLocality);

        return pojoRequestPickup;
    }

    /**
     * Convert java client Geocoding service response to PojoRequestPickup
     * @param address Address from java client Geocoding
     * @return PojoRequestPickup object.
     * @since V49 - NEW MVP
     */
    private PojoRequestPickup convertAddressToRequestPickupData(Address address) {

        String strAddress = (address.getAddressLine(0) != null
                ? address.getAddressLine(0)
                : ""),
        strAdminArea = (address.getAddressLine(3) != null
                ? address.getAddressLine(3)
                : ""),
        strLocality = (address.getLocality() != null
                ? address.getLocality()
                : ""),
        strZipCode = (address.getPostalCode() != null
                ? address.getPostalCode()
                : "");

        PojoRequestPickup pojoRequestPickup = new PojoRequestPickup();
        pojoRequestPickup.setLatitude(address.getLatitude());
        pojoRequestPickup.setLongitude(address.getLongitude());
        pojoRequestPickup.setFormattedAddress(strAddress);
        pojoRequestPickup.setAdminArea(strAdminArea);
        pojoRequestPickup.setZipCode(strZipCode);
        pojoRequestPickup.setLocality(strLocality);

        return pojoRequestPickup;
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     * @param latLng LatLon info to process.
     */
    private void startIntentService(LatLng latLng) {

        Utils.Log(TAG, "Start intent service.");

        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this.mContext, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the LatLng data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, latLng);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        if (this.mContext != null) {
            this.mContext.startService(intent);
        }

        // Set mAddressRequested to true because user already request once
        mAddressRequested = true;
    }

    //////
    // S: Volley listener
    @Override
    public void onStart(PojoBase clazz) {
        if (this.mPickupPresenter != null) {
            this.mPickupPresenter.updateViewState(UIState.LOADING);
        }
    }

    @Override
    public void onSuccess(PojoBase clazz) {

        if (clazz instanceof PojoPickupBook) {

            // Request pickup result
            this.mPickupPresenter.onPickupConfirmationResponse((PojoPickupBook) clazz);

        } else if (clazz instanceof PojoPickUpConfirmation) {

            // Check whether pickup is available
            this.mPickupPresenter.onPickupRequestResponse((PojoPickUpConfirmation) clazz);

        } else if (clazz instanceof PojoGeocode) {

            PojoGeocode pojoGeocode = (PojoGeocode) clazz;
            if (pojoGeocode.results != null && pojoGeocode.results.length > 0) {
                PojoGeocodeResult pojoGeocodeResult = pojoGeocode.results[0];

                // Convert to request pickup data model
                PojoRequestPickup pojoRequestPickup = convertToRequestPickupData(pojoGeocodeResult);
                this.mPickupPresenter.onGeocodingResponse(pojoRequestPickup);
            } else {
                this.mPickupPresenter.onGeocodingResponse(new PojoRequestPickup());
            }

        }

    }

    @Override
    public void onFinish(PojoBase clazz) {
        if (this.mPickupPresenter != null && clazz != null) {
            this.mPickupPresenter.updateViewState(UIState.FINISHED);
        }

    }

    @Override
    public boolean onError(PojoBase clazz) {
        if (this.mPickupPresenter != null) {
            this.mPickupPresenter.updateViewState(UIState.ERROR);
        }

        return false;
    }
    // S: Volley listener
    //////

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                Utils.Logs('i', TAG, "Address found.");
                // Display the address string or an error message sent from the intent service.
                Address resultObject = resultData.getParcelable(Constants.RESULT_DATA_KEY);
                PojoRequestPickup pojoRequestPickup = convertAddressToRequestPickupData(resultObject);
                PickupInteractor.this.mPickupPresenter.onGeocodingResponse(pojoRequestPickup);

            } else if (resultCode == Constants.FAILURE_RESULT) {
                Utils.Logs('e', TAG, "Java Geocoding Fail - Address not FOUND");
                PickupInteractor.this.mPickupPresenter.onJavaGeocodingFailure (
                        PickupInteractor.this.mLat,
                        PickupInteractor.this.mLon
                );
            }

            // Restore address is not requested to allow start service again
            PickupInteractor.this.mAddressRequested = false;
        }
    }
}
