package id.unware.poken.ui.TrackPackage.TrackingMain.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.BuildConfig;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.pojo.RecentSearchKeyword;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.TrackPackage.TrackingMain.model.TrackingMainModel;
import id.unware.poken.ui.TrackPackage.TrackingMain.presenter.ITrackingMainPresenter;
import id.unware.poken.ui.TrackPackage.TrackingMain.presenter.TrackingMainPresenter;
import id.unware.poken.ui.TrackPackage.TrackingMain.view.adapter.TrackingResultPagerAdapter;
import id.unware.poken.ui.TrackPackage.TrackingResult.view.adapter.TrackingAdapter;

public class TrackingActivity extends AppCompatActivity implements View.OnClickListener, ITrackingMainView {

    private final String TAG = "TrackingActivity";

    @BindView(R.id.viewVerticalLine) View viewVerticalLine;
    @BindView(R.id.imageButtonBack) ImageButton imageButtonBack;
    @BindView(R.id.recyclerViewTracking) RecyclerView mRecyclerView;
    @BindView(R.id.editTextTracking) EditText mEditText;
    @BindView(R.id.viewGroupBtnCheck) ViewGroup viewGroupBtnCheck;
    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @SuppressWarnings("FieldCanBeLocal") private TrackingResultPagerAdapter mSectionsPagerAdapter;

    private ProgressDialog progress;

    private List<Object> mListTracking = new ArrayList<>();
    private TrackingAdapter trackingAdapter;

    // View container for snackbar
    private ViewGroup viewGroup;

    private ITrackingMainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        ButterKnife.bind(this);

        presenter = new TrackingMainPresenter(this, new TrackingMainModel());
        // View container for snackbar
        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);

        // Prevent keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        trackingAdapter = new TrackingAdapter(this, mListTracking);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(trackingAdapter);

        // Get recent search from Shared Preferences
        setupRecent();

        // Recycler item click
        trackingAdapter.setOnItemClickListener(new TrackingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String airWayBill) {

                if (position < 0) return;

                Utils.Log(TAG, "Air way bill: " + airWayBill + ", Pos: " + position);
                MyLog.FabricLog(Log.DEBUG, "Air way bill: " + airWayBill + ", Pos: " + position);

                try {
                    String[] recentDatas = airWayBill.split(" ");

                    Utils.Log(TAG, "Recent datas length: " + recentDatas.length);

                    if (recentDatas.length != 0) {
                        Utils.Log(TAG, "Number: " + recentDatas[0]);
                        Utils.Log(TAG, "Name: " + recentDatas[1]);

                        requestTracking(recentDatas[0]);
                    }

                } catch (PatternSyntaxException e) {
                    e.printStackTrace();
                }
            }
        });

        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setupRecent();
            }
        });

        // Register action listener, in case user press DONE while on weight edit text.
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

                if (event != null) {
                    if (KeyEvent.ACTION_DOWN == event.getAction() && KeyEvent.KEYCODE_SEARCH == event.getKeyCode()) {
                        // Physical keyboard.
                        mEditText.clearFocus();
                        requestTracking(mEditText.getText().toString());
                        return false;  // To prevent this call again
                    }

                } else if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                    // Soft keyboard enter button
                    mEditText.clearFocus();
                    requestTracking(mEditText.getText().toString());
                    return false;
                }

                return false;
            }
        });

        imageButtonBack.setOnClickListener(this);
        viewGroupBtnCheck.setOnClickListener(this);

        // Set text (as sample) only in DEV_MODE
        if (BuildConfig.DEV_MODE) {
            mEditText.setText(BuildConfig.TRACK_CODE_SAMPLE);
        }

        // Crashlytics user information
        MyLog.FabricSetUserInformation();

    }

    private void setupRecent() {

        viewVerticalLine.setVisibility(View.GONE);

        showMultipleTrackings(false);

        mListTracking.clear();

        mListTracking.addAll(ControllerRealm.getInstance().getAllRecentKeywordByTag(Constants.TAG_TRACKING));

        if (mListTracking.size() != 0) {

            MyLog.FabricLog(Log.INFO, "Recent tracking size: " + mListTracking.size());

            RecentSearchKeyword header = new RecentSearchKeyword();
            header.setId(Constants.TAG_HEADER_RECENT);

            mListTracking.add(0, header);
        }

        refreshList();
    }

    /**
     * Request Tracking state.
     *
     * @param strResiKeyword : Package airway bill
     */
    private void requestTracking(final String strResiKeyword) {

        if (!TextUtils.isEmpty(strResiKeyword)) {

            if (mEditText == null) return;

            mEditText.setText(strResiKeyword);
            mEditText.setSelection(mEditText.getText().length());

            presenter.track();
//            ApiClient.getInstance().track(
//                    TrackingActivity.this,
//                    strResiKeyword,
//                    new ApiClient.Callback<PojoTrackingData>() {
//                @Override
//                public void onStart(String message) {
//                    TrackingActivity.this.showProgressDialog();
//
//                    MyLog.FabricLog(Log.INFO, TAG + " - [start] Start Tracking for: " + strResiKeyword);
//
//                }
//
//                @Override
//                public void success(PojoTrackingData data) {
//
//                    if (TrackingActivity.this.isFinishing()) return;
//
//                    TrackingActivity.this.dismissProgressDialog();
//
//                    int trackingSize = data.getTrackings().length;
//
//                    if (trackingSize > 0 && data.success == 1) {
//
//                        // Make sure vertical line is empty
//                        viewVerticalLine.setVisibility(View.VISIBLE);
//
//                        // First tracking data to check whether tracking success or no
//                        PojoTracking firstPojoTracking = data.getTrackings()[0];
//
//                        MyLog.FabricLog(Log.INFO, TAG + " - [success] Tracking size: " + trackingSize);
//
//                        // [V30] Some case cnno key not available, the abort process
//                        // Check whether "msg" / "error" available.
//                        if (Util.isEmpty(firstPojoTracking.getCnno())) {
//
//                            if (!Util.isEmpty(firstPojoTracking.getMsg())) {
//
//                                MyLog.FabricLog(Log.ERROR, TAG + " - Tracking CNNO not available with msg: " + firstPojoTracking.getMsg());
//                                failed(firstPojoTracking.getMsg());
//
//                            } else if (!Util.isEmpty(firstPojoTracking.getError())) {
//
//                                MyLog.FabricLog(Log.ERROR, TAG + " - Tracking CNNO not available with error field: " + firstPojoTracking.getError());
//                                failed(firstPojoTracking.getError());
//
//                            }
//
//                            return;
//                        }
//
//                        // Add data to db
//                        ControllerRealm.getInstance().addAllTrackingData(data);
//
//                        // Collect RESI
//                        ArrayList<String> cnnoArrayList = new ArrayList<>();
//                        for (PojoTracking pojoTracking: data.getTrackings()) {
//
//                            cnnoArrayList.add(pojoTracking.getCnno());
//                        }
//
//                        // Show multi RESI
//                        if (cnnoArrayList.size() > 1) {
//
//                            showMultipleTrackings(true);
//                            setupViewPager(cnnoArrayList);
//
//                        // Show single RESI
//                        } else if (cnnoArrayList.size() == 1){
//
//                            showMultipleTrackings(false);
//
//                            PojoTracking pojoTracking = data.getTrackings()[0];
//
//                            mListTracking.clear();
//                            mListTracking.add(pojoTracking);  // For header purpose
//
//                            if (pojoTracking.getStatuses().length > 0) {
//                                Collections.addAll(mListTracking, pojoTracking.getStatuses());  // Tracking item
//                            } else {
//                                // Show "Belum ada data tracking untuk saat ini." message
//                                mListTracking.add(pojoTracking.getMsg());
//
//                                // Hide vertical line when no data available
//                                viewVerticalLine.setVisibility(View.GONE);
//                            }
//
//                            trackingAdapter.notifyDataSetChanged();
//                        }
//
//                        // [V29] Save last tracking keyword.
//                        if (!cnnoArrayList.isEmpty()) {
//                            String strKeyword = strResiKeyword.trim() + " (" + data.getTrackings()[0].getConsigneeName().trim() + ")";
//
//                            // Save recent tracking
//                            ControllerRealm.getInstance().addRecentKeyword(strKeyword, MyApp.TAG_TRACKING);
//                        }
//
//                    } else {
//                        Util.snackBar(viewGroup, TrackingActivity.this.getString(R.string.msg_tracking_not_found));
//                    }
//                }
//
//                @Override
//                public void failed(String message) {
//
//                    if (TrackingActivity.this.isFinishing()) {
//                        return;
//                    }
//
//                    TrackingActivity.this.dismissProgressDialog();
//
//                    Util.complain(TrackingActivity.this, message);
//
//                    // Crashlytics log
//                    MyLog.FabricLog(Log.ERROR, "Tracking request failed. Message: " + message);
//                }
//            });
//
        } else {
            showMessage(TrackingActivity.this.getString(R.string.msg_empty_tracking_number));
        }
    }

    public void setupViewPager(ArrayList<String> trackingCnnoList) {

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new TrackingResultPagerAdapter(this, getSupportFragmentManager());
        mSectionsPagerAdapter.setPageContent(trackingCnnoList);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);
    }

    public void showMultipleTrackings(boolean isShow) {

        if (isShow) {
            mViewPager.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.VISIBLE);

            mRecyclerView.setVisibility(View.GONE);
        } else {
            mViewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);

            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Show progress dialog when dialog is not available yet.
     */
    public void showProgressDialog() {
        if (progress == null) {
            progress = ControllerDialog.getInstance().showLoadingNotCancelable(TrackingActivity.this);
        } else {
            progress.show();
        }
    }

    /**
     * Dismiss dialog when it's appear.
     */
    public void dismissProgressDialog() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public void setVerticalLineVisible() {
        viewVerticalLine.setVisibility(View.VISIBLE);
    }

    @Override
    public List getListTracking() {
        return mListTracking;
    }

    @Override
    public View getVerticalLine() {
        return viewVerticalLine;
    }

    @Override
    public void refreshList() {
        trackingAdapter.notifyDataSetChanged();
    }

    @Override
    public String getResi() {
        return mEditText.getText().toString();
    }

    @Override
    public void showMessage(String message) {
        Utils.snackBar(viewGroup, message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonBack:
                this.finish();
                break;
            case R.id.viewGroupBtnCheck:
                final String strResiKeyword = String.valueOf(mEditText.getText()).trim();
                requestTracking(strResiKeyword);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        // Dismiss Progress Dialog when activity is going to destroy to prevent throwing
        // android.view.WindowLeaked exeption.
        this.dismissProgressDialog();

        super.onDestroy();
    }


}
