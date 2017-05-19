package id.unware.poken.ui.pickup.view;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.unware.poken.PokenApp;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.interfaces.InputDialogListener;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoPickUpConfirmation;
import id.unware.poken.pojo.PojoPickupBook;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.pojo.PojoRequestPickup;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.BitmapUtil;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.PixelUtil;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.pickup.model.GeocodingModel;
import id.unware.poken.ui.pickup.presenter.PickupPresenterImpl;


/**
 * A simple {@link Fragment} subclass.
 * <p/>
 * Pickup Map to choose Pickup location on map.
 *
 * @since V49 - Apply MVP pattern.
 */
public class FragmentPickupMap extends BaseFragment
        implements
        PickupView,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnMapLoadedCallback,
        CompoundButton.OnCheckedChangeListener{

    private final String TAG = "FragmentPickupMap";
    public static final String ADMINISTRATIVE_AREA = "ADMINISTRATIVE_AREA";
    public static final String ADDRESS_PICKUP = "address_pickup";
    public static final String VEHICLE_PICKUP = "vehicle_pickup";
    public static final String LATITUDE_PICKUP = "latitude_pickup";
    public static final String LONGITUDE_PICKUP = "longitud_pickup";
    public static final String ZIP_CODE_PICKUP = "zip_code_pickup";
    public static final String LOCALITY_PICKUP = "locality_pickup";

    private PickupPresenterImpl mPickupPresenter;

    private final int VEHICLE_ID_BIKE = 1;
    private final int VEHICLE_ID_CAR = 2;

    private final float ZOOM_LEVEL = 15.0f;
    private final int REQUEST_CODE_ASK_PERMISSIONS = 124;

    @BindView(R.id.frameLayoutPrepareMap) ViewGroup frameLayoutPrepareMap;

    // Address container
    @BindView(R.id.parentAddressContainer) ViewGroup parentAddressContainer;
    @BindView(R.id.parentClickableFetchedAddress) ViewGroup parentClickableFetchedAddress;

    @BindView(R.id.parentClickablePickupNote) ViewGroup parentClickablePickupNote;
    @BindView(R.id.textViewAddedNotes) TextView textViewAddedNotes;

    @BindView(R.id.frameSearchAddress) ViewGroup frameSearchAddress;

    // Bottom sheet
    private BottomSheetBehavior mBottomSheetBehavior;
    @BindView(R.id.bottomSheetLocationInfo) View bottomSheet;

    @BindView(R.id.txtTitle) TextView txtAddress;

    @BindView(R.id.cardViewPickupOptions) CardView cardViewPickupOptions;

    // Main container for all options
    @BindView(R.id.parentMainContainer) ViewGroup parentMainContainer;

    // Summary
    @BindView(R.id.parentPickupMainFrame) ViewGroup parentPickupMainFrame;

    @BindView(R.id.parentPickupOptionsSummary) ViewGroup parentPickupOptionsSummary;

    @BindView(R.id.parentClickableChooseVendor) ViewGroup parentClickableChooseVendor;
    @BindView(R.id.imageViewSelectedVendor) ImageView imageViewSelectedVendor;
    @BindView(R.id.textViewSelectedVendor) TextView textViewSelectedVendor;

    @BindView(R.id.parentClickableChooseVehicle) ViewGroup parentClickableChooseVehicle;
    @BindView(R.id.imageViewSelectedVehicle) ImageView imageViewSelectedVehicle;
    @BindView(R.id.textViewSelectedVehicle) TextView textViewSelectedVehicle;

    @BindView(R.id.parentClickableRequestPickup) ViewGroup parentClickableRequestPickup;
    @BindView(R.id.imageViewRequestPickupDrawable) ImageView imageViewRequestPickupDrawable;
    @BindView(R.id.textViewRequestNow) TextView textViewRequestNow;
    @BindView(R.id.progressBarRequestingPickup) ProgressBar progressBarRequestingPickup;

    // Vendor options
    @BindView(R.id.parentVendorOptions) HorizontalScrollView parentVendorOptions;
    @BindView(R.id.parentCouriers) RadioGroup parentCouriers;

    // Vehicle
    @BindView(R.id.parentVehicleOptions) ViewGroup parentVehicleOptions;
    @BindView(R.id.parentClickableBike) ViewGroup parentClickableBike;
    @BindView(R.id.imageViewSelectedBike) ImageView imageViewSelectedBike;
    @BindView(R.id.textViewSelectedBike) TextView textViewSelectedBike;

    @BindView(R.id.parentClickableCar) ViewGroup parentClickableCar;
    @BindView(R.id.imageViewSelectedCar) ImageView imageViewSelectedCar;
    @BindView(R.id.textViewSelectedCar) TextView textViewSelectedCar;

    private View parentView;

    private GoogleMap map;
    private LocationManager locationManager;
    private Location myLoc;

    private Address selectedAddress;

    private FragmentPickupMapListener listener;

    private AlertDialog dialog;

    /**
     * Indicate that user begin to interact with the app.
     */
    private boolean mIsUserInteractWithMap = false;

    /**
     * Handle to wait getting address before user really stop
     * interact with the map.
     */
    private Handler mHandler = new Handler();

    private Runnable getAddressHandler = new Runnable() {
        @Override
        public void run() {

            if (parent == null || parent.isFinishing()) return;

            Utils.Log(TAG, "Beginning get address from LatLon. Zoom level: " + ZOOM_LEVEL);

            // Show bottom sheet
            showAllPickupInfoImmediately(true);

            if (Utils.isNetworkNotConnected(parent)) {
                Utils.snackBar(parentView, parent.getString(R.string.msg_no_network));
            } else {
                setAddress(map.getCameraPosition().target);
            }
        }
    };

    private boolean mIsPickupOptionSummaryVisible = false;
    private boolean mIsRepickup = false;

    // Request Pickup datas
    private String mFormattedAddress = "";
    private String mExtraDetail = "";
    private long mSelectedVendorId = 1467L;
    private String mSelectedVendorTerm;  // Selected vendor term
    private int mSelectedVehicle = VEHICLE_ID_BIKE;

    private List<PojoCourier> mVendorList;
    private int mSelectedVendorIndex = 0;


    public FragmentPickupMap() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_pickup_map, container, false);
        ButterKnife.bind(this, parentView);

        // Initialize Pickup prsenter
        GeocodingModel geocodingModel = new GeocodingModel(parent);
        mPickupPresenter = new PickupPresenterImpl(FragmentPickupMap.this, geocodingModel);

        initView();
        prepareMap();

        return parentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPickupPresenter.requestVendorList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utils.Log(TAG, "onActivityResult. Data: " + data);

        if (requestCode == Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                Utils.Log(TAG, "Place: " + place.getName() + ", lat/lon: " + place.getLatLng());

                // Set current location and fetch vendor around
                Location searchedLocation = new Location(LocationManager.GPS_PROVIDER);
                searchedLocation.setLatitude(place.getLatLng().latitude);
                searchedLocation.setLongitude(place.getLatLng().longitude);

                moveCameraTolocation(place.getLatLng());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                Utils.Log(TAG, status.getStatusMessage());

            } else if (resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Utils.Log(TAG, "Result cancel");
            }
        }
    }

    // Handle when Fragment is visible or not
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Utils.Logs('w', TAG, "IS HIDDEN");
            // Stop on-going process
            mHandler.removeCallbacks(getAddressHandler);

            // Cancel http geocoding when occours
            PokenApp.getInstance().cancelPendingRequests(FragmentPickupMap.class);

        } else {
            Utils.Logs('i', TAG, "IS NOT HIDDEN");
            // Visible back to foreground

            // FALSE no need to show vendor options
            validateVendorRealmDataAndShow(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.Logs('w', TAG, "On destroy view");
    }

    private void validateVendorRealmDataAndShow(boolean isShow) {

        boolean vendorIsReadyToShow = false;

        // Validate vendor list
        if (mVendorList != null && !mVendorList.isEmpty()) {
            if (!mVendorList.get(0).isValid()) {

                // Re init all vendor list
                if (mPickupPresenter != null) {
                    mPickupPresenter.requestVendorList();
                }

            } else {
                Utils.Logs('i', TAG, "Vendor available and still valid.");
                vendorIsReadyToShow = true;
            }

        } else {
            Utils.Logs('e', TAG, "Vendor list is empty or null");
            // Re init all vendor list when no item on list
            if (mPickupPresenter != null) {
                mPickupPresenter.requestVendorList();
            }
        }

        if (isShow && vendorIsReadyToShow) {

            showVendorOptions(true);

        } else if (!vendorIsReadyToShow) {

            MyLog.FabricLog(Log.WARN, TAG + " - Vendor options is not ready. Reload necessary.");
            showVendorOptionsWithDelay(isShow);

        }

    }

    private void showVendorOptionsWithDelay(final boolean isShow) {
        textViewSelectedVendor.setText(parent.getString(R.string.loading));

        // Wait then show vendor options
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    FragmentPickupMap.this.showVendorOptions(true);
                } else {
                    Utils.Logs('e', TAG, "Test - Vendor option refreshed.");
                }
            }
        }, Constants.DURATION_SUPER_LONG);
    }

    @OnClick({
            R.id.parentClickableFetchedAddress,
            R.id.parentClickablePickupNote,

            R.id.parentClickableChooseVendor,
            R.id.parentClickableChooseVehicle,
            R.id.parentClickableRequestPickup,

            R.id.parentClickableBike,
            R.id.parentClickableCar
    })
    public void onClickPickupViews(View v) {
        switch (v.getId()) {

            case R.id.parentClickableFetchedAddress:
                showLocationSearch();
                break;
            case R.id.parentClickablePickupNote:
                openAddNoteScreen();
                break;

            case R.id.parentClickableChooseVendor:

                // Make sure Realm vendor data still valid before display it
                validateVendorRealmDataAndShow(true);

                // Set flag that pickup summary is hidden
                mIsPickupOptionSummaryVisible = false;


                break;
            case R.id.parentClickableChooseVehicle:

                showVehicleOptions(true);

                // Set flag that pickup summary is hidden
                mIsPickupOptionSummaryVisible = false;

                break;

            case R.id.parentClickableRequestPickup:
                Utils.Logs('i', TAG, "Request pickup begins.");
                if (mPickupPresenter != null
                        && selectedAddress != null) {

                    // Save current Address before request pickup
                    saveAddress();

                    // Request pickup via Presenter.
                    mPickupPresenter.requestPickup();
                }
                break;

            case R.id.parentClickableBike:

                mSelectedVehicle = VEHICLE_ID_BIKE;
                showPickupOptionsSummary(true);

                break;
            case R.id.parentClickableCar:

                mSelectedVehicle = VEHICLE_ID_CAR;
                showPickupOptionsSummary(true);

                break;
        }
    }

    private void showLocationSearch() {
        Utils.Log(TAG, "Start place auto complete API.");

        try {
            // Filter search result only for Indonesias' places
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry("id")
                    .build();

            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(this.parent);

            Utils.Log(TAG, "Start for result.");

            startActivityForResult(intent, Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();

            Utils.Log(TAG, "Search maps error: " + e.getMessage());

            GoogleApiAvailability.getInstance()
                    .getErrorDialog(this.parent, e.getConnectionStatusCode(), 0 /* requestCode */)
                    .show();

        } catch (GooglePlayServicesNotAvailableException e) {

            Utils.Log(TAG, "Search maps error: " + e.getMessage());

            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Utils.Log(TAG, message);
        }
    }

    private void openAddNoteScreen() {

        Utils.Log(TAG, "Open add note screen");

        ControllerDialog.getInstance().showInputDialog (
            parent.getString(R.string.lbl_pickup_address_detail),
            mExtraDetail,
            parent.getString(R.string.hint_note_for_driver_ex),
            parent,
            new InputDialogListener() {
                @Override
                public void onInputTextDone(CharSequence text) {

                    Utils.Logs('i', TAG, "Text from dialog: \"" + text + "\"");

                    if (!StringUtils.isEmpty(String.valueOf(text))) {
                        textViewAddedNotes.setText(text);
                    } else if (text.length() == 0) {
                        textViewAddedNotes.setText(R.string.hint_note_for_driver);
                    }
                    mExtraDetail = String.valueOf(text);

                }
            });
    }

    private void showPickupOptionsSummary(boolean isVisible) {

        parentPickupOptionsSummary.setVisibility(isVisible
            ? View.VISIBLE
            : View.GONE);

        if (isVisible) {

            // Setup selected vendor, make sure it's valid
            if (mSelectedVendorIndex >= 0
                    && mVendorList.size() > 0) {

                Utils.Logs('i', TAG, "Update selected vendor view");
                PojoCourier selectedVendor = mVendorList.get(mSelectedVendorIndex);

                if (selectedVendor != null
                        && selectedVendor.isValid()) {
                    textViewSelectedVendor.setText(String.valueOf(selectedVendor.getVendorName()));

                    imageViewSelectedVendor.setAlpha(1F);
                    Picasso.with(this.parent)
                            .load(selectedVendor.getVendorLogoUrl())
                            .placeholder(R.drawable.seek_thumb_disabled)
                            .error(R.drawable.ic_circle_24dp)
                            .into(imageViewSelectedVendor);
                }

            }

            if (mSelectedVehicle == VEHICLE_ID_CAR) {
                textViewSelectedVehicle.setText(R.string.lbl_car);
                imageViewSelectedVehicle.setImageResource(R.drawable.ic_pickup_car);
            } else if (mSelectedVehicle == VEHICLE_ID_BIKE) {
                textViewSelectedVehicle.setText(R.string.lbl_bike);
                imageViewSelectedVehicle.setImageResource(R.drawable.ic_pickup_bike);
            }

            // Hide Vendor and Vehicle option view
            showVendorOptions(false);
            showVehicleOptions(false);

            // Set flag that pickup summary is visible
            mIsPickupOptionSummaryVisible = true;
        }
    }

    public boolean isPickupOptionSummaryVisible() {
        if (!mIsPickupOptionSummaryVisible) {
            showPickupOptionsSummary(true);
            return false;
        } else {
            return true;
        }
    }

    private void showVendorOptions(boolean isVisible) {

        parentVendorOptions.setVisibility(isVisible
                ? View.VISIBLE
                : View.GONE);

        // Hide unnecessary view
        if (isVisible) {
            showPickupOptionsSummary(false);
            showVehicleOptions(false);
        }
    }

    private void showVehicleOptions(boolean isVisible) {
        parentVehicleOptions.setVisibility(isVisible
                ? View.VISIBLE
                : View.GONE);

        // Highlight selected vehicle
        int activeColor = R.color.myAccentColor;
        int inactiveColor = R.color.style_overlay_grey;

        Utils.Logs('i', TAG, "Selected vehicle: " + (mSelectedVehicle == VEHICLE_ID_BIKE? " Bike." : " Car."));

        if (mSelectedVehicle == VEHICLE_ID_BIKE) {

            // Activate BIKE
            imageViewSelectedBike.setColorFilter(BitmapUtil.getDrawableFilter(parent, activeColor));
            textViewSelectedBike.setTextColor(ContextCompat.getColor(parent, activeColor));
            textViewSelectedBike.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);

            imageViewSelectedCar.setColorFilter(BitmapUtil.getDrawableFilter(parent, inactiveColor));
            textViewSelectedCar.setTextColor(ContextCompat.getColor(parent, inactiveColor));
            textViewSelectedCar.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);

        } else if (mSelectedVehicle == VEHICLE_ID_CAR) {

            // Activate car
            imageViewSelectedCar.setColorFilter(BitmapUtil.getDrawableFilter(parent, activeColor));
            textViewSelectedCar.setTextColor(ContextCompat.getColor(parent, activeColor));
            textViewSelectedCar.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);

            imageViewSelectedBike.setColorFilter(BitmapUtil.getDrawableFilter(parent, inactiveColor));
            textViewSelectedBike.setTextColor(ContextCompat.getColor(parent, inactiveColor));
            textViewSelectedBike.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
        }

        if (isVisible) {
            showPickupOptionsSummary(false);
            showVendorOptions(false);
        }

    }

    /**
     * Init view.
     *
     * @since Nov 8 2016 - Save this fragment as last opened page.
     */
    private void initView() {

        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        // Initial view state for Address info translateY (move up)
        int parentAddressContainerHeight = this.parent.getResources().getDimensionPixelSize(R.dimen.height_100dp);
        Utils.Logs('w', TAG, "parentAddressContainerHeight: " + parentAddressContainerHeight);
        parentAddressContainer.animate().setDuration(0).translationY(parentAddressContainerHeight * -1).alpha(0);

        // Initial state for Bottom Sheet
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

    }

    public void setupRepickup(String pickupId) {
        PojoPickupHistory pojoPickupHistory =
                ControllerRealm.getInstance()
                        .getPickupHistoryById(pickupId);

        if (pojoPickupHistory == null) return;

        mIsRepickup = true;

        Utils.Log(TAG, "Pickup address: " + pojoPickupHistory.getAddress());
        Utils.Log(TAG, "Pickup Lat: " + pojoPickupHistory.getLat());
        Utils.Log(TAG, "Pickup Lon: " + pojoPickupHistory.getLon());
        Utils.Log(TAG, "Pickup extra: " + pojoPickupHistory.getExtra_detail());
        Utils.Log(TAG, "Pickup vehicle: " + pojoPickupHistory.getVehicle_type());
        Utils.Log(TAG, "Pickup ZIP code: " + pojoPickupHistory.getZip_code());
        Utils.Log(TAG, "Pickup branch ID: " + pojoPickupHistory.getVendor_id());
        Utils.Log(TAG, "Pickup branch name: " + pojoPickupHistory.getVendorBranch());
        Utils.Log(TAG, "Pickup branch phone: " + pojoPickupHistory.getVendorBranchPhone());

        CharSequence extraDetail = StringUtils.isEmpty(pojoPickupHistory.getExtra_detail())
                ? parent.getString(R.string.hint_note_for_driver)
                : pojoPickupHistory.getExtra_detail();

        // Fill form
        txtAddress.setText(pojoPickupHistory.getAddress());
        textViewAddedNotes.setText(extraDetail);

        moveCameraTolocation(new LatLng(
                Utils.getParsedDouble(pojoPickupHistory.getLat()),
                Utils.getParsedDouble(pojoPickupHistory.getLon())
        ));

        // Set memeber vars for Request Pickup purpose
        mFormattedAddress = pojoPickupHistory.getAddress();
        mExtraDetail = pojoPickupHistory.getExtra_detail();
        mSelectedVehicle = pojoPickupHistory.getVehicle_type().equals("bike") ? 1 : 2;
        mSelectedVendorId = pojoPickupHistory.getVendor_id();

        // Get selected vendor index
        if (mVendorList != null
                && mVendorList.size() > 0
                && mVendorList.get(0).isValid()) {

            int vendorListSize = mVendorList.size();
            int pos = -1;
            for (int i = 0; i < vendorListSize; i++) {
                if (mSelectedVendorId == mVendorList.get(i).getVendorId()) {
                    pos = i;
                    break;
                }
            }

            Utils.Logs('i', TAG, "Vendor index: " + pos);
            mSelectedVendorIndex = pos;
        }

        initVendorSelection();

        showPickupOptionsSummary(true);

        // loadData(pojoPickupHistory.getAddress(), pojoPickupHistory.getExtra_detail(), selectedVehicle, selectedVendorId);
    }

    /**
     * Setup courier selection. Data resource is from Realm.
     *
     * @since Nov 3 2016 - NEW!
     */
    @Override
    public void setupCourierOption(ArrayList<PojoCourier> vendorList) {

        if (mVendorList != null && !mVendorList.isEmpty()) {
            mVendorList.clear();

            // Make sure all RadioGroup childs is gone
            Utils.Logs('i', TAG, "Radio group child count: " + parentCouriers.getChildCount());
            parentCouriers.removeAllViews();
        }

        mVendorList = new ArrayList<>(vendorList);

        Utils.Log(TAG, "Vendor list size: " + mVendorList.size());

        // Get summary container width
        int rootParentWidth = parent.findViewById(android.R.id.content).getWidth();
        int tenDpParentPadding = PixelUtil.dpToPx(parent, 10);
        int itemWidth = (rootParentWidth / 3) - (tenDpParentPadding * 3);

        // Param to control RadioButton behave on it's parent (RadioGroup)
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT
        );
        // For case vendors only 3
        // Update is necessary if vendor more than three
        layoutParams.weight = 1;
        layoutParams.setMargins(0, 0, tenDpParentPadding, 0);

        // Initial RadioButton position on RadioGroup
        int pos = 0;

        for (PojoCourier pojoCourier : mVendorList) {
            RadioButton radioButton = new RadioButton(parent);
            radioButton.setLayoutParams(layoutParams);

            radioButton.setLayoutParams(layoutParams);
            radioButton.setGravity(Gravity.CENTER);
            radioButton.setMinimumWidth(itemWidth);

            radioButton.setButtonDrawable(android.R.color.transparent);
            radioButton.setPadding(0, 0, 0, 0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                radioButton.setBackground(parent.getResources().getDrawable(R.drawable.bg_selection_accent, parent.getTheme()));
            } else {
                radioButton.setBackground(ContextCompat.getDrawable(parent, R.drawable.bg_selection_accent));
            }

            radioButton.setText(pojoCourier.getVendorName());

            radioButton.setOnCheckedChangeListener(this);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentPickupMap.this.showPickupOptionsSummary(true);
                }
            });

            // This tag used to indicate this RadioButton position on RadioGroup
            radioButton.setTag(pos);

            parentCouriers.addView(radioButton);

            // Remember selected vendor index when vendor id is not ""
            if (mSelectedVendorId != -1 && mSelectedVendorId == pojoCourier.getVendorId()) {
                Utils.Log(TAG, "Selected vendor id is found while create vendor options.");
                mSelectedVendorIndex = pos;
            }

            pos++;
        }

        // parentCouriers.setOnCheckedChangeListener(this);

        // Set checked RadioButton at "mSelectedVendorIndex"
        initVendorSelection();
    }

    /**
     * Set default selection to first Vendor. <br />
     *
     * Set {@link RadioGroup} checked at first {@link RadioButton}.
     *
     * @since Nov 4 2016 - NEW!
     */
    private void initVendorSelection() {

        if (parentCouriers == null || parentCouriers.getChildCount() < 1) return;

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                RadioButton firstRadioButton = (RadioButton) parentCouriers.getChildAt(mSelectedVendorIndex);
                if (firstRadioButton != null) {
                    parentCouriers.check(firstRadioButton.getId());
                }
            }
        }, 250);

    }

    @Override
    public void onCheckedChanged(CompoundButton radioButton, boolean isChecked) {

        if (isChecked && radioButton.getTag() != null) {
            Utils.Log(TAG, "RadioButton tag: " + radioButton.getTag() + " is checked.");

            radioButton.setTextColor(ContextCompat.getColor(parent, R.color.myAccentColor));

            // RadioButton pos define from saved tag while RadioButton creation
            int radioButtonPosition = (int) radioButton.getTag();

            // Abort process if no Courier data
            if (mVendorList.isEmpty()) return;

            PojoCourier pojoCourier = mVendorList.get(radioButtonPosition);
            if (pojoCourier != null && pojoCourier.isValid()) {

                // Set selected vendor ID on checked courier
                mSelectedVendorId = pojoCourier.getVendorId();
                mSelectedVendorIndex = radioButtonPosition;
                mSelectedVendorTerm = String.valueOf(pojoCourier.getVendorPickupTerms());

                Utils.Log(TAG, "Selected vendor ID: " + mSelectedVendorId);
                Utils.Log(TAG, "Selected vendor ID index : " + mSelectedVendorIndex);
                Utils.Log(TAG, "Selected Vendor term: " + mSelectedVendorTerm);

            }

        } else {
            Utils.Log(TAG, "RadioButton text: " + radioButton.getText() + " is unchecked.");
            radioButton.setTextColor(ContextCompat.getColor(parent, R.color.black_90));
        }

        // Show pickup card summary
        showPickupOptionsSummary(true);
    }

    @Override
    public View getParentView() {
        return parentView;
    }

    @Override
    public PojoPickupHistory getPickupRequestData() {
        PojoPickupHistory pickupData = new PojoPickupHistory();
        SPHelper sp = SPHelper.getInstance();
        pickupData.setLat(String.valueOf(sp.getSharedPreferences(LATITUDE_PICKUP, "")));
        pickupData.setLon(String.valueOf(sp.getSharedPreferences(LONGITUDE_PICKUP, "")));
        pickupData.setAddress(mFormattedAddress);
        pickupData.setExtra_detail(mExtraDetail);
        pickupData.setZip_code(sp.getSharedPreferences(ZIP_CODE_PICKUP, ""));
        pickupData.setVehicle_type(String.valueOf(mSelectedVehicle));
        pickupData.setAdministrativeArea(sp.getSharedPreferences(ADMINISTRATIVE_AREA, ""));
        pickupData.setVendor_id(mSelectedVendorId);
        return pickupData;
    }

    @Override
    public boolean isPickupReady() {

        if (StringUtils.isEmpty(mFormattedAddress)) {
            Utils.snackBar(parentView, parent.getString(R.string.msg_no_pickup_location), Log.ERROR);
        } else if(mSelectedVendorId == -1) {
            Utils.snackBar(parentView, parent.getString(R.string.msg_no_selected_vendor), Log.ERROR);
        } else {
            return true;
        }

        return false;
    }

    private void saveLastChoice() {
        SPHelper.getInstance().setPreferences(VEHICLE_PICKUP, mSelectedVehicle);
    }

    //////
    // S: Pickup View
    @Override
    public void showAddress(PojoRequestPickup pojoRequestPickup) {

        if (parent == null || parent.isFinishing()) return;

        // Reset view to normal (instead on loading mode)
        showAddressLoadingIndicator(false);
        txtAddress.setText(String.valueOf(pojoRequestPickup.getFormattedAddress()));

        // Save seleted address
        Address address = new Address(Locale.ENGLISH);
        address.setAddressLine(0, pojoRequestPickup.getFormattedAddress());
        address.setAddressLine(3, pojoRequestPickup.getAdminArea());
        address.setLatitude(pojoRequestPickup.getLatitude());
        address.setLongitude(pojoRequestPickup.getLongitude());
        address.setPostalCode(pojoRequestPickup.getZipCode());
        address.setLocality(pojoRequestPickup.getLocality());
        selectedAddress = address;
    }

    @Override
    public void showNoAddressFound() {

        if (parent == null || parent.isFinishing()) return;

        // Show normal view when no address found
        showAddressLoadingIndicator(false);

        int intColorDisabled = ContextCompat.getColor(parent, R.color.style_overlay_grey);

        txtAddress.setText(R.string.msg_error_address_not_found);
        txtAddress.setTextColor(intColorDisabled);
    }

    @Override
    public void showPickupConfirmation(final PojoPickUpConfirmation pickUpConfirmation) {

        if (parent == null || parent.isFinishing() || pickUpConfirmation == null) return;

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this.parent);
        @SuppressLint("InflateParams")
        View mView = layoutInflaterAndroid.inflate(R.layout.dialog_pickup_confirmation, null);

        TextView textViewPickupMessage = (TextView) mView.findViewById(R.id.textViewPickupMessage);
        TextView textViewVendorTermAndCondition = (TextView) mView.findViewById(R.id.textViewVendorTermAndCondition);

        //noinspection deprecation
        textViewPickupMessage.setText(Html.fromHtml(String.valueOf(pickUpConfirmation.info)));
        //noinspection deprecation
        textViewVendorTermAndCondition.setText(Html.fromHtml(String.valueOf(mSelectedVendorTerm)));

        AlertDialog.Builder builder = new AlertDialog.Builder(this.parent);
        builder.setTitle(null);
        builder.setView(mView);

        // Set up the buttons
        builder.setPositiveButton(this.parent.getString(R.string.pickup), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mPickupPresenter != null
                        && pickUpConfirmation.available == 1) {
                    mPickupPresenter.confirmPickupRequest(pickUpConfirmation, true);
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(this.parent.getString(R.string.btn_negative_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mPickupPresenter != null) {
                    mPickupPresenter.confirmPickupRequest(pickUpConfirmation, false);
                }
                dialog.dismiss();
            }
        });

//        AlertDialog dialog = ControllerDialog.getInstance().showYesNoDialog (
//                pickUpConfirmation.info,
//                parent,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        if (which == DialogInterface.BUTTON_POSITIVE) {
//                            if (mPickupPresenter != null
//                            && pickUpConfirmation.available == 1) {
//                                mPickupPresenter.confirmPickupRequest(pickUpConfirmation, true);
//                            }
//
//                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
//                            if (mPickupPresenter != null) {
//                                mPickupPresenter.confirmPickupRequest(pickUpConfirmation, false);
//                            }
//                            dialog.dismiss();
//                        }
//                    }
//                },
//                parent.getString(R.string.pickup),
//                parent.getString(R.string.btn_negative_cancel)
//        );

        builder.setCancelable(false);

        if (pickUpConfirmation.available == 0) {
            Button btn = builder.show().getButton(DialogInterface.BUTTON_POSITIVE);
            btn.setOnClickListener(null);
            btn.setTextColor(Color.GRAY);
        }

        builder.show();

    }

    @Override
    public void showPickupConfirmationResult(PojoPickupBook pickupConfirmationResult) {

        if (parent == null || parent.isFinishing()) return;

        if (pickupConfirmationResult.book == 1) {

            Utils.snackbarDismiss();

            ControllerDialog.getInstance().showDialogInfo (
                parent.getString(R.string.info),
                String.valueOf(pickupConfirmationResult.msg),
                parent,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Utils.Logs('i', TAG, "Pickup request success. :D");
                        if (listener != null) {
                            listener.onRequestPickupSuccess();
                        }
                    }
                });
        }
    }

    @Override
    public void showAddressLoadingIndicator(boolean isShow) {

        if (parent == null || parent.isFinishing()) return;

        int intColorDisabled = ContextCompat.getColor(parent, R.color.style_overlay_grey);
        int intColorDefault = ContextCompat.getColor(parent, R.color.black_90);
        int intColorPrimary = ContextCompat.getColor(parent, R.color.myPrimaryDarkColor);

        // Get background
        GradientDrawable drawable = (GradientDrawable) imageViewRequestPickupDrawable.getBackground();

        // Show loading indicator
        if (isShow) {

            // Disable click on Request pickup button
            drawable.setColorFilter(BitmapUtil.getDrawableFilter(parent, R.color.style_overlay_grey));
            parentClickableRequestPickup.setEnabled(false);
            textViewRequestNow.setTextColor(intColorDisabled);
            txtAddress.setTextColor(intColorDisabled);

            txtAddress.setText(parent.getString(R.string.searching));
        } else {

            drawable.setColorFilter(BitmapUtil.getDrawableFilter(parent, R.color.myPrimaryDarkColor));
            parentClickableRequestPickup.setEnabled(true);
            textViewRequestNow.setTextColor(intColorPrimary);
            txtAddress.setTextColor(intColorDefault);
        }
    }

    @Override
    public void toggleAddressInfo(boolean showOrHide) {

    }

    @Override
    public void showViewState(UIState uiState) {
        Utils.Logs('i', TAG, "UI State: " + uiState);
        switch (uiState) {
            case LOADING:
                showVolleyRequestLoadingIndicator(true);
                break;
            case ERROR:
            case FINISHED:
                showVolleyRequestLoadingIndicator(false);
                break;
        }
    }
    // E: Pickup View
    //////

    private void showVolleyRequestLoadingIndicator(boolean isLoading) {
        if (parent == null || parent.isFinishing()) return;

        int intColorDisabled = ContextCompat.getColor(parent, R.color.style_overlay_grey);
        int intColorPrimary = ContextCompat.getColor(parent, R.color.myPrimaryDarkColor);

        if (isLoading) {

            progressBarRequestingPickup.animate().alpha(1);

            imageViewRequestPickupDrawable.animate().alpha(0);
            textViewRequestNow.setTextColor(intColorDisabled);

        } else {

            progressBarRequestingPickup.animate().alpha(0);

            imageViewRequestPickupDrawable.animate().alpha(1);
            textViewRequestNow.setTextColor(intColorPrimary);

        }
    }

    private void prepareMap() {

        SupportMapFragment mapFragmentView = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapPickup);
        mapFragmentView.getMapAsync(this);  // Register: OnMapReadyCallback
    }

    /**
     * Set selected address based on {@link LatLng} data then show on view.
     * Address fetching include java client request http when java client failed.
     *
     * @param latLng LatLng data.
     *
     * @since Nov 10 2016 - Abort process when fragment no available.
     */
    private void setAddress(final LatLng latLng) {

        mPickupPresenter.requestAddress(
                latLng.latitude,
                latLng.longitude
        );

    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(parent, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            // Check for Rationale Option
            if (!ActivityCompat.shouldShowRequestPermissionRationale(parent, permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    setupMap();
                } else {
                    // Permission Denied
                    Utils.Logs('e', TAG, "permission denied - permission denied");
                    Utils.snackBar(parentView, "Gagal mengambil data, mohon aktifkan izin lokasi");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setupMap() {

        // Move camera to Jakarta as initial behavior
        map.moveCamera (
                CameraUpdateFactory.newLatLngZoom(new LatLng(-6.177647, 106.826377), 12.0f)
        );

        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Network Location");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "Anda harus memberi izin pada " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);

                if (dialog == null) {
                    dialog = ControllerDialog.getInstance().showYesNoDialog(message, parent,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Utils.Log(TAG, "Dialog di pilih yes " + (which == DialogInterface.BUTTON_POSITIVE));

                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                    Uri.fromParts("package", parent.getPackageName(), null));
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            parent.startActivity(intent);
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                                    REQUEST_CODE_ASK_PERMISSIONS);
                                            break;
                                    }

                                }
                            });

                } else if (!dialog.isShowing()) {
                    dialog.show();
                }

                return;
            }
            ActivityCompat.requestPermissions(parent, permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }

        // Add permission check
        if (ActivityCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);

        setupMapPadding();

        // Register map click listener
        map.setOnMapClickListener(this);

        // Register drag and drop lsitener
        map.setOnCameraMoveStartedListener(this);
        map.setOnCameraMoveListener(this);
        map.setOnCameraMoveCanceledListener(this);
        map.setOnCameraIdleListener(this);

        map.setOnMapLoadedCallback(this);

        currentLocation();
    }

    private void setupMapPadding() {

        if (parentAddressContainer == null || bottomSheet == null) return;

        // Set map padding bottom to bottom info height
        // GoogleMap.setPadding(left, top, right, bottom)
        int parentAddressContainerHeight = parentAddressContainer.getHeight();
        int bottomSheetHeight = bottomSheet.getHeight();
        map.setPadding(0, parentAddressContainerHeight, 0, bottomSheetHeight);
    }

    public void setListener(FragmentPickupMapListener listener) {
        this.listener = listener;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Utils.Logs('i', TAG, "--------- Map is ready! ---------");

        map = googleMap;
        setupMap();

    }

    @Override
    public void onMapClick(LatLng latLng) {
        Utils.Log(TAG, "Click on map at pos: " + latLng);

        // Show pickup option summary when click on map
        showPickupOptionsSummary(true);
    }

    //////
    // S: On Camera Event Listeners
    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            Utils.Log(TAG, "The user gestured on the map.");

            // Set user begin interacting with app
            mIsUserInteractWithMap = true;

            // Repickup mode off when user begin touching map
            mIsRepickup = false;

            // Cancel getting address when user touching on the map
            mHandler.removeCallbacks(getAddressHandler);

            // Make sure BottomSheet collapsed when user drag-and-drop on the Map
            showAllPickupInfoImmediately(false);

        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION) {
            Utils.Log(TAG, "The user tapped something on the map.");

            // [V49] Set user begin interacting with app by tapping something on map
            mIsUserInteractWithMap = true;

            // Repickup mode off when user begin touching map
            mIsRepickup = false;

            // [V49] Cancel getting address when user touching the map
            mHandler.removeCallbacks(getAddressHandler);

        } else if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
            Utils.Log(TAG, "The app moved the camera.");
        }
    }

    @Override
    public void onCameraMove() {
        // Log(TAG, "The camera is moving. ");
    }

    @Override
    public void onCameraMoveCanceled() {
        Utils.Log(TAG, "Camera movement canceled.");
    }

    @Override
    public void onCameraIdle() {
        Utils.Log(TAG, "The camera has stopped moving.");

        // Abort get address data when user isn't interact with map yet
        if (!mIsUserInteractWithMap) {
            return;
        }

        // Wait before get address
        // Make sure in Repickup mode
        if (!mIsRepickup)
            mHandler.postDelayed(getAddressHandler, this.getResources().getInteger(android.R.integer.config_mediumAnimTime));

    }
    // E: On Camera Event Listeners
    //////

    private boolean saveAddress() {
        // Save data to be used on pickup form
        if (selectedAddress != null
                && !Utils.isEmpty(selectedAddress.getAddressLine(0))
                && !Utils.isEmpty(selectedAddress.getAddressLine(3))
                && !Utils.isEmpty(selectedAddress.getPostalCode())) {

            SPHelper sp = SPHelper.getInstance();

            sp.setPreferences(ADDRESS_PICKUP, selectedAddress.getAddressLine(0));
            sp.setPreferences(ADMINISTRATIVE_AREA, selectedAddress.getAddressLine(3));
            sp.setPreferences(LOCALITY_PICKUP, String.valueOf(selectedAddress.getLocality()));

            sp.setPreferences(LATITUDE_PICKUP, selectedAddress.getLatitude() + "");
            sp.setPreferences(LONGITUDE_PICKUP, selectedAddress.getLongitude() + "");
            sp.setPreferences(ZIP_CODE_PICKUP, selectedAddress.getPostalCode());

            // Save address to member var
            mFormattedAddress = selectedAddress.getAddressLine(0);

            return true;

        } else {
            return false;
        }
    }

    private void showPickupAddressInfo(boolean isVisible) {
        int parentAddressContainerHeight = parentAddressContainer.getHeight();

        if (isVisible) {
            parentAddressContainer.animate().setDuration(300).translationY(0).alpha(1);
        } else {
            parentAddressContainer.animate().setDuration(300).translationY(parentAddressContainerHeight * -1).alpha(0);
        }
    }

    private void showBottomSheetPickupInfo(boolean isVisible) {
        if (isVisible) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    private void showAllPickupInfoSequently(boolean isVisible) {

        if (isVisible) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPickupAddressInfo(true);
                }
            }, 500);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showBottomSheetPickupInfo(true);
                }
            }, Constants.DURATION_SUPER_LONG);

        } else {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showPickupAddressInfo(false);
                }
            }, Constants.DURATION_SUPER_LONG);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showBottomSheetPickupInfo(false);
                }
            }, 500);

        }
    }

    private void showMapPreparationScreen(boolean isVisible) {
        if (isVisible) {

            frameLayoutPrepareMap.animate().withStartAction(new Runnable() {
                @Override
                public void run() {
                    frameLayoutPrepareMap.setVisibility(View.VISIBLE);
                }
            }).alpha(1);

        } else {

            frameLayoutPrepareMap.animate().alpha(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    frameLayoutPrepareMap.setVisibility(View.GONE);
                }
            });

        }
    }

    private void showAllPickupInfoImmediately(boolean isVisible) {
        if (mBottomSheetBehavior == null) return;

        showPickupAddressInfo(isVisible);

        showBottomSheetPickupInfo(isVisible);
    }

    private void currentLocation() {

        locationManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);

        boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            Utils.snackBar(parentView, parent.getString(R.string.please_enable_your_gps), Log.WARN);

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            parent.startActivity(intent);
        }

        String provider = locationManager.getBestProvider(new Criteria(), false);

        // Add permission check
        if (ActivityCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);

        if (location == null) {
            locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);
        } else {
            myLoc = location;
            setAddress(new LatLng(myLoc.getLatitude(), myLoc.getLongitude()));
            moveCameraToMyLocation(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        Utils.Log(TAG, "On pause.");

        try {

            if (locationManager != null) {

                // Add permission check
                if (ActivityCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.removeUpdates(locationListener);
            }

        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        }

    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {
            Utils.Log(TAG, "Location has change,");
            myLoc = location;

            if (ActivityCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            setAddress(new LatLng(location.getLatitude(), location.getLongitude()));

            moveCameraToMyLocation(true);

            if (locationManager != null)
                locationManager.removeUpdates(this);

            Utils.Log(TAG, "Location has change finished.");
        }
    };

    private void moveCameraToMyLocation(boolean withZoom) {
        if (withZoom)
            moveCameraTolocation(new LatLng(myLoc.getLatitude(), myLoc.getLongitude()), ZOOM_LEVEL);
        else
            moveCameraTolocation(new LatLng(myLoc.getLatitude(), myLoc.getLongitude()));
    }

    private void moveCameraTolocation(LatLng latLng) {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(map.getCameraPosition().zoom) // Sets the zoom
                .build(); // Creates a CameraPosition from the builder

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void moveCameraTolocation(LatLng latLng, float zoomLevel) {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(zoomLevel)    // Sets the zoom
                .build();           // Creates a CameraPosition from the builder
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentPickupMapListener) {
            listener = (FragmentPickupMapListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentPickupMapListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;

    }

    @Override
    public void onMapLoaded() {
        Utils.Logs('w', TAG, "On Map loaded call back");
        setupMapPadding();

        showMapPreparationScreen(false);

        // Animate Address info and bottom sheet visibility one-by-one
        showAllPickupInfoSequently(true);
    }

    /**
     * Hast activity should implement this interface.
     */
    public interface FragmentPickupMapListener {
        void onSelectedAddress(Address address);
        void onRequestPickupSuccess();

        void onVerifyPhoneNumberNow(boolean isVerifyNow);
    }

}