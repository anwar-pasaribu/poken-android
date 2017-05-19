package id.unware.poken.ui.newPackage.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import id.unware.poken.PokenApp;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.interfaces.FragmentProgress;
import id.unware.poken.pojo.GeneralListItem;
import id.unware.poken.pojo.PojoAddressBook;
import id.unware.poken.pojo.PojoNewPackage;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.BitmapUtil;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.NewPackageSummary.FragmentDialogNewPackageSummary;
import id.unware.poken.ui.addressBook.AcAddressBook;
import id.unware.poken.ui.newPackage.presenter.NewPackagePresenter;
import id.unware.poken.ui.newPackage.view.adapters.AddressBookAutocompleteAdapter;
import id.unware.poken.ui.newPackage.view.adapters.PackageServicesSpinnerAdapter;


public class FragmentNewPackage2 extends BaseFragment implements
        INewPackageView,
        View.OnFocusChangeListener,
        FragmentDialogNewPackageSummary.OnFragmentViewClick {

    private final String TAG = "FragmentNewPackage2";

    private NewPackagePresenter mPresenter;

    @BindView(R.id.parentViewScroll) ScrollView parentView;

    /**
     * Parent ViewGroup that hold all from
     */
    @BindView(R.id.parentNewPackageForm) ViewGroup parentNewPackageForm;

    //////
    // S: Sender input form
    @BindView(R.id.parentSender) ViewGroup parentSender;  // This view hold all Sender views.

    @BindView(R.id.parentHeaderSender) ViewGroup parentHeaderSender;
    @BindView(R.id.textViewLableSender) TextView textViewLableSender;
//    @BindView(R.id.imageButtonInfoSender) ImageButton imageButtonInfoSender;
    @BindView(R.id.imageButtonAttachToSender) ImageButton imageButtonAttachToSender;

    @BindView(R.id.editTextSender) AutoCompleteTextView editTextSender;
    @BindView(R.id.buttonClearTextSender) ImageButton buttonClearTextSender;
    @BindView(R.id.textViewSenderHelperText) TextView textViewSenderHelperText;
    @BindView(R.id.sepSender) View sepSender;
    // E: Sender input form
    //////

    //////
    // S: Receiver input form
    @BindView(R.id.parentReceiver) ViewGroup parentReceiver;  // This view hold all Receiver views.

    @BindView(R.id.parentHeaderReceiver) ViewGroup parentHeaderReceiver;
    @BindView(R.id.textViewLableReceiver) TextView textViewLableReceiver;
//    @BindView(R.id.imageButtonInfoReceiver) ImageButton imageButtonInfoReceiver;
    @BindView(R.id.imageButtonAttachToReceiver) ImageButton imageButtonAttachToReceiver;

    @BindView(R.id.editTextReceiver) AutoCompleteTextView editTextReceiver;
    @BindView(R.id.buttonClearTextReceiver) ImageButton buttonClearTextReceiver;
    @BindView(R.id.textViewReceiverHelperText) TextView textViewReceiverHelperText;
    @BindView(R.id.sepReceiver) View sepReceiver;
    // E: Receiver input form
    //////

    //////
    // S: Content input form
    @BindView(R.id.parentContent) ViewGroup parentContent;

    @BindView(R.id.parentHeaderContent) ViewGroup parentHeaderContent;
    @BindView(R.id.textViewLableContent) TextView textViewLableContent;
//    @BindView(R.id.imageButtonInfoContent) ImageButton imageButtonInfoContent;

    @BindView(R.id.editTextContent) EditText editTextContent;
    @BindView(R.id.buttonClearTextContent) ImageButton buttonClearTextContent;
    @BindView(R.id.sepContent) View sepContent;
    // E: Content input form
    //////

    //////
    // S: Service selection input form
    @BindView(R.id.parentService) ViewGroup parentService;

    @BindView(R.id.parentHeaderService) ViewGroup parentHeaderService;
    @BindView(R.id.textViewLableService) TextView textViewLableService;
//    @BindView(R.id.imageButtonInfoService) ImageButton imageButtonInfoService;

    @BindView(R.id.spinnerPackageServices) Spinner spinnerPackageServices;

    @BindView(R.id.recyclerViewServices) RecyclerView recyclerViewServices;
    @BindView(R.id.textViewSelectedService) TextView textViewSelectedService;
    @BindView(R.id.sepService) View sepService;
    // E: Service selection input form
    //////

    //////
    // S: Insurance input form
    @BindView(R.id.parentInsurance) ViewGroup parentInsurance;

    @BindView(R.id.parentHeaderInsurance) ViewGroup parentHeaderInsurance;
    @BindView(R.id.textViewLableInsurance) TextView textViewLableInsurance;
//    @BindView(R.id.imageButtonInfoInsurance) ImageButton imageButtonInfoInsurance;

    @BindView(R.id.parentEditTextInsurance) ViewGroup parentEditTextInsurance;  // Catch user touch even outside EditText
    @BindView(R.id.editTextInsurance) EditText editTextInsurance;
    @BindView(R.id.buttonClearInsuranceText) ImageButton buttonClearInsuranceText;

    @BindView(R.id.textViewInsuranceDescription) TextView textViewInsuranceDescription;
    // @BindView(R.id.sepInsurance) View sepInsurance;
    // E: Insurance input form
    //////

    //////
    // S: Note input form
    @BindView(R.id.parentNote) ViewGroup parentNote;

    @BindView(R.id.parentHeaderNote) ViewGroup parentHeaderNote;
    @BindView(R.id.textViewLableNote) TextView textViewLableNote;
//    @BindView(R.id.imageButtonInfoNote) ImageButton imageButtonInfoNote;

    @BindView(R.id.editTextNote) EditText editTextNote;
    @BindView(R.id.buttonClearTextNote) ImageButton buttonClearTextNote;
    @BindView(R.id.sepNote) View sepNote;
    // E: Note input form
    //////

    // Widget to show success message when user is awesome
    @BindView(R.id.parentInfo) RelativeLayout parentInfo;
    @BindView(R.id.imageViewInfoIcSend) ImageView imageViewInfoIcSend;

    // Button to skip tutorial
    @BindView(R.id.parentBtnSkipTutorial) RelativeLayout parentBtnSkipTutorial;
    @BindView(R.id.btnSkipTutorial) Button btnSkipTutorial;

    /**
     * ButterKnife Unbinder for views
     */
    private Unbinder mUnbinder;

    private final PokenApp values = PokenApp.getInstance();

    // ArrayList on newly created Package (PojoBooking)
    private ArrayList<String> newBookingIdList = new ArrayList<>();

    private Toolbar mToolbar;
    private ImageView toolbarImageButtonNewPackage;

    private SPHelper spHelper;

    /**
     * String builder for text processing. (much faster)
     */
    private StringBuilder mSb;

    /**
     * Totorial purpose vars.
     */
    private boolean isTutorialMode = false;
//    private MaterialIntroListener materialIntroListener;
    private List<View> learntView = new ArrayList<>();

    private boolean isMatched = false;
    private static final String REGEX_SENDER_RECEIVER_FORMAT = ".+[^\\d]\\s[\\d\\+\\s\\(\\)\\[\\]\\-\\/]{7,}\\s.+[^\\d]\\s\\d+";

    /** String tog for dialog fragment*/
    private String mStringLastTag;

    /**
     * String tag for clear text ImageButton.
     */
    private String mTagClearTextButton;

    /**
     * String tag indicate Info button.
     */
    private String mTagFieldInfo;

    /**
     * String selected servie. REG (Regular) is default choice.
     */
    private String mStrSelectedService = "REG";

    private InputMethodManager inputMethodManager;

    private AddressBookAutocompleteAdapter adSuggestion;
    private List<PojoAddressBook> listAllContact = new ArrayList<>();

    /** Adapter for service selection*/
    // private PackageServiceAdapter mPackageServiceAdapter;

    private List<Object> mPackageServiceList;

    private SparseArray<ImageButton> mImageButtonHashMap;
    private SparseArray<EditText> mEditTextHashMap;

    private FragmentProgress mFragmentProgressListener;

    /**
     * Current typed text on amount edit text
     */
    private String mCurrentTypedStr = "";

    /**
     * Decimal format for Indonesia
     */
    private DecimalFormat mMoneyFormatIndo = new DecimalFormat(
            "#,##0", new DecimalFormatSymbols(new Locale("id", "ID")));

    // Fabric user behavior tracking purpose
    private int intBtnHelpTrial = 0;
    private int intBtnAddressBookTrial = 0;
    private int intBtnSubmitNewPackageTrial = 0;


    public static FragmentNewPackage2 newInstance(){
        return new FragmentNewPackage2();
    }

    public FragmentNewPackage2() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utils.Logs('w', TAG, "ON CREATE ALL VIEW");
        View view = inflater.inflate(R.layout.f_new_package, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mPresenter = new NewPackagePresenter(this);

        mTagClearTextButton = parent.getString(R.string.view_tag_clear_text_button);
        mTagFieldInfo = parent.getString(R.string.view_tag_info_button);

        // Array to hold views for auto view operations (e.g. set on click event)
        mImageButtonHashMap = new SparseArray<ImageButton>();
        mEditTextHashMap = new SparseArray<EditText>();

        mSb = new StringBuilder();

        /* Shared Preferences helper in order to save user configurations */
        spHelper = SPHelper.getInstance();

        /* When view is created, then decide is in tutorial mode */
        if (getArguments() != null && getArguments().containsKey(Constants.EXTRA_IS_TUTORIAL)) {
            isTutorialMode = getArguments().getBoolean(Constants.EXTRA_IS_TUTORIAL, false);
        }

        initView();

        initToolBar();

        /* Set TextWatcher listener */
        initTextWatcherListener();

        /* For tutorial purpose */
        initMaterialTutorialListener();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Prepare Package Services option
        mPresenter.requestPackageServices();

        // Populate views for futher operation, then views save to HashMap
        populateViewsToArray(this.parentNewPackageForm);

        // Request all address book for Sender/Receiver autocomplete purpose.
        mPresenter.requestAddressBook(true);
        setupAutoComplete();

        // Set all image button invisible
        setupImageButtonsVisibility(-1, false);

        // Set Initial Sender text. Data from Shared Preferences
        setData();

        // Set field label with <bullet> Optional
        setupLabel();

        showTutorialMode();
    }

    /**
     * Show description of selected service and set selected Service to mStrSelectedService
     * @param itemPosition Position of selected service.
     */
    @SuppressWarnings("unused")
    private void showServiceDescription(int itemPosition) {

        if (itemPosition >= 0
                && mPackageServiceList != null
                && itemPosition < mPackageServiceList.size()
                && textViewSelectedService != null) {

            Utils.Log(TAG,"Set description for selected service pos: " + itemPosition);

            GeneralListItem item = (GeneralListItem) mPackageServiceList.get(itemPosition);

            mStrSelectedService = item.getTitle();

            textViewSelectedService.setText(item.getContent());
        }
    }

    /**
     * ButterKnife impl to catch EditText text on change.
     *
     * @param editable Text being entered.
     */
    @OnTextChanged(value = {
            R.id.editTextSender,
            R.id.editTextReceiver,
            R.id.editTextContent,
            R.id.editTextNote},
            callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void onNewPackageTextFormChange(Editable editable) {

        int intClearTextButtonId;
        boolean isEditTextOnFocus;

        // Compare editable with "==" instead of "equals" to compare reference.
        if (editable == editTextSender.getEditableText()) {

            // Sender Text change
            intClearTextButtonId = R.id.buttonClearTextSender;

            // In tutorial mode match typed string with regex
            setupSenderTutorial(editable);

            // Get info whether edit text is in focus
            isEditTextOnFocus = editTextSender.isFocused();

        } else if (editable == editTextReceiver.getEditableText()) {

            // Receiver Text change
            intClearTextButtonId = R.id.buttonClearTextReceiver;

            // In tutorial mode match typed string with regex
            setupReceiverTutorial(editable);

            // Get info whether edit text is in focus
            isEditTextOnFocus = editTextReceiver.isFocused();

        } else if (editable == editTextContent.getEditableText()) {

            // Content Text change
            intClearTextButtonId = R.id.buttonClearTextContent;

            Utils.Log(TAG, "Content is on change: " + editable);

            isEditTextOnFocus = editTextContent.isFocused();

        } else if (editable == editTextInsurance.getEditableText()) {

            // Insurance Text change
            intClearTextButtonId = R.id.buttonClearInsuranceText;

            Utils.Log(TAG, "Insurance is on change: " + editable);

            isEditTextOnFocus = editTextInsurance.isFocused();

        } else {

            // Note
            Utils.Log(TAG, "Other (Note) is on change: " + editable);
            intClearTextButtonId = R.id.buttonClearTextNote;

            isEditTextOnFocus = editTextNote.isFocused();
        }

        // Show clear text button for EditTexts
        if (isEditTextOnFocus) {
            toggleClearTextButton(intClearTextButtonId, editable.length());
        }
    }

    private void scrollToSection(final ViewGroup parentForm) {
        Utils.Logs('i', TAG, "Scroll view: " + parentView.getMaxScrollAmount());

        parentView.postDelayed(new Runnable() {
            @Override
            public void run() {
                parentView.smoothScrollTo(0, (int) parentForm.getY());
            }
        }, 500);
    }

    /**
     * Tutorial while focus on Sender. While Paket REGEX not matched other
     * part still invisible.
     *
     * @param editable Typed text on EditText (editable)
     */
    private void setupSenderTutorial(Editable editable) {
        if (isTutorialMode()) {
            Pattern pattern = Pattern.compile(REGEX_SENDER_RECEIVER_FORMAT);
            if (pattern.matcher(editable.toString()).find() && !isMatched) {

                Utils.Logs('i', TAG, "YEY! Sender pattern matched! next to Penerima");

                // To make sure only once to proceed this
                isMatched = true;

                // Add Receiver part to enabled list when Paket pattern matched.
                // In order to make Receiver part visible.
                learntView.add(parentReceiver);
                toggleViewsEnabled(parentNewPackageForm, learntView, true);

                // Set placeholder to EditText Receiver in order
                // to catch user attention.
                editTextReceiver.setHint(this.parent.getString(R.string.hint_enter_receiver_data));
            }
        } else {
            Utils.Log(TAG, "Not in tut mode, Sender.");
        }
    }

    /**
     * Tutorial after Receiver created.
     *
     * @param editable Typed text on EditText (editable)
     */
    private void setupReceiverTutorial(Editable editable) {

        if (isTutorialMode()) {

            Pattern pattern = Pattern.compile(REGEX_SENDER_RECEIVER_FORMAT);

            if (pattern.matcher(editable.toString()).find() && !isMatched) {
                Utils.Logs('i', TAG, "YEY! Receiver pattern matched! next is fill Optional fields.");

                isMatched = true;
                learntView.add(parentService);
                learntView.add(parentContent);
                learntView.add(parentInsurance);
                learntView.add(parentNote);
                toggleViewsEnabled(parentNewPackageForm, learntView, true);

                /* Success Info show message (success message when user follow the tutor).
                  Include to hide Skip button feature, and register click event to the success
                  message.
                 */
                showSuccessInfo(true);

            }
        } else {
            Utils.Log(TAG, "Not in tut mode, Receiver.");
        }
    }

    private void setupImageButtonsVisibility(@IdRes int intRes, boolean isVisible) {

        float floatTranslation = (isVisible? 1F : 0F);

        final int buttonsSize = this.mImageButtonHashMap.size();

        if (intRes == -1) {

            // Hide all ImageButton
            for (int i = 0; i < buttonsSize; i++) {

                // Hide Image Button when not Info button
                String strTag = String.valueOf(this.mImageButtonHashMap.valueAt(i).getTag());
                if (!mTagFieldInfo.equals(strTag)) {
                    this.mImageButtonHashMap.valueAt(i)
                            .animate()
                            .setDuration(0)
                            .scaleX(floatTranslation)
                            .scaleY(floatTranslation);
                }
            }

        } else {

            // Show specific ImageButton
            try {

                this.mImageButtonHashMap.get(intRes).animate()
                        .scaleX(floatTranslation)
                        .scaleY(floatTranslation);

            } catch (NullPointerException e){

                MyLog.FabricLog(Log.ERROR, TAG + " - Image button with id: " + intRes + ", NOT FOUND", e);
            }
        }
    }

    private void populateViewsToArray(ViewGroup viewGroup) {

        int parentLv1ChildCount = viewGroup.getChildCount();

        for (int i = 0; i < parentLv1ChildCount; i++) {

            final View child = viewGroup.getChildAt(i);

            // Repeat process if view is parent of form (ViewGroup)
            if (child instanceof ViewGroup) {
                populateViewsToArray((ViewGroup) child);
            }

            // Save ImageButton to array.
            if (child instanceof ImageButton) {
                this.mImageButtonHashMap.put(child.getId(), (ImageButton) child);
            }

            // Save EditText to array.
            if (child instanceof EditText) {
                this.mEditTextHashMap.put(child.getId(), (EditText) child);
            }

            // Save AutoCompleteTextView to array.
            if (child instanceof AutoCompleteTextView) {
                this.mEditTextHashMap.put(child.getId(), (AutoCompleteTextView) child);
            }
        }
    }

    private void initView() {

        setHasOptionsMenu(true);

        /* Input manager in order to force soft keyboard appear */
        inputMethodManager = (InputMethodManager) this.parent.getSystemService(Context.INPUT_METHOD_SERVICE);

        // Set formatted text for Sender/Receiver and required label text helper
        // noinspection deprecation
        textViewSenderHelperText.setText(Html.fromHtml(this.parent.getString(R.string.lbl_booking_format)));
        // noinspection deprecation
        textViewReceiverHelperText.setText(Html.fromHtml(this.parent.getString(R.string.lbl_booking_format)));

        // Listener to detect whether EditText is focus or no.
        editTextSender.setOnFocusChangeListener(this);
        editTextReceiver.setOnFocusChangeListener(this);
        editTextContent.setOnFocusChangeListener(this);
        editTextInsurance.setOnFocusChangeListener(this);
        editTextNote.setOnFocusChangeListener(this);

        // [V49]
        // !!! - Button click event register on #toggleMultiImageButtonsVisibility

        // Button to skip tutorial click event registered on #onNewPackageViewClick

        // Set success message image filter
        imageViewInfoIcSend.setColorFilter(BitmapUtil.getDrawableFilter(this.parent, R.color.black_90));
    }

    private void setupAutoComplete() {

        adSuggestion = new AddressBookAutocompleteAdapter(this.parent, listAllContact);

        editTextSender.setThreshold(1);
        editTextSender.setAdapter(adSuggestion);

        editTextReceiver.setThreshold(1);
        editTextReceiver.setAdapter(adSuggestion);

        editTextSender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PojoAddressBook selectedItem = adSuggestion.getItem(i);
                if (selectedItem != null) {

                    mSb.delete(0, mSb.length());
                    mSb.append(selectedItem.getName()).append(" ")
                            .append(selectedItem.getPhoneNumber()).append(" ")
                            .append(selectedItem.getAddress());
                    editTextSender.setText(mSb);
                    editTextSender.setSelection(editTextSender.getText().length());

                }
            }
        });

        editTextReceiver.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PojoAddressBook selectedItem = adSuggestion.getItem(i);
                if (selectedItem != null) {

                    mSb.delete(0, mSb.length());
                    mSb.append(selectedItem.getName()).append(" ")
                            .append(selectedItem.getPhoneNumber()).append(" ")
                            .append(selectedItem.getAddress());

                    editTextReceiver.setText(mSb);
                    editTextReceiver.setSelection(editTextReceiver.getText().length());
                }
            }
        });
    }

    /**
     * Append * Optional to optional field such as:
     * - Service
     * - Content
     * - Insurance
     * - Note
     */
    private void setupLabel() {

        // Setup label with * Optional (term)
        textViewLableContent.setText(makeOptionalLabel(String.valueOf(textViewLableContent.getText())));
        textViewLableInsurance.setText(makeOptionalLabel(String.valueOf(textViewLableInsurance.getText())));
        textViewLableNote.setText(makeOptionalLabel(String.valueOf(textViewLableNote.getText())));

    }

    private Spanned makeOptionalLabel(String strLabel) {
        mSb.delete(0, mSb.length());
        //noinspection deprecation
        return Html.fromHtml(mSb.append(strLabel).append(" ").append(parent.getString(R.string.lbl_optional)).toString());
    }

    /**
     * Toolbar item from toolbar_default.xml
     * Toolbar object from HOST (AcNewPaket.java)
     */
    private void initToolBar() {
        AcNewPaket acNewPaketHost = (AcNewPaket) getActivity();
        mToolbar = acNewPaketHost.getToolBar();
        toolbarImageButtonNewPackage = (ImageView) mToolbar.findViewById(R.id.toolbarImageButtonNewPackage);
        toolbarImageButtonNewPackage.setVisibility(View.VISIBLE);

        // Special case for toolbar buttons; can't init click event on ButterKnife
        toolbarImageButtonNewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.bookNewPackage();
            }
        });

    }

    /**
     * Init TextWatcher listener for Sender and Receiver edit text.
     */
    private void initTextWatcherListener() {

        // Text formatting while typing amount (format to Indo decimal)
        editTextInsurance.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals(mCurrentTypedStr) && s.length() > 0) {
                    editTextInsurance.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("\\.", "");
                    double parsed = Utils.getParsedDouble(cleanString);
                    String formatted = mMoneyFormatIndo.format(parsed);

                    mCurrentTypedStr = formatted;
                    editTextInsurance.setText(formatted);
                    editTextInsurance.setSelection(formatted.length());

                    editTextInsurance.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Show clear text button for Receiver
                toggleClearTextButton(R.id.buttonClearInsuranceText, editable.length());
                showInsuranceValueRange(String.valueOf(editable));
            }
        });
    }

    private void showInsuranceValueRange(String insuranceValue) {

        String cleanString = insuranceValue.replaceAll("\\.", "");
        int parsed = Utils.getParsedInt(cleanString);

        double startInsuranceValue = Math.abs(0.0015 * parsed);
        double maximumInsuranceValue = Math.abs(0.0025 * parsed);

        if (parsed >= 10000
                && startInsuranceValue > 0
                && maximumInsuranceValue > 0) {
            //noinspection deprecation
            textViewInsuranceDescription.setText(Html.fromHtml(parent.getString(
                    R.string.lbl_insurance_value_range,
                    makeCharSequence(mMoneyFormatIndo.format(startInsuranceValue)),
                    makeCharSequence(mMoneyFormatIndo.format(maximumInsuranceValue)))
            ));
        } else {
            textViewInsuranceDescription.setText(R.string.msg_info_insurance);
        }

    }

    private CharSequence makeCharSequence(String strBalance) {

        if (parent == null || parent.isFinishing()) return null;

        String prefix = parent.getString(R.string.lbl_idr);
        String sequence = prefix + " " + strBalance;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(Typeface.NORMAL), 0, prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), prefix.length(), sequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    /**
     * Init listener for MaterialView Tutorial
     */
    private void initMaterialTutorialListener() {
//        materialIntroListener = new MaterialIntroListener() {
//            @Override
//            public void onUserClicked(String materialIntroViewId) {
//                Utils.Log(TAG, "Material clicked: " + materialIntroViewId);
//                switch (materialIntroViewId) {
//                    case AppClass.SHARED_LEARNT_SENDER_FIELD:
//                        Utils.Log(TAG, materialIntroViewId + " field is begin to learnt.");
//                        inputMethodManager.toggleSoftInputFromWindow(editTextSender.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//
//                        if (!editTextSender.getText().toString().equals("")) {
//                            editTextSender.setSelection(editTextSender.getText().length());
//                            editTextSender.requestFocusFromTouch();
//                            editTextSender.requestFocus();
//                        }
//
//                        break;
//                    case AppClass.SHARED_LEARNT_RECEIVER_FIELD:
//                        Utils.Log(TAG, materialIntroViewId + " field is begin to learnt.");
//                        inputMethodManager.toggleSoftInputFromWindow(editTextReceiver.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//                        editTextReceiver.requestFocusFromTouch();
//
//                        // Set isMatch to false to restore default state
//                        isMatched = false;
//
//                        break;
//                    case AppClass.SHARED_LEARNT_PROCEDD_CREATE:
//                        Utils.Log(TAG, "Toolbar button proceed is begin to learnt");
//
//                        /* User already learn how to create new package then stop tutorial.*/
//                        spHelper.setPreferences(AppClass.SHARED_LEARNT_PROCEDD_CREATE, false);
//
//                        break;
//                }
//            }
//        };
    }

    /**
     * Show messsage about user success following tutorial.
     *
     * @param isVisible : Boolean show or not
     */
    private void showSuccessInfo(boolean isVisible) {
        if (isVisible) {
            parentInfo.setVisibility(View.VISIBLE);

            /* When success it's time to enable "New Package" button. */
            toolbarImageButtonNewPackage.setEnabled(true);
            toolbarImageButtonNewPackage.setAlpha(1f);

            /* Hide skip tutorial button when user successfully follow the tutorial*/
            parentBtnSkipTutorial.setVisibility(View.GONE);
        } else {
            parentInfo.setVisibility(View.GONE);
        }
    }

    /**
     * Show in tutorial MODE
     */
    private void showTutorialMode() {
//        Utils.Log(TAG, "Begin show tutorial New Package --> " + isTutorialMode());
//        // Show tutorial when in TUTORIAL MODE
//        if (isTutorialMode()) {
//
//            Utils.devModeToast(this.parent, "TUTORIAL MODE ON");
//
//            // Check whether how create New Package is learnt
//            if (spHelper.getSharedPreferences(Constants.SHARED_LEARNT_PROCEDD_CREATE, false)) {
//
//                inputMethodManager.toggleSoftInputFromWindow(editTextSender.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//
//                Utils.Log(TAG, "User dont't know how to create New Package");
//
//                mToolbar.setSubtitle(R.string.subtitle_tut_mode);
//
//                // Disable compose toolbar button
//                toolbarImageButtonNewPackage.setEnabled(false);
//                toolbarImageButtonNewPackage.setAlpha(0.1f);
//
//                // Display skip button when in tutorial mode
//                parentBtnSkipTutorial.setVisibility(View.VISIBLE);
//
//                // Delete Sender text when in tutorial mode
//                editTextSender.setText(null);
//                // editTextSender.setFocusableInTouchMode(false);
//                // editTextSender.clearFocus();
//
//                // Set visible views for the first time in Tutorial Mode
//                learntView.add(parentSender);  // Parent for Sender field
//                learntView.add(parentBtnSkipTutorial);  // Parent to skip tutorial
//
//                /* Show tutorial and highlight Sender */
//                showMaterialIntro(
//                        parentNewPackageForm,
//                        learntView,
//                        AppClass.SHARED_LEARNT_SENDER_FIELD,
//                        this.parent.getString(R.string.tutorial_field_sender),
//                        getActivity(),
//                        BuildConfig.DEV_MODE,  // Dot animation when on DEV_MODE
//                        materialIntroListener);
//            } else {
//                /* User are able to create a New Package */
//                Utils.Log(TAG, "User already know how to create New Package");
//                toggleAllViewsEnabled(parentNewPackageForm, true);
//                showSuccessInfo(false);
//            }
//
//        }
    }

//    private void showMaterialIntro(
//            ViewGroup parentContainer,
//            List<View> listTargetView,
//            String strTutorialId,
//            String strTutorialText,
//            Activity activity,
//            boolean dotAnimation,
//            MaterialIntroListener materialIntroListener) {
//
//        // Make sure soft keyboard is hidden
//        Utils.hideKeyboardFrom(this.parent, editTextSender);
//
//        for (View v: listTargetView) {
//            Utils.Log(TAG, "Target: " + v);
//        }
//
//        // Focus on TextInputLayout->EditText Sender
//        toggleViewsEnabled(parentContainer, listTargetView, true);
//
//        if (!(new PreferencesManager(this.parent).isDisplayed(strTutorialId))) {
//            Utils.showMatrialTutorial(
//                    listTargetView.get(0),
//                    strTutorialId,
//                    strTutorialText,
//                    activity,
//                    dotAnimation,
//                    false,
//                    materialIntroListener);
//        } else {
//            Utils.devModeToast(this.parent, "Tutorial already displayed.");
//        }
//    }

    /**
     * Turn ALL widget on or off.
     *
     * @param viewGroup : Parent ViewGroup contains all the View (widget)
     * @param isEnable  : On (true) or Off (false)
     */
    private void toggleAllViewsEnabled(ViewGroup viewGroup, boolean isEnable) {
        int childCount = viewGroup.getChildCount();
        float alpha = (isEnable? 1F : 0F);
        for (int i = 0; i < childCount; i++) {

            viewGroup.getChildAt(i).setAlpha(alpha);

        }
    }

    /**
     * Turn CERTAIN widget ON or OFF.
     *
     * @param viewGroup      : Parent widget contains the widget.
     * @param listViewTarget : Widget collection to turn ON/OFF.
     * @param isEnable       : ON (true) or OFF (false).
     * @since (Sep 21, 2016) Version 40 - Get EditText inside TextInputLayout using getEditText()
     */
    private void toggleViewsEnabled(ViewGroup viewGroup, List<View> listViewTarget, boolean isEnable) {

        Utils.Log(TAG, "List target view size: " + listViewTarget.size() + ", visible? " + isEnable);
        float alpha = (isEnable? 1F : 0F);
        int childCount = viewGroup.getChildCount();

        for (int i = 0; i < childCount; i++) {

            View v = viewGroup.getChildAt(i);

            for (View viewTarget : listViewTarget) {

                if (v.getId() == viewTarget.getId()) {
                    Utils.Log(TAG, "View target matched: " + v);
                    viewTarget.setAlpha(alpha);
                    // viewTarget.setVisibility(isEnable? View.VISIBLE : View.INVISIBLE);
                    break;
                } else if(v instanceof ViewGroup && v.getId() != viewTarget.getId()) {

                    Utils.Log(TAG, "Set invisible other view group: " + v);
                    // v.setVisibility(View.INVISIBLE);
                    v.setAlpha(0F);

                }

            }
        }
    }

    /**
     * Set PojoBooking first data (From Name, Phone, and Address) to Sender field.
     */
    private void setData() {

        if (isTutorialMode()) return;

        final String name = spHelper.getSharedPreferences(Constants.SHARED_NAME, ""),
                address = spHelper.getSharedPreferences(Constants.SHARED_ADDRESS, ""),
                phone = spHelper.getSharedPreferences(Constants.SHARED_PHONE, "");

        Utils.Log(TAG, "SET Sender data: \"" + String.format("%s %s %s", name, phone, address) + "\"" );

        // [V9] Make sure no focus on Sender / Receiver
        editTextSender.clearFocus();
        editTextReceiver.clearFocus();

        // Set text all fields is not empty
        if (!StringUtils.isEmpty(name)
                && !StringUtils.isEmpty(address)
                && !StringUtils.isEmpty(phone)) {

            mSb.delete(0, mSb.length());
            mSb.append(name)
                    .append(" ")
                    .append(phone)
                    .append(" ")
                    .append(address);

            editTextSender.setText(mSb);

            editTextReceiver.requestFocusFromTouch();

        } else {
            editTextSender.requestFocusFromTouch();

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utils.Log(TAG, "Data: " + data + ", req code: " + requestCode + ", res code: " + resultCode);

        /*
          Set text for "Receiver" when requirement met e.g. resultCode is 1.
          When data is not null and Extra "address_detail" is available, then
          take the string to put on the textEdit (Receiver).
         */
        if (data != null && data.hasExtra(Constants.EXTRA_ADDRESS_DETAIL)) {
            Utils.Log(TAG, "DATA INTENT AVAILABLE");
            String receiverString = data.getStringExtra(Constants.EXTRA_ADDRESS_DETAIL).split("#")[0];
            Utils.Log(TAG, "ADDRESS STRING: " + receiverString);
            // (2 Agust) Make sure Edit Text widget is initialized
            if (editTextSender != null && editTextReceiver != null) {
                Utils.Log(TAG, "WIDGETS AVAILABLE");
                if (Constants.focusOnSender) {
                    Utils.Log(TAG, "FOCUS ON SENDER");
                    editTextSender.setText(receiverString);
                    editTextSender.setSelection(editTextSender.getText().length());
                } else {
                    Utils.Log(TAG, "FOCUS ON RECEIVER");
                    editTextReceiver.setText(receiverString);
                    editTextReceiver.setSelection(editTextReceiver.getText().length());
                }
            } else {
                Utils.Logs('w', TAG, "Views edit text is not ready.");
                Utils.Logs('w', TAG, "Parent : " + (parentView == null ? " PARENT NULL " : " PARENT AVAILABLE "));
                Utils.Logs('w', TAG, "Parent ACTIVITY : " + (parent == null ? " PARENT ACTIVITY NULL " : " PARENT ACTIVITY AVAILABLE "));
            }
        } else {
            Utils.Logs('e', TAG, "Data --> " + (data == null ? " DATA NULL " : " DATA AVAILABLE "));

            if (data == null) return;
            Utils.Logs('e', TAG, "EXTRA_ADDRESS_DETAIL " + data.getStringExtra(Constants.EXTRA_ADDRESS_DETAIL));
        }
    }


    /**
     * Determine whether refresh Package List. <br/>
     * <p/>
     * When user successfully created a new Package, store its booking_id.
     * The booking_id will be used to get PojoBooking then prepend to list.
     */
    public void showPackageList() {
        Utils.Log(TAG, "Back to package list");

//        if (isTutorialMode()) {
//            Utils.Log(TAG, "Turn off Tutorial mode.");
//            setTutorialMode(false);
//            Utils.switchTutorial(this.parent, false);
//
//            Utils.toast(this.parent, this.parent.getString(R.string.msg_tutorial_is_off));
//        }

        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_REFRESH_PAGE, getNewBookingIdList().size() >= 1);
        intent.putStringArrayListExtra(Constants.EXTRA_ARRAYLIST_BOOKING_ID, getNewBookingIdList());
        this.parent.setResult(Activity.RESULT_OK, intent);
        this.parent.finish();
    }

    private void triggerShowProgress(boolean isShow) {
        if (mFragmentProgressListener == null) return;

        mFragmentProgressListener.showProgress(isShow, FragmentNewPackage2.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // [V49] Unbind butterknife
        mUnbinder.unbind();

        cancelRequest();
    }

    private void cancelRequest() {

        MyLog.FabricLog(Log.WARN, TAG + " - User cancel creating new package.");

        values.cancelPendingRequests(FragmentNewPackage2.class);
    }

    private void resetForm() {
        editTextReceiver.setText("");
        editTextReceiver.requestFocus();
        editTextNote.setText("");
        editTextContent.setText("");

        // Hide Success info (bottom info with party poppers)
        showSuccessInfo(false);

        // Reset trial count
        intBtnHelpTrial = 0;
        intBtnAddressBookTrial = 0;
        intBtnSubmitNewPackageTrial = 0;
    }

    /**
     * Decide to show or hide "x" button to clear editText string.
     * 
     * @param intButtonRes Clear button id to control
     * @param editableLength Length of editable on EditText
     */
    private void toggleClearTextButton(@IdRes int intButtonRes, int editableLength) {
        // Show clear text button for Receiver
        if (editableLength > 0) {
            setupImageButtonsVisibility(intButtonRes, true);
        } else {
            setupImageButtonsVisibility(intButtonRes, false);
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
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.Logs('d', TAG, "Close dialog: " + mStringLastTag);
        hideDialog(mStringLastTag);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        try {

            if (hasFocus) {
                
                // Focused EditText
                EditText focussedEditText = (EditText) v;
                int textLength = focussedEditText.getEditableText().length();
                
                switch (v.getId()) {
                    case R.id.editTextSender:
                        Utils.Logs('i', TAG, "Fokus di Sender");

                        // Toggle image button surroud this view except ImageButton for clear text.
                        toggleMultiImageButtonsVisibility(parentSender, true);
                        textViewSenderHelperText.setVisibility(View.VISIBLE);
                        toggleClearTextButton(R.id.buttonClearTextSender, textLength);

                        Constants.focusOnSender = Boolean.TRUE;

                        break;
                    case R.id.editTextReceiver:
                        Utils.Logs('i', TAG, "Fokus di Receiver");

                        // Toggle image button surroud this view
                        toggleMultiImageButtonsVisibility(parentReceiver, true);
                        textViewReceiverHelperText.setVisibility(View.VISIBLE);
                        toggleClearTextButton(R.id.buttonClearTextReceiver, textLength);

                        Constants.focusOnSender = Boolean.FALSE;

                        /* On tutorial mode, isMatched means regex is matched with one input string. */
                        if (isTutorialMode && isMatched) {
                            isMatched = false;
                        }

                        break;
                    case R.id.editTextContent:
                        Utils.Logs('i', TAG, "Fokus di editTextContent");
                        toggleMultiImageButtonsVisibility(parentContent, true);

                        toggleClearTextButton(R.id.buttonClearTextContent, textLength);

                        break;
                    case R.id.editTextInsurance:
                        Utils.Logs('i', TAG, "Fokus di editTextInsurance");
                        toggleMultiImageButtonsVisibility(parentInsurance, true);

                        toggleClearTextButton(R.id.buttonClearInsuranceText, textLength);

                        scrollToSection(parentInsurance);

                        // Show Insurance helper
                        textViewInsuranceDescription.setVisibility(View.VISIBLE);

                        break;
                    case R.id.editTextNote:
                        Utils.Logs('i', TAG, "Fokus di editTextNote");
                        toggleMultiImageButtonsVisibility(parentNote, true);

                        toggleClearTextButton(R.id.buttonClearTextNote, textLength);

                        break;
                }

            } else {

                switch (v.getId()) {
                    case R.id.editTextSender:
                        Utils.Logs('w', TAG, "NOT Fokus di Sender");

                        // Toggle image button surroud this view
                        toggleMultiImageButtonsVisibility(parentSender, false);
                        textViewSenderHelperText.setVisibility(View.INVISIBLE);

                        break;
                    case R.id.editTextReceiver:
                        Utils.Logs('w', TAG, "NOT Fokus di Receiver");

                        // Toggle image button surroud this view
                        toggleMultiImageButtonsVisibility(parentReceiver, false);
                        textViewReceiverHelperText.setVisibility(View.INVISIBLE);

                        break;
                    case R.id.editTextContent:
                        Utils.Logs('w', TAG, "NOT Fokus di editTextContent");
                        toggleMultiImageButtonsVisibility(parentContent, false);
                        break;
                    case R.id.editTextInsurance:
                        Utils.Logs('w', TAG, "NOT Fokus di editTextInsurance");
                        toggleMultiImageButtonsVisibility(parentInsurance, false);

                        // Hide Insurance helper
                        textViewInsuranceDescription.setVisibility(View.INVISIBLE);

                        break;
                    case R.id.editTextNote:
                        Utils.Logs('w', TAG, "NOT Fokus di editTextNote");
                        toggleMultiImageButtonsVisibility(parentNote, false);
                        break;
                }
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            MyLog.FabricLog(Log.ERROR, "NullPointerException on NewPackage form focus.", npe);
        } catch (ClassCastException e) {
            MyLog.FabricLog(Log.ERROR, "Class EditText casting failure on NewPackage form focus.", e);
        }
    }

    private void toggleMultiImageButtonsVisibility(ViewGroup viewGroup, final boolean isVisible) {

        long duration = (isVisible? 250L : 100L);
        float scale = (isVisible? 1F : 0F);

        final int parentChildCount = viewGroup.getChildCount();

        for (int i = 0; i < parentChildCount; i++) {
            View child = viewGroup.getChildAt(i);

            // Repeat process if view is parent of form (ViewGroup)
            if (child instanceof ViewGroup) {
                toggleMultiImageButtonsVisibility((ViewGroup) child, isVisible);
            }

            // ImageButtons
            if (child instanceof ImageButton) {

                final ImageButton imageButton = (ImageButton) child;

                // Detect whether ImageButton is EditText-clearer.
                // then decide show/hide it's view.
                String imageButtonTag = String.valueOf(imageButton.getTag());

                boolean isClearTextImageButton = imageButtonTag.equals(this.mTagClearTextButton);
                boolean isInfoImageButton = imageButtonTag.equals(this.mTagFieldInfo);

                if (isClearTextImageButton) {
                    scale = 0F;
                }

                // Proceed view scaling when view is not info button
                if (!isInfoImageButton) {
                    imageButton
                            .animate()
                            .setDuration(duration)
                            .scaleX(scale)
                            .scaleY(scale).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            imageButton.setVisibility(isVisible? View.VISIBLE : View.GONE);
                        }
                    });
                }
                // endif - Image scaling
            }
        }
    }

    @OnClick({
            R.id.parentSender,
            R.id.parentReceiver,
            R.id.parentContent,
            R.id.parentInsurance,
            R.id.parentNote,
            R.id.imageButtonAttachToSender,
            R.id.imageButtonAttachToReceiver,
            R.id.buttonClearTextSender,
            R.id.buttonClearTextReceiver,
            R.id.buttonClearTextContent,
            R.id.buttonClearInsuranceText,
            R.id.buttonClearTextNote,
            R.id.parentInfo,
            R.id.btnSkipTutorial})
    public void onNewPackageViewClick(View view) {
        switch (view.getId()) {

            // Click for Info button
//            case R.id.imageButtonInfoSender:
//            case R.id.imageButtonInfoReceiver:
//                openHelpWritingFormat();
//                break;
//            case R.id.imageButtonInfoService:
//                showInfoScreen(parent.getString(R.string.msg_info_service));
//                break;
//            case R.id.imageButtonInfoContent:
//                showInfoScreen(parent.getString(R.string.msg_info_content));
//                break;
//            case R.id.imageButtonInfoInsurance:
//                showInfoScreen(parent.getString(R.string.msg_info_insurance));
//                break;
//            case R.id.imageButtonInfoNote:
//                showInfoScreen(parent.getString(R.string.msg_info_note));
//                break;

            // Click button for attach address book
            case R.id.imageButtonAttachToSender:
            case R.id.imageButtonAttachToReceiver:
                showAddressBook();
                break;

            // Click listener for clear button
            // Listener registered from #toggleMultiImageButtonsVisibility
            case R.id.buttonClearTextSender:
                clearEditText(editTextSender, buttonClearTextSender);
                break;
            case R.id.buttonClearTextReceiver:
                clearEditText(editTextReceiver, buttonClearTextReceiver);
                break;
            case R.id.buttonClearTextContent:
                clearEditText(editTextContent, buttonClearTextContent);
                break;
            case R.id.buttonClearInsuranceText:
                clearEditText(editTextInsurance, buttonClearInsuranceText);
                break;
            case R.id.buttonClearTextNote:
                clearEditText(editTextNote, buttonClearTextNote);
                break;

            case R.id.parentInfo:
                view.setEnabled(false);
                /* For data collecting purpose, user click on the text. */
                Utils.Log(TAG, "Click on text!!");
                break;
            case R.id.btnSkipTutorial:
                skipAllTutorials();
                break;

            // Click on edit text parent to scroll up
            case R.id.parentSender:

                editTextSender.requestFocusFromTouch();

                inputMethodManager.showSoftInputFromInputMethod(
                        editTextSender.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED);

                break;
            case R.id.parentReceiver:

                editTextReceiver.requestFocusFromTouch();

                inputMethodManager.showSoftInputFromInputMethod(
                        editTextReceiver.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED);

                break;
            case R.id.parentContent:

                editTextContent.requestFocusFromTouch();

                inputMethodManager.showSoftInputFromInputMethod(
                        editTextContent.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED);

                break;
            case R.id.parentInsurance:

                editTextInsurance.requestFocusFromTouch();

                inputMethodManager.showSoftInputFromInputMethod(
                        editTextInsurance.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED);

                // Scroll to top on insurance
                scrollToSection(parentInsurance);

                break;
            case R.id.parentNote:

                editTextNote.requestFocusFromTouch();

                inputMethodManager.showSoftInputFromInputMethod(
                        editTextNote.getApplicationWindowToken(),
                        InputMethodManager.SHOW_FORCED);

                break;

        }
    }

    private void clearEditText(EditText editText, ImageButton imageButton) {
        if (editText != null && imageButton != null) {

            Utils.Logs('w', TAG, "Clear edittext: " + editText);

            editText.setText(null);
            imageButton.animate().scaleX(0).scaleY(0);
        }
    }

    /**
     * Skip all tutorial attributes and restore all view state to normal.
     */
    private void skipAllTutorials() {

        // Set TUTORIAL MODE OFF
        SPHelper.getInstance().setPreferences(Constants.SHARED_TUTORIAL_MODE, false);
        setTutorialMode(false);

        // Turn on all views
        toggleAllViewsEnabled(parentNewPackageForm, true);

        // Restore title
        mToolbar.setTitle(this.parent.getString(R.string.action_new_package));
        mToolbar.setSubtitle(null);

        // Set EditText hint to default value
        editTextReceiver.setHint(this.parent.getString(R.string.hint_receiver));

        toolbarImageButtonNewPackage.setEnabled(true);
        toolbarImageButtonNewPackage.setAlpha(1f);

        parentBtnSkipTutorial.animate().translationY(parentBtnSkipTutorial.getHeight()).alpha(0);

        // Make sure success info is hidden
        showSuccessInfo(false);

    }

    /**
     * Open writing format dialog. Dialog content is used html format text.
     */
    @SuppressWarnings("unused")
    private void openHelpWritingFormat() {

        // Track user click
//        MyLog.FabricTrackNewPackage(
//                editTextSender.getText().toString(),
//                editTextReceiver.getText().toString(),
//                "WritingFormat",
//                intBtnHelpTrial++);

        ControllerDialog.getInstance().showDialogInfo(
                this.parent.getString(R.string.msg_helper_title),
                this.parent.getString(R.string.msg_helper_text),
                getActivity());
    }

    public ArrayList<String> getNewBookingIdList() {
        return newBookingIdList;
    }

    public boolean isTutorialMode() {
        return isTutorialMode;
    }

    public void setTutorialMode(boolean tutorialMode) {
        isTutorialMode = tutorialMode;
    }

    /**
     * Make sure previous "dialog" fragment is hidden.
     * @return : New {@link FragmentTransaction}
     */
    private FragmentTransaction hideDialog(String strTag) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(strTag);

        if (prev != null) {

            Utils.Log(TAG, "Dialog fragment found");
            try {

                if (prev instanceof DialogFragment) {

                    ((DialogFragment) prev).dismiss();
                    ft.remove(prev);
                }

            } catch (IllegalStateException e) {
                MyLog.FabricLog(Log.ERROR, TAG + " - Error to hide fragment dialog.", e);
            }

        }

        return ft;
    }

    /**
     * Callbacks from Fragment Package summary.
     */
    @Override
    public void onClickAtView(View v) {

        switch (v.getId()) {
            case R.id.buttonEstimateTariff:
                Utils.Logs('i', TAG, "Load estimate tariff on Host fragment");
                break;
            case R.id.relativeLayoutBtnBack:
                Utils.Logs('i', TAG, "Back to package list");
                showPackageList();
                hideDialog(mStringLastTag);
                break;
            case R.id.relativeLayoutBtnNewPackage:
                Utils.Logs('i', TAG, "Reset form");
                resetForm();
                hideDialog(mStringLastTag);
                break;
        }
    }

    //////
    // S: New Package View
    @Override
    public ScrollView getParentView() {
        return parentView;
    }

    @Override
    public void setParentView(ScrollView parentView) {
        this.parentView = parentView;
    }

    @Override
    public boolean isFormReady() {

        // Validate Sender
        if (StringUtils.isEmpty(String.valueOf(editTextSender.getText()))) {
            Utils.snackBar(parentView, this.parent.getString(R.string.nama_pengirim_harus_di_isi), Log.ERROR);

            editTextSender.requestFocusFromTouch();

        } else if (StringUtils.isEmpty(String.valueOf(editTextReceiver.getText()))) {
            Utils.snackBar(parentView, this.parent.getString(R.string.nama_penerima_harus_di_isi), Log.ERROR);

            editTextReceiver.requestFocusFromTouch();

        } else if (!StringUtils.isEmpty(String.valueOf(editTextSender.getText()))
                && !StringUtils.isEmpty(String.valueOf(editTextReceiver.getText()))){
            return true;
        }

        // In case no service selected.
        if (StringUtils.isEmpty(mStrSelectedService)) {
            Utils.snackBar(parentView, this.parent.getString(R.string.msg_shipping_service_not_valid), Log.ERROR);
            return false;
        }

        return false;
    }

    @Override
    public PojoNewPackage getFormData() {
        PojoNewPackage newPackage = new PojoNewPackage();

        String strInsuranceText = String.valueOf(editTextInsurance.getText());
        String strInsuranceAmountWithoutFormatting = strInsuranceText.replaceAll("\\.", "");

        newPackage.setFromFull(String.valueOf(editTextSender.getText()));
        newPackage.setToFull(String.valueOf(editTextReceiver.getText()));
        newPackage.setService(mStrSelectedService);
        newPackage.setContent(String.valueOf(editTextContent.getText()));
        newPackage.setInsuredValue(strInsuranceAmountWithoutFormatting);
        newPackage.setNote(String.valueOf(editTextNote.getText()));

        return newPackage;
    }

    @Override
    public void showViewState(UIState uiState) {
        Utils.Log(TAG, "UI State : " + uiState);
        switch (uiState) {
            case LOADING:
                triggerShowProgress(true);
                break;
            case FINISHED:
                triggerShowProgress(false);
                break;
            case ERROR:
                triggerShowProgress(false);

                MyLog.FabricLog(Log.ERROR, "Error creating new package at submit trial: " + intBtnSubmitNewPackageTrial);
                break;
        }
    }

    @Override
    public void populateAutoComplete(ArrayList<PojoAddressBook> pojoBookingArrayList) {

        if (!listAllContact.isEmpty()) {
            listAllContact.clear();
        }

        listAllContact.addAll(pojoBookingArrayList);
        adSuggestion.notifyDataSetChanged();

        Utils.Log(TAG, "List suggestion size: " + listAllContact.size());
        Utils.Log(TAG, "Adapter item count: " + adSuggestion.getCount());
    }

    @Override
    public void initPackageServiceList(final ArrayList<Object> packageServices) {

        mPackageServiceList = new ArrayList<>(packageServices);

        PackageServicesSpinnerAdapter spinnerAdapter = new PackageServicesSpinnerAdapter(parent, packageServices);
        spinnerPackageServices.setAdapter(spinnerAdapter);
        spinnerPackageServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mStrSelectedService = ((GeneralListItem) packageServices.get(position)).getTitle();
                Utils.Log(TAG, "Clicked pos: " + position + ", Title: " + mStrSelectedService);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.Log(TAG, "Nothing selected from Spinner");
            }
        });

        spinnerPackageServices.setSelection(0);
        mStrSelectedService = ((GeneralListItem) spinnerPackageServices.getItemAtPosition(0)).getTitle();
        Utils.Logs('i', TAG, "Default service: " + mStrSelectedService);

//        mPackageServiceAdapter = new PackageServiceAdapter(this.parent, this.mPackageServiceList, new OnClickRecyclerItem() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Utils.Logs('i', TAG, "Click on service item at pos: " + position);
//
//                Object item = mPackageServiceList.get(position);
//
//                if (item != null && item instanceof GeneralListItem) {
//
//                    GeneralListItem selectedItem = (GeneralListItem) item;
//
//                    Utils.Log(TAG, "Recent text clicked: " + selectedItem.getTitle());
//
//                    // Deselect last item
//                    ((GeneralListItem) mPackageServiceList.get(mLastServiceSelectionIndex)).setSelected(false);
//                    mPackageServiceAdapter.notifyItemChanged(mLastServiceSelectionIndex);
//
//                    // Select this selection
//                    ((GeneralListItem) mPackageServiceList.get(position)).setSelected(!selectedItem.isSelected());
//                    mPackageServiceAdapter.notifyItemChanged(position);
//
//                    // Save last selection position
//                    mLastServiceSelectionIndex = position;
//
//                    // Show service description
//                    showServiceDescription(position);
//                }
//            }
//        });
//
//        LinearLayoutManager horizontalLayoutManagaer
//                = new LinearLayoutManager(this.parent, LinearLayoutManager.HORIZONTAL, false);
//        recyclerViewServices.setLayoutManager(horizontalLayoutManagaer);
//
//        recyclerViewServices.setAdapter(mPackageServiceAdapter);
//
//        // Select REG ad default choice
//        // Show service description
//        showServiceDescription(0);

    }

    @Override
    public void saveLastSender(PojoNewPackage pojoNewPackage) {
        // Save Name, Address, and Phone to Shared Preferences
        spHelper.setPreferences(Constants.SHARED_NAME, pojoNewPackage.getFromName());
        spHelper.setPreferences(Constants.SHARED_ADDRESS, pojoNewPackage.getFromAddress());
        spHelper.setPreferences(Constants.SHARED_PHONE, pojoNewPackage.getFromPhone());

        // Add booking_id string to list.
        // in order to trigger list refresh on Package List page.
        newBookingIdList.add(pojoNewPackage.getBookingId());

    }

    @Override
    public void showAddressBook() {
        Utils.Log(TAG, "Open Address Book");

        // Track user click
//        MyLog.FabricTrackNewPackage(
//                editTextSender.getText().toString(),
//                editTextReceiver.getText().toString(),
//                "AddressBook",
//                intBtnAddressBookTrial++);

        Intent addressBookIntent = new Intent(parent, AcAddressBook.class);
        startActivityForResult(addressBookIntent, 1);

    }

    @Override
    public void showNewPackageSummaryScreen(PojoNewPackage pojoNewPackage) {

        final String dialogFragmentTag = "dialog-new-package-summary";
        mStringLastTag = dialogFragmentTag;

        FragmentTransaction ft = hideDialog(dialogFragmentTag);
        ft.addToBackStack(null);

        FragmentDialogNewPackageSummary newDialogFragment =
                FragmentDialogNewPackageSummary.newInstance(pojoNewPackage);
        newDialogFragment.setCallbacks(FragmentNewPackage2.this);

        try {
            newDialogFragment.show(ft, dialogFragmentTag);
        } catch (IllegalStateException e) {
            MyLog.FabricLog(Log.ERROR, TAG + " - Error to show summary dialog.", e);
        }
    }

    @Override
    public void skipAllTutorial() {
        // Turn off New Package tutorial when user successfully created one.
//        Utils.switchTutorial(this.parent, false);
//        skipAllTutorials();
    }

    // E: New Package View
    //////
}
