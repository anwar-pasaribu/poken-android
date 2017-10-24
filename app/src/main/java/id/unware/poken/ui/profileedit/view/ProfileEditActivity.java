package id.unware.poken.ui.profileedit.view;

import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.domain.Customer;
import id.unware.poken.domain.User;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.interfaces.InputDialogListener;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.profileedit.model.ProfileEditModel;
import id.unware.poken.ui.profileedit.presenter.ProfileEditPresenter;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class ProfileEditActivity extends BaseActivity implements LoaderCallbacks<Cursor>, IProfileEditView {

    private final String TAG = "ProfileEditActivity";

    // UI references.
    @BindView(R.id.login_form) ViewGroup mLoginFormView;
    @BindView(R.id.editProfileEtFullName) EditText editProfileEtFullName;
    @BindView(R.id.editProfileEtMobilePhone) EditText editProfileEtMobilePhone;
    @BindView(R.id.email) AutoCompleteTextView mEmailView;
    @BindView(R.id.editProfileEtNewPassword) EditText editProfileEtNewPassword;

    private MenuItem menuItemSaveEdits;

    private ProfileEditPresenter presenter;

    private long currentCustId;
    private Customer currentCustomer;
    private String currentPassword, newPassword;

    private static final int REQUEST_READ_CONTACTS = 0;

    private TextWatcher editProfileFormTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setupSaveReadiness(s);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        setContentView(R.layout.activity_profile_edit);

        if (getIntent() != null) {
            currentCustId = getIntent().getLongExtra(Constants.KEY_DOMAIN_ITEM_ID, -1);
            currentCustomer = getIntent().getParcelableExtra(Constants.EXTRA_PARCELABLE_CUSTOMER);

            Utils.Log(TAG, "Customer parcel: " + currentCustomer.related_user.email);
        }

        ButterKnife.bind(this);

        presenter = new ProfileEditPresenter(new ProfileEditModel(), this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (currentCustId != 0) {
            presenter.loadPokenCustomerInfo(String.valueOf(currentCustId));
        } else {
            String custToken = SPHelper.getInstance().getSharedPreferences(Constants.SP_AUTH_TOKEN, "");
            if (!StringUtils.isEmpty(custToken)) {
                presenter.loadPokenCustomerInfo(custToken);
            } else {
                finish();
            }
        }

        // Add text watcher listener
        editProfileEtFullName.addTextChangedListener(editProfileFormTextWatcher);
        editProfileEtMobilePhone.addTextChangedListener(editProfileFormTextWatcher);
        mEmailView.addTextChangedListener(editProfileFormTextWatcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);

        menuItemSaveEdits = menu.findItem(R.id.action_save_profile_edits);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save_profile_edits) {
            attemptEditProfile();
            return true;
        } else if (id == android.R.id.home) {
            Utils.Log(TAG, "Home navigation cliked.");
            this.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        setupActionBar();

        populateCustomerData(currentCustomer);

        // Set up the login form.
        populateAutoComplete();

    }

    private void populateCustomerData(Customer customer) {
        String strFullName = customer.related_user.getFullName();
        String strPhoneNumber = customer.phone_number;
        String strEmail = customer.related_user.email;

        editProfileEtFullName.setText(strFullName);
        editProfileEtMobilePhone.setText(strPhoneNumber);
        mEmailView.setText(strEmail);
    }

    private void setupSaveReadiness(Editable s) {
//        if (s.hashCode() == editProfileEtFullName.getText().hashCode()) {
//
//        }

        // Store values at the time of the login attempt.
        String fullName = editProfileEtFullName.getText().toString();
        String phoneNumber = editProfileEtMobilePhone.getText().toString();
        String email = mEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(fullName)) {
            editProfileEtFullName.setError(getString(R.string.error_field_required));
            focusView = editProfileEtFullName;
            cancel = true;
        } else if (TextUtils.isEmpty(phoneNumber)) {
            editProfileEtMobilePhone.setError(getString(R.string.error_field_required));
            focusView = editProfileEtMobilePhone;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (menuItemSaveEdits != null) {
                menuItemSaveEdits.setEnabled(false);
            }
        } else {
            if (menuItemSaveEdits != null) {
                menuItemSaveEdits.setEnabled(true);
            }
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptEditProfile() {

        // Reset errors.
        editProfileEtFullName.setError(null);
        editProfileEtMobilePhone.setError(null);
        mEmailView.setError(null);

        // Store values at the time of the login attempt.
        String fullName = editProfileEtFullName.getText().toString();
        String phoneNumber = editProfileEtMobilePhone.getText().toString();
        String email = mEmailView.getText().toString();
        String passwordPlain = editProfileEtNewPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(fullName)) {
            editProfileEtFullName.setError(getString(R.string.error_field_required));
            focusView = editProfileEtFullName;
            cancel = true;
        } else if (TextUtils.isEmpty(phoneNumber)) {
            editProfileEtMobilePhone.setError(getString(R.string.error_field_required));
            focusView = editProfileEtMobilePhone;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        Utils.hideKeyboardFrom(this, editProfileEtMobilePhone);
        String[] nameChunks = String.valueOf(editProfileEtFullName.getText()).split(" ", 2);
        Utils.Logs('i', TAG, "Name part: " + Arrays.asList(nameChunks));
        String firstName = String.valueOf(editProfileEtFullName.getText());
        String lastName = "";
        if (nameChunks.length == 2) {

            firstName = nameChunks[0];
            lastName = nameChunks[1];

        }

        final Customer editedPokenCustomerData = new Customer();
        editedPokenCustomerData.phone_number = String.valueOf(editProfileEtMobilePhone.getText());

        User pokenUser = new User();
        pokenUser.first_name = firstName;
        pokenUser.last_name = lastName;
        pokenUser.email = email;

        editedPokenCustomerData.related_user = pokenUser;

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            if (!StringUtils.isEmpty(passwordPlain)) {

                newPassword = passwordPlain;

                ControllerDialog.getInstance().showInputDialog(
                        "Password Lama",
                        "",
                        "Password lama Anda.",
                        InputType.TYPE_TEXT_VARIATION_PASSWORD,
                        this,
                        new InputDialogListener() {
                            @Override
                            public void onInputTextDone(CharSequence text) {
                                if (!TextUtils.isEmpty(text)) {
                                    currentPassword = String.valueOf(text);
                                    presenter.startEditProfilePassword(currentCustId, editedPokenCustomerData, currentPassword, newPassword);
                                }
                            }
                        }
                );
            } else {
                presenter.startEditProfile(currentCustId, editedPokenCustomerData);
            }
        }
    }

    private boolean isEmailValid(String email) {
        return StringUtils.isValidEmail(email);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {

        if (show) {
            mLoginFormView.animate().alpha(0.3F);

            if (menuItemSaveEdits != null) {
                menuItemSaveEdits.setEnabled(false);
            }

        } else {
            mLoginFormView.animate().alpha(1F);

            if (menuItemSaveEdits != null) {
                menuItemSaveEdits.setEnabled(true);
            }
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(ProfileEditActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                showProgress(true);
                break;
            case FINISHED:
                showProgress(false);
                break;
            case ERROR:
                showProgress(false);
                break;
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return isFinishing();
    }

    @Override
    public void showMessage(int stateFinished, String s) {
        if (stateFinished == Constants.STATE_FINISHED) {
            Utils.snackBar(mLoginFormView, s, Log.INFO);
        } else if (stateFinished == Constants.NETWORK_CALLBACK_FAILURE) {
            Utils.snackBar(mLoginFormView, s, Log.ERROR);
        } else {
            Utils.snackBar(mLoginFormView, s, Log.VERBOSE);
        }
    }

    @Override
    public void showCustomerData(Customer customer) {
        this.currentCustomer = customer;

        populateCustomerData(customer);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
    }

}

