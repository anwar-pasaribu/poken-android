package id.unware.poken.controller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.PokenApp;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.httpConnection.UrlComposer;
import id.unware.poken.interfaces.OnUpdateDialog;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.PojoBookingData;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoCourierLocationData;
import id.unware.poken.pojo.PojoEstimateTariffData;
import id.unware.poken.pojo.PojoGeocode;
import id.unware.poken.pojo.PojoHistoryData;
import id.unware.poken.pojo.PojoLogin;
import id.unware.poken.pojo.PojoNewPackageData;
import id.unware.poken.pojo.PojoPickUpConfirmation;
import id.unware.poken.pojo.PojoPickupBook;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.pojo.PojoPickupHistoryCancel;
import id.unware.poken.pojo.PojoPostcodeData;
import id.unware.poken.pojo.PojoWalletData;
import id.unware.poken.pojo.PojoWalletTopup;
import id.unware.poken.pojo.PojowithdrawAdd;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.MainPage.MainPage;
import id.unware.poken.ui.packages.view.FragmentDialogSearchPackage;
import id.unware.poken.ui.packages.view.FragmentPackage;
import id.unware.poken.ui.pickup.model.PickupInteractor;
import id.unware.poken.ui.pickup.view.FragmentPickupMap;
import id.unware.poken.ui.wallet.addBankAccount.model.DialogBankAccountModel;
import id.unware.poken.ui.wallet.main.model.WalletModel;
import id.unware.poken.ui.wallet.topup.model.WalletTopupModel;
import id.unware.poken.ui.wallet.withdrawal.model.WithdrawalModel;
import id.unware.poken.ui.wallet.withdrawalStatus.model.WithdrawSummaryModel;
import io.realm.Realm;


/**
 * Created by marzellaalfamega on 6/22/15.
 * Controller to request API
 */
public class ControllerPaket {

    private final String TAG = "ControllerPaket";

    private static ControllerPaket instance;
    private final PokenApp values = PokenApp.getInstance();
    private final UrlComposer urlComposer = UrlComposer.getInstance();
    private final Map<String, String> params = new HashMap<>();

    public static ControllerPaket getInstance() {
        if (instance == null) {
            instance = new ControllerPaket();
        }
        return instance;
    }

    /**
     * Clear all param.
     */
    private void clearParams() {
        params.clear();
    }

    public void login(View snackContainer, String username, String password, VolleyResultListener listener) {

        clearParams();

        Utils.Log(TAG, "Email/username: " + username + ", Password: " + password);

        if (!StringUtils.isEmpty(username)) {
            params.put("user_email", username);
        }

        if (!StringUtils.isEmpty(password)) params.put("user_password", password);
        // Begin request to server
        values.addToRequestQueue(
                snackContainer,
                urlComposer.composeLogin(),
                params,
                PojoLogin.class,
                listener,
                MainPage.class, true);
    }

    public void doSignOut(Context context, String message) {
        // Begin log out
        Utils.Log(TAG, "Log out 1");


        // [WARNING] Delete all Realm content and
        // Shared Preferences
        ControllerRealm.getInstance().removeAllData();
        SPHelper.getInstance().clearData();

        Intent intent = new Intent(context, MainPage.class);
        if (!TextUtils.isEmpty(message)) {
            Utils.Log("add message","add message to login menu");
            intent.putExtra(Constants.EXTRA_MESSAGE, message);
        }
        context.startActivity(intent);
    }


    /**
     * Login with session invoke data downloading.
     * This method accept whether download whole data or not by set {@code reloadAll} TRUE|FALSE.
     *
     * @param snackContainer View to hold snackbar view
     * @param reloadAll      true to reaload whole data.
     * @param listener       {@link VolleyResultListener} to handle server response.
     * @since Dec 15 2016 - V46 NEW
     */
    public void loginWithSession(View snackContainer, boolean reloadAll, VolleyResultListener listener) {
        clearParams();

        String token = SPHelper.getInstance().getSharedPreferences(Constants.GCM_TOKEN, "");
        String lastUpdate = SPHelper.getInstance().getSharedPreferences(Constants.SHARED_LAST_UPDATE, "");

        lastUpdate = reloadAll ? "" : lastUpdate;

        if (!StringUtils.isEmpty(lastUpdate)) params.put("updated_on", lastUpdate);
        if (!StringUtils.isEmpty(token)) params.put("device_id", token);

        Utils.Log(TAG, "Device id: " + params.get("device_id"));
        Utils.Log(TAG, "Updated on: " + params.get("updated_on"));

        values.addToRequestQueue(
                snackContainer,
                urlComposer.composeLogin(),
                params, PojoLogin.class,
                listener,
                MainPage.class);
    }

    /**
     * Login with session invoke data downloading.
     *
     * @param snackContainer : View to hold process
     * @param listener       : Callbacks to handle result.
     */
    public void loginWithSession(View snackContainer, VolleyResultListener listener) {

        loginWithSession(snackContainer, false, listener);
    }

    /**
     * Create New Package.
     *
     * @since [V49] - Add Insurance and Shipping service.
     */
    public void newPackage(
            View snackContainer,
            String from,
            String to,
            String strService,
            String content,
            String strInsurance,
            String catatan,
            VolleyResultListener listener) {
        clearParams();

        params.put("from", from.trim());
        params.put("to", to.trim());
        params.put("service", strService);
        params.put("content", content.trim());
        params.put("insured_value", strInsurance);
        params.put("note", catatan.trim());

        values.addToRequestQueue(snackContainer, urlComposer.composeNewpackage(), params, PojoNewPackageData.class, listener, MainPage.class);
    }


    /**
     * Delete Booked package. Delete here change status to "Deleted".
     *
     * @param snackContainer : View that corresponding to this request.
     * @param bookingId      : Booking identifier to delete.
     * @param listener       : Callbacks listener when data is loaded.
     * @since Nov 9 2016 - Param key for {@code "booking_id"} now from {@link PojoBooking} var.
     */
    public void deletePackage(View snackContainer, String bookingId, VolleyResultListener listener) {
        clearParams();
        params.put(PojoBooking.KEY_BOOKING_ID, bookingId);
        Utils.Log(TAG, "Delete booking_id: " + bookingId);
        values.addToRequestQueue(snackContainer, urlComposer.composeDeletePackage(), params, PojoBase.class, listener, MainPage.class);
    }

    public void estimateTariffCheck(View parentPrice, String bookingId, VolleyResultListener listener) {
        clearParams();

        Utils.Log(TAG, "Est tariff for booking id: " + bookingId);

        values.addToRequestQueue(parentPrice, urlComposer.composeCheckEstimateTariff(bookingId), params, PojoEstimateTariffData.class, listener, MainPage.class);
    }

    public void setServiceTariff(View parentPrice, String bookingId, String strSelectedService, VolleyResultListener listener) {
        clearParams();

        params.put("service_code", strSelectedService);

        Utils.Log(TAG, "Update note with selected service on booking id: " + bookingId);
        Utils.Log(TAG, "Note: " + strSelectedService);

        values.addToRequestQueue(parentPrice, urlComposer.composeSetServiceTariff(bookingId), params, PojoBase.class, listener, MainPage.class);
    }

    public void getBookingHistory(String strBookingId, String strBookingHistoryId, View parentView, VolleyResultListener listener) {
        clearParams();
        values.addToRequestQueue(
                parentView,
                urlComposer.composeGetBookingHistory(strBookingId, strBookingHistoryId),
                params,
                PojoHistoryData.class,
                listener,
                MainPage.class);
    }

    public void getVendorLocation(@Nullable String strLat, @Nullable String strLon, View parentView, VolleyResultListener listener) {
        clearParams();
        values.addToRequestQueue(
                parentView,
                urlComposer.composeGetCourierLocation(),
                params,
                PojoCourierLocationData.class,
                listener,
                MainPage.class);
    }

    /**
     * Search Postal code using found keyword.
     * This request have no post body, but URL param instead.
     *
     * @param parentView Parent view to hold the result.
     * @param keywords   Term for Postal code search.
     * @param listener   Volley listener to handle server response.
     * @since Nov 18 2016 - NEW!
     */
    public void postcodeCheck(View parentView, String keywords, VolleyResultListener listener) {
        clearParams();
        values.addToRequestQueue(parentView, urlComposer.composeCheckPostcode(keywords), params, PojoPostcodeData.class, listener, MainPage.class);
    }

    public void pickUp(View snackContainer, String lat, String lon, String address, String extraDetail, String zipCode,
                       int vehicleType, String administrativeArea,
                       String vendorId, VolleyResultListener listener) {
        clearParams();
        params.put("lat", lat);
        params.put("lon", lon);
        params.put("extra_detail", extraDetail);
        params.put("address", address);
        params.put("zip_code", zipCode);
        params.put("vehicle_type", String.valueOf(vehicleType));
        params.put("administrative_area", administrativeArea);
        params.put("vendor_id", vendorId);

        Utils.Log(TAG, "Request pickup params: " + params);

        values.addToRequestQueue(
                snackContainer,
                urlComposer.composePickupCheck(),
                params,
                PojoPickUpConfirmation.class,
                listener,
                PickupInteractor.class);
    }

    public void pickUpConfirm(View snackContainer, String pickupId, String response, VolleyResultListener listener) {
        clearParams();
        params.put("pickup_id", pickupId);
        params.put("response", response);

        values.addToRequestQueue(
                snackContainer,
                urlComposer.composePickupBook(),
                params,
                PojoPickupBook.class,
                listener,
                PickupInteractor.class);
    }

    public void walletTopup(View snackContainer, String amountString, VolleyResultListener listener) {
        clearParams();
        params.put("amount", amountString);
        values.addToRequestQueue(snackContainer, urlComposer.composeWalletTopup(), params, PojoWalletTopup.class,
                listener, WalletTopupModel.class);
    }


    public void geocode(View snackContainer, double lat, double lng, VolleyResultListener listener) {
        String url = "http://maps.google.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&sensor=true";
        values.addToRequestQueue(snackContainer, url, null, PojoGeocode.class, listener, FragmentPickupMap.class);
    }

    /**
     * Cancel Pickup by Pickup ID
     *
     * @param snackContainer       : View as container for snackbar (notifier)
     * @param pickupId             : Pickup identifier to cancel.
     * @param volleyResultListener : Callback to handle responses.
     */
    public void pickUpCancel(View snackContainer, String pickupId, VolleyResultListener volleyResultListener) {
        clearParams();

        params.put("pickup_id", pickupId);

        values.addToRequestQueue(
                snackContainer,
                urlComposer.composePickupCancel(),
                params,
                PojoPickupHistoryCancel.class,
                volleyResultListener,
                MainPage.class);
    }


    public void loadWalletData(View snackContainer, VolleyResultListener listener) {
        clearParams();
        params.put("updated_on", SPHelper.getInstance().getSharedPreferences(Constants.SHARED_UPDATED_ON_WALLET, " "));
        values.addToRequestQueue(snackContainer, urlComposer.composeLoadWalletData(), params,
                PojoWalletData.class, listener, WalletModel.class);
    }


    public void afterSignin(final Context context,
                            Realm myRealm,
                            final PojoBase clazz,
                            final Realm.Transaction.Callback listener,
                            final OnUpdateDialog dialogListener) {

        final int[] androidVersion = {0};

        if (myRealm == null) {
            myRealm = Realm.getDefaultInstance();
        }

        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SPHelper sp = SPHelper.getInstance();

                if (dialogListener != null) dialogListener.publishUpdate("Loading Package.");

                PojoLogin pojoLogin = (PojoLogin) clazz;

                androidVersion[0] = pojoLogin.android_version;

                sp.setPreferences(Constants.SHARED_EMAIL, pojoLogin.user.getUserEmail());
                sp.setPreferences(Constants.SHARED_PROFILE_NAME, StringUtils.isEmpty(pojoLogin.user.getUserName()) ? "" : pojoLogin.user.getUserName());
                sp.setPreferences(Constants.SHARED_PROFILE_PHONE, StringUtils.isEmpty(pojoLogin.user.getUserPhone()) ? "" : pojoLogin.user.getUserPhone());
                sp.setPreferences(Constants.SHARED_PROFILE_PHONE_VERIFY, pojoLogin.user.getPhoneVerified());

                sp.setPreferences(Constants.USER_ID, pojoLogin.user.getUserId());

                String userId = sp.getSharedPreferences(Constants.USER_ID, "0");
                String userPhone = sp.getSharedPreferences(Constants.SHARED_PROFILE_PHONE, "0");
                String userEmail = sp.getSharedPreferences(Constants.SHARED_EMAIL, "NO_EMAIL");
                String userName = sp.getSharedPreferences(Constants.SHARED_PROFILE_NAME, "NO_USERNAME");

                Utils.Log(TAG, "After login name: " + userName);
                Utils.Log(TAG, "After login email: " + userEmail);


                // Manage booking / Package data
                if (pojoLogin.booking.length <= 0) {
                    // When there is no data booking
                    // set Package Empty true in order to prevent making Pickup
                    SPHelper.getInstance().setPreferences(Constants.SHARED_IS_PACKAGE_EMPTY, true);
                }

                // Insert PojoBooking into Realm, limit to AppClass.MAX_PACKAGES_ON_DB
                Utils.Log(TAG, "[Package] 1 - Insert Booking into Realm.");
                int bookingSize = pojoLogin.booking != null ? pojoLogin.booking.length : -1;
                int packageLimit = bookingSize > Constants.MAX_PACKAGES_ON_DB
                        ? Constants.MAX_PACKAGES_ON_DB
                        : bookingSize;
                realm.copyToRealmOrUpdate(Arrays.asList(pojoLogin.booking).subList(0, packageLimit));

                // NO MORE AVAILABLE
                // pojoLogin.booking_history always return empty array

                // NO MORE AVAILABLE
                // pojoLogin.location always return empty array

                if (dialogListener != null) dialogListener.publishUpdate("Loading Pickup.");

                // Handle pickup history
                Utils.Log(TAG, "[Pickup] 2 - Insert Pickup. Loading pickup size: " + pojoLogin.pickup.length);
                int totalOtwDriver = 0;
                for (PojoPickupHistory pojoPickupHistory : pojoLogin.pickup) {
                    int statusNumber = Integer.parseInt(pojoPickupHistory.getStatus());

                    // Add number of OTW driver
                    totalOtwDriver = statusNumber == PojoPickupHistory.STATUS_OTW ?
                            totalOtwDriver + 1 : totalOtwDriver;

                    realm.copyToRealmOrUpdate(pojoPickupHistory);
                }

                // Save last "updated_on"
                sp.setPreferences(Constants.SHARED_LAST_UPDATE, pojoLogin.updated_on);

                Utils.Log(TAG, "[Vendors] 3 - Insert Vendors. Size: " + pojoLogin.vendors.length);
                // Save Vendor data to realm
                // [Dec. 9 2016] Always replace all offline vendor with online response.
                realm.where(PojoCourier.class).findAll().deleteAllFromRealm();
                realm.copyToRealmOrUpdate(Arrays.asList(pojoLogin.vendors));

                // Set flag whether app need to download area list
                Utils.Log(TAG, "Download Indonesia's area (0/1) ? --> " + pojoLogin.areaInfo);
                if (pojoLogin.areaInfo == 1) {
                    sp.setPreferences(Constants.SHARED_LOAD_AREA_LV0, pojoLogin.areaInfo);
                }

                listener.onSuccess();

            }
        });

    }


    public void walletCancelTopup(View snackContainer, VolleyResultListener listener) {
        clearParams();
        values.addToRequestQueue(snackContainer, UrlComposer.getInstance().composeCancelTopup(),
                params, PojoBase.class, listener, WalletTopupModel.class);
    }

    public void createBankAccount(View snackContainer, String bankId, String userBankAccNo, String userBankAccName,
                                  String password, VolleyResultListener listener) {
        clearParams();
        params.put("bank_id", bankId);
        params.put("user_bank_acc_no", userBankAccNo);
        params.put("user_bank_acc_name", userBankAccName);
        params.put("password", password);
        values.addToRequestQueue(snackContainer, UrlComposer.getInstance().composeCreateBankUser(),
                params, PojoBase.class, listener, DialogBankAccountModel.class);
    }

    /**
     * Request wallet withdrawal.
     *
     * @param snackContainer Responsible view to show message
     * @param amount         Amount to withdraw
     * @param userBankId     User bank id from Spinner
     * @param password       Paket ID password (plain text)
     * @param listener       Volley listener to manage server result.
     * @since Feb 17th - Change calss type to PojoWithdrawAdd.class
     */
    public void requestWithdrawal(View snackContainer, String amount, String userBankId, String password, VolleyResultListener listener) {
        clearParams();
        params.put("amount", amount);
        params.put("user_bank_id", userBankId);
        params.put("password", password);
        values.addToRequestQueue(snackContainer, UrlComposer.getInstance().composeRequestWithdrawal(),
                params, PojowithdrawAdd.class, listener, WithdrawalModel.class);
    }

    public void cancelWithdraw(View snackContainer, VolleyResultListener listener) {
        clearParams();
        values.addToRequestQueue(snackContainer, UrlComposer.getInstance().composeCancelWithdraw(),
                params, PojoBase.class, listener, WithdrawSummaryModel.class);
    }

    public void getPagedPackages(View parentView, long longBookingId, int intLimit, VolleyResultListener listener) {
        clearParams();
        final String strBookingId = String.valueOf(longBookingId);
        final String strLimit = intLimit == 0 ? "" : String.valueOf(intLimit);

        values.addToRequestQueue(parentView, urlComposer.composeGetPagedPackages(strBookingId, strLimit), params, PojoBookingData.class, listener, FragmentPackage.class);
    }


    public void searchPackage(View parentView, String strKeyword, long minBookingId, long maxBookingId, int intLimit, VolleyResultListener listener) {
        clearParams();

        final String strMinBookingId = String.valueOf(minBookingId);
        final String strMaxBookingId = String.valueOf(maxBookingId);
        final String strLimit = String.valueOf(intLimit);

        values.addToRequestQueue(
                parentView,
                urlComposer.composeSearchPackages(
                        strKeyword,
                        strMinBookingId,
                        strMaxBookingId,
                        strLimit),
                params,
                PojoBookingData.class,
                listener,
                FragmentDialogSearchPackage.class);
    }
}