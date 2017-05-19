package id.unware.poken.ui.pickup.history.view.fragments;


import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoLogin;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.pojo.PojoPickupHistoryCancel;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.pickup.history.view.adapters.AdapterPickupHistory;
import id.unware.poken.ui.pickup.view.FragmentPickupMap;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Page for Pickup History list.
 */
public class FragmentPickupHistoryList extends BaseFragment
        implements OnClickRecyclerItem, VolleyResultListener {

    private final String TAG = "FragmentPickupHistoryList";

    @BindView(R.id.parentView) ViewGroup parentView;

    // Empty state view
    @BindView(R.id.emptyStateParent) RelativeLayout emptyStateParent;

    @BindView(R.id.rvPickupHistory) RecyclerView rvPickupHistory;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    private PickupHistoryListListener mPickupHistoryListListener;

    private boolean mShowActivePickup;

    private AdapterPickupHistory adapterPickupHistory;

    private List<PojoPickupHistory> listPickupHistory;

    private Realm myRealm;


    public static FragmentPickupHistoryList newInstance(boolean activeHistory) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_SHOW_ACTIVE_PICKUP, activeHistory);
        FragmentPickupHistoryList fragmentPickupHistory = new FragmentPickupHistoryList();
        fragmentPickupHistory.setArguments(bundle);
        return fragmentPickupHistory;
    }

    public FragmentPickupHistoryList() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mShowActivePickup = getArguments().getBoolean(Constants.EXTRA_SHOW_ACTIVE_PICKUP, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pickup_history_list, container, false);
        ButterKnife.bind(this, view);

        myRealm = Realm.getDefaultInstance();

        getData();
        initView();
        refresh();

        return view;
    }

    private void getData() {

        listPickupHistory = new ArrayList<>();
        listPickupHistory.addAll(getAllPickupHistory());

        interfaceState(listPickupHistory.isEmpty() ? Constants.STATE_NODATA : Constants.STATE_FINISHED);

    }

    private void initView() {

        adapterPickupHistory = new AdapterPickupHistory(
                listPickupHistory,
                FragmentPickupHistoryList.this /*Click listener*/,
                parent);

        rvPickupHistory.setLayoutManager(new LinearLayoutManager(
                parent,
                LinearLayoutManager.VERTICAL,
                false));

        rvPickupHistory.setAdapter(adapterPickupHistory);
        rvPickupHistory.setHasFixedSize(true);

        // rvPickupHistory.addItemDecoration(new ItemDecorationDivider(context, ItemDecorationDivider.VERTICAL_LIST));
        // rvPickupHistory.addItemDecoration(new ItemDecorationSpace(parent.getResources().getDimensionPixelOffset(R.dimen.recycle_space)));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToRefresh();
            }
        });
    }

    /**
     * Update fragment interface based on Address Book availability.
     *
     * @param stateNum : State number to indicate package availability.
     */
    private void interfaceState(int stateNum) {
        switch (stateNum) {
            case Constants.STATE_NODATA:
                //"No data"
                emptyStateParent.setVisibility(View.VISIBLE);
                break;
            case Constants.STATE_FINISHED:
                //"Data available"
                emptyStateParent.setVisibility(View.GONE);
                break;
        }

    }

    private void refresh() {
        swipeToRefresh();
    }

    /**
     * Get Pickup History remote data when device is connected to the internet.
     */
    private void swipeToRefresh() {
        Utils.Log(TAG, "Package auto checkNewPackage");

        if (Utils.isNetworkNotConnected(parent)) {
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setRefreshing(false);
                Utils.snackBar(swipeRefreshLayout, parent.getString(R.string.this_is_an_offline_mode));
            }
        } else {

            // !!! Get Remote data
            ControllerPaket.getInstance().loginWithSession(swipeRefreshLayout, this);
        }
    }

    private List<PojoCourier> getAllVendors() {
        RealmResults<PojoCourier> pojoCouriers = myRealm.where(PojoCourier.class).findAll();
        Utils.Logs('i', TAG, "Vendors list size: " + pojoCouriers.size());

        return new ArrayList<>(pojoCouriers);
    }

    /**
     * Get 20 Pickup history data from Realm.
     * The data picked when "status_number" are: -3, -2, 1, 2, 3 and 5.
     * <p>
     * UPDATE:
     * - (10 Aug) - Show all data (NO FILTER)
     *
     * @return Filtered realmReasults.
     */
    private List<PojoPickupHistory> getAllPickupHistory() {

        Utils.Log(TAG, "Get all : " + (mShowActivePickup? " ON GOING PICKUP HISTORY." : " FINISHED PICKUP HISTORY."));

        int LIMIT = 20;
        RealmResults<PojoPickupHistory> pojoPickupHistories;
        if (mShowActivePickup) {
            // In progress
            pojoPickupHistories = myRealm.where(PojoPickupHistory.class)
                    .equalTo(PojoPickupHistory.KEY_STATUS_NUMBER, "1")
                    .or().equalTo(PojoPickupHistory.KEY_STATUS_NUMBER, "2")
                    .or().equalTo(PojoPickupHistory.KEY_STATUS_NUMBER, "3")
                    .or().equalTo(PojoPickupHistory.KEY_STATUS_NUMBER, "4")
                    .findAllSorted(
                            new String[]{"created_on", "pickup_id"},
                            new Sort[]{Sort.DESCENDING, Sort.DESCENDING});
        } else {
            // Done
            pojoPickupHistories = myRealm.where(PojoPickupHistory.class)
                    .equalTo(PojoPickupHistory.KEY_STATUS_NUMBER, "-4")
                    .or().equalTo(PojoPickupHistory.KEY_STATUS_NUMBER, "-3")
                    .or().equalTo(PojoPickupHistory.KEY_STATUS_NUMBER, "-2")
                    .or().equalTo(PojoPickupHistory.KEY_STATUS_NUMBER, "-1")
                    .or().equalTo(PojoPickupHistory.KEY_STATUS_NUMBER, "0")
                    .or().equalTo(PojoPickupHistory.KEY_STATUS_NUMBER, "5")
                    .findAllSorted(
                            new String[]{"created_on", "pickup_id"},
                            new Sort[]{Sort.DESCENDING, Sort.DESCENDING});
        }

        // Set number of OTW driver in order to show on going Pickup Request
        long totalOtw = pojoPickupHistories.where()
                .equalTo(PojoPickupHistory.KEY_STATUS_NUMBER,
                        String.valueOf(PojoPickupHistory.STATUS_OTW)).count();
//        Utils.setNumberOfOtwDriver((int) totalOtw);

        Utils.Log(TAG, "Pojo Pickup History size: " + pojoPickupHistories.size());
        Utils.Log(TAG, "Pojo Pickup History OTW Driver: " + totalOtw);

        LIMIT = pojoPickupHistories.size() > LIMIT ? LIMIT : pojoPickupHistories.size();

        return pojoPickupHistories.subList(0, LIMIT);
    }

    /**
     * Refresh Recycler View
     */
    private void refreshData() {
        listPickupHistory.clear();
        listPickupHistory.addAll(getAllPickupHistory());

        if (listPickupHistory.isEmpty()) {
            interfaceState(Constants.STATE_NODATA);
        } else {
            interfaceState(Constants.STATE_FINISHED);
            adapterPickupHistory.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PickupHistoryListListener) {
            mPickupHistoryListListener = (PickupHistoryListListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPickupHistoryListListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        AppClass.getInstance().cancelPendingRequests(FragmentLogin.class);
    }

    @Override
    public void onItemClick(View view, final int position) {

        if (position < 0 || position >= listPickupHistory.size()) return;

        try {
            final PojoPickupHistory pickupHistoryItem = listPickupHistory.get(position);
            final int intStatusNumber = Integer.parseInt(pickupHistoryItem.getStatus());

            switch (view.getId()) {

                // [V49] New direct button to cancel, repickup, and details
                case R.id.buttonCancel:
                    Utils.Log(TAG, "Cancel pickup");
                    // Cancelling by using pickup id
                    cancelPickupById(pickupHistoryItem.getPickup_id(), position);
                    break;
                case R.id.buttonRepickup:
                    Utils.Log(TAG, "Re-pickup");
                    repickupPreviousPickup(pickupHistoryItem);
                    break;
                case R.id.buttonDetail:
                    Utils.Log(TAG, "Show pickup detail");
                    dialogDetail(listPickupHistory.get(position));
                    break;
                case R.id.imageButtonDriverCall:
                    // Open dialer
                    if (!Utils.isEmpty(pickupHistoryItem.getDriver_phone())) {
                        Utils.openDialer(this.parent, pickupHistoryItem.getDriver_phone().trim());
                    }

                    break;
                case R.id.imageButtonDriverSms:

                    if (Utils.isEmpty(pickupHistoryItem.getDriver_phone())) return;

                    // Open SMS composer
                    String smsUri = String.format("sms:%s", pickupHistoryItem.getDriver_phone().trim());
                    Intent intentSms = new Intent(Intent.ACTION_VIEW);

                    // To count with Play market backstack, After pressing back button,
                    // to taken back to our application, we need to add following flags to intent.
                    intentSms.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                    // New document flag when on API >= 20
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        intentSms.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
                    }

                    intentSms.setData(Uri.parse(smsUri));

                    try {
                        startActivity(intentSms);
                    } catch (ActivityNotFoundException e) {
                        MyLog.FabricLog(Log.ERROR, "SMS Activity not found", e);
                    }

                    break;
                case R.id.frameLayoutPickedPackage:
                    // Show picked packages
                    if (pickupHistoryItem.getBookings() != null) {
                        dialogPackageList(pickupHistoryItem.getBookings());
                    }

                    Utils.Log(TAG, "Bookings data: " + (pickupHistoryItem.getBookings() != null ? pickupHistoryItem.getBookings() : "NO_BOOKINGS"));

                    break;
                default:
                    Utils.Log(TAG, "Default action.");
            }

        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private void repickupPreviousPickup(PojoPickupHistory pojoPickupHistory) {

        if (mPickupHistoryListListener != null
                && pojoPickupHistory != null) {

            /* This SP set to ensure open FragmentPickupForm, then set data there */
            SPHelper.getInstance().setPreferences(FragmentPickupMap.LATITUDE_PICKUP, pojoPickupHistory.getLat());
            SPHelper.getInstance().setPreferences(FragmentPickupMap.LONGITUDE_PICKUP, pojoPickupHistory.getLon());

            mPickupHistoryListListener.onRepickup(pojoPickupHistory.getPickup_id());
        }
    }

    private FragmentTransaction hideDialog(String strTag) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        Fragment prev = getFragmentManager().findFragmentByTag(strTag);

        if (prev != null) {
            Utils.Log(TAG, "Prev fragment is not null. Tag: " + strTag);

            ((DialogFragment) prev).dismiss();
            ft.remove(prev);
        }

        return ft;
    }

    /**
     * Display all
     */
    private void dialogPackageList(String strCsvBookings) {
        try {

            String strPackagesTag = "dialog_packages";

            String arrBookings[] = strCsvBookings.split(",");
            Arrays.sort(arrBookings);
            ArrayList<String> arrListBookingIds = new ArrayList<>();
            Collections.addAll(arrListBookingIds, arrBookings);

            FragmentTransaction ft = hideDialog(strPackagesTag);
            ft.addToBackStack(null);

            FragmentDialogPackages fragmentDialogPackages = FragmentDialogPackages.newInstance(arrListBookingIds);
            fragmentDialogPackages.show(ft, strPackagesTag);

        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    /**
     * Cancel certain Pickup
     */
    private void cancelPickupById(final String pickupId, final int itemPosition) {
        ControllerDialog.getInstance().showYesNoDialog(
                parent.getString(R.string.msg_pickup_cancel),
                this.getContext(),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // When choosing positive button
                        if (Dialog.BUTTON_POSITIVE == which) {

                            Utils.Log(TAG, "Cancel pickup with ID: " + pickupId + ", item pos: " + itemPosition);

                            ControllerPaket.getInstance().pickUpCancel(swipeRefreshLayout, pickupId, new VolleyResultListener() {
                                @Override
                                public void onSuccess(PojoBase clazz) {

                                    PojoPickupHistoryCancel pojoPickupHistoryCancel = (PojoPickupHistoryCancel) clazz;

                                    if (null != pojoPickupHistoryCancel.cancel) {
                                        Utils.Log(TAG, "Pickup cancel success with cancel: " + pojoPickupHistoryCancel.cancel);

                                        // Update realm data
                                        PojoPickupHistory pickupHistoryToUpdate = myRealm.where(PojoPickupHistory.class)
                                                .equalTo("pickup_id", pickupId)
                                                .findFirst();

                                        if (!myRealm.isInTransaction() && pickupHistoryToUpdate != null) {
                                            myRealm.beginTransaction();

                                            // Offline changes in order to update RecyclerView
                                            listPickupHistory.get(itemPosition).setStatus("-1");
                                            listPickupHistory.get(itemPosition).setStatus_number("-1");
                                            listPickupHistory.get(itemPosition).setStatus_text("Cancelled by user");
                                            adapterPickupHistory.notifyDataSetChanged();

                                            pickupHistoryToUpdate.setStatus("-1");
                                            pickupHistoryToUpdate.setStatus_number("-1");
                                            pickupHistoryToUpdate.setStatus_text("Cancelled by user");
                                            myRealm.commitTransaction();
                                        }

                                        // Refresh data
                                        refreshData();
                                    }

                                }

                                @Override
                                public void onFinish(PojoBase clazz) {
                                    Utils.Log(TAG, "Pickup cancel finish");

                                }

                                @Override
                                public void onStart(PojoBase clazz) {
                                    Utils.Log(TAG, "Pickup cancel start");

                                }

                                @Override
                                public boolean onError(PojoBase clazz) {
                                    Utils.Log(TAG, "Pickup cancel error");
                                    return false;
                                }
                            });

                            dialog.dismiss();

                        } else if (Dialog.BUTTON_NEGATIVE == which) {
                            // When choose negative button
                            dialog.dismiss();
                        }
                    }
                });
    }

    /**
     * Show Pickup History detail based on {@link PojoPickupHistory} object.
     *
     * @param pojoPickupHistory {@link PojoPickupHistory} object.
     */
    private void dialogDetail(final PojoPickupHistory pojoPickupHistory) {

        if (pojoPickupHistory == null || Utils.isEmpty(pojoPickupHistory.getPickup_id())) return;

        Utils.Log(TAG, "Open dialog with pickup id: " + pojoPickupHistory.getPickup_id());

        String strDetailTag = String.format("dialog_detail#%s", pojoPickupHistory.getPickup_id());

        FragmentTransaction ft = hideDialog(strDetailTag);
        ft.addToBackStack(null);

        FragmentPickupHistoryDetail fragmentPickupHistoryDetail = FragmentPickupHistoryDetail.newInstance(pojoPickupHistory.getPickup_id());
        fragmentPickupHistoryDetail.show(ft, strDetailTag);

    }

    @Override
    public void onSuccess(final PojoBase clazz) {

        if (parent.isFinishing() || !FragmentPickupHistoryList.this.isAdded()) return;

        if (clazz instanceof PojoLogin) {

            // Handle server response with PojoLogin data include:
            // - PojoPickupHistory.
            ControllerPaket.getInstance().afterSignin(parent, myRealm, clazz, new Realm.Transaction.Callback() {
                @Override
                public void onSuccess() {
                    super.onSuccess();
                    refreshData();
                }
            }, null);

        }
    }

    @Override
    public void onFinish(PojoBase clazz) {

        if (parent.isFinishing() || !FragmentPickupHistoryList.this.isAdded()) return;

        try {
            swipeRefreshLayout.setRefreshing(false);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }


    @Override
    public void onStart(PojoBase clazz) {

    }

    @Override
    public boolean onError(PojoBase clazz) {
        return false;
    }

    public interface PickupHistoryListListener {
        void onRepickup(String pickupId);
    }
}
