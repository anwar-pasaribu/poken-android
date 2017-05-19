package id.unware.poken.ui.Tariff.TariffMain.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.BuildConfig;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDate;
import id.unware.poken.customView.ExpandableHeightListView;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.pojo.PojoArea;
import id.unware.poken.pojo.PojoRateItem;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.Tariff.TariffArea.view.AreaFragment;
import id.unware.poken.ui.Tariff.TariffMain.model.RateModel;
import id.unware.poken.ui.Tariff.TariffMain.presenter.IRatePresenter;
import id.unware.poken.ui.Tariff.TariffMain.presenter.RatePresenter;
import id.unware.poken.ui.Tariff.TariffMain.view.adapter.RateAdapter;

public class RateActivity extends BaseActivity
        implements AreaFragment.OnListFragmentInteractionListener, IRateView {

    private final String TAG = this.getClass().getSimpleName();

    @BindView(R.id.parentOrigin) ViewGroup parentOrigin;
    @BindView(R.id.textViewOrigin) TextView textViewOrigin;
    @BindView(R.id.parentDestination) ViewGroup parentDestination;
    @BindView(R.id.textViewDestination) TextView textViewDestination;

    @BindView(R.id.editTextWeight) EditText mTextWeight;
    @BindView(R.id.listViewRate) ExpandableHeightListView mListView;
    @BindView(R.id.progressBarRateActivity) ProgressBar mProgressBarRateActivity;
    @BindView(R.id.buttonPlus) Button buttonPlus;
    @BindView(R.id.buttonMinus) Button buttonMinus;
    @BindView(R.id.buttonCheck) Button buttonCheck;
    @BindView(R.id.parentView) ScrollView parentView;

    private String originTariffCode;
    private String destTariffCode;

    private ProgressDialog progress;

    // ArrayList for AreaFragment Instance
    private ArrayList<String> offlineMapList = new ArrayList<>();

    // Max weight
    @SuppressWarnings("FieldCanBeLocal") private final float MAX_WEIGHT = 1000F;

    // Locale for formatting purpose
    private Locale locale;

    // Main view
//    private ViewGroup viewGroup;

    private final float initialWeight = 0.5f;

    private IRatePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init widget
        ButterKnife.bind(this);

        presenter = new RatePresenter(this, new RateModel());

        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        locale = ControllerDate.getInstance().getDefLocale();

        initViewEventListener();

        // View for snackbar purpose
//        viewGroup = (ViewGroup) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        mListView.setExpanded(true);

        // Check whether Shared Preferences contain last originTariffCode
        String lastOrigin = SPHelper.getInstance().getSharedPreferences(Constants.LAST_ORIGIN, "");
        if (!lastOrigin.equals("")) {

            String[] lastOrigins = lastOrigin.split("#");
            textViewOrigin.setText(lastOrigins[0]);

            if (lastOrigins.length > 1) originTariffCode = lastOrigins[1];
        }

        // Load offline data
        loadData("kota_sorted.json");

        // Set prefilled data for development purpose
        setPrefilledData();

        setOriginDestinationStyle();
    }

    /**
     * For development purpose, set prefilled
     * Jakarta (CGK01.00) to Bogor (BGR01.00)
     */
    @SuppressLint("SetTextI18n")
    private void setPrefilledData() {
        if (BuildConfig.DEV_MODE) {
            textViewOrigin.setText("Jakarta");
            originTariffCode = "CGK01.00";

            textViewDestination.setText("Bogor");
            destTariffCode = "BGR01.00";

            mTextWeight.setText(String.valueOf(initialWeight * 25f));
        }
    }

    /**
     * Style Origin and Destination based on tariff code availability
     */
    private void setOriginDestinationStyle() {
        final int intColorDefault = ContextCompat.getColor(this, R.color.black_90);
        final int intColorActive = ContextCompat.getColor(this, R.color.red);

        textViewOrigin.setTextColor(!TextUtils.isEmpty(originTariffCode) ? intColorDefault : intColorActive);
        textViewDestination.setTextColor(!TextUtils.isEmpty(destTariffCode) ? intColorDefault : intColorActive);
    }

    /**
     * Register all event to views.
     */
    private void initViewEventListener() {
        parentOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseAreaDialog(AreaFragment.ORIGIN, offlineMapList);
            }
        });

        parentDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChooseAreaDialog(AreaFragment.DESTINATION, offlineMapList);
            }
        });

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWeightValue(true);
            }
        });

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWeightValue(false);
            }
        });

        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTariffOnServer();
            }
        });
    }

    /**
     * Add or substract weight with one.
     *
     * @param isAdd : true the add the value or vice versa.
     */
    private void addWeightValue(boolean isAdd) {

        float num;
        String strWeight = mTextWeight.getText().toString();

        if (TextUtils.isEmpty(strWeight)) {
            num = initialWeight;
        } else {
            try {
                num = Float.parseFloat(strWeight.replaceAll(",", "."));
            } catch (NumberFormatException nfe) {
                num = initialWeight;

                // Crashlytics purpose logging
                MyLog.FabricLog(Log.ERROR, "Number format exception while parsing weight.", nfe);
            }
        }

        num = isAdd ? (num + initialWeight)
                : (num - initialWeight) < initialWeight ? initialWeight : (num - initialWeight);

        /**
         * When active Locale is except Indonesia, set default locale to English
         */
        final String strFormatAddition = "%.2f";
        if (!locale.getCountry().equals("ID")) {
            locale = ControllerDate.getInstance().getDefLocale();
            mTextWeight.setText(String.format(locale, strFormatAddition, num));
        } else {
            mTextWeight.setText(String.format(locale, strFormatAddition, num));
        }
    }

    /**
     * Check tariff on server when all requirement is satisfied.
     */
    private void checkTariffOnServer() {
        // Make sure user input a valid range of number
        boolean isValidNumber = false;
        String strWeightText = mTextWeight.getText().toString().replaceAll(",", ".");
        try {

            if (TextUtils.isEmpty(strWeightText)) {
                mTextWeight.setText(String.format(locale, "%.2f", initialWeight));
            }

            float num = Float.parseFloat(strWeightText);

            // For request to server
            strWeightText = String.format(locale, "%.3f", num);

            if (num < 0) {
                mTextWeight.setText(String.format(locale, "%.2f", initialWeight));
            } else if (num > MAX_WEIGHT) {
                mTextWeight.setText(String.format(locale, "%.0f", MAX_WEIGHT));
                isValidNumber = true;
                showMessage(getString(R.string.msg_error_number_to_large, MAX_WEIGHT));
            }

        } catch (NumberFormatException nuex) {
            nuex.printStackTrace();
            isValidNumber = true;
            showMessage(getString(R.string.msg_error_number_format_not_supported, strWeightText));
//            Utils.snackBar(viewGroup, getString(R.string.msg_error_number_format_not_supported, strWeightText));

            // Crashlytics logging
            MyLog.FabricLog(Log.ERROR, TAG + " Number Format Exception occour while parsing float", nuex);
        }

        if (!TextUtils.isEmpty(originTariffCode)
                && !TextUtils.isEmpty(destTariffCode)
                && !isValidNumber) {

            presenter.checkTariff();
//            ApiClient.getInstance().rate(
//                    originTariffCode,
//                    destTariffCode,
//                    getUSNumber(strWeightText.replaceAll(",", ".")),
//                    new ApiClient.Callback<PojoTariffCheck>() {
//
//                        @Override
//                        public void onStart(String message) {
//                            MyLog.FabricLog(Log.INFO, "Rate checker started");
//                            RateActivity.this.showProgressDialog();
//                        }
//
//                        @Override
//                        public void success(PojoTariffCheck data) {
//                            MyLog.FabricLog(Log.INFO, "Rate checker success with tariff data: " + ((data == null) ? "NULL" : "DATA AVAILABLE"));
//
//                            // Dismiss progress dialog before show the data
//                            RateActivity.this.dismissProgressDialog();
//
//                            if (data == null) return;
//
//                            // Detect when no delivery provided by check when
//                            // Tariff is zero
//                            List<PojoRateItem> listRateItem = new ArrayList<>();
//                            for (PojoRateItem rateItem : data.getTariff()) {
//                                if (rateItem.getTariff() != 0) {
//                                    listRateItem.add(rateItem);
//                                }
//                            }
//
//                            if (listRateItem.isEmpty()) {
//                                // Provide empty message
//                                listRateItem.clear();
//                                String strMessage;
//
//                                // Display message from server
//                                if (data.getMsg() != null && data.getMsg().length() > 0) {
//                                    strMessage = data.getMsg();
//                                } else {
//                                    // Display no-delivery message
//                                    strMessage = RateActivity.this
//                                            .getString(
//                                                    R.string.lbl_rate_no_delivery,
//                                                    textViewOrigin.getText().toString(),
//                                                    textViewDestination.getText().toString());
//                                }
//
//                                mListView.setAdapter(new RateAdapter(RateActivity.this, listRateItem));
//
//                                ((TextView) findViewById(R.id.tvEmptyRate)).setText(Html.fromHtml(strMessage));
//
//                            } else {
//
//                                ((TextView) findViewById(R.id.tvEmptyRate)).setText(getString(R.string.lbl_empty_string));
//
//                                mListView.setAdapter(new RateAdapter(RateActivity.this, listRateItem));
//                            }
//                        }
//
//                        @Override
//                        public void failed(String message) {
//                            MyLog.FabricLog(Log.ERROR, "Rate checker failed with msg: " + message);
//
//                            // When activity is going to destroy, abort all.
//                            if (RateActivity.this.isFinishing()) {
//                                Util.Log(TAG, "Activity Rate is finishing");
//                                return;
//                            }
//
//                            RateActivity.this.dismissProgressDialog();
//
//                            // [V30] Make sure no list item visible
//                            mListView.setAdapter(new RateAdapter(RateActivity.this, Collections.<PojoRateItem>emptyList()));
//
//                            // [V30] Show error message and appropriate message when Tiki server down
//                            if (!Util.isEmpty(message)) {
//                                ((TextView) findViewById(R.id.tvEmptyRate)).setText(message);
//                            } else {
//                                // noinspection deprecation
//                                ((TextView) findViewById(R.id.tvEmptyRate)).setText(
//                                        Html.fromHtml(getString(R.string.lbl_error_to_connect)));
//                            }
//                        }
//                    }
//            );

        } else {
            // Make sure list is cleared when form is not valid.
            mListView.setAdapter(new RateAdapter(RateActivity.this, new ArrayList<PojoRateItem>()));

            if (TextUtils.isEmpty(originTariffCode)) {
                showMessage(getString(R.string.msg_choose_origin));
//                Utils.snackBar(viewGroup, getString(R.string.msg_choose_origin));
            } else if (TextUtils.isEmpty(destTariffCode)) {
                showMessage(getString(R.string.msg_choose_destination));
//                Utils.snackBar(viewGroup, getString(R.string.msg_choose_destination));
            }
        }
    }

    /**
     * Show progress dialog when dialog is not available yet.
     */
    public void showProgressDialog() {
        if (progress == null) {
            Utils.Log(TAG, "Create progress dialog.");
            progress = new ProgressDialog(RateActivity.this);
            progress.setTitle(this.getString(R.string.loading));
            progress.setMessage(null);
            progress.setIndeterminate(true);
            progress.setCancelable(false);
        }

        progress.show();
    }

    /**
     * Dismiss dialog when it's appear.
     */
    public void dismissProgressDialog() {
        if (progress != null) {
            Utils.Log(TAG, "Dismiss progress dialog.");
            progress.dismiss();
        }
    }

    /**
     * Covert non-US characters to US (latin text).
     *
     * @param strNum : String to covert to US String
     * @return : Clean US String
     */
    private String getUSNumber(String strNum) {

        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        try {
            if (strNum.contains("٫")) {
                strNum = formatter.parse(strNum.split("٫")[0].trim()) + "." + formatter.parse(strNum.split("٫")[1].trim());
            } else {
                strNum = formatter.parse(strNum).toString();
            }

        } catch (ParseException e) {
            e.printStackTrace();

            MyLog.FabricLog(Log.ERROR, TAG + "getUSNumber Parse exception occur.");
        }

        return strNum;
    }

    /**
     * Open Area Chooser dialog.
     *
     * @param areaFor         : Target area as Origin or Destination
     * @param offlineCityList : City ArrayList
     */
    private void showChooseAreaDialog(int areaFor, ArrayList<String> offlineCityList) {
        FragmentTransaction ft = hideDialog();
        ft.addToBackStack(null);
        AreaFragment newFragment = AreaFragment.newInstance(areaFor, offlineCityList);
        newFragment.show(ft, "dialog");
    }

    /**
     * Make sure previous "dialog" fragment is hidden.
     *
     * @return : New {@link FragmentTransaction}
     */
    private FragmentTransaction hideDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ((AreaFragment) prev).dismiss();
            ft.remove(prev);
        }
        return ft;
    }

    /**
     * Handler when user clicked location item
     *
     * @param item    : AreaItem object consist
     * @param areaFor : Target text field. (Origin or Destination)
     */
    @Override
    public void onListFragmentInteraction(PojoArea item, int areaFor) {

        switch (areaFor) {
            case AreaFragment.ORIGIN:
                originTariffCode = item.tariffCode;
                textViewOrigin.setText(item.daerah);

                // Save last originTariffCode to shared preferences with format:
                // <nama_lokasi>#<tariff_code> e.g. Jakarta#CGK01.00
                // So for next session it's can be used.
                String strOriginTariff = String.format("%s#%s", item.daerah, item.tariffCode);
                SPHelper.getInstance()
                        .setPreferences(Constants.LAST_ORIGIN, strOriginTariff);
                break;
            case AreaFragment.DESTINATION:
                destTariffCode = item.tariffCode;
                textViewDestination.setText(item.daerah);

                // Hide keyboard when user choose destionation
                // Check if no view has focus:
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                break;
        }

        setOriginDestinationStyle();

        hideDialog();
    }

    /**
     * Load static file (assets/kota.json) and then pupulate to memory.
     *
     * @param inFile : File address (assets/kota_sorted.json)
     */
    private void loadData(String inFile) {
        Utils.Log(TAG, "Begin load static data.");
        LoadDataAsyncTask loadDataAsyncTask = new LoadDataAsyncTask();
        loadDataAsyncTask.execute(inFile);
    }

    /**
     * Show progress bar when loading city data.
     *
     * @param isShow : State to show progress or not.
     */
    private void showProgress(boolean isShow) {

        parentOrigin.setVisibility(!isShow ? View.VISIBLE : View.INVISIBLE);
        parentDestination.setVisibility(!isShow ? View.VISIBLE : View.INVISIBLE);
        mTextWeight.setVisibility(!isShow ? View.VISIBLE : View.INVISIBLE);
        buttonPlus.setVisibility(!isShow ? View.VISIBLE : View.INVISIBLE);
        buttonMinus.setVisibility(!isShow ? View.VISIBLE : View.INVISIBLE);
        buttonCheck.setVisibility(!isShow ? View.VISIBLE : View.INVISIBLE);
        mListView.setVisibility(!isShow ? View.VISIBLE : View.INVISIBLE);

        // Special for progress bar
        // Hide when showProgress(FALSE)
        mProgressBarRateActivity.setVisibility(!isShow ? View.GONE : View.VISIBLE);

    }

    @Override
    public String getOrigin() {
        return originTariffCode;
    }

    @Override
    public String getDest() {
        return destTariffCode;
    }

    @Override
    public String getWeight() {
        String strWeightText = mTextWeight.getText().toString().replaceAll(",", ".");
        return getUSNumber(strWeightText.replaceAll(",", "."));
    }

    @Override
    public void setEmptyRate(String strMessage) {
        ((TextView) findViewById(R.id.tvEmptyRate)).setText(Html.fromHtml(strMessage));
    }

    @Override
    public void setListAdapter(List<PojoRateItem> listRateItem) {
        mListView.setAdapter(new RateAdapter(RateActivity.this, listRateItem));
    }

    @Override
    public void showMessage(String msg) {

        Utils.snackBar(parentView, msg);
    }

    /**
     * AsyncTask to load "kota_sorted.json", each line of text then saved to
     * ArrayList.
     */
    public class LoadDataAsyncTask extends AsyncTask<String, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(Boolean.TRUE);
        }

        @Override
        protected ArrayList<String> doInBackground(String... strings) {

            String tContents = "";

            try {

                InputStream stream = getAssets().open(strings[0]);

                int size = stream.available();
                byte[] buffer = new byte[size];
                int streamRead = stream.read(buffer);
                stream.close();
                tContents = new String(buffer);

                Utils.Log(TAG, "Stream read --> " + streamRead);
            } catch (IOException e) {
                // Handle exceptions here
                e.printStackTrace();
            }

            ArrayList<String> arrayListCityTariff = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(tContents);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String cityString = jsonObject.optString("kota");
                    String tariffCodeString = jsonObject.optString("tariff_code");

                    arrayListCityTariff.add(String.format("%s#%s", cityString, tariffCodeString));
                }

                Utils.Log(TAG, "Data size: " + arrayListCityTariff.size());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return arrayListCityTariff;
        }


        @Override
        protected void onPostExecute(ArrayList<String> resStringArrayList) {

            // Abort process when Activity no longer available
            if (RateActivity.this.isFinishing()) {
                return;
            }

            offlineMapList = resStringArrayList;
            showProgress(Boolean.FALSE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        setOriginDestinationStyle();
    }

    @Override
    protected void onDestroy() {

        // Dismiss Progress Dialog when activity is going to destroy
        this.dismissProgressDialog();

        super.onDestroy();
    }
}
