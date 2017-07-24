package id.unware.poken.ui.nearby.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.pojo.PojoOfficeLocation;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.nearby.presenter.NearbyPresenter;


public class NearbyActivity extends BaseActivity
        implements OnMapReadyCallback,
        INearbyView,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowCloseListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener {

    private final String TAG = "NearbyActivity";
    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    @BindView(R.id.parentMap) FrameLayout parentMap;
    @BindView(R.id.progressBarNearby) ProgressBar progressBarNearby;
    // Progress on actionbar
    Menu optionsMenu;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private List<Marker> listMarker = new ArrayList<>();
    private LocationManager locationManager;
    // Flag first load
    private boolean isFirstLoad = true;
    // Is user begin interact with map
    private boolean isUserInteractWithMap = false;
    private NearbyPresenter mPresenter;
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLastLocation = location;
            getAllPartner();

            if (ActivityCompat.checkSelfPermission(NearbyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(NearbyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            locationManager.removeUpdates(this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.Log(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        ButterKnife.bind(this);

        // Remove explicit toolbar creation
        // This also fix onResultAcitivity never be called.

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mPresenter = new NearbyPresenter(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)        // For Places access: Places.GEO_DATA_API,
                    .addApi(Places.PLACE_DETECTION_API) // Places.PLACE_DETECTION_API, .enableAutoManage(this, this)
                    .enableAutoManage(this, this)       // is required
                    .build();

            mGoogleApiClient.connect();
        }

        // Add click listener on Info Window due to call feature
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);

        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);
        mMap.setOnCameraIdleListener(this);


    }

    @Override
    protected void onStop() {
        Utils.Log(TAG, "onStop");
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();

        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Utils.Log(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            getAllPartner();
        } else {
            currentLocation(this);
        }

    }

    private void getAllPartner() {
        final LatLng currentLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        // Control: zoom animation appear once on the first shot!
        if (isFirstLoad) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 8));
            isFirstLoad = false;
        } else
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));

        Utils.Log(TAG, "Last location. lat " + mLastLocation.getLatitude() + " lon " + mLastLocation.getLongitude());

        mPresenter.requestNearbyBranches(mLastLocation.getLatitude(), mLastLocation.getLongitude());
    }

    private void progressStateInterface(int stateNum) {
        switch (stateNum) {
            // Loading succeed
            case -1:
                progressBarNearby.setVisibility(View.VISIBLE);
                parentMap.setVisibility(View.INVISIBLE);
                break;
            case 1:
                progressBarNearby.setVisibility(View.INVISIBLE);
                parentMap.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void showAllMarkerOnmap() {
        // Find the boundaries of the item set
        // item contains a list of GeoPoints
        try {
            if (listMarker.size() > 0) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (Marker marker : listMarker) {
                    builder.include(marker.getPosition());
                }

                LatLngBounds bounds = builder.build();
                int padding = getResources().getDimensionPixelOffset(R.dimen.item_gap_32); // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                mMap.animateCamera(cu, 1000, null);

            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Utils.Log(TAG, "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Utils.Log(TAG, "onConnectionFailed with message: " + connectionResult.getErrorMessage());
    }

    private void currentLocation(Context context) {
        locationManager = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        boolean enabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        Utils.Log(TAG, "GPS enable: " + enabled);

        if (!enabled) {
            // Utilss.snackBar(parentInfo, getString(R.string.please_enable_your_gps));
            Toast.makeText(this, getString(R.string.please_enable_your_gps), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return;
        }

        String provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(NearbyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(NearbyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onInfoWindowClick(Marker marker) {

        if (marker == null) return;

        Utils.Log(TAG, String.format("Marker. Pos: %s, ID: %s, Is shown: %s", marker.getPosition(), marker.getId(), marker.isInfoWindowShown()));

        Utils.Log(TAG, "onInfoWindowClick. Marker: " + marker.getSnippet());

        final String datas[] = marker.getSnippet().split("#");

        /*Launch dialer*/
        if (datas.length > 0) {
            final String uri = String.format("tel:%s", datas[1].trim());
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(uri));

            startActivity(intent);
        } else {
            Utils.Log("Map snippet not contains any data. ", "So phone number is empty");
        }
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        Utils.Log(TAG, "onInfoWindowClose. Marker: " + marker.getSnippet());
    }

    //////
    // S: Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Utils.Log(TAG, "onCreateOptionsMenu");

        this.optionsMenu = menu;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nearby, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Utils.Log(TAG, "onOptionsItemSelected");

        switch (item.getItemId()) {
            case R.id.menu_item_search_maps:
                Utils.Log(TAG, "Search maps");

                try {
                    // Filter search result only for Indonesias' places
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setCountry("id")
                            .build();

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .setFilter(typeFilter)
                                    .build(NearbyActivity.this);

                    Utils.Log(TAG, "Start for result.");

                    NearbyActivity.this.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();

                    Utils.Log(TAG, "Search maps error: " + e.getMessage());

                    GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                            0 /* requestCode */).show();

                } catch (GooglePlayServicesNotAvailableException e) {

                    Utils.Log(TAG, "Search maps error: " + e.getMessage());

                    // Indicates that Google Play Services is not available and the problem is not easily
                    // resolvable.
                    String message = "Google Play Services is not available: " +
                            GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

                    Log.e(TAG, message);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }

                // break;
        }

        // Just to use var
        //optionsMenu.getItem(0);

        return super.onOptionsItemSelected(item);
    }
    // E: Options Menu
    //////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult. Data: " + data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName() + ", lat/lon: " + place.getLatLng());

                // Set current location and fetch vendor around
                Location searchedLocation = new Location(LocationManager.GPS_PROVIDER);
                searchedLocation.setLatitude(place.getLatLng().latitude);
                searchedLocation.setLongitude(place.getLatLng().longitude);

                mLastLocation = searchedLocation;

                getAllPartner();

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i(TAG, "Result cancel");
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Utils.Log(TAG, "Map is clicked!");

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Utils.Log(TAG, "Marker is clicked!");

        Utils.Log(TAG, String.format("Marker. Pos: %s, ID: %s, Is shown: %s", marker.getPosition(), marker.getId(), marker.isInfoWindowShown()));

        Utils.Log(TAG, "onInfoWindowClick. Marker: " + marker.getSnippet());

        return false;
    }

    //////
    // S: On Camera Event Listeners
    @Override
    public void onCameraMoveStarted(int reason) {
        Utils.Log(TAG, "onCameraMoveStarted!");

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            Utils.Log(TAG, "The user gestured on the map. Is first open: " + isFirstLoad);
            isUserInteractWithMap = true;
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
            Utils.Log(TAG, "The user tapped something on the map.");
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {

            Utils.Log(TAG, "The app moved the camera.");
        }
    }

    @Override
    public void onCameraMove() {
        Utils.Log(TAG, "The camera is moving. ");
    }

    @Override
    public void onCameraMoveCanceled() {
        Utils.Log(TAG, "Camera movement canceled.");
    }

    @Override
    public void onCameraIdle() {
        Utils.Log(TAG, "The camera has stopped moving.");

        boolean isInfoWindowOpen = false;
        for (Marker marker : listMarker) {

            Utils.Log(TAG, String.format("Marker: Pos: %s, ID: %s, Is show: %s, Is visible: %s, Is flat: %s, Data: %s",
                    marker.getPosition(),
                    marker.getId(),
                    marker.isInfoWindowShown(),
                    marker.isVisible(),
                    marker.isFlat(),
                    marker.getSnippet()));

            if (marker.isInfoWindowShown()) {
                isInfoWindowOpen = true;
                break;
            }
        }

        if (!isInfoWindowOpen && isUserInteractWithMap) {
            Utils.Log(TAG, "Info window is not open.");
            Location temp = new Location(LocationManager.GPS_PROVIDER);
            temp.setLatitude(mMap.getCameraPosition().target.latitude);
            temp.setLongitude(mMap.getCameraPosition().target.longitude);

            mLastLocation = temp;

            NearbyActivity.this.getAllPartner();

            isUserInteractWithMap = false;

        } else {
            Utils.Log(TAG, "Info window is open.");
        }

    }

    @Override
    public void showNearbyBranches(List<PojoOfficeLocation> officeLocationList) {
        Utils.Log(TAG, "Show nearby branches: " + officeLocationList.size());
        // User interface state to (1) view map
        progressStateInterface(1);

        Utils.Log(TAG, "Success with Partners data size: " + officeLocationList.size());

        if (!listMarker.isEmpty()) {
            for (Marker marker : listMarker) {
                marker.remove();
            }
            listMarker.clear();
        }

        BitmapDescriptor bitmapDescriptor;
        for (PojoOfficeLocation item : officeLocationList) {
            final LatLng markerLoc = new LatLng(item.getBranchLat(), item.getBranchLon());

            // Set Marker Option
            String sData = String.format("%s#%s#%s#%s#%s",
                    item.getBranchName(),
                    item.getBranchPhone(),
                    item.getBranchAddress(),
                    item.getBranchOpenTime(),
                    item.getBranchCloseTime());

            Utils.Logs('w', TAG, "Book status: \"" + item.getBook() + "\"");

            if (item.getBook() == 1) {
                bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
            } else {
                bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            }

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(markerLoc)
                    .icon(bitmapDescriptor)
                    .title(String.format("%s", item.getBranchName()))
                    .snippet(sData);

            listMarker.add(mMap.addMarker(markerOptions));

            // Apply custom adapter for each marker
            mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter(NearbyActivity.this));
        }

        showAllMarkerOnmap();
    }

    @Override
    public void showViewState(UIState uiState) {
        Utils.Log(TAG, "View state: " + uiState);

        if (uiState == UIState.LOADING) {

            progressBarNearby.animate().withStartAction(new Runnable() {
                @Override
                public void run() {
                    progressBarNearby.setVisibility(View.VISIBLE);
                }
            }).alpha(1F);

        } else if (uiState == UIState.FINISHED) {

            progressBarNearby.animate().alpha(0F).withEndAction(new Runnable() {
                @Override
                public void run() {
                    progressBarNearby.setVisibility(View.GONE);
                }
            });

        }

    }

    @Override
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }

    // E: On Camera Event Listeners
    //////
}
