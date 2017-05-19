package id.unware.poken.ui.pickup.history.view.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.GeneralListItem;
import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.pickup.history.view.adapters.AdapterPickupDetail;
import id.unware.poken.ui.pickup.view.FragmentPickupMap;


public class FragmentPickupHistoryDetail extends DialogFragment implements OnClickRecyclerItem,
        OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private final String TAG = "FragmentDialogPackages";

    private AppCompatActivity parent;
    private static final String ARG_PICKUP_ID = "arg_pickup_id";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @BindView(R.id.relativeLayoutBtnReuseAddress) ViewGroup parentButtonPickupFromHere;
    @BindView(R.id.recylerViewContent) RecyclerView recyclerView;
    @BindView(R.id.mapView) MapView mapView;

    private PojoPickupHistory pojoPickupHistory;

    private String mPickupId;
    private List<GeneralListItem> generalListItems = new ArrayList<>();

    // private GoogleMap mMap;
    private LatLng pickupLatLng;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentPickupHistoryDetail() {}

    public static FragmentPickupHistoryDetail newInstance(String pickup_id) {
        FragmentPickupHistoryDetail fragment = new FragmentPickupHistoryDetail();
        Bundle args = new Bundle();

        // Set offline data as bundle
        args.putString(ARG_PICKUP_ID, pickup_id);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // Get argument from bundle
            mPickupId = getArguments().getString(ARG_PICKUP_ID);
        }

        pojoPickupHistory = ControllerRealm.getInstance().getPickupHistoryById(mPickupId);
        initPickupHistoryData(pojoPickupHistory);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_pickup_history_detail, container, false);

        ButterKnife.bind(this, view);

        AdapterPickupDetail adapterPackage = new AdapterPickupDetail(generalListItems, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(parent));
        //recyclerView.addItemDecoration(new ItemDecorationDivider(parent, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapterPackage);

        parentButtonPickupFromHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Utils.Log(TAG, "Pickup ID: " + pojoPickupHistory.getPickup_id());
                Utils.Log(TAG, "Address: " + pojoPickupHistory.getAddress());
                Utils.Log(TAG, "Extra detail: " + pojoPickupHistory.getExtra_detail());
                Utils.Log(TAG, "Lat: " + pojoPickupHistory.getLat() + ", Lon: " + pojoPickupHistory.getLon());

                if (FragmentPickupHistoryDetail.this.isVisible()) {
                    FragmentPickupHistoryDetail.this.dismiss();
                }

                /** This SP set to ensure open FragmentPickupForm, then set data there */
                SPHelper.getInstance().setPreferences(FragmentPickupMap.LATITUDE_PICKUP, pojoPickupHistory.getLat());
                SPHelper.getInstance().setPreferences(FragmentPickupMap.LONGITUDE_PICKUP, pojoPickupHistory.getLon());

                Intent intentPickupForm = new Intent();
                intentPickupForm.putExtra(PojoPickupHistory.KEY_PICKUP_ID, pojoPickupHistory.getPickup_id());
                parent.setResult(Activity.RESULT_OK, intentPickupForm);
                parent.finish();
            }
        });

        // Get the map and register for the ready callback
        setupMapView(savedInstanceState);

        return view;
    }

    /**
     * Setup {@link MapView} to show static map (Map Lite)
     *
     * @param savedInstanceState Bundle instance.
     * @since Nov 17 2016 - NEW!
     */
    private void setupMapView(Bundle savedInstanceState) {

        MapsInitializer.initialize(parent);

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();

        switch (googleApiAvailability.isGooglePlayServicesAvailable(parent)) {
            case ConnectionResult.SUCCESS:

                // *** IMPORTANT ***
                // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
                // objects or sub-Bundles.
                Bundle mapViewBundle = null;
                if (savedInstanceState != null) {
                    mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
                }
                mapView.onCreate(mapViewBundle);

                // Gets to GoogleMap from the MapView and does initialization stuff
                if (mapView != null) {
                    mapView.getMapAsync(FragmentPickupHistoryDetail.this);
                }

                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(parent, "SERVICE MISSING", Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(parent, "UPDATE REQUIRED", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(parent, googleApiAvailability.isGooglePlayServicesAvailable(parent), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (AppCompatActivity) context;
    }

    @Override
    public void onItemClick(View view, int position) {

        if (generalListItems.isEmpty() || position < 0) return;

        Utils.Log(TAG, "Item clicked at pos: " + position);

        Utils.Log(TAG, "Click item title: " + generalListItems.get(position).getTitle());
        Utils.Log(TAG, "Click item uri: " + generalListItems.get(position).getActionUri());

        if (generalListItems.get(position).getActionType().equals(Intent.ACTION_DIAL)) {
            Utils.openDialer(parent, generalListItems.get(position).getActionUri());
        }
    }

    /**
     * Prevent dialog to show it's native title.
     *
     * @param savedInstanceState : Default bundle data.
     * @return Dialog which gonna be displayed.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        if (dialog.getWindow() != null)
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Utils.Log(TAG, "onDismiss Fragment Pickup Detail");
    }

    private void initPickupHistoryData(PojoPickupHistory pickupHistory) {

        Utils.Log(TAG, "Pickup ID: " + pickupHistory.getPickup_id());

        double lat = 0, lon = 0;
        try {
            lat = Double.parseDouble(pickupHistory.getLat());
            lon = Double.parseDouble(pickupHistory.getLon());
        } catch (NumberFormatException e) {
            MyLog.FabricLog(Log.ERROR, "Parse double error on Pickup history id: " + pickupHistory.getPickup_id(), e);
        }
        pickupLatLng = new LatLng(lat, lon);

        // Get PojoCourier data based on "vendor_id"
        final PojoCourier pojoCourier = getCourierById(pickupHistory.getVendor_id());

        String strCourierLogoRes = "";
        String strCourierName = Utils.isEmpty(pickupHistory.getVendorBranch())?
                ""
                : pickupHistory.getVendorBranch();

        String strCourierPhone = Utils.isEmpty(pickupHistory.getVendorBranchPhone())?
                ""
                : pickupHistory.getVendorBranchPhone();

        if (pojoCourier != null) {
            strCourierLogoRes = pojoCourier.getVendorLogoUrl();

            strCourierName = Utils.isEmpty(strCourierName)?
                    pojoCourier.getVendorName()
                    : strCourierName;

            strCourierPhone = Utils.isEmpty(strCourierPhone)?
                    pojoCourier.getVendorSupportPhone()
                    : strCourierPhone;
        }

        generalListItems.add(new GeneralListItem(
                        R.drawable.ic_place_black_24dp,                 /*List item icon*/
                        0,                                              /*Action icon such as call button*/
                        parent.getString(R.string.lbl_pickup_location), /*List lable*/
                        "",                                             /*List sub-lable if available*/
                        pickupHistory.getAddress(),                     /*List main content*/
                        "",                                             /*List action to proceed such as open activity.*/
                        "")
        );

        generalListItems.add(new GeneralListItem(
                        R.drawable.ic_note_add_black_24dp,
                        0,  /*Action icon*/
                        parent.getString(R.string.lbl_note_for_driver),
                        "",
                        Utils.isEmpty(pickupHistory.getExtra_detail()) ? parent.getString(R.string.lbl_note_empty) : pickupHistory.getExtra_detail(),
                        "",
                        "")
        );

        // Courier at last
        generalListItems.add(new GeneralListItem(
                        0,                          /*List icon; 0 when use image URL*/
                        R.drawable.ic_notif_call,   /*Action icon*/
                        strCourierLogoRes,          /*List icon URL; Picasso do the job*/
                        "",                         /*Action icon URL; empty string when not used*/
                        parent.getString(R.string.lbl_delivery_service),         /*List lable/caption*/
                        "",
                        strCourierName,
                        strCourierPhone,
                        Intent.ACTION_DIAL)         /*Action to proceed, in this case to open dialer when user taps the icon.*/
        );

    }

    /**
     * Get {@link PojoCourier} in order to show Courier data on list.
     *
     * @param vendor_id Identifier
     * @return PojoCourier onject.
     */
    private PojoCourier getCourierById(long vendor_id) {
        if (vendor_id == 0) return null;

        return ControllerRealm.getInstance().getCourierById(vendor_id);
    }

    //////
    // S: MapView need to implement all this
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    // E: MapView need to implement all this
    //////

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Utils.Log(TAG, "Map ready!!!");

        // Hide Point of Interest from map to enhance clarity
        boolean success = googleMap.setMapStyle(new MapStyleOptions(getResources()
                .getString(R.string.map_style_hide_poi)));

        if (!success) {
            MyLog.FabricLog(Log.ERROR, TAG + " - Style parsing failed. So map style is not applied.");
        }

        googleMap.setOnMapClickListener(FragmentPickupHistoryDetail.this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickupLatLng, 15.0F));
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.addMarker(new MarkerOptions()
                .position(pickupLatLng)
                .title(null));

    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Do nothing when user clicking on Map
        MyLog.FabricLog(Log.INFO, TAG + " User clicking on map view.");
    }
}
