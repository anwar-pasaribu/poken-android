package id.unware.poken.ui.detailPackage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.PokenApp;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.customView.ExpandableHeightListView;
import id.unware.poken.httpConnection.UrlComposer;
import id.unware.poken.interfaces.FragmentProgress;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.BookingStatus;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.pojo.PojoHistory;
import id.unware.poken.pojo.PojoHistoryData;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.BitmapUtil;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;

/**
 * Created by marzellaalfamega on 7/6/15.
 * Show Package Detail
 */
public class FragmentPackageDetail2 extends BaseFragment implements VolleyResultListener {

    private static final String TAG = "FragmentPackageDetail2";

    @BindView(R.id.parentSwipeRefresh) SwipeRefreshLayout parentSwipeRefresh;

    @BindView(R.id.scrollViewDetail) ScrollView scrollViewDetail;

    @BindView(R.id.txtBookingCode) TextView txtBookingCode;
    @BindView(R.id.txtStatus) TextView txtStatus;
    @BindView(R.id.imageButtonShowTrackingHistory) ImageButton imageButtonShowTrackingHistory;

    @BindView(R.id.txtToName) TextView txtToName;
    @BindView(R.id.txtToPhone) TextView txtToPhone;
    @BindView(R.id.txtToAddress) TextView txtToAddress;
    @BindView(R.id.txtToCityZip) TextView txtToCityZip;

    @BindView(R.id.txtFromName) TextView txtFromName;
    @BindView(R.id.txtFromPhone) TextView txtFromPhone;
    @BindView(R.id.txtFromAddress) TextView txtFromAddress;
    @BindView(R.id.txtFromCityZip) TextView txtFromCityZip;

    @BindView(R.id.txtService) TextView txtService;
    @BindView(R.id.txtContent) TextView txtContent;
    @BindView(R.id.txtInsuredValue) TextView txtInsuredValue;
    @BindView(R.id.txtNote) TextView txtNote;

    // Separator below package detail
    @BindView(R.id.sepBelowPackageDetailBorder) View sepBelowPackageDetailBorder;
    @BindView(R.id.sepBelowPackageDetail) View sepBelowPackageDetail;

    @BindView(R.id.txtVendorName) TextView txtVendorName;
    @BindView(R.id.txtReceiptNumber) TextView txtReceiptNumber;
    @BindView(R.id.txtType) TextView txtType;
    @BindView(R.id.txtBerat) TextView txtBerat;
    @BindView(R.id.txtETA) TextView txtETA;
    @BindView(R.id.txtPrice) TextView txtPrice;
    @BindView(R.id.txtVolume) TextView txtVolume;
    @BindView(R.id.txtVolumetricWeight) TextView txtVolumetricWeight;
    @BindView(R.id.parentViewScroll) LinearLayout parentView;

    // Separator below vendor generated values ex. air waybill
    @BindView(R.id.sepBelowTariffBorder) View sepBelowTariffBorder;
    @BindView(R.id.sepBelowTariff) View sepBelowTariff;


    @BindView(R.id.parentVendor) ViewGroup parentVendor;
    @BindView(R.id.parentMessage) ViewGroup parentMessage;

    // Header for booking history (Tracking history)
    @BindView(R.id.parentHistory) ViewGroup parentHistory;
    @BindView(R.id.parentHeader) ViewGroup parentHeader;
    @BindView(R.id.textViewHeaderTitle) TextView textViewHeaderTitle;
    @BindView(R.id.progressIndicatorTrackingHistory) ProgressBar progressBarHistory;
    @BindView(R.id.textViewNoTracking) TextView textViewNoTracking;

    // Last indicator
    @BindView(R.id.viewVerticalLine) View viewVerticalLine;
    @BindView(R.id.parentLastIndicator) ViewGroup parentLastIndicator;
    @BindView(R.id.imageViewIndicatorColor) ImageView imageViewIndicatorColor;
    @BindView(R.id.imageViewIndicatorLogo) ImageView imageViewIndicatorLogo;

    // List for tracking history
    @BindView(R.id.lvHistory) ExpandableHeightListView lvHistory;

    private int mIntStatusNumber = -9;
    private String mBookingCode;

    private String mBookingId;
    private String bookingHistoryId = "";

    private FragmentProgress mFragmentProgressListener;

    private AdapterTrackingHistory adapterTrackingHistory;
    private List<PojoHistory> listHistory = new ArrayList<>();

    /** Flag to indicate delete action show loading progress*/
    private boolean IS_LOADING_DELETE = false;

    /** Flag indicate loading booking history*/
    private boolean IS_LOADING_BOOKING_HISTORY = true;

    /**
     * Handle to wait getting address before user really stop
     * interact with the map.
     */
    private Handler mHandler = new Handler();

    private Runnable requestBookingHistoryRunnable = new Runnable() {
        @Override
        public void run() {

            if (Utils.isNetworkNotConnected(parent)) {
                Utils.snackBar(parentView, parent.getString(R.string.msg_no_network));
            } else {

                // Set flag indicate loading booking history
                FragmentPackageDetail2.this.IS_LOADING_BOOKING_HISTORY =  true;

                requestBookingHistory();
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_package_detail_2, container, false);
        ButterKnife.bind(this, view);

        // Set view with PojoBooking data from FragmentPackage
        // set current booking_id
        Bundle packageBundle = getArguments().getBundle(Constants.EXTRA_POJO_BOOKING_DATA);
        if (packageBundle != null && packageBundle.containsKey(PojoBooking.KEY_BOOKING_ID)) {

            // Set current booking_id that will use for booking history request.
            mBookingId = String.valueOf(packageBundle.getLong(PojoBooking.KEY_BOOKING_ID));

            initView(view);
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Show offline booking history data immediately
        setupBookingHistoryList();

        Utils.Log(TAG, "Status number: \"" + mIntStatusNumber + "\"");

        // Setup booking history list
        // [V47] If Package status is Booked, do not download Tracking History
        if (mIntStatusNumber != BookingStatus.BOOKED.getIntVal()) {
            mHandler.postDelayed(requestBookingHistoryRunnable, Constants.DURATION_SUPER_LONG);
        } else {
            Utils.Log(TAG, "No need to download Tracking History.");
        }

        // [V47] Special view for each Booking status
        setupPackageDetailView(mIntStatusNumber);
    }

    /**
     * Special view case for each Booing status.
     * For ex. Booked: then hide "show tracking history button"
     *
     * @param intStatusNumber Booking status number
     */
    private void setupPackageDetailView(int intStatusNumber) {
        if (intStatusNumber == BookingStatus.BOOKED.getIntVal()) {
            // Hide Tracking history attribute
            imageButtonShowTrackingHistory.setVisibility(View.GONE);
            showNoTrackingHistory(true);
        }
    }

    /**
     * Request Booking History from server based on current booking_id.
     * @since Nov 30 2016 - NEW
     */
    private void requestBookingHistory() {
        Utils.Log(TAG, "Begin booking history request");

        // mBookingId sets from onActivityCreated

        // Assume first item on PojoHistory list is last booking history id
        if (!listHistory.isEmpty()) {
            bookingHistoryId = String.valueOf(listHistory.get(0).getId());
        }

        ControllerPaket.getInstance().getBookingHistory(mBookingId, bookingHistoryId, parentView, FragmentPackageDetail2.this);
    }

    private void scrollToTrackingHistorySection() {
        Utils.Logs('i', TAG, "Scroll view: " + scrollViewDetail.getMaxScrollAmount());
        Utils.Logs('i', TAG, "Parent tracking x y: " + parentHistory.getX() + " - " + parentHistory.getY());

        scrollViewDetail.post(new Runnable() {
            @Override
            public void run() {
                scrollViewDetail.smoothScrollTo(0, (int) parentHistory.getY());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.m_package_detail, menu);

        Utils.Logs('i', TAG, "Status number: " + mIntStatusNumber);

        menu.findItem(R.id.action_delete).setVisible(mIntStatusNumber == 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                sharePackage();
                break;
            case R.id.action_delete:
                showDeleteConfirmation();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmation() {

        // TODO For testing purpose to delelete item on low-memory scenaryo
        // Intent data with PojoBooking id
//        Intent intent = new Intent();
//        intent.putExtra(PojoBooking.KEY_BOOKING_ID, 3);
//        intent.putExtra("pos", 3);
//        parent.setResult(Activity.RESULT_OK, intent);
//        parent.finish();

        ControllerDialog.getInstance().showYesNoDialog(
                parent.getString(R.string.delete_confirmation),
                parent,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                // Update flag that user begin to delete data
                                // in order to show progress dialog or no
                                FragmentPackageDetail2.this.IS_LOADING_DELETE = true;

                                deletePackage();

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                        dialog.dismiss();
                    }
                }, getString(R.string.btn_positive_delete), getString(R.string.btn_negative_no));
    }

    /**
     * Share Paket ID code
     * Compose Paket ID link to track the code.
     */
    private void sharePackage() {

        // V48 - Fabric Answer Events
//        MyLog.FabricTrackContentView("Share Paket Code", "Feature", "SH-00");

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Paket ID");
        sendIntent.putExtra(Intent.EXTRA_TEXT, UrlComposer.getInstance().composeShareUrl(mBookingCode));
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, parent.getString(R.string.title_share_intent)));
    }

    private void deletePackage() {
        Utils.Log(TAG, "Begin delete booking id: " + mBookingId);
        ControllerPaket.getInstance().deletePackage(parentView, mBookingId, this);
    }

    /**
     * Display PojoBooking data.
     *
     * @param view : Parent view that hold all require widgets.
     *
     * @since Dec 1 2016 - No more bundle data.
     * @since Nov 9 2016 - Check whether {@link PojoBooking} based
     *                     on desired {@code booking_id} is available.
     */
    private void initView(View view) {

        setHasOptionsMenu(true);

        long longBookingId = Utils.getParsedLong(mBookingId);

        Context context = view.getContext();
        PojoBooking pojoBooking = ControllerRealm.getInstance().getBookingById(longBookingId);

        // Make sure PojoBooking data is available
        if (pojoBooking == null) return;

        // [V49] Ini swipe refresh layout to force Booking History refresh
        parentSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Utils.Log(TAG, "Refresh parent swipe refresh layout.");
                requestBookingHistory();
            }
        });

        // Hide no tracking history for the first time
        showNoTrackingHistory(false);

        // Scroll to Tracking History section
        imageButtonShowTrackingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToTrackingHistorySection();
            }
        });

        if (txtToName == null) {
            initAllViewManually(view);
        }

        if (parent != null) {
            parent.invalidateOptionsMenu();
        }

        // Values for views
        mBookingCode = pojoBooking.getBooking_code();
        mIntStatusNumber = Utils.getParsedInt(pojoBooking.getStatus());

        String  strFromName = pojoBooking.getFrom_name(),
                strFromPhone = pojoBooking.getFrom_phone(),
                strFromAddress = pojoBooking.getFrom_address(),
                /*NO FROM MORE EMAIL*/
                strFromZipCode = String.valueOf(pojoBooking.getFrom_zip_code()),

                strToName = pojoBooking.getTo_name(),
                strToPhone = pojoBooking.getTo_phone(),
                strToAddress = pojoBooking.getTo_address(),
                /*NO MORE TO EMAIL*/
                strToZipCode = String.valueOf(pojoBooking.getTo_zip_code()),

                strPaketIdService = String.valueOf(pojoBooking.getPaketIdService()),
                strContent = String.valueOf(pojoBooking.getContent()),
                strNote = String.valueOf(pojoBooking.getNote()),

                strVendorName = pojoBooking.getVendor_name(),
                strVendorAirWaybill = pojoBooking.getVendor_airway_bill(),

                strServiceCode = pojoBooking.getService_code(),
                strServiceName = pojoBooking.getService_name(),

                strWeight = pojoBooking.getWeight(),
                strVolume =             "" /*pojoBooking.getVolume()*/,  // [Build 53] Hide field
                strVolumetricWeight =   ""/*pojoBooking.getVolumetric_weight()*/,  // [Build 53] Hide field
                strEta = pojoBooking.getEta(),
                strTariff = pojoBooking.getTariff();

        // [V49] Insured value
        long longInsuredValue = pojoBooking.getInsuredValue();

        // Preformatted string
        // ---
        StringBuilder sb = new StringBuilder();

        // Add small bullet to separate SDS * Sameday service
        String textServiceName = "";
        if (!Utils.isEmpty(strServiceCode) && !Utils.isEmpty(strServiceName)) {
            sb.delete(0, sb.length());
            textServiceName = sb
                    .append(strServiceCode)
                    .append(" &#8226; ")
                    .append(strServiceName)
                    .toString();
        }

        // Weight with measure; 12 kg
        String textWeight = "";
        if (!Utils.isEmpty(strWeight) && !isZero(strWeight)) {
            sb.delete(0, sb.length());
            textWeight = sb
                    .append(strWeight)
                    .append(" ")
                    .append(parent.getString(R.string.lbl_weight_unit)).toString();
        }

        // Use StringBuilder to enable cm<cubiq>
        StringBuilder sbVolume = getFormattedVolumeText(strVolume);
        StringBuilder sbVolumetricWeight = getFormattedVolumeText(strVolumetricWeight);

        // Add <currency> <amount>
        // Format Insured value and tariff
        DecimalFormat df = new DecimalFormat(
                "#,##0", new DecimalFormatSymbols(new Locale("id", "ID")));

        String strCurrency = (StringUtils.isEmpty(pojoBooking.getCurrency_unit_code())
                            ? parent.getString(R.string.lbl_idr)
                            : pojoBooking.getCurrency_unit_code());

        String textPrice = "";
        if (!Utils.isEmpty(strTariff) && !isZero(strTariff)) {

            Double doubleTariff = Utils.getParsedDouble(strTariff);
            strTariff = df.format(doubleTariff);

            sb.delete(0, sb.length());
            textPrice = sb
                    .append(strCurrency)
                    .append(" ")
                    .append(strTariff).toString();
        }

        // Insured value
        String textInsuredValue = "";
        if (longInsuredValue != 0) {
            sb.delete(0, sb.length());
            textInsuredValue = sb
                    .append(strCurrency)
                    .append(" ")
                    .append(df.format(longInsuredValue)).toString();
        }

        // Set value to views
        txtBookingCode.setText(mBookingCode);
        txtStatus.setTextColor(pojoBooking.getStatusTextColor(context));
        txtStatus.setText(pojoBooking.getBooking_status_text());

        txtFromName.setText(strFromName);
        txtFromPhone.setText(strFromPhone);
        txtFromAddress.setText(strFromAddress);
        txtFromCityZip.setText(strFromZipCode);

        txtToName.setText(strToName);
        txtToPhone.setText(strToPhone);
        txtToAddress.setText(strToAddress);
        txtToCityZip.setText(strToZipCode);

        txtService.setText(strPaketIdService);
        txtContent.setText(strContent);
        txtInsuredValue.setText(textInsuredValue);
        txtNote.setText(strNote);

        // Show or hide ZIP codes (Sender/Receiver), Paket ID Services, Content, Insured Value, and Note
        showOrHideTextViewParent(txtFromCityZip, strFromZipCode);
        showOrHideTextViewParent(txtToCityZip, strToZipCode);
        showOrHideTextViewParent(txtService, strPaketIdService);
        showOrHideTextViewParent(txtContent, strContent);
        showOrHideTextViewParent(txtInsuredValue, textInsuredValue);
        showOrHideTextViewParent(txtNote, strNote);

        // Show or hide (when air waybill non-null, show vendor container)
        // [V54] Toggle separator visibility
        if (StringUtils.isEmpty(strVendorAirWaybill)) {

            // Hide package detail and vendor generated separator
            sepBelowPackageDetail.setVisibility(View.GONE);
            sepBelowPackageDetailBorder.setVisibility(View.GONE);

            parentVendor.setVisibility(View.GONE);

        } else {

            txtVendorName.setText(strVendorName);
            //noinspection deprecation
            txtType.setText(Html.fromHtml(textServiceName));
            txtReceiptNumber.setText(strVendorAirWaybill);

            txtBerat.setText(textWeight);
            txtVolume.setText(sbVolume);
            txtVolumetricWeight.setText(sbVolumetricWeight);
            txtETA.setText(strEta);
            txtPrice.setText(textPrice);

            // Show or hide row; Hide when value is empty
            showOrHideTextViewParent(txtType, textServiceName);  // Selected service
            showOrHideTextViewParent(txtBerat, textWeight);  // Weight
            showOrHideTextViewParent(txtVolume, sbVolume.toString());  // Volume
            showOrHideTextViewParent(txtVolumetricWeight, sbVolumetricWeight.toString());  // Volumetric weight
            showOrHideTextViewParent(txtETA, strEta);  // Estimate arrival time (in days)
            showOrHideTextViewParent(txtPrice, textPrice);  // Shipment price
        }
    }

    /**
     * Compose formatted volume. 78 cm3
     *
     * @param strVolume Volume string
     * @return Formatted string ex. 78 cm^3
     */
    private StringBuilder getFormattedVolumeText(String strVolume) {
        if (Utils.isEmpty(strVolume) || isZero(strVolume)) return new StringBuilder("");

        return new StringBuilder()
                .append(strVolume)
                .append(" ")
                .append(parent.getString(R.string.lbl_volume_unit));
    }

    /**
     * Show data table row when value is available. Or delete row (TableRow) when no data.
     * [V49] For ZIP Code with LinearLayout parent, delete TextView instead.
     *
     * @param tv TextView that hold the data, it's parent (TableRow) will be removed when no value.
     * @param value Value to be displayed.
     */
    private void showOrHideTextViewParent(TextView tv, String value) {

        if (StringUtils.isEmpty(value)) {

            ViewParent textViewParent = tv.getParent();

            if (textViewParent instanceof TableRow) {
                Utils.Log(TAG, "TextView has TableRow parent.");
                ((TableRow) textViewParent).setVisibility(View.GONE);
            } else if (textViewParent instanceof LinearLayout){
                Utils.Log(TAG, "TextView has LinearLayout parent.");
                tv.setVisibility(View.GONE);
            }

        }

    }

    /**
     * Check whether string number's value is zero.
     *
     * @param strNumber Number string
     * @return true when number string is 0
     */
    private boolean isZero(String strNumber) {
        return Utils.getParsedDouble(strNumber) == 0;
    }

    /**
     * Setup Booking History list. Include get data from Realm which is possibly slow on certain
     * case for example on slow phone with huge {@link PojoHistory} data.
     *
     * @since Nov 30 2016 - NEW!
     */
    private void setupBookingHistoryList() {
        // Set Expandable list view for Booking History
        lvHistory.setExpanded(true);

        listHistory.addAll(ControllerRealm.getInstance().getAllHistory(mBookingId));

        adapterTrackingHistory = new AdapterTrackingHistory(parent, -1, listHistory);
        lvHistory.setAdapter(adapterTrackingHistory);

        Utils.Log(TAG, "Booking History size: " + listHistory.size());
    }

    private void refreshTrackingHistory(List<PojoHistory> pojoHistories) {
        if (pojoHistories.size() != 0) {
            listHistory.addAll(0, pojoHistories);
            adapterTrackingHistory.notifyDataSetChanged();

            showNoTrackingHistory(false);

        } else {

            if (listHistory.size() == 0) {
                showNoTrackingHistory(true);
            }
        }
    }

    /**
     * Show no tracking available status include:
     * - Show textview "no tracking yet"
     * - Hide indicator
     * - Hide button show tracking when no Tracking history.
     *
     * @param isShow Show or not
     *
     * @since V47 - NEW
     */
    private void showNoTrackingHistory(boolean isShow) {
        // Show "no tracking yet" when no history found.
        textViewNoTracking.setVisibility(isShow ? View.VISIBLE : View.GONE);

        // [V49] Hide all booking history
        // if isShow then remove all Booking History view.
        parentHistory.setVisibility(isShow? View.GONE : View.VISIBLE);

        // Hide when no tracking history
        parentLastIndicator.setVisibility(!isShow ? View.VISIBLE : View.GONE);

        // [V47] Hide "button show tracking" when no Tracking History. or show it.
        if (isShow) {
            imageButtonShowTrackingHistory.setVisibility(View.INVISIBLE);

            // [V54] Hide separator between vendor generated values and booking history
            sepBelowTariff.setVisibility(View.GONE);
            sepBelowTariffBorder.setVisibility(View.GONE);

        } else {
            imageButtonShowTrackingHistory.setVisibility(View.VISIBLE);

            // [V54] Show separator between vendor generated values and booking history
            sepBelowTariff.setVisibility(View.VISIBLE);
            sepBelowTariffBorder.setVisibility(View.VISIBLE);

        }
    }

    /**
     * Init all widget manually in case Butter Knife is fail.
     */
    private void initAllViewManually(View view) {
        txtBookingCode = (TextView) view.findViewById(R.id.txtBookingCode);
        txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        txtToName = (TextView) view.findViewById(R.id.txtToName);
        txtToPhone = (TextView) view.findViewById(R.id.txtToPhone);
        txtToAddress = (TextView) view.findViewById(R.id.txtToAddress);
        txtToCityZip = (TextView) view.findViewById(R.id.txtToCityZip);
        lvHistory = (ExpandableHeightListView) view.findViewById(R.id.lvHistory);
        txtFromName = (TextView) view.findViewById(R.id.txtFromName);
        txtFromPhone = (TextView) view.findViewById(R.id.txtFromPhone);
        txtFromAddress = (TextView) view.findViewById(R.id.txtFromAddress);
        txtFromCityZip = (TextView) view.findViewById(R.id.txtFromCityZip);
        parentMessage = (ViewGroup) view.findViewById(R.id.parentMessage);
        parentHistory = (ViewGroup) view.findViewById(R.id.parentHistory);
        txtContent = (TextView) view.findViewById(R.id.txtContent);
        txtNote = (TextView) view.findViewById(R.id.txtNote);
        txtVendorName = (TextView) view.findViewById(R.id.txtVendorName);
        txtType = (TextView) view.findViewById(R.id.txtType);
        txtReceiptNumber = (TextView) view.findViewById(R.id.txtReceiptNumber);
        txtBerat = (TextView) view.findViewById(R.id.txtBerat);
        txtETA = (TextView) view.findViewById(R.id.txtETA);
        txtPrice = (TextView) view.findViewById(R.id.txtPrice);
        parentVendor = (ViewGroup) view.findViewById(R.id.parentVendor);
        txtVolume = (TextView) view.findViewById(R.id.txtVolume);
        txtVolumetricWeight = (TextView) view.findViewById(R.id.txtVolumetricWeight);
    }

    //////
    // S: VolleyListener
    @Override
    public void onStart(PojoBase clazz) {
        showViewState(UIState.LOADING);
    }

    @Override
    public void onSuccess(PojoBase clazz) {

        if (parent == null || parent.isFinishing()) return;

        if (clazz != null) {

            showViewState(UIState.FINISHED);

            if (clazz instanceof PojoHistoryData) {
                //////
                // Manage PojoHistory (booking history) response data.

                final PojoHistory[] pojoHistories = ((PojoHistoryData) clazz).getBookingHistories();
                final int responseDataSize = pojoHistories.length;

                Utils.Logs('i', TAG, "Response Pojo history size: " + responseDataSize);

                // Refresh list only when response PojoHistory size
                // is not the same with current data
                if (responseDataSize != 0) {

                    // Save PojoHistory to Realm
                    ControllerRealm.getInstance().addAllHistory(pojoHistories);

                    // Convert array to list
                    List<PojoHistory> pojoHistoryList = Arrays.asList(pojoHistories);

                    Utils.Log(TAG, "List history size: " + pojoHistoryList.size());

                    refreshTrackingHistory(pojoHistoryList);

                    triggerShowProgress(false);

                } else {
                    Utils.Logs('w', TAG, "NO NEW HISTORY");

                    refreshTrackingHistory(Collections.<PojoHistory>emptyList());
                }

            } else {
                //////
                // Manage package deletion
                long longBookingId = Utils.getParsedLong(mBookingId);

                // [V49] Package presenter responsibility to delete package from Realm
                // ControllerRealm.getInstance().removePackageWithId(longBookingId);

                showViewState(UIState.FINISHED);

                // Intent data with PojoBooking id
                Intent intent = new Intent();
                intent.putExtra(PojoBooking.KEY_BOOKING_ID, longBookingId);
                parent.setResult(Activity.RESULT_OK, intent);
                parent.finish();
            }
        }
    }

    @Override
    public void onFinish(PojoBase clazz) {
        showViewState(UIState.FINISHED);

    }

    @Override
    public boolean onError(PojoBase clazz) {
        showViewState(UIState.ERROR);
        return false;
    }
    // E: VolleyListener
    //////

    /**
     * Show progress indicator. The indicator different between deletion and loading
     * booking history.
     *
     * @param isShow Show progress or no.
     */
    private void triggerShowProgress(boolean isShow) {

        if (this.IS_LOADING_DELETE && mFragmentProgressListener != null) {
            // Show loading dialog when ProgressListener is non-null
            mFragmentProgressListener.showProgress(isShow, FragmentPackageDetail2.class);
        } else if (this.IS_LOADING_BOOKING_HISTORY) {

            if (isShow) {

                // Set indicator color to grey
                imageViewIndicatorColor.setColorFilter(BitmapUtil.getDisabledColor(parent));
                imageViewIndicatorLogo.setVisibility(View.GONE);

                progressBarHistory.setVisibility(View.VISIBLE);

            } else {

                // Set indicator color to green (success)
                imageViewIndicatorColor.setColorFilter(BitmapUtil.getDrawableFilter(parent, R.color.green));
                imageViewIndicatorLogo.setVisibility(View.VISIBLE);

                progressBarHistory.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FragmentProgress) {
            mFragmentProgressListener = (FragmentProgress) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentProgress interface.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentProgressListener = null;

        PokenApp.getInstance().cancelPendingRequests(FragmentPackageDetail2.class);

        // Remove ongoing booking history request
        mHandler.removeCallbacks(requestBookingHistoryRunnable);
    }

    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                parentSwipeRefresh.setRefreshing(true);
                triggerShowProgress(true);
                break;
            case FINISHED:
                parentSwipeRefresh.setRefreshing(false);
                triggerShowProgress(false);
                break;
            case NODATA:
                parentSwipeRefresh.setRefreshing(false);
                break;
            case ERROR:
                parentSwipeRefresh.setRefreshing(false);
                break;
        }
    }
}
