package id.unware.poken.ui.packages.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.interfaces.PackageFiltersListener;
import id.unware.poken.pojo.BookingStatus;
import id.unware.poken.pojo.GeneralListItem;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.packages.view.adapters.AdapterPackageFilter;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author Anwar Pasaribu
 * @since Dec 13 2016
 */

public class BookingFiltersDialog extends BottomSheetDialogFragment implements View.OnClickListener {

    private final String TAG = "BookingFiltersDialog";

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.buttonDoneFiltering) Button buttonDoneFiltering;
    @BindView(R.id.buttonResetFiltering) Button buttonResetFiltering;

    private PackageFiltersListener mListener;

    private AdapterPackageFilter adapterFilter;

    private List<GeneralListItem> packageFilterList = new ArrayList<>();
    private ArrayList<String> mBookingStatusTextList = new ArrayList<>();

    private SPHelper mSpHelper;


    public BookingFiltersDialog() {}

    public static BookingFiltersDialog newInstance() {
        return new BookingFiltersDialog();
    }

    public void setCallbacks(PackageFiltersListener listener) {
        this.mListener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.package_filter_bottom_sheet, container, false);
        ButterKnife.bind(this, parentView);

        mSpHelper = SPHelper.getInstance();

        initPackageFilterList();

        initView();

        return parentView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    private void initPackageFilterList() {

        final String strAllLable = getString(R.string.lbl_filter_all_packages);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<PojoBooking> pojoBookingRealmResults = realm
                .where(PojoBooking.class)
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.RETURNED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.BOOKED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.SENT.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.DELIVERED.getStrVal())
                .or().equalTo(PojoBooking.KEY_STATUS, BookingStatus.PICKED.getStrVal())
                .distinct(PojoBooking.KEY_BOOKING_STATUS_TEXT);

        /*
        TreeMap (auto sort for K).
        Hold status text with proper order key*/
        TreeMap<Integer, String> treeMapStatusText = new TreeMap<Integer, String>();
        treeMapStatusText.put(-2, strAllLable);
        for (PojoBooking pojoBooking: pojoBookingRealmResults) {
            int intBookingStatusNumber = this.getSectionOrder(Integer.parseInt(pojoBooking.getStatus()));
            treeMapStatusText.put(intBookingStatusNumber, pojoBooking.getBooking_status_text());
        }

        /* Iterate trhough TreeMap*/
        int selectedFilterCount = 0;
        String strTreeMapValue;
        Set set = treeMapStatusText.entrySet();
        Iterator iterator = set.iterator();
        // noinspection WhileLoopReplaceableByForEach
        while(iterator.hasNext()) {

            Map.Entry mentry = (Map.Entry) iterator.next();
            strTreeMapValue = mentry.getValue().toString();

            // [V49] Get item count per Booking status text
            int itemCount = 0;
            if (strTreeMapValue.equals(strAllLable)) {
                // Get all avalable package count locally
                itemCount = (int) realm.where(PojoBooking.class).count();
            } else {
                itemCount = (int) realm.where(PojoBooking.class).equalTo(PojoBooking.KEY_BOOKING_STATUS_TEXT, strTreeMapValue).count();
            }

            boolean isSelected = mSpHelper.getSharedPreferences(strTreeMapValue, false);
            Utils.Log(TAG, "Booking status: " + strTreeMapValue + " is selected --> " + isSelected);

            if (isSelected) {
                selectedFilterCount++;
            }

            GeneralListItem filterItem = new GeneralListItem();
            filterItem.setTitle(strTreeMapValue);
            filterItem.setSelected(isSelected);

            // [V49] Set badge
            filterItem.setBadgeAvailable(true);
            filterItem.setBadgeCount(itemCount);

            packageFilterList.add(filterItem);
        }

        if (selectedFilterCount == 0 && packageFilterList.size() > 0) {
            Utils.Logs('i', TAG, "NO FILTER SELECTED, THEN SELECT ALL");
            packageFilterList.get(0).setSelected(true);
        }

        realm.close();
    }

    private void initView() {

        setupFilteringView();

        buttonDoneFiltering.setOnClickListener(this);
        buttonResetFiltering.setOnClickListener(this);
    }

    private void setupFilteringView() {

        adapterFilter = new AdapterPackageFilter(packageFilterList, new OnClickRecyclerItem() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < 0) return;

                GeneralListItem selectedItem = packageFilterList.get(position);

                if (selectedItem == null) return;

                Utils.Log(TAG, "Recent text clicked: " + selectedItem.getTitle());

                packageFilterList.get(position).setSelected(!selectedItem.isSelected());
                adapterFilter.notifyItemChanged(position);

                // Clear all saved filter
                for (GeneralListItem filteritem: packageFilterList) {
                    Utils.Logs('w', TAG, "Unselect : " + filteritem.getTitle());
                    mSpHelper.setPreferences(filteritem.getTitle(), false);
                }

                // Toggle SP status (TRUE|FALSE)
                mSpHelper.setPreferences(selectedItem.getTitle(), true);

                if (mListener != null) {
                    mListener.onFilterItemClick(selectedItem.getTitle());
                }

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterFilter);
        recyclerView.setHasFixedSize(true);
    }

    /**
     * <p>Determine section order on the list based on booking_status number.
     * Using following rule:</p>
     * <p>
     * <code>by case <br/>
     * when status = 3  then 1 <br/>
     * when status = 1  then 2 <br/>
     * when status = 2  then 3 <br/>
     * when status = -3 then 4 <br/>
     * end;</code>
     *
     * @param intBookingStatus Booking status number
     * @return Section order number.
     */
    private int getSectionOrder(int intBookingStatus) {

        switch (intBookingStatus) {
            case 0:
                return 0;
            case 3:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case -3:
                return 4;
            default:
                return -1;
        }

    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }

    @Override
    public void onClick(View v) {

        if (v == buttonDoneFiltering && mListener != null) {

            // Collect selected filter
            int packageFilterListSize = packageFilterList.size();
            for (int i = packageFilterListSize - 1; i >= 0; i--) {

                if (packageFilterList.get(i).isSelected()) {

                    // Save to SP as TRUE
                    mSpHelper.setPreferences(packageFilterList.get(i).getTitle(), true);

                    mBookingStatusTextList.add(packageFilterList.get(i).getTitle());
                } else {

                    // Save to SP as FALSE
                    mSpHelper.setPreferences(packageFilterList.get(i).getTitle(), false);

                }
            }

            Utils.Log(TAG, "Selected filter: " + mBookingStatusTextList);

            mListener.onFilterDone(mBookingStatusTextList);

        } else if (v == buttonResetFiltering && mListener != null) {
            mListener.onFilterReset();
        }

    }
}
