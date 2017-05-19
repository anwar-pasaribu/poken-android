package id.unware.poken.ui.packages.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoBookingData;
import id.unware.poken.pojo.RecentSearchKeyword;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.packages.view.adapters.AdapterRecentKeyword;
import id.unware.poken.ui.pickup.history.view.adapters.AdapterPackage;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FragmentDialogSearchPackage extends DialogFragment
        implements OnClickRecyclerItem, VolleyResultListener {

    private final String TAG = "FragmentDialogSearchPackage";

    private String mKeyword = "";
    private OnListFragmentInteractionListener mListener;

    // Searchbar section
    @BindView(R.id.imageButtonClose) ImageButton imageButtonClose;
    @BindView(R.id.editTextSearchPackage) EditText editTextSearchPackage;
    @BindView(R.id.progress) ProgressBar progressBarSearch;

    // Recent header
    @BindView(R.id.parentRecentHeader) ViewGroup parentRecentHeader;
    @BindView(R.id.textViewRecent) TextView textViewRecent;
    @BindView(R.id.imageButtonClearRecentConfirm) ImageButton imageButtonClearRecentConfirm;

    // Result section
    @BindView(R.id.parentResult) ViewGroup parentResult;
    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.textViewNoResult) TextView textViewNoResult;

    private int REQUEST_LIMIT = 10;

    private List<PojoBooking> mListPojoBooking = new ArrayList<>();
    private List<String> keywordList = new ArrayList<>();
    private AdapterPackage adapterPackage;

    private AppCompatActivity parent;

    private Handler handler = new Handler();
    private Runnable offlineAutoComplete = new Runnable() {
        @Override
        public void run() {
            // Search PojoBooking with only "keyword"
            List<PojoBooking> temp = ControllerRealm.getInstance().searchPackage(mKeyword, 0L, 0);

            // Instruct to recreate all data with new local search result
            setupSearchResult(temp, true);

            if (!temp.isEmpty()) {
                Utils.Log(TAG, "OFFLINE DATA FOUND");
                showViewState(UIState.FINISHED);

                // Save keyword when data is available
                ControllerRealm.getInstance().addRecentKeyword(mKeyword, Constants.TAG_PACKAGE);

            } else {
                Utils.Logs('e', TAG, "NO OFFLINE DATA FOUND");
                loadMore();

            }
        }
    };


    public FragmentDialogSearchPackage() {

    }

    public static FragmentDialogSearchPackage newInstance() {
        FragmentDialogSearchPackage fragment = new FragmentDialogSearchPackage();
        Bundle args = new Bundle();

        // Create args param here

        fragment.setArguments(args);

        return fragment;
    }

    public void setCallbacks(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyLog.FabricLog(Log.INFO, "Search package dialog created.");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.fragment_search_package, null, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Make dialog fullscreen
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            final Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private void initView() {

        // Recent keyword list
        // and set RecyclerView adapter to AdapterRecentKeyword
        setupRecentData();
        setupClearRecent();

        // Search result
        if (keywordList.isEmpty()) {

            setupSearchResult(mListPojoBooking, true);

        }

        editTextSearchPackage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                // Show recent list when user begin to delete search text
                if (Utils.isEmpty(editable.toString()) || editable.length() < 3) {
                    setupRecentData();
                    return;
                }

                searchPackageLocally(editable);
            }
        });

        // Register action listener, in case user press "magnifying glass" on soft. keyboard.
        editTextSearchPackage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

                if (event != null) {
                    if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_ENTER == event.getKeyCode()) {
                        // Physical keyboard.
                        Utils.hideKeyboardFrom(parent, editTextSearchPackage);
                        return false;  // To prevent this call again
                    }

                } else if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                    // Soft keyboard enter button
                    Utils.hideKeyboardFrom(parent, editTextSearchPackage);
                    return false;
                }

                return false;
            }
        });

        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboardFrom(parent, editTextSearchPackage);

                if (FragmentDialogSearchPackage.this.getDialog() != null) {
                    // if appear as dialog
                    FragmentDialogSearchPackage.this.dismiss();

                } else {
                    // If fullscreen
                    FragmentDialogSearchPackage.this.parent.onBackPressed();
                }
            }
        });
    }

    /**
     * Setup search result.
     * @param mListPojoBooking List of {@link PojoBooking}
     * @param isRecreate Indicate recreate adapter or update only dataset on already available adapter
     *
     * @since Dec 8 2016 - NEW v.45
     */
    private void setupSearchResult(List<PojoBooking> mListPojoBooking, boolean isRecreate) {
        if (mListPojoBooking.isEmpty()) {
            Utils.Logs('e', TAG, "Search item is empty");

            int adapterItemSize = (adapterPackage != null ? adapterPackage.getItemCount() : -1);

            Utils.Log(TAG, "Adapter size: " + adapterItemSize);
            Utils.Log(TAG, "Is recreate --> " + isRecreate);

            // Case: Last item online
            if (adapterItemSize > 0 && !isRecreate) {

                adapterPackage.removeLastPackage();

            } else if (adapterItemSize > 0 && isRecreate) {
                Utils.Logs('e', TAG, "No local search found, show no data state");

                adapterPackage.replaceAllPackage(new ArrayList<PojoBooking>());

                // Show no data state when search query available
                if (editTextSearchPackage.getText().length() != 0) {
                    showViewState(UIState.NODATA);
                }

            } else {
                Utils.Logs('e', TAG, "Other reason to show no data state");

                // Show no data state when search query available
                if (editTextSearchPackage.getText().length() != 0) {
                    showViewState(UIState.NODATA);
                }
            }

            return;
        }

        showViewState(UIState.FINISHED);

        if (!isRecreate) {

            if (recyclerView.getAdapter() instanceof AdapterPackage) {
                Utils.Log(TAG, "Append / Add all result item to adapter");

                adapterPackage.removeLastPackage();

                adapterPackage.addAllPackage(mListPojoBooking);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterPackage.addPackage(createLoadMoreItem());
                    }
                }, Constants.DURATION_SUPER_LONG);

            } else {
                Utils.Log(TAG, "Recreate result item - USER SELECT RECENT LIST");

                adapterPackage = new AdapterPackage(parent, mListPojoBooking, this);

                recyclerView.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapterPackage);
                recyclerView.setHasFixedSize(true);

                adapterPackage.addPackage(createLoadMoreItem());
            }

        } else {
            if (recyclerView.getAdapter() instanceof AdapterPackage) {
                Utils.Log(TAG, "Replace all content on adapter Package for " + mListPojoBooking.size() + " new item(s).");

                adapterPackage.replaceAllPackage(mListPojoBooking);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterPackage.addPackage(createLoadMoreItem());
                    }
                }, Constants.DURATION_SUPER_LONG);

            } else {
                Utils.Log(TAG, "Recreate result item");
                adapterPackage = new AdapterPackage(parent, mListPojoBooking, this);

                recyclerView.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapterPackage);
                recyclerView.setHasFixedSize(true);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapterPackage.addPackage(createLoadMoreItem());
                    }
                }, Constants.DURATION_SUPER_LONG);

            }

        }
    }

    /**
     * Get all recent keyword for Package page.
     * This page tag refer to {@code AppClass.TAG_PACKAGE}
     *
     * @return List of keyword string.
     */
    public List<String> getRecentKeyword() {

        List<String> keywords = new ArrayList<>();

        RealmResults<RecentSearchKeyword> recentSearchKeywords =
                ControllerRealm.getInstance().getAllRecentKeywordByTag(Constants.TAG_PACKAGE);

        if (recentSearchKeywords == null || !recentSearchKeywords.isValid()) {
            Utils.Logs('e', TAG, "RECENT KEYWORD REALMRESULTS NOT VALID, THEN RECREATE REALM INSTANCE");
            Realm realm = Realm.getDefaultInstance();
            recentSearchKeywords = realm
                    .where(RecentSearchKeyword.class)
                    .equalTo(RecentSearchKeyword.KEY_TAG, Constants.TAG_POSTCODE)
                    .findAllSorted(RecentSearchKeyword.KEY_TIMESTAMP, Sort.DESCENDING);
        }

        Utils.Log(TAG, "Recent is valid? " + recentSearchKeywords.isValid());

        for (RecentSearchKeyword recent : recentSearchKeywords ) {
            keywords.add(recent.getKeywordString());
        }

        return keywords;
    }

    /**
     * Show list of recent keyword.
     *
     * @since Dec 8 2016 - NEW v45
     */
    private void setupRecentData() {

        keywordList = new ArrayList<>(getRecentKeyword());

        /* Adapter for recent keyword.*/
        AdapterRecentKeyword adapterRecentKeyword = new AdapterRecentKeyword(keywordList, new OnClickRecyclerItem() {
            @Override
            public void onItemClick(View view, int position) {
                if (position < 0) return;

                Utils.Log(TAG, "Recent text clicked: " + keywordList.get(position));

                editTextSearchPackage.setText(keywordList.get(position));
                editTextSearchPackage.setSelection(editTextSearchPackage.getText().length());

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterRecentKeyword);
        recyclerView.setHasFixedSize(true);

        // Show recent header only when Recent item available
        if (!keywordList.isEmpty()) {

            showViewState(UIState.DEFAULT);

        } else {
            parentRecentHeader.setVisibility(View.GONE);
        }
    }

    /**
     * Create tap confirmation on "Clear recent" button.
     *
     * User should tap the button again when she/he sure to clear recent item. <br />
     *
     * This action delete all related recent keyword based on page tag (ex. Package tag)
     */
    private void setupClearRecent() {
        imageButtonClearRecentConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.Log(TAG, "Confirm clear recent");

                // Delete from Realm
                ControllerRealm.getInstance().removeRecentKeywordByTag(Constants.TAG_PACKAGE);

                // Recreate recent data.
                setupRecentData();
            }
        });
    }

    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:

                triggerShowProgress(true);

                break;
            case NODATA:
                parentResult.setVisibility(View.VISIBLE);
                textViewNoResult.setVisibility(View.VISIBLE);
                textViewNoResult.setText(parent.getString(R.string.msg_search_result_empty));

                recyclerView.setVisibility(View.GONE);

                parentRecentHeader.setVisibility(View.GONE);
                break;
            case FINISHED:

                triggerShowProgress(false);

                parentResult.setVisibility(View.VISIBLE);
                textViewNoResult.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);

                parentRecentHeader.setVisibility(View.GONE);
                break;
            case DEFAULT:
                parentResult.setVisibility(View.VISIBLE);
                textViewNoResult.setVisibility(View.GONE);

                recyclerView.setVisibility(View.VISIBLE);

                parentRecentHeader.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * Search locally based on keyword from search bar.
     *
     * @param editable Editable Keyword
     */
    private void searchPackageLocally(Editable editable) {
        // Offline searching
        mKeyword = editable.toString();

        showViewState(UIState.LOADING);

        // While offline search result is not empty, run offlineAutoComplete Runnable
        handler.removeCallbacks(offlineAutoComplete);
        handler.postDelayed(offlineAutoComplete, Constants.DURATION_SUPER_LONG);
    }

    @Override
    public void onStop() {
        Utils.Log(TAG, "On stop");
        handler.removeCallbacks(offlineAutoComplete);
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (AppCompatActivity) context;
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

        Utils.Log(TAG, "On create dialog.");

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        if (dialog.getWindow() != null) {
            final Window window = dialog.getWindow();
            // Important to call here: Disable dialog title
            window.requestFeature(Window.FEATURE_NO_TITLE);

            /* Input manager in order to force soft keyboard appear */
            InputMethodManager inputMethodManager = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

        dialog.setCancelable(true);
        return dialog;
    }


    @Override
    public void onItemClick(View view, int position) {

        if (position < 0) return;
        PojoBooking pojoBooking = adapterPackage.getPackage(position);

        if (pojoBooking != null) {
            Utils.Log(TAG, "Clicked on: \"" + pojoBooking.getBooking_code() + "\"");
            Utils.Log(TAG, "Clicked on ID: " + pojoBooking.getBooking_id());

            if (pojoBooking.getBooking_id() == Constants.HEADER_ITEM_ID) {
                // Trigger load more data
                loadMore();
            } else {
                Utils.Log(TAG, "Begin to show package detail.");

                // Return control to FragmentPackage, to open detail
                if (mListener != null) {
                    mListener.onListFragmentInteraction(pojoBooking);
                }

                closeDialogFragment();
            }
        }
    }

    private void closeDialogFragment() {
        if (FragmentDialogSearchPackage.this.getDialog() != null) {
            // if appear as dialog, dismiss when click on package item
            FragmentDialogSearchPackage.this.dismiss();

        }
    }

    /**
     * Create dummy {@link PojoBooking} for "Load more" button at the bottom of the list.
     *
     * @return {@link PojoBooking} with ID {@link Constants#HEADER_ITEM_ID} and status text "Load more"
     */
    private PojoBooking createLoadMoreItem() {
        final PojoBooking loadMoreItem = new PojoBooking();
        loadMoreItem.setBooking_status_text(parent.getString(R.string.lbl_load_more));
        loadMoreItem.setBooking_id(Constants.HEADER_ITEM_ID);
        loadMoreItem.setContent("*");
        return loadMoreItem;
    }

    /**
     * Load more area based search query. Process only when search bar not empty.
     */
    private void loadMore() {

        if (Utils.isEmpty(mKeyword)) return;

        MyLog.FabricLog(Log.INFO, TAG + " - Load more area for: \"" + mKeyword + "\", limit: " + REQUEST_LIMIT);

        Map<String, Long> minMaxBookingId = ControllerRealm.getInstance().getMinAndMaxBookingId();
        long minBookingId = minMaxBookingId.get("MIN");
        long maxBookingId = minMaxBookingId.get("MAX");

        Utils.Log(TAG, "Min booking ID: " + minBookingId);
        Utils.Log(TAG, "Max booking ID: " + maxBookingId);

        ControllerPaket.getInstance().searchPackage(
                null,           /* NULL prevent custom snackbar appear.*/
                mKeyword,       /* Search query to search.*/
                minBookingId,   /* Minimum booking id on Realm*/
                maxBookingId,   /* Max booking ID*/
                REQUEST_LIMIT,  /* Data limit, 10 data each request.*/
                FragmentDialogSearchPackage.this  /* Volley listener*/);
    }

    //////
    // S: Volley result listener
    @Override
    public void onStart(PojoBase clazz) {
        showViewState(UIState.LOADING);
    }

    @Override
    public void onSuccess(PojoBase clazz) {

        // Abort process when fragment is not ready.
        if (parent.isFinishing() || !FragmentDialogSearchPackage.this.isAdded()) return;

        if (clazz == null) return;

        showViewState(UIState.FINISHED);

        if (clazz instanceof PojoBookingData) {
            PojoBookingData pojoBookingData = (PojoBookingData) clazz;

            // Create an unmutable (add/remove) list
            List<PojoBooking> responseData = Arrays.asList(pojoBookingData.getBookings());

            Utils.Log(TAG, "Online data response: " + responseData.size());

            setupSearchResult(responseData, false);

            if (responseData.size() != 0) {

                // Add search results to Realm
                ControllerRealm.getInstance().addAllPackage(responseData);

                // Save recent keyword
                ControllerRealm.getInstance().addRecentKeyword(mKeyword, Constants.TAG_PACKAGE);
            }

            // Increment REQUEST_LIMIT every requests succeed
            this.REQUEST_LIMIT += this.REQUEST_LIMIT;
        }

    }

    @Override
    public void onFinish(PojoBase clazz) {

        // showViewState(UIState.FINISHED);
    }

    @Override
    public boolean onError(PojoBase clazz) {
        showViewState(UIState.ERROR);
        return false;
    }
    // S: Volley result listener
    //////

    private void triggerShowProgress(boolean isShow) {
        if (progressBarSearch == null) return;

        progressBarSearch.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(PojoBooking item);
    }
}
