package id.unware.poken.ui.packages.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.PokenApp;
import id.unware.poken.R;
import id.unware.poken.interfaces.PackageFiltersListener;
import id.unware.poken.pojo.GeneralListItem;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.detailPackage.ActivityMainWithUp;
import id.unware.poken.ui.pokenaccount.login.view.fragment.FragmentLogin;
import id.unware.poken.ui.main.model.CompanionButton;
import id.unware.poken.ui.newPackage.view.AcNewPaket;
import id.unware.poken.ui.packages.presenter.PackagesPresenterImpl;
import id.unware.poken.ui.packages.view.adapters.AdapterPackageMain;

/*
 * Fragment to show Packages. Hosted on <code>AcMain</code>
 *
 * @see AcMain
 * @since Sep 10, 2016 - Implement expandable RecyclerView based on Booking Status
 */
public class FragmentPackage extends BaseFragment implements
        PackagesView {

    private final String TAG = "FragmentPackage";

    private PackagesPresenterImpl packagesPresenter;

    @BindView(R.id.fabCreatePackage) FloatingActionButton fabCreatePackage;

    @BindView(R.id.recyclerViewMain) RecyclerView recyclerViewMain;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    private PackageFragmentListener listener;

    private List<Object> mListPojoBooking = new ArrayList<>();
    private AdapterPackageMain adapterPackage;

    /**
     * Determine which empty state view will be showed.
     */
    private View rootView;

    /**
     * String tog for dialog fragment
     */
    private String mStringLastTag;
    private ArrayList<String> arrayListNewBookingId = new ArrayList<String>();

    private final String KEY_RECYCLER_STATE = "recycler_state";

    private Handler handler = new Handler();

    private PokenApp mAppClass = PokenApp.getInstance();

    public static FragmentPackage newInstance() {
        return new FragmentPackage();
    }

    public FragmentPackage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Utils.Logs('w', TAG, "ON CREATE ALL VIEW");

        rootView = inflater.inflate(R.layout.f_package, container, false);
        ButterKnife.bind(this, rootView);

        packagesPresenter = new PackagesPresenterImpl(FragmentPackage.this);

        initView();

        // Load all package data from db
        // when loading finished, update adapter
        packagesPresenter.getLocalPackageList();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Utils.Logs('i', TAG, "RESTORE PACKAGE LIST FRAGMENT");
            Parcelable listState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            if (listState != null) {
                Utils.Logs('i', TAG, "1 listState Available");
                recyclerViewMain.getLayoutManager().onRestoreInstanceState(listState);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Utils.Logs('i', TAG, "On Save Instance State");

        // save RecyclerView state
        Parcelable listState = recyclerViewMain.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_RECYCLER_STATE, listState);

        super.onSaveInstanceState(outState);
    }

    // Handle when Fragment is visible or not
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Utils.Logs('w', TAG, "IS HIDDEN");
            // Stop on-going process
        } else {
            Utils.Logs('i', TAG, "IS NOT HIDDEN");
            if (listener != null) {
                CompanionButton companionButton = new CompanionButton();
                companionButton.setText(parent.getString(R.string.action_search));
//                listener.setupMainCompanionButton(companionButton);
            }
        }
    }

    private void initPackageListItem(List<PojoBooking> pojoBookingList) {
        mListPojoBooking.clear();
        mListPojoBooking.addAll(pojoBookingList);
        adapterPackage.replaceAllPackage(pojoBookingList);

        // Fabric log for view side item
        MyLog.FabricLog(Log.INFO, TAG + " - FINAL ADAPTER SIZE: " + adapterPackage.getItemCount());
        MyLog.FabricLog(Log.INFO, TAG + " - FINAL LIST SIZE: " + mListPojoBooking.size());
    }

    private void initPackageList() {
        adapterPackage = new AdapterPackageMain(parent, mListPojoBooking, packagesPresenter);
        recyclerViewMain.setLayoutManager(
                new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false)
        );
        recyclerViewMain.setAdapter(adapterPackage);
        recyclerViewMain.setHasFixedSize(true);
    }

    /**
     * Show filter dialog. The dialog implement {@code BottomSheetDialogFragment}
     *
     * @since March 12 2017 - V54 Make public. <br/>
     * Dec 15 2016 - V46 NEW
     */
    public void showFilter() {

        String filtersPackageTag = "dialog-filter-packages";
        mStringLastTag = filtersPackageTag;

        FragmentTransaction ft = hideDialog(filtersPackageTag);
        ft.addToBackStack(null);

        BookingFiltersDialog bookingFiltersDialog = BookingFiltersDialog.newInstance();
        bookingFiltersDialog.setCallbacks(new PackageFiltersListener() {
            @Override
            public void onFilterItemClick(String bookingStatus) {
                Utils.Log(TAG, "Filter item clicked: " + bookingStatus);
                hideDialog(mStringLastTag);

                if (bookingStatus.isEmpty()) return;

                ArrayList<String> selectedFilter = new ArrayList<String>();
                if (!bookingStatus.equals(parent.getString(R.string.lbl_filter_all_packages))) {
                    selectedFilter.add(bookingStatus);
                }
                packagesPresenter.getLocalFilteredList(selectedFilter);

            }

            @Override
            public void onFilterDone(ArrayList<String> statusTextList) {
                Utils.Log(TAG, "Filter done clicked. Selected: " + statusTextList);
            }

            @Override
            public void onFilterReset() {
                Utils.Log(TAG, "Filter reset clicked");
            }
        });

        bookingFiltersDialog.show(ft, filtersPackageTag);
    }

    /**
     * Open fullscreen search dialog.
     *
     * @since Dec 15 2016 - V46 NEW
     */
    public void showSearchPage() {

        final String searchPackageDialogTag = "dialog-search-packages";
        mStringLastTag = searchPackageDialogTag;

        FragmentTransaction ft = hideDialog(searchPackageDialogTag);
        ft.addToBackStack(null);

        FragmentDialogSearchPackage newFragment = FragmentDialogSearchPackage.newInstance();
        newFragment.setCallbacks(new FragmentDialogSearchPackage.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(PojoBooking item) {

                if (item == null) return;

                Utils.Log(TAG, "List item clicked for " + item.getBooking_id());

                showDetail(item.getBooking_id());
            }
        });

        newFragment.show(ft, searchPackageDialogTag);
    }

    /**
     * Scroll to top of the list.
     */
    public void scrollListToTop() {
        if (recyclerViewMain != null && recyclerViewMain.getLayoutManager() != null) {

            LinearLayoutManager layoutManager =
                    (LinearLayoutManager) recyclerViewMain.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(0, 0);
        }
    }

    private void initView() {

        // Init RecyclerView with empty data
        initPackageList();

        // Init FAB click listener
        fabCreatePackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                packagesPresenter.startNewPackage();
            }
        });

        // Pull to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        Utils.Log(TAG, "Pause!!!");

    }

    @Override
    public void onResume() {
        super.onResume();

        Utils.Log(TAG, "Resume!!!");

//        showPackageTutorial();
    }

    /**
     * Show tutorial to open New Package when in tutorial mode ON
     * Highlight FAB
     */
    public void showPackageTutorial() {
//        Utils.Log(TAG, "Begin showing tutorial -> " + AppClass.isTutorialMode);
//        if (AppClass.isTutorialMode) {
//
//            AppClass.isTutorialMode = false;
//
//            // When user not sure how to open Create New Package
//            if (SPHelper.getInstance().getSharedPreferences(AppClass.SHARED_LEARNT_OPEN_CREATE_PACKAGE, false)) {
//                Utils.showMatrialTutorial(
//                        fabCreatePackage,
//                        AppClass.SHARED_LEARNT_OPEN_CREATE_PACKAGE,
//                        parent.getString(R.string.tutorial_new_package),
//                        getActivity(),
//                        BuildConfig.DEV_MODE,  // Dot animation when on DEV_MODE
//                        false,
//                        new MaterialIntroListener() {
//                            @Override
//                            public void onUserClicked(String materialIntroViewId) {
//                                Utils.Log(TAG, "Material clicked: " + materialIntroViewId);
//                                switch (materialIntroViewId) {
//                                    case AppClass.SHARED_LEARNT_OPEN_CREATE_PACKAGE:
//
//                                        // Set open create New Package as learnt.
//                                        SPHelper.getInstance().setPreferences(AppClass.SHARED_LEARNT_OPEN_CREATE_PACKAGE, true);
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
     * Called via on refresh on RecyclerView.
     */
    private void pullToRefresh() {

        Utils.Log(TAG, "Begin - Pull to refresh");

        if (Utils.isNetworkNotConnected(parent)) {
            Utils.snackBar(rootView, parent.getString(R.string.this_is_an_offline_mode), Log.WARN);

            packagesPresenter.getLocalPackageList();

        } else {

            Utils.Logs('w', TAG, "REQUEST get_data. Call refresh checkNewPackage with no PARAM");

            // Begin online data downloading
            // FALSE : Not reload all data, but use updated-on
            packagesPresenter.requestPackagesOnline(false);

        }
    }

    /*
     * Add new data to list when user is from <code>FragmentNewPackage2</code>.
     *
     * @param bookingIds ArrayList of booking_id String
     * @see FragmentNewPackage2
     * @since Sep 14, 2016 - vc. 40
     */
    public void checkNewPackage(ArrayList<String> bookingIds) {
        Utils.Log(TAG, "Call refresh checkNewPackage with PARAM: " + bookingIds);
        pullToRefresh();
//        Intent intent = new Intent(parent, AcNewPaket.class);
//        intent.putExtra(Constants.EXTRA_IS_TUTORIAL, isTutorial);
//        parent.startActivityForResult(intent, Constants.TAG_NEW_PACKAGE);
//        if (!arrayListNewBookingId.isEmpty()) {
//            arrayListNewBookingId.clear();
//        }
//
//        arrayListNewBookingId.addAll(bookingIds);
//
//        // Begin online data downloading
//        // when finished refresh adapter by calling refresh()
//        // FALSE : Not reload all data, but use updated-on
//        packagesPresenter.requestPackagesOnline(false);
    }

    public void updatePackageListItem(long bookingId, int pos) {
        // Delete package by search item location (index) on the list
        packagesPresenter.deletePackage(bookingId, pos);
    }

    /*
     * Show detail package.
     *
     * @param bookingId : {@link PojoBooking#booking_id}
     *
     * @since Nov 29 2016 - Nomore {@link ControllerRealm} calling in order to just get booking_id.
     */
    private void showDetail(long booking_id) {

        if (booking_id < 0L) {
            Utils.toast(parent, parent.getString(R.string.msg_no_package));
            return;
        }

        // BottomStyle detail
//        String packageDetailTag = "dialog-package-detail";
//        mStringLastTag = packageDetailTag;
//
//        FragmentTransaction ft = hideDialog(packageDetailTag);
//        ft.addToBackStack(null);
//
//        FragmentPackageDetail fragmentPackageDetail = FragmentPackageDetail.newInstance(booking_id);
//        fragmentPackageDetail.setParent(parent);
//        fragmentPackageDetail.show(ft, packageDetailTag);

        Bundle bndlDetail = new Bundle();
        bndlDetail.putLong(PojoBooking.KEY_BOOKING_ID, booking_id);

        mAppClass.idDetail = mAppClass.idDetail > -1 ? booking_id : mAppClass.idDetail;

        Intent intentPackDetail = new Intent(parent, ActivityMainWithUp.class);
        intentPackDetail.putExtra(Constants.USE_FRAGMENT, Constants.TAG_PACKAGE_DETAIL);
        intentPackDetail.putExtra(Constants.EXTRA_POJO_BOOKING_DATA, bndlDetail);
        parent.startActivityForResult(intentPackDetail, Constants.TAG_PACKAGE_DETAIL);
    }

    /**
     * Update fragment interface based on package availability.
     *
     * @param uiState : {@link UIState} to indicate package availability.
     */
    private void packageInterfaceState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                setRefreshing(true);
                break;
            case NODATA:
                setRefreshing(false);
                // Create empty state view
                setEmptyStateView();

                break;
            case FINISHED:
                setRefreshing(false);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                break;
        }

    }

    /**
     * Setup empty state view on Home page.
     * <br />
     * <p>
     * Text on empty state view is clickable.
     */
    private void setEmptyStateView() {
        GeneralListItem emptyItem = new GeneralListItem();
        emptyItem.setTitle(parent.getString(R.string.lbl_no_package));
        emptyItem.setListIcon(R.drawable.empty_state_package);

        adapterPackage.replaceWithEmptyItem(emptyItem);
    }

    /**
     * Set refreshing status on {@link SwipeRefreshLayout} while loading network data.
     *
     * @param isRefreshing Whether show refresh or not.
     * @since Nov 11 2016 - NEW!
     */
    private void setRefreshing(boolean isRefreshing) {

        if (parent.isFinishing() || !isAdded()) return;

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(isRefreshing);
        }
    }

    private FragmentTransaction hideDialog(String strTag) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        Fragment prev = getFragmentManager().findFragmentByTag(strTag);

        if (prev != null && prev instanceof DialogFragment) {
            ((DialogFragment) prev).dismiss();
            ft.remove(prev);
        }

        return ft;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PackageFragmentListener) {
            listener = (PackageFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement PackageFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mAppClass.cancelPendingRequests(FragmentPackage.class);
        mAppClass.cancelPendingRequests(FragmentLogin.class);
    }

    private void addNewPackageItemsToRecyclerView(final List<PojoBooking> newPojoBookings) {
        Utils.Logs('i', TAG, "ADD NEW BOOKED ITEM. New ID size: " + newPojoBookings.size());

        // Make sure no Empty state item on the bottom of the list
        if (adapterPackage != null && adapterPackage.getItemCount() > 0
                && newPojoBookings.size() > 0) {
            Object objectItem = adapterPackage.getItem(adapterPackage.getItemCount() - 1);
            if (objectItem != null && objectItem instanceof GeneralListItem) {
                adapterPackage.removeLastPackage();
            }
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                mListPojoBooking.addAll(0, newPojoBookings);
                adapterPackage.addAllPackage(0, newPojoBookings);

                scrollListToTop();

            }
        }, Constants.DURATION_SUPER_LONG);
    }

    private void appendNewOnlinePackage(final List<PojoBooking> pojoBookingList) {

        // Make sure no "Load more" item when load more just succeed
        Object objectItem = adapterPackage.getItem(adapterPackage.getItemCount() - 1);
        if (objectItem != null && objectItem instanceof PojoBooking) {
            PojoBooking loadMoreItem = (PojoBooking) objectItem;
            if (loadMoreItem.getBooking_id() == Constants.FOOTER_LOAD_MORE_ITEM_ID) {
                adapterPackage.removeLastPackage();
            }
        }

        adapterPackage.addAllPackage(pojoBookingList);

    }

    //////
    // S: View implementation
    @Override
    public void showAllPackages(List<PojoBooking> pojoBookingList) {

        Utils.Log(TAG, "Show all packages. Size: " + pojoBookingList.size());
        initPackageListItem(pojoBookingList);

        // Show filter status: No filter applied to Main activity
        if (listener != null) {
            listener.onFilterApplied(new ArrayList<String>(), pojoBookingList.size());
        }

    }

    @Override
    public void showFilteredPackages(List<PojoBooking> pojoBookingList, ArrayList<String> filterStrings) {
        Utils.Log(TAG, "Show filtered packages. Size: " + pojoBookingList.size() + ", Filter: " + filterStrings);

        // Scroll to the top of the list every filter applied
        scrollListToTop();

        // Replace all list with new one
        initPackageListItem(pojoBookingList);

        // Show filter status: Filter applied to Main activity
        if (listener != null) {
            listener.onFilterApplied(filterStrings, pojoBookingList.size());
        }
    }

    @Override
    public void showPackageDetail(long bookingId, int itemPosition) {
        Utils.Log(TAG, "Open Package Detail Screen");
        showDetail(bookingId);
//        if (listener != null) {showDetail(item.getBooking_id());
//            listener.onPackageItemClicked(null, bookingId, itemPosition);
//        }
    }

    @Override
    public void showNewPackageScreen() {
        Utils.Log(TAG, "Open New Package Screen");
        createNewPackage();

//        if (listener != null) {
//            listener.onNewPackageClicked();
//        }
    }

    private void createNewPackage() {
//        boolean isTutorial = SPHelper.getInstance()
//                .getSharedPreferences(AppClass.SHARED_LEARNT_OPEN_CREATE_PACKAGE, false);

//        Utils.Log(TAG, "Open New Package. Tutorial SP --> " + isTutorial);

        Intent intent = new Intent(parent, AcNewPaket.class);
//        intent.putExtra(Constants.EXTRA_IS_TUTORIAL, isTutorial);
        parent.startActivityForResult(intent, Constants.TAG_NEW_PACKAGE);
    }

    @Override
    public void showNewPackages(List<PojoBooking> newBookingList) {
        Utils.Log(TAG, "Add new package. Size: " + newBookingList.size());

        addNewPackageItemsToRecyclerView(newBookingList);
    }

    @Override
    public void showMorePackages(List<PojoBooking> moreBookingList) {
        Utils.Log(TAG, "Load more data response. Size: " + moreBookingList.size());
        appendNewOnlinePackage(moreBookingList);
    }

    @Override
    public void deleteRecyclerItemAt(final int position) {
        Utils.Logs('w', TAG, "Delete item at: " + position);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mListPojoBooking != null && adapterPackage != null
                        && !mListPojoBooking.isEmpty()
                        && adapterPackage.getItemCount() != 0) {

                    mListPojoBooking.remove(position);
                    adapterPackage.removeAt(position);

                    // Fabric log for view side item
                    MyLog.FabricLog(Log.INFO, TAG + " AFTER DELETE - FINAL ADAPTER SIZE: " + adapterPackage.getItemCount());
                    MyLog.FabricLog(Log.INFO, TAG + " AFTER DELETE - FINAL LIST SIZE: " + mListPojoBooking.size());
                }
            }
        }, parent.getResources().getInteger(android.R.integer.config_mediumAnimTime));

    }

    @Override
    public void refreshRecyclerViewItem(int position) {
        Utils.Log(TAG, "Refresh item at: " + position);
    }

    @Override
    public void refreshRecyclerView() {

    }

    @Override
    public void showRequestPickupItem(PojoBooking requestPickupItem) {
        Utils.Log(TAG, "Add request pickup item on top");
        // Make sure no "Request from here" item
        Object objectItem = adapterPackage.getItem(0);
        if (objectItem != null && objectItem instanceof PojoBooking) {
            PojoBooking currentRequestPickupItem = (PojoBooking) objectItem;
            if (currentRequestPickupItem.getBooking_id() != Constants.HEADER_ITEM_ID) {
                // Add Pickup NOW Button on top
                adapterPackage.addPackage(0, requestPickupItem);
            }
        }
    }

    @Override
    public void showLoadMoreItem(PojoBooking loadMoreItem) {
        Utils.Log(TAG, "Add load more item on bottom");
        // Make sure no "Load more" item when load more just succeed
        Object objectItem = adapterPackage.getItem(adapterPackage.getItemCount() - 1);
        if (objectItem != null && objectItem instanceof PojoBooking) {
            PojoBooking recentLoadMoreItem = (PojoBooking) objectItem;
            if (recentLoadMoreItem.getBooking_id() != Constants.HEADER_ITEM_ID) {
                // Add load more button
                adapterPackage.addPackage(loadMoreItem);
            }
        }
    }

    @Override
    public void showPickupMapScreen() {
        Utils.Log(TAG, "Open Pickup Map");
        // Launch pickup map
        listener.onClickRequestPickup();
    }

    @Override
    public void showViewState(UIState uiState) {
        Utils.Log(TAG, "View state: " + uiState);

        packageInterfaceState(uiState);
    }

    @Override
    public void showTutorial() {
//        Utils.Log(TAG, "Show tutorial...");
//        Utils.switchTutorial(parent, true);
//        showPackageTutorial();

    }

    @Override
    public void showPrepareAllData(boolean isShow) {
        MyLog.FabricLog(Log.WARN, TAG + " - Prepare all data. After update app.");
    }
    // E: View implementation
    //////

    public interface PackageFragmentListener {

        // Show Package detail with Main activity controll
//        void onPackageItemClicked(@Nullable View view, long bookingId, int itemPosition);

        // Show New Package screen
//        void onNewPackageClicked();

        void onClickRequestPickup();

        void onFilterApplied(ArrayList<String> appliedFilter, int itemCount);
    }
}
