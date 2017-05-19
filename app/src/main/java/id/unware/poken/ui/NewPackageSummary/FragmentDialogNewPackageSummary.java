package id.unware.poken.ui.NewPackageSummary;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.customView.ExpandableHeightListView;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.PojoNewPackage;
import id.unware.poken.pojo.PojoTarif;
import id.unware.poken.tools.BitmapUtil;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.NewPackageSummary.adapter.AdapterTarif;


public class FragmentDialogNewPackageSummary extends DialogFragment implements
        View.OnClickListener
//        , VolleyResultListener
        , OnClickRecyclerItem {

    private final String TAG = "FragmentDialogNewPackageSummary";

    @BindView(R.id.scrollViewSummary) ScrollView scrollViewSummary;

    @BindView(R.id.parentSummaryHeader) ViewGroup parentSummaryHeader;

    @BindView(R.id.parentInfoZipCode) ViewGroup parentInfoZipCode;
    @BindView(R.id.textViewInfoNewPackage) TextView textViewInfoNewPackage;
    @BindView(R.id.buttonEstimateTariff) Button buttonEstimateTariff;
    @BindView(R.id.progressBarLoadingEstimateTariff) ProgressBar progressBarLoadingEstimateTariff;

    @BindView(R.id.txtBookingCode) TextView textViewBookingCode;
    @BindView(R.id.txtToName) TextView txtToName;
    @BindView(R.id.txtFromName) TextView txtFromName;
    @BindView(R.id.txtToPhone) TextView txtToPhone;
    @BindView(R.id.txtToAddress) TextView txtToAddress;
    @BindView(R.id.txtFromPhone) TextView txtFromPhone;
    @BindView(R.id.txtFromAddress) TextView txtFromAddress;

    @BindView(R.id.relativeLayoutBtnBack) ViewGroup relativeLayoutBtnBack;
    @BindView(R.id.relativeLayoutBtnNewPackage) ViewGroup relativeLayoutBtnNewPackage;

    @BindView(R.id.imageViewPlus) ImageView imageViewPlus;
    @BindView(R.id.imageViewLeftArrow) ImageView imageViewLeftArrow;
    @BindView(R.id.imageViewInfoNewPackage) ImageView imageViewInfoNewPackage;

    // Estimate tariff
    @BindView(R.id.parentEstTariff) ViewGroup parentEstTariff;
    @BindView(R.id.textViewNoEstimateTariff) TextView textViewNoEstimateTariff;
    @BindView(R.id.lvEstTarif) ExpandableHeightListView lvEstTarif;

    /** Flag to indicate whether estimate tariff is loaded or no */
    private boolean mIsLoaded = false;

    private final int STATE_SERVICE_SET = 3;

    /** Save selected service as class member in order, Runnable could access it*/
    private String mStrSelectedService;

    private String mBookingId;
    private PojoNewPackage mPojoNewPackage;

    /** Custom message from server when no ets. tariff available*/
    private String mNoEstimateTariffMessage;

    private List<PojoTarif> listTarif;
    private AdapterTarif adapterTarif;

    private OnFragmentViewClick mListener;

    private AppCompatActivity parent;
    private View mRootView;

    private Handler handler = new Handler();
    private Runnable requestSetServiceRunnable = new Runnable() {
        @Override
        public void run() {
            Utils.Logs('v', TAG, "Request set service runnable begin");
            requestSetService();
        }
    };


    public FragmentDialogNewPackageSummary() {

    }

    public static FragmentDialogNewPackageSummary newInstance(PojoNewPackage pojoNewPackage) {
        FragmentDialogNewPackageSummary fragment = new FragmentDialogNewPackageSummary();
        Bundle args = new Bundle();

        args.putParcelable(Constants.EXTRA_NEW_PACKAGE_DATA, pojoNewPackage);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyLog.FabricLog(Log.INFO, "Package summary dialog created.");

        Bundle bundle = getArguments();
        if (bundle != null) {
            mPojoNewPackage = bundle.getParcelable(Constants.EXTRA_NEW_PACKAGE_DATA);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.dialog_report, null, false);
        ButterKnife.bind(this, mRootView);

        initView();

        return mRootView;
    }

    public void setCallbacks(OnFragmentViewClick listener) {
        mListener = listener;
    }

    @SuppressLint("DefaultLocale")
    private void initView() {

        if (getDialog() != null && getDialog().getWindow() != null) {
            // request a window without the title
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            // request keyboard to show
            // getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            getDialog().setCancelable(true);
        }

        if (mPojoNewPackage == null) return;

        // Set booking ID val as member
        mBookingId = String.valueOf(mPojoNewPackage.getBookingId());

        textViewBookingCode.setText(String.valueOf(mPojoNewPackage.getBookingCode()));

        txtToName.setText(String.valueOf(mPojoNewPackage.getToName()));
        txtFromName.setText(String.valueOf(mPojoNewPackage.getFromName()));

        txtToPhone.setText(String.valueOf(mPojoNewPackage.getToPhone()));
        txtToAddress.setText(String.valueOf(mPojoNewPackage.getToAddress()));

        txtFromPhone.setText(String.valueOf(mPojoNewPackage.getFromPhone()));
        txtFromAddress.setText(String.valueOf(mPojoNewPackage.getFromAddress()));

        /*
          Set ZIP code info appear or not.
          Success the hide banner or vice versa.
         */
        if (mPojoNewPackage.getValidZip() != null && !mPojoNewPackage.getValidZipIsSuccess()) {
            parentInfoZipCode.setVisibility(View.VISIBLE);
            // noinspection deprecation
            textViewInfoNewPackage.setText(Html.fromHtml(mPojoNewPackage.getValidZipMsg()));
        } else {
            parentInfoZipCode.setVisibility(View.GONE);
        }

        textViewBookingCode.setOnClickListener(this);

        buttonEstimateTariff.setOnClickListener(this);
        relativeLayoutBtnBack.setOnClickListener(this);
        relativeLayoutBtnNewPackage.setOnClickListener(this);

        imageViewPlus.setColorFilter(BitmapUtil.getDrawableFilter(parent, R.color.colorAccent));
        imageViewLeftArrow.setColorFilter(BitmapUtil.getDrawableFilter(parent, R.color.colorPrimaryDark));

        imageViewInfoNewPackage.setColorFilter(BitmapUtil.getDrawableFilter(parent, R.color.black_90));

        setupEstTariff();

    }

    private void setupEstTariff() {

        listTarif = new ArrayList<>();
        adapterTarif = new AdapterTarif(
                parent,  /* Context*/
                -1,  /* Layout resource*/
                listTarif,  /* List data*/
                FragmentDialogNewPackageSummary.this  /* Click listener on item*/
        );

        lvEstTarif.setExpanded(true);
        lvEstTarif.setAdapter(adapterTarif);
        lvEstTarif.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txtBookingCode:
                MyLog.FabricLog(Log.INFO, "User click on Booking Code");
                scrollToEstimateTariffSection();
                break;
            case R.id.buttonEstimateTariff:
                Utils.Logs('i', TAG, "Load estimate tariff --> " + mIsLoaded);
                if (!mIsLoaded) {
                    requestEstimateTariff();
                } else {
                    scrollToEstimateTariffSection();
                }
                break;
            case R.id.relativeLayoutBtnBack:
            case R.id.relativeLayoutBtnNewPackage:
                if (mListener != null) {
                    mListener.onClickAtView(v);
                }
                break;
        }

    }

    private void scrollToEstimateTariffSection() {
        Utils.Logs('i', TAG, "Scroll view: " + scrollViewSummary.getMaxScrollAmount());
        Utils.Logs('i', TAG, "Parent tariff x y: " + parentEstTariff.getX() + " - " + parentEstTariff.getY());

        scrollViewSummary.post(new Runnable() {
            @Override
            public void run() {
                scrollViewSummary.smoothScrollTo(0, (int) parentEstTariff.getY());
            }
        });
    }

    private void requestEstimateTariff() {
        Utils.Log(TAG, "Begin load estimate tariff");
//        ControllerPaket.getInstance().estimateTariffCheck(
//                null,
//                mBookingId,
//                FragmentDialogNewPackageSummary.this);
    }

    private void requestSetService() {
//        ControllerPaket.getInstance().setServiceTariff(
//                null,
//                mBookingId,
//                mStrSelectedService,
//                FragmentDialogNewPackageSummary.this
//        );
    }

    @Override
    public void onStop() {
        Utils.Log(TAG, "On stop FragmentDialogArea");

        mListener = null;

//        AppClass.getInstance().cancelPendingRequests(FragmentDialogNewPackageSummary.class);

        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (AppCompatActivity) context;
    }

    /**
     * Select service from estimate tariff list.
     * @param view Clicked view from RecyclerView adapter
     * @param position Position of clicked item
     *
     * @since [V49] No more service selection from estimate list.
     */
    @Override
    public void onItemClick(View view, int position) {
        Utils.Log(TAG, "Estimate tariff item clicked.");
//        if (position < 0 ) return;
//        PojoTarif clickedPojoTariff = listTarif.get(position);
//
//        if (clickedPojoTariff == null) return;
//
//        // Setup vendor name (TIKI, JNE, etc.) text.
//        String strVendorName = Utils.isEmpty(clickedPojoTariff.getVendorCode())
//                ? clickedPojoTariff.getVendorName()
//                : clickedPojoTariff.getVendorCode().toUpperCase();
//
//        String strSelectedTariff = String.format("%s/%s",
//                strVendorName,
//                clickedPojoTariff.getServiceCode());
//
//        Utils.Logs('v', TAG, "Click on pos: " + position + ", tariff: " + strSelectedTariff);
//
//        this.mStrSelectedService = strSelectedTariff;
//
//        handler.removeCallbacks(requestSetServiceRunnable);
//        handler.postDelayed(requestSetServiceRunnable, AppClass.DURATION_SUPER_LONG);
    }

    private void interfaceState(int stateNum) {
        switch (stateNum) {
            case STATE_SERVICE_SET:
                showLoadingIndicator(false);

                // Wait a second then remove custom snackbar
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Utils.snackbarDismiss();
                    }
                }, Constants.DURATION_SUPER_LONG);

                break;
            case Constants.STATE_LOADING:

                showLoadingIndicator(true);

                break;
            case Constants.STATE_DEFAULT:

                showLoadingIndicator(false);

                parentEstTariff.setVisibility(View.GONE);
                break;
            case Constants.STATE_FINISHED:

                showLoadingIndicator(false);

                parentEstTariff.setVisibility(View.VISIBLE);
                textViewNoEstimateTariff.setVisibility(View.GONE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scrollToEstimateTariffSection();
                    }
                }, 500);

                break;
            case Constants.STATE_NODATA:

                showLoadingIndicator(false);

                parentEstTariff.setVisibility(View.VISIBLE);
                String msgNoData = Utils.isEmpty(mNoEstimateTariffMessage)
                        ? parent.getString(R.string.no_items)
                        : mNoEstimateTariffMessage;

                textViewNoEstimateTariff.setText(msgNoData);
                textViewNoEstimateTariff.setVisibility(View.VISIBLE);
                Utils.Logs('w', TAG, "No est. tariff available.");
                break;
        }
    }

    private void showLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            // Hide button and show progress indicator
            progressBarLoadingEstimateTariff.setVisibility(View.VISIBLE);
            ViewCompat.animate(buttonEstimateTariff)
                    .scaleX(0f).scaleY(0f)
                    .alpha(0f).setDuration(100)
                    .setStartDelay(100L)
                    .start();

        } else {
            // Show button and hide progress
            progressBarLoadingEstimateTariff.setVisibility(View.GONE);
            ViewCompat.animate(buttonEstimateTariff)
                    .scaleX(1f).scaleY(1f)
                    .alpha(1f).setDuration(100)
                    .setStartDelay(300L)
                    .start();
        }
    }

    //////
    // S: Volley listener
//    @Override
//    public void onStart(PojoBase clazz) {
//        interfaceState(Constants.STATE_LOADING);
//    }
//
//    @Override
//    public void onSuccess(PojoBase clazz) {
//
//        if (parent.isFinishing() || !FragmentDialogNewPackageSummary.this.isAdded()) return;
//
//        if (clazz == null) return;
//
//        if (clazz instanceof PojoEstimateTariffData) {
//
//            PojoEstimateTariffData pojoTarifData = (PojoEstimateTariffData) clazz;
//            if (pojoTarifData.getTariffs() != null) {
//
//                Utils.Log(TAG, "Pojo Vendor Tariff Size: " + pojoTarifData.getTariffs().length);
//
//                if (pojoTarifData.getTariffs().length > 0) {
//
//                    Collections.addAll(listTarif, pojoTarifData.getTariffs());
//
//                    adapterTarif.notifyDataSetChanged();
//
//                    // Show data when it's available
//                    interfaceState(AppClass.STATE_FINISHED);
//
//                    // Set flag that estimated tariff has been loaded
//                    FragmentDialogNewPackageSummary.this.mIsLoaded = true;
//
//                } else {
//                    // Show empty state
//                    interfaceState(AppClass.STATE_NODATA);
//                }
//
//                Utils.Log(TAG, "Tariff list size: " + listTarif.size());
//
//            } else {
//                Utils.Log(TAG, "Something missing on response :(.");
//            }
//
//        } else {
//            Utils.Logs('i', TAG, "Set service on note success. Success --> " + clazz.success);
//
//            if (clazz.success == 1) {
//                Utils.Logs('i', TAG, "SUCCESSFULL");
//
//                if (mRootView != null) {
//                    Utils.snackBar(mRootView, "Selected service add to note", Log.INFO);
//                    interfaceState(FragmentDialogNewPackageSummary.this.STATE_SERVICE_SET);
//                }
//
//            }
//
//        }
//
//    }
//
//    @Override
//    public void onFinish(PojoBase clazz) {
//        if (parent.isFinishing() || !FragmentDialogNewPackageSummary.this.isAdded()) return;
//
//        if (clazz != null) {
//            Utils.Logs('w', TAG, "Message from server: " + clazz.msg );
//
//            if (!Utils.isEmpty(clazz.msg)) {
//                FragmentDialogNewPackageSummary.this.mNoEstimateTariffMessage = clazz.msg;
//                FragmentDialogNewPackageSummary.this.interfaceState(AppClass.STATE_NODATA);
//
//                // Set flag that estimated tariff has been loaded
//                FragmentDialogNewPackageSummary.this.mIsLoaded = true;
//            }
//        }
//    }
//
//    @Override
//    public boolean onError(PojoBase clazz) {
//        if (clazz != null) {
//            Utils.Logs('e', TAG, "Error request est. tariff with message: " + (clazz.msg == null ? "NULL" : clazz.msg));
//        }
//
//        FragmentDialogNewPackageSummary.this.interfaceState(AppClass.STATE_DEFAULT);
//        return false;
//    }
    // E: Volley listener
    //////

    public interface OnFragmentViewClick {
        void onClickAtView(View v);
    }
}
