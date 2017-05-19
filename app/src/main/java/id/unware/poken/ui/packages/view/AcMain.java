package id.unware.poken.ui.packages.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

import id.unware.poken.PokenApp;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.interfaces.FragmentProgress;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.tools.BitmapUtil;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.pickup.view.FragmentPickupMap;
import id.unware.poken.ui.wallet.main.view.FragmentWallet;


/*
 * ActivityBase define content view (refer to activity_main_topdrawer).<br/>
 *
 * Inside layout contains FrameLayout with id : parentView. This Frame will be used as
 * a container for fragment:<br/>
 * - Package <br/>
 * - Pickup (Map and Form)<br/>
 * - Map : Where list all vendor/courier. <br/>
 * - Tariff Check <br/>
 * - Postcode <br />
 *
 * @see FragmentPackage
 * @see FragmentPickupMap
 * @see FragmentMap
 * @see FragmentTariffCheck
 * @see FragmentPostcode
 *
 * @since
 * Jan 10 2017 - [V47] Collect user behavior to determine which feature is used most <br />
 * Dec 19 2016 - Replace fragment when it's not available on container {@code R.id.fragmentMain} <br />
 * Nov 11 2016 - Implement {@link FragmentProgress} to show {@link ProgressDialog} with
 *               host activity control. <br />
 * Nov 7 2016 - No more FragmentPickup, but open {@link FragmentPickupMap} directly.
 *
 */
public class AcMain extends AcMainBase implements
        FragmentProgress, /*Control Progress dialog.*/
        FragmentPackage.PackageFragmentListener
        //,
//        FragmentPickupMap.FragmentPickupMapListener
//        , FragmentPickupForm.Listener
//        , FragmentPickupForm.FragmentPickupFormListener
{

    private final String TAG = "AcMain";

    private Bundle mSavedState;

    private Fragment mCurrentFragmentOnMain = null;

    // Fragments to show on this Activity
    private FragmentPackage fragmentPackage;

    /**
     * Not cancellable {@code ProgressDialog}
     */
    private ProgressDialog loading;

    private SPHelper spInstance;

    public static FragmentManager fragmentManager;

    /**
     * Last opened drawer positon saved instance key.
     */
    private final String KEY_LAST_PAGE = "last_page";
//    private int mLastNavItemId = Constants.POSITION_PACKAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.Logs('w', TAG, "MAIN ON CREATE!!!");

        /** INFO!: Content view already defined in Super Class (AcMainBase) */

        fragmentManager = this.getSupportFragmentManager();
        mSavedState = savedInstanceState;
        mCurrentFragmentOnMain = Utils.getCurrentFragment(this, R.id.fragmentMain);

        // Open fragment based on last opened fragment when
        // savedInstanceState available
        int tagLastPage;

        if (mSavedState != null) {

            tagLastPage = savedInstanceState.getInt(KEY_LAST_PAGE);

            Utils.Log(TAG, "Last opened fragment tag: " + tagLastPage);
            Utils.Logs('i', TAG, "Fragment from saved instance: " + mCurrentFragmentOnMain);

//            if (tagLastPage != AppClass.POSITION_SUPPORT
//                    && tagLastPage != AppClass.POSITION_SETTINGS) {
//                // Make sure active drawer item is highlighted.
//                setHighlightActivePage(tagLastPage);
//                onNavigationDrawerItemSelected(tagLastPage, true);
//
//                Utils.Log(TAG, "OPEN LAST TAG: " + tagLastPage);
//            }

        } else {

            Utils.Logs('w', TAG, "Saved instance is NULL.");

            // Show package for the first time load.
//            setHighlightActivePage(Constants.POSITION_PACKAGE);
//            onNavigationDrawerItemSelected(AppClass.POSITION_PACKAGE, true);
        }

        initToolbar();


        spInstance = SPHelper.getInstance();

        showPaketIdTutorial();

        showPackage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utils.Log(TAG, String.format(Locale.US, "Req. : %d, res. : %d, intent data: %s", requestCode, resultCode, data));

        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case Constants.TAG_NEW_PACKAGE:
                    Utils.Log(TAG, "Case tag new package");
                    if (data.hasExtra(Constants.EXTRA_REFRESH_PAGE) &&
                            data.hasExtra(Constants.EXTRA_ARRAYLIST_BOOKING_ID)) {

                        if (data.getBooleanExtra(Constants.EXTRA_REFRESH_PAGE, false)) {

                            if (fragmentPackage != null) {
                                fragmentPackage.checkNewPackage(data.getStringArrayListExtra(Constants.EXTRA_ARRAYLIST_BOOKING_ID));
                            } else {
                                Fragment fragment = Utils.getCurrentFragment(this, R.id.fragmentMain);
                                if (fragment != null && fragment instanceof FragmentPackage) {
                                    ((FragmentPackage) fragment).checkNewPackage(data.getStringArrayListExtra(Constants.EXTRA_ARRAYLIST_BOOKING_ID));
                                }
                            }
                        }
                    }
                    break;
                case Constants.TAG_PACKAGE_DETAIL:
                    Utils.Log(TAG, "Case tag package detail");

                    if (data.hasExtra(PojoBooking.KEY_BOOKING_ID)) {
                        long bookingId = data.getLongExtra(PojoBooking.KEY_BOOKING_ID, 0L);

                        Utils.Logs('i', TAG, "Result data from PackageDetail: " + bookingId);

                        if (fragmentPackage != null) {
                            Utils.Logs('i', TAG, "Fragment Package still available");
                            fragmentPackage.updatePackageListItem(bookingId, 0);
                        } else {
                            Utils.Logs('w', TAG, "Fragment Package not available anymore");
                            Fragment fragment = Utils.getCurrentFragment(this, R.id.fragmentMain);
                            if (fragment != null && fragment instanceof FragmentPackage) {
                                ((FragmentPackage) fragment).updatePackageListItem(bookingId, 0);
                            }
                        }
                    }

                    break;

                case Constants.TAG_SUPPORT:
                case Constants.TAG_HELP:

                    Utils.Log(TAG, "Case tag help and support");

                    if (data.hasExtra(Constants.EXTRA_TRIGGER_TUTORIAL) &&
                            data.getBooleanExtra(Constants.EXTRA_TRIGGER_TUTORIAL, false)) {
                        Utils.Logs('i', TAG, "TUTORIAL HAS TRIGGERED!");

                        Fragment fragment = Utils.getCurrentFragment(this, R.id.fragmentMain);

                        if (fragment != null && !(fragment instanceof FragmentPackage)) {

                            showPackage();
                        }

                    } else {
                        Utils.Logs('e', TAG, "NO TUTORIAL TRIGGERED!");
                    }

                    break;

                // Receive from "Pickup from here" feature
                case Constants.TAG_PICKUP_HISTORY:

                    Utils.Logs('i', TAG, "TAG_PICKUP_HISTORY TRIGGERED!");

                    String pickup_id = data.getStringExtra(PojoPickupHistory.KEY_PICKUP_ID);

                    showForm(null, pickup_id);
                    break;

                default:
                    Utils.Logs('e', TAG, "No tag found!");
            }
        }
    }


//    @Override
//    public void onNavigationDrawerItemSelected(int itemId, boolean forceChange) {
//
//        // Implement Super class abstract method which is bridge drawer item click
//        Utils.Logs('i', TAG, "OPEN PAGE: " + itemId);
//
//        // Set last selected item
////        mLastNavItemId = itemId;
//
//        if (itemId != AppClass.POSITION_SETTINGS && itemId != AppClass.POSITION_SUPPORT) {
//            AppClass.getInstance().lastNavDrawerItemId = itemId;
//        }
//
//        switch (itemId) {
//            case AppClass.POSITION_PACKAGE:
//                showPackage();
//                break;
//            case AppClass.POSITION_PICKUP:
//                showPickupMap();
//                break;
//            case AppClass.POSITION_WALLET:
//                showWallet();
//                break;
//            case AppClass.POSITION_LOCATION:
//                showCourierMap();  // Open vendor/courier location
//                break;
//            case AppClass.POSITION_TARIFF:
//                showChekHarga(null);
//                break;
//            case AppClass.POSITION_POSTCODE:
//                showPostcode();
//                break;
//            case AppClass.POSITION_SETTINGS:
//                openSettings();
//                break;
//            case AppClass.POSITION_SUPPORT:
//                openSupport();
//                break;
//            case AppClass.POSITION_HELP:
//                openHelp();
//                break;
//        }
//    }

    /**
     * Wallet
     *
     * @since V49 - NEW
     */
    private void showWallet() {
//        MyLog.FabricTrackContentView("Wallet" /*Page name*/, "Page" /*Type*/, String.valueOf(AppClass.TAG_SUPPORT) /*Feature tag*/);

        setTitle(this.getString(R.string.title_wallet));

        Fragment fragment = Utils.getCurrentFragment(this, R.id.fragmentMain);

        if (fragment != null && fragment instanceof FragmentWallet) {
            MyLog.FabricLog(Log.INFO, "Open Wallet menu: Fragment already on container");
        } else {

            MyLog.FabricLog(Log.INFO, "Open Postcode menu");

            FragmentWallet fragmentWallet = FragmentWallet.newInstance();
            Utils.changeFragment(this, R.id.fragmentMain, fragmentWallet);
        }

    }

    /**
     * Help and Support menu.
     */
    private void openSupport() {
//        MyLog.FabricTrackContentView("Support" /*Page name*/, "Page" /*Type*/, String.valueOf(AppClass.TAG_SUPPORT) /*Feature tag*/);
//
//        Intent intent = new Intent(this, AcSettings.class);
//        intent.putExtra(AppClass.USE_FRAGMENT, AppClass.TAG_SUPPORT);
//        startActivityForResult(intent, AppClass.TAG_SUPPORT);
    }


    /**
     * [NOT AVAILABLE YET]
     * Open Help and Support page
     *
     * @since Jan 9 - V49 : Only contain Help <br />
     * (Sep 20, 2016) Version 40
     */
    private void openHelp() {
//        MyLog.FabricTrackContentView("Help"/*Page name*/, "Page", String.valueOf(AppClass.TAG_HELP) /*Feature tag*/);
//
//        Intent intent = new Intent(this, AcSettings.class);
//        intent.putExtra(AppClass.USE_FRAGMENT, AppClass.TAG_HELP);
//        startActivityForResult(intent, AppClass.TAG_HELP);
    }

    /**
     * Launch Settings activity when clicked "Settings".
     */
    private void openSettings() {
//        MyLog.FabricTrackContentView("Settings"/*Page name*/, "Page", String.valueOf(AppClass.TAG_SETTINGS) /*Feature tag*/);
//
//        Intent intent = new Intent(this, AcSettings.class);
//        intent.putExtra(AppClass.USE_FRAGMENT, AppClass.TAG_SETTINGS);
//        startActivity(intent);
    }

    /**
     * Open Pickup Page which is contain two part Pickup for Map chooser and Pickup for
     * more form such as extra detail for driver, vehicle option, and NEW! vendor options.
     * Pickup Map can be open when there is at least one Package to pick.
     *
     * @since Nov 8 2016 - Open {@link FragmentPickupMap} directly instead of FragmentPickup.
     */
    private void showPickupMap() {
        // Cek is Package available (Always false for now)
        // noinspection ConstantConditions
//        if (isPackageAvailable()) {
//            // Confirm to open New Package
//            ControllerDialog.getInstance().showYesNoDialog(
//                this.getString(R.string.msg_confirm_no_package),
//                this, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if ( which == Dialog.BUTTON_POSITIVE) {
//
//                            if (Utils.isNetworkNotConnected(AcMain.this)) {
//                                Utils.snackBar(parentView, getString(R.string.msg_package_no_network));
//                            } else {
//                                Intent intent = new Intent(AcMain.this, AcNewPaket.class);
//                                startActivityForResult(intent, Constants.TAG_NEW_PACKAGE);
//                            }
//
//                        } else {
//                            dialog.dismiss();
//                        }
//
//                    }
//                }, this.getString(R.string.btn_positive_create), this.getString(R.string.btn_negative_no));
//
//        } else {
//
////            MyLog.FabricTrackContentView("Pickup Map"/*Page name*/, "Page", String.valueOf(AppClass.TAG_PICKUP_MAP) /*Feature tag*/);
//
//            setTitle(this.getString(R.string.title_pickup));
//
//            Fragment fragment = Utils.getCurrentFragment(this, R.id.fragmentMain);
//
//            if (fragment != null && fragment instanceof FragmentPickupMap) {
//                MyLog.FabricLog(Log.INFO, "Open Pickup Map: Fragment already on container");
//            } else {
//
//                MyLog.FabricLog(Log.INFO, "Open Pickup Map: Replace from container");
//
//                FragmentPickupMap fragmentPickupMap = new FragmentPickupMap();
//                fragmentPickupMap.setListener(AcMain.this);  // FragmentPickupMapListener
//
//                Utils.changeFragment(this, R.id.fragmentMain, fragmentPickupMap);
//            }
//        }
    }

    /**
     * Open Pickup Form fragment and replace to current fragment container.
     *
     * @param selectedAddress {@link Address}
     * @param id              String pickup_id
     * @since Nov 7 2016 - NEW!
     */
    private void showForm(Address selectedAddress, String id) {

//        MyLog.FabricTrackContentView("Pickup Form"/*Page name*/, "Page", String.valueOf(AppClass.TAG_PICKUP_FORM) /*Feature tag*/);

//        FragmentPickupForm fragmentPickupForm = new FragmentPickupForm();
//        fragmentPickupForm.setListener(this);
//
//        if (!StringUtils.isEmpty(id)) {
//            Utils.Log(TAG, "Pickup id is found.");
//
//            Bundle bndl = new Bundle();
//            bndl.putString(FragmentPickupForm.ID_HISTORY_PICKUP, id);
//            fragmentPickupForm.setArguments(bndl);
//        }
//
//        if (selectedAddress != null) {
//            Utils.Log(TAG, "Address is found.");
//
//            Bundle bndl = new Bundle();
//            bndl.putParcelable(AppClass.EXTRA_SELECTED_ADDRESS, selectedAddress);
//            fragmentPickupForm.setArguments(bndl);
//        }
//
//        Utils.changeFragment(this, R.id.fragmentMain, fragmentPickupForm);
    }

    /**
     * -NOT AVAILABLE YET, DUE BACK-END IN PROGRESS-
     * Check if Package availablity.
     * Pickup request only available when there is at least one Package to pick.
     *
     * @return boolean : Is Package available.
     */
    private boolean isPackageAvailable() {
        return false;
        // return SPHelper.getInstance().getSharedPreferences(AppClass.SHARED_IS_PACKAGE_EMPTY, false);
    }

    /**
     * Package list.
     *
     * @since Nov 8 2016 - Highlight drawer item at package position.
     */
    private void showPackage() {

        Utils.Logs('w', TAG, "Show Package");

//        MyLog.FabricTrackContentView("Package List"/*Page name*/, "Page", String.valueOf(AppClass.TAG_PACKAGE) /*Feature tag*/);

        setTitle(this.getString(R.string.title_package));

        Fragment fragment = Utils.getCurrentFragment(this, R.id.fragmentMain);

        if (fragment != null && fragment instanceof FragmentPackage) {
            MyLog.FabricLog(Log.INFO, "Open Package List: Replace from container");
        } else {

            MyLog.FabricLog(Log.INFO, "Create New Package Fragment");

            fragmentPackage = FragmentPackage.newInstance();
            Utils.changeFragment(this, R.id.fragmentMain, fragmentPackage);
        }
    }

    /**
     * Partner Location on map.
     */
    private void showCourierMap() {

//        MyLog.FabricTrackContentView("Package Map"/*Page name*/, "Page", String.valueOf(AppClass.TAG_MAP) /*Feature tag*/);
//
//        setTitle(this.getString(R.string.title_partner_location));
//
//        Fragment fragment = Utils.getCurrentFragment(this, R.id.fragmentMain);
//
//        if (fragment != null && fragment instanceof FragmentMap) {
//            MyLog.FabricLog(Log.INFO, "Open Courier Map (vendors location): Fragment already on container");
//        } else {
//
//            MyLog.FabricLog(Log.INFO, "Open Courier Map (vendors location)");
//
//            FragmentMap fragmentMap = new FragmentMap();
//            Utils.changeFragment(this, R.id.fragmentMain, fragmentMap);
//        }
    }

    /**
     * Tariff fragment.
     */
    private void showChekHarga(Bundle bndl) {

//        MyLog.FabricTrackContentView("Tariff Check"/*Page name*/, "Page", String.valueOf(AppClass.TAG_TARIFF_CHECK) /*Feature tag*/);
//
//        setTitle(this.getString(R.string.title_tariff));
//
//        Fragment fragment = Utils.getCurrentFragment(this, R.id.fragmentMain);
//
//        if (fragment != null && fragment instanceof FragmentTariffCheck) {
//            MyLog.FabricLog(Log.INFO, "Open Tariff Check: Fragment already on container");
//        } else {
//
//            MyLog.FabricLog(Log.INFO, "Open Tariff Check");
//
//            FragmentTariffCheck fragmentCheckHarga = new FragmentTariffCheck();
//            if (bndl != null) {
//                fragmentCheckHarga.setArguments(bndl.getBundle("data_bundle"));
//            }
//            Utils.changeFragment(this, R.id.fragmentMain, fragmentCheckHarga);
//        }
    }

    /**
     * Postal code search.
     *
     * @since Nov 18 2016 - NEW!
     */
    private void showPostcode() {

//        MyLog.FabricTrackContentView("Postcode"/*Page name*/, "Page", String.valueOf(AppClass.TAG_POSTCODE) /*Feature tag*/);
//
//        setTitle(this.getString(R.string.title_postcode));
//
//        Fragment fragment = Utils.getCurrentFragment(this, R.id.fragmentMain);
//
//        if (fragment != null && fragment instanceof FragmentPostcode) {
//            MyLog.FabricLog(Log.INFO, "Open Postcode menu: Fragment already on container");
//        } else {
//
//            MyLog.FabricLog(Log.INFO, "Open Postcode menu");
//
//            FragmentPostcode fragmentPostcode = new FragmentPostcode();
//            Utils.changeFragment(this, R.id.fragmentMain, fragmentPostcode);
//        }
    }

    /**
     * Init SupportActionBar include click on toolbar,
     * then scroll Package list to top.
     */
    private void initToolbar() {
        super.baseToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = Utils.getCurrentFragment(AcMain.this, R.id.fragmentMain);

                if (fragment != null && fragment instanceof FragmentPackage) {
                    Utils.Log(TAG, "On click on toolbar. View: " + view);

                    if (fragmentPackage != null) {
                        fragmentPackage.scrollListToTop();
                    } else {
                        ((FragmentPackage) fragment).scrollListToTop();
                    }
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

//        Utils.Log(TAG, "Save instance state. Time to save last page position: " + mLastNavItemId);
//
//        outState.putInt(KEY_LAST_PAGE, mLastNavItemId);
//        outState.putBundle("data_bundle", AppClass.getInstance().getBndl());

        super.onSaveInstanceState(outState);
    }

//    @Override
//    public void onBackPressed() {
//        Fragment fragment = Utils.getCurrentFragment(this, R.id.fragmentMain);
//
//        if (mNavigationDrawerFragment.isDrawerOpen()) {
//            super.onBackPressed();
//        } else if (fragment instanceof FragmentPackage) {
//
//            // Make sure tutorial is off when user press back button
//            AppClass.isTutorialMode = false;
//            spInstance.setPreferences(AppClass.SHARED_LEARNT_OPEN_CREATE_PACKAGE, false);
//
//            super.onBackPressed();
//
//        } else if (fragment instanceof FragmentPickupForm) {
//            // Back to Pickup Map when active fragment is Pickup Form
//            setHighlightActivePage(AppClass.POSITION_PICKUP);
//            showPickupMap();
//
//        } else {
//            setHighlightActivePage(AppClass.POSITION_PACKAGE);
//            onNavigationDrawerItemSelected(AppClass.POSITION_PACKAGE, true);
//        }
//
//    }

//    @Override
//    public void onSuccessPickup() {
//        // showPackage();
//
//    }

    @Override
    protected void onResume() {
        super.onResume();

        // Make sure user is logged in, close the activity if not
//        if (spInstance.getSharedPreferences(AppClass.SHARED_EMAIL, "").equals("")) {
//            Intent intent = new Intent(this, AcLoginMenu.class);
//            startActivity(intent);
//            finish();
//        }
//
//        mNavigationDrawerFragment.updateProfile();
    }

    @Override
    protected void onDestroy() {
        Utils.Logs('w', TAG, "onDestroy Main Activity.");
        PokenApp.getInstance().cancelPendingRequests(AcMain.class);

        // Make sure appear PorgressDialog is closed before destroying activity.
        closeLoading();

        super.onDestroy();
    }

    private void showPaketIdTutorial() {
//        if (AppClass.isTutorialMode) {
//
//            /** Show tutorial how to open navigation drawer in order to request a pickup.
//             * In this case user must successfully following how to create New Package.
//             */
//            Utils.Log(TAG, "Proceed New Package -> " + spInstance.getSharedPreferences(AppClass.SHARED_LEARNT_PROCEDD_CREATE, false) );
//            Utils.Log(TAG, "Open Pickup Request -> " + spInstance.getSharedPreferences(AppClass.SHARED_LEARNT_PIREQ_OPEN_NAVDRAW, false) );
//
//            if (spInstance.getSharedPreferences(AppClass.SHARED_LEARNT_PROCEDD_CREATE, false) &&
//                    spInstance.getSharedPreferences(AppClass.SHARED_LEARNT_PIREQ_OPEN_NAVDRAW, false)) {
//
//                Utils.Log(TAG, "Show tutorial hamburger");
//
//                Utils.showMatrialTutorial(
//                        super.hamburgerButton,
//                        AppClass.SHARED_LEARNT_PIREQ_OPEN_NAVDRAW,
//                        this.getString(R.string.tutorial_open_nav_drawer),
//                        this,
//                        true,
//                        false,  // Dismissable
//                        new MaterialIntroListener() {
//                            @Override
//                            public void onUserClicked(String materialIntroViewId) {
//                                Utils.Log(TAG, "Material clicked: " + materialIntroViewId);
//                                switch (materialIntroViewId) {
//                                    case AppClass.SHARED_LEARNT_PIREQ:
//
//                                        // Set open create New Package as learnt.
//                                        spInstance.setPreferences(AppClass.SHARED_LEARNT_PIREQ_OPEN_NAVDRAW, true);
//
//                                        break;
//                                }
//                            }
//                        }
//                );
//            }
//        }
    }

    /**
     * Set activate item on Navigation drawer.
     *
     * @param itemId Active list item position.
     * @since Feb 27 - [V49] Item highlighting based on item id.
     */
    private void setHighlightActivePage(int itemId) {
        Utils.Log(TAG, "HIGHLIGHT POS : " + itemId);
//        mNavigationDrawerFragment.setNavigationDrawerItemSelected(itemId, true);
    }

    //////
    // S: Controll Progress dialog on fragment
    @Override
    public void showProgress(boolean isShow, Object progressTag) {
        if (isShow) {
            showLoading(progressTag);
        } else {
            closeLoading();
        }
    }

    private void showLoading(final Object progressTag) {
        if (loading == null) {

            // Parse progress dialog tag object
            String strTitle = this.getString(R.string.loading);
            String strContent = this.getString(R.string.msg_loading_data);
//            if (progressTag instanceof HashMap) {
//                Utils.Logs('i', TAG, "Hashmap object is found on progressTag");
//                Map<String, Object> progressObject = (Map<String, Object>) progressTag;
//
//                Utils.Log(TAG, "Message msg_title: " + progressObject.get("msg_title"));
//                Utils.Log(TAG, "Message content: " + progressObject.get("msg_content"));
//                Utils.Log(TAG, "Class tag: " + progressObject.get("progress_object_tag"));
//
//                strTitle = String.valueOf(progressObject.get("msg_title"));
//                strContent = String.valueOf(progressObject.get("msg_content"));
//            }

            // Generate general progress dialog.
            loading = ControllerDialog.getInstance().showLoading(
                    this,
                    strTitle,
                    strContent);

            loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Utils.devModeToast(AcMain.this, "Request cancelled.");
                    Utils.Log(TAG, "Cancel request for: " + progressTag);
                    PokenApp.getInstance().cancelPendingRequests(progressTag);
                }
            });

            loading.show();
        } else {
            loading.show();
        }
    }

    private void closeLoading() {
        if (loading != null) {
            loading.dismiss();
        }
    }
    // E: Controll Progress dialog on fragment
    //////

    //////
    // S: FragmentPackage Listener
    @Override
    public void onClickRequestPickup() {
        Utils.Log(TAG, "On Click Button Request Pickup on Fragment Package");

        // Set nav drawer active item on "Pickup" item, when user click on "Request pickup"
        // on Package list.
//        mNavigationDrawerFragment.selectNavigationDrawerActiveItem(AppClass.POSITION_PICKUP);
    }

    @Override
    public void onFilterApplied(ArrayList<String> appliedFilter, int itemCount) {
        Utils.Log(TAG, "Applied filter: " + appliedFilter);

        if (appliedFilter != null && appliedFilter.size() > 0) {
            Menu menu = super.baseToolbar.getMenu();

            if (menu != null) {
                Utils.Log(TAG, "Modify menu item");

                MenuItem menuItem = menu.findItem(R.id.action_filter);

                if (menuItem != null) {
                    Utils.Log(TAG, "Change color filter");
                    menuItem.getIcon().setColorFilter(BitmapUtil.getEnabledColor(this));
                }
            }

        } else {

            Menu menu = super.baseToolbar.getMenu();

            if (menu != null) {
                Utils.Log(TAG, "Replace menu item");

                MenuItem menuItem = menu.findItem(R.id.action_filter);

                if (menuItem != null) {
                    Utils.Log(TAG, "Replace color filter");
                    menuItem.getIcon().setColorFilter(BitmapUtil.getDrawableFilter(this, R.color.white_100));
                }
            }
        }
    }
    // E: FragmentPackage Listener
    //////


    /////
    // S: FragmentPickupMapListener and PickupForm Listener
//    @Override
//    public void onSelectedAddress(Address selectedAddress) {
//        showForm(selectedAddress, "");
//    }


//    @Override
//    public void onSetLocationClicked() {
//        showPickupMap();
//    }
    // E: FragmentPickupMapListener and PickupForm Listener
    /////
}
