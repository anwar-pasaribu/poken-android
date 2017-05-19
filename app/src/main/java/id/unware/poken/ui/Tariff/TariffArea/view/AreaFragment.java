package id.unware.poken.ui.Tariff.TariffArea.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.PojoArea;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.Tariff.TariffArea.model.AreaModel;
import id.unware.poken.ui.Tariff.TariffArea.presenter.AreaPresenter;
import id.unware.poken.ui.Tariff.TariffArea.view.adapter.AreaAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AreaFragment extends DialogFragment implements OnClickRecyclerItem, IAreaFragment {

    private final String TAG = "AreaFragment";

    public static final int ORIGIN = 1;

    public static final int DESTINATION = 2;
    private static final String ARG_AREA_FOR = "area-for";
    private static final String ARG_OFFLINE_MAP_LIST = "offline-map-list";

    private int mAreaFor = 1;
    private OnListFragmentInteractionListener mListener;

    @BindView(R.id.editTextTracking) EditText editTextSearchArea;
    @BindView(R.id.progress) ProgressBar progressBarSearch;
    @BindView(R.id.list) RecyclerView recyclerView;

    private Handler handler = new Handler();

    // City datas from kota_sorted.json
    private ArrayList<String> mArrayListCity = new ArrayList<>();
    private ArrayList<PojoArea> occurenceList = new ArrayList<>();
    private AreaAdapter areaAdapter;

    // Detect more data trigger. Whether by clicking More > or EditText (Search bar)
    private boolean isFromMoreListItem = false;

    private AppCompatActivity parent;

    private AreaPresenter presenter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AreaFragment() {
    }

    @SuppressLint("DefaultLocale")
    public static AreaFragment newInstance(int areaFor, ArrayList<String> listOfflineData) {
        AreaFragment fragment = new AreaFragment();
        Bundle args = new Bundle();

        // Edit Text that trigger AreaFragment dialog.
        // this determine to which edit text will accept the Name#Phone#Address data.
        args.putInt(ARG_AREA_FOR, areaFor);

        // Set offline data as bundle
        args.putStringArrayList(ARG_OFFLINE_MAP_LIST, listOfflineData);

        fragment.setArguments(args);

        MyLog.FabricLog(Log.INFO, String.format("City list size: %d, target area: %d", listOfflineData.size(), areaFor));

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyLog.FabricLog(Log.INFO, "Area fragment created.");

        if (getArguments() != null) {
            mAreaFor = getArguments().getInt(ARG_AREA_FOR);

            // Get City#Tariff list from bundle
            mArrayListCity = getArguments().getStringArrayList(ARG_OFFLINE_MAP_LIST);
        }

        // Adaper with empty list
        areaAdapter = new AreaAdapter(occurenceList, AreaFragment.this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String title = "";
        switch (mAreaFor) {
            case ORIGIN:
                title = parent.getResources().getString(R.string.origin);
                break;
            case DESTINATION:
                title = parent.getResources().getString(R.string.destination);
                break;
        }

        getDialog().setTitle(title);

        View view = inflater.inflate(R.layout.fragment_area, container, false);

        ButterKnife.bind(this, view);

        presenter = new AreaPresenter(this, new AreaModel());

        recyclerView.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(areaAdapter);

        // Request keyboard to show
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        editTextSearchArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                int minLen = 2; // trigger completion
                if (TextUtils.getTrimmedLength(editable) > minLen) {

                    // Offline searching
                    occurenceList.clear();
                    for (String s : mArrayListCity) {
                        if (s.toUpperCase().contains(editable.toString().toUpperCase())) {
                            String[] datas = s.split("#");
                            occurenceList.add(new PojoArea(datas[0], datas[1], false /*false: this item is a regular item*/));
                        }
                    }
                    // While offline search result is not empty, run offlineAutoComplete Runnable
                    if (!occurenceList.isEmpty()) {
                        Utils.Log(TAG, "Local found! For : " + occurenceList.size());
                        handler.removeCallbacks(offlineAutoComplete);
                        handler.postDelayed(offlineAutoComplete, 100);

                    } else {
                        Utils.Log(TAG, "Begin server searching.");

                        occurenceList.clear();
                        recyclerView.getRecycledViewPool().clear();
                        areaAdapter.notifyDataSetChanged();

                        handler.removeCallbacks(autoComplete);
                        handler.postDelayed(autoComplete, 1000);
                    }

                } else {
                    occurenceList.clear();
                    areaAdapter.notifyDataSetChanged();
                }
            }
        });

        return view;
    }

    @Override
    public void onStop() {
        Utils.Log(TAG, "On stop AreaFragment");
        handler.removeCallbacks(autoComplete);
        handler.removeCallbacks(offlineAutoComplete);
        //ApiClient.getInstance().cancelCurrentCall();

        super.onStop();
    }

    private Runnable autoComplete = new Runnable() {
        @Override
        public void run() {

            if (editTextSearchArea == null || editTextSearchArea.getText().length() == 0) {
                MyLog.FabricLog(Log.ERROR, "EditText is not ready or text is empty");
                return;
            }

//            final String strAreaKeyword = TextUtils.htmlEncode(editTextSearchArea.getText().toString().trim());

//            Utils.Log(TAG, "Area keyword: " + strAreaKeyword);
            presenter.searchArea();
//            ApiClient.getInstance().searchArea(
//                    strAreaKeyword,  // Term to search on server.
//                new ApiClient.Callback<List<PojoArea>>() {
//
//                    @Override
//                    public void onStart(String message) {
//
//                        // If autoComplete not called by tapping More,
//                        // then show progressBarSearch on the right right side of EditText.
//                        if (AreaFragment.this.getActivity() != null && AreaFragment.this.isAdded()) {
//                            showLoadingIndicator(true);
//                        }
//                    }
//
//                    @Override
//                    public void success(List<PojoArea> data) {
//
//                        if (AreaFragment.this.getActivity() != null && AreaFragment.this.isAdded()) {
//
//                            // Return when holder activity is finishing
//                            if (AreaFragment.this.getActivity().isFinishing()) return;
//
//                            showLoadingIndicator(false);
//
//                            // Provide "No Data" state when no response from server.
//                            if (data.isEmpty()) {
//                                Util.Log(TAG, "AREA DATA EMPTY");
//                                occurenceList.clear();
//                                occurenceList.add(createEmptyStateItem());
//
//                                // REF: http://stackoverflow.com/questions/30220771/recyclerview-inconsistency-detected-invalid-item-position
//                                // To make sure RecycleView is clear
//                                recyclerView.getRecycledViewPool().clear();
//                                areaAdapter.notifyDataSetChanged();
//
//                            } else if (isFromMoreListItem) {
//
//                                // Append data into list
//
//                                if (areaAdapter != null && areaAdapter.getItemCount() > 0) {
//                                    Util.Log("TRIGGER", "Adapter sudah ada.");
//                                    Util.Log("TRIGGER", "Total occurencelist data: " + occurenceList.size());
//                                    Util.Log("TRIGGER", "Total adapter data: " + areaAdapter.getItemCount());
//
//                                    int headerDataPos = areaAdapter.getItemCount() - 1;
//
//                                    // Add server data to adapater
//                                    // when the data is not available offline.
//                                    for (PojoArea areaItem : data) {
//                                        if(!areaDataAlreadyAvailable(areaItem.daerah))
//                                            occurenceList.add(areaItem);
//                                    }
//
//                                    // Change "More" text to "District"
//                                    if (headerDataPos >= 0) {
//                                        Util.Log("TRIGGER", "Header pos: " + headerDataPos);
//                                        areaAdapter.setHeaderText(headerDataPos, AreaFragment.this.parent.getString(R.string.lbl_rate_district));
//                                    } else {
//                                        Util.Log("TRIGGER", "Masalah Header pos: " + headerDataPos);
//                                    }
//
//                                    // REF: http://stackoverflow.com/questions/30220771/recyclerview-inconsistency-detected-invalid-item-position
//                                    // To make sure RecycleView is clear
//                                    recyclerView.getRecycledViewPool().clear();
//                                    areaAdapter.notifyDataSetChanged();
//
//                                    showLoadingIndicator(false);
//
//                                    // Set "isFromMoreListItem" to false
//                                    // in order to enable list items recreation
//                                    isFromMoreListItem = !isFromMoreListItem;
//                                }
//
//                            } else {
//                                // Recreate all list item with data from network
//                                occurenceList.clear();
//                                occurenceList.addAll(data);
//                                areaAdapter.notifyDataSetChanged();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void failed(String message) {
//
//                        MyLog.FabricLog(Log.ERROR, "Failed to rates check. Message : " + message);
//
//                        if (AreaFragment.this.getActivity() != null && AreaFragment.this.isAdded()) {
//
//                            if (AreaFragment.this.getActivity().isFinishing()) return;
//
//                            showLoadingIndicator(false);
//
//                            // Show snackbar when request is failed
//                            // the snackbar contain failed string.
//                            if (parent != null && !Util.isEmpty(message))
//                                Snackbar.make(progressBarSearch, message, Snackbar.LENGTH_LONG).show();
//                        }
//                    }
//                }
//            );
        }
    };

    /**
     * Show loading indicator when load notwork data.
     *
     * @param isShow Show loading indicator or not.
     */
    public void showLoadingIndicator(boolean isShow) {
        if (progressBarSearch == null) return;

        progressBarSearch.setVisibility(isShow ? View.VISIBLE : View.GONE);

    }

    /**
     * Create empty state item indicate "daerah" and "tariff_code" is empty.
     *
     * @return Empty state {@link PojoArea}
     */
    private PojoArea createEmptyStateItem() {
        return new PojoArea("", "", true);
    }

    /**
     * Check whether area data is available.
     *
     * @param daerah : Area name to check.
     * @return true when data is available.
     */
    public boolean areaDataAlreadyAvailable(String daerah) {
        if (areaAdapter == null || areaAdapter.getAdapterData().isEmpty()) {
            return false;
        }

        for (PojoArea areaItem : areaAdapter.getAdapterData()) {
            if (areaItem.daerah.toUpperCase().equals(daerah.toUpperCase())) {
                return true;
            }
        }

        return false;
    }

    private Runnable offlineAutoComplete = new Runnable() {
        @Override
        public void run() {
            occurenceList.add(new PojoArea(parent.getString(R.string.lbl_rate_more), "", false));  // This item is a More button
            areaAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (AppCompatActivity) context;
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Click listener returned from {@link AreaAdapter}.
     *
     * @param view     : Clicked view
     * @param position : Clicked position on {@link AreaAdapter}
     * @since Oct. 20, 2016 (V 24) - NEW!
     */
    @Override
    public void onItemClick(View view, int position) {

        if (position < 0 || occurenceList.isEmpty()) return;

        Utils.Log(TAG, "AreaAdapter size: " + areaAdapter.getItemCount());
        Utils.Log(TAG, "AreaAdapter item type: " + areaAdapter.getItemViewType(position));
        Utils.Log(TAG, "Area item clicked at pos: " + position + ", View: " + view);
        Utils.Log(TAG, "Area item : " + occurenceList.get(position).daerah + ", area for: " + mAreaFor);

        PojoArea selectedPojoArea = occurenceList.get(position);

        if (selectedPojoArea == null) {
            MyLog.FabricLog(Log.ERROR, TAG + " - Selected PojoArea isn't available");
            return;
        }

        if (!selectedPojoArea.isExpanded()
                && selectedPojoArea.daerah.equals(parent.getString(R.string.lbl_rate_more))) {

            Utils.Log(TAG, "Load more area item begins... Pos: " + position);
            selectedPojoArea.daerah = parent.getString(R.string.loading);
            selectedPojoArea.setExpanded(false);

            areaAdapter.notifyItemChanged(position);

            // Set headerViewHolder.mMoreItem.tariffCode = "expanded"
            // when data loading is finished.
            isFromMoreListItem = Boolean.TRUE;

            // Call runnable to fetch server data
            handler.removeCallbacks(AreaFragment.this.autoComplete);
            handler.postDelayed(AreaFragment.this.autoComplete, 500);

        } else {

            // Trigger listener implemented on RateActivity
            mListener.onListFragmentInteraction(selectedPojoArea, mAreaFor);
        }
    }

    @Override
    public String getQuery() {
        return TextUtils.htmlEncode(editTextSearchArea.getText().toString().trim());
    }

    @Override
    public void showMessage(String msg) {
        Snackbar.make(progressBarSearch, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void prepareList() {
        occurenceList.clear();
        occurenceList.add(createEmptyStateItem());
    }

    @Override
    public void clearList() {
        // REF: http://stackoverflow.com/questions/30220771/recyclerview-inconsistency-detected-invalid-item-position
        // To make sure RecycleView is clear
        recyclerView.getRecycledViewPool().clear();
        refreshList();
    }

    @Override
    public boolean getIsFromMoreListItem() {
        return isFromMoreListItem;
    }

    @Override
    public AreaAdapter getAreaAdapter() {
        return areaAdapter;
    }

    @Override
    public void addToList(PojoArea data) {
        occurenceList.add(data);
    }

    @Override
    public void setFromMoreListItem(boolean b) {
        isFromMoreListItem = b;
    }

    @Override
    public void addAllToList(List<PojoArea> data) {
        // Recreate all list item with data from network
        occurenceList.clear();
        occurenceList.addAll(data);
    }

    @Override
    public void refreshList() {
        areaAdapter.notifyDataSetChanged();
    }


    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(PojoArea item, int areaFor);
    }
}
