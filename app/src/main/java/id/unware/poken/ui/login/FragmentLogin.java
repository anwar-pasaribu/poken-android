package id.unware.poken.ui.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.BuildConfig;
import id.unware.poken.PokenApp;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.controller.ControllerPaket;
import id.unware.poken.interfaces.OnUpdateDialog;
import id.unware.poken.interfaces.VolleyResultListener;
import id.unware.poken.pojo.PojoBase;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import io.realm.Realm;

/**
 * Created by marzellaalfamega on 6/22/15.
 * Fragment for Sign In page.
 *
 * @since [V49] Remove unnecessary init for Google Sign In and Facebook Sign In
 */
public class FragmentLogin extends BaseFragment implements
        View.OnClickListener,
        VolleyResultListener,
        OnUpdateDialog {

    private final String TAG = "FragmentLogin";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final int REGISTER_PAGE = 0X678;

    @BindView(R.id.txtEmailLogin) AppCompatEditText txtEmailLogin;
    @BindView(R.id.txtPasswordLogin) AppCompatEditText txtPasswordLogin;
    @BindView(R.id.imageButtonTogglePassword) ImageButton imageButtonTogglePassword;
    @BindView(R.id.btnSignIn) Button btnSignIn;
    @BindView(R.id.btnRegister) Button btnRegister;
    @BindView(R.id.btnResetPassword) Button btnResetPassword;
    @BindView(R.id.parentView) FrameLayout parentView;

    private final ControllerDialog controllerDialog = ControllerDialog.getInstance();
    private final ControllerPaket controllerPaket = ControllerPaket.getInstance();

    private final PokenApp values = PokenApp.getInstance();

    private ProgressDialog progressDialog;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ac_login, container, false);
        ButterKnife.bind(this, view);

        // Init view contain: Password visibility toggle button
        initView();
//        initGcm();

        return view;
    }


    private void initView() {
        Utils.Log(TAG, "Init view");

        // Fill checkNewPackage form with tester account in DEV_MODE
        if (BuildConfig.DEV_MODE) {
            txtEmailLogin.setText(BuildConfig.USER_EMAIL);
            txtPasswordLogin.setText(BuildConfig.USER_PASSWORD);
        }

        // Password Visibility toggle image button
        imageButtonTogglePassword.setOnClickListener(this);
        // Button Sign In
        btnSignIn.setOnClickListener(this);
        // Button Register
        btnRegister.setOnClickListener(this);
        // Reset password
        btnResetPassword.setOnClickListener(this);

    }

//    private void initGcm() {
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                autoLogin();
//            }
//        };
//
//        if (checkPlayServices()) {
//            // Start IntentService to register this application with GCM.
//            Intent intent = new Intent(parent, RegistrationIntentService.class);
//            parent.startService(intent);
//        }
//    }

    /**
     * Register click event action for button:
     * - imageButtonTogglePassword : Toggle password masking
     * - btnSignIn : Button to begin sign in process
     * - btnRegister : Open Register activity
     * - btnResetPassword : Launch chrome custom tab to open forgot password page.
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imageButtonTogglePassword:
                togglePassword();
                break;
            case R.id.btnSignIn:
                beginSignIn();
                break;
            case R.id.btnRegister:
                // registerAccount();
                break;
            case R.id.btnResetPassword:
                forgetPassword();
                break;
        }

    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Utils.Log(TAG, "Permission result. Request code: " + requestCode);
//        if (requestCode == PermissionHelper.REQUEST_CODE_ASK_PERMISSIONS) {
//            // initGcm();
//            Utils.Log(TAG, "Permission requestCode equals to REQUEST_CODE_ASK_PERMISSIONS");
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            Utils.Log(TAG, "Activity result " + requestCode);
//            switch (requestCode) {
//                case REGISTER_PAGE:
//                    if (data.hasExtra(AcRegister.SHOW_MESSAGE)) {
//                        String msg = data.getStringExtra(AcRegister.SHOW_MESSAGE);
//                        if (!StringUtils.isEmpty(msg)) {
//                            Utils.snackBar(parentView, msg);
//                        }
//                    }
//                    break;
//
//            }
//        }
//    }

    /**
     * Begin Sign In Process when all form is filled.
     */
    public void login() {
        // saveUserNamePasswordFromEditText();
        if (isLoginReady()) {
            Utils.hideKeyboardFrom(parent, txtPasswordLogin);

            // Begin checkNewPackage with params:
            // 1. View for snackbar container
            // 2. Email string as (user identifier)
            // 3. Password string as (user key)
            // 4. Volley Listener to handle server responses.
            controllerPaket.login(
                    parentView,
                    txtEmailLogin.getText().toString(),
                    txtPasswordLogin.getText().toString(),
                    this
            );
        }
    }

    /**
     * Validate checkNewPackage form in order to continue checkNewPackage
     */
    private boolean isLoginReady() {
//        String googleToken = SPHelper.getInstance().getSharedPreferences(AppClass.GOOGLE_TOKEN_ID, "");
//        if (!StringUtils.isEmpty(googleToken)) {
//            return true;
//        }
//
//        String fbToken = SPHelper.getInstance().getSharedPreferences(AppClass.FB_TOKEN_ID, "");
//        if (!StringUtils.isEmpty(fbToken)) {
//            return true;
//        }

        if (StringUtils.isEmpty(txtEmailLogin.getText().toString())) {
            Utils.snackBar(parentView, parent.getString(R.string.please_insert_your_email), Log.WARN);
            txtEmailLogin.requestFocus();
        } else if (StringUtils.isEmpty(txtPasswordLogin.getText().toString())) {
            Utils.snackBar(parentView, parent.getString(R.string.please_insert_password), Log.WARN);
            txtPasswordLogin.requestFocus();
        } else {
            return true;
        }

        return false;
    }


    /**
     * Begin Sign In when clicking on btnSignIn
     */
    public void beginSignIn() {
//        SPHelper.getInstance().setPreferences(AppClass.GOOGLE_TOKEN_ID, "");
//        SPHelper.getInstance().setPreferences(AppClass.FB_TOKEN_ID, "");

        if (isLoginReady()) {
            login();
        }
    }

//    /**
//     * Open Activity Register when click on btnRegister
//     */
//    public void registerAccount() {
//        Intent intent = new Intent(parent, AcRegister.class);
//        startActivityForResult(intent, REGISTER_PAGE);
//    }

    /**
     * Toggle possword maksed or plain text
     */
    public void togglePassword() {
        if (txtPasswordLogin.getTransformationMethod() != null && txtPasswordLogin.getTransformationMethod() instanceof PasswordTransformationMethod) {
            // Show Plain text and change image button source image
            txtPasswordLogin.setTransformationMethod(null);
            imageButtonTogglePassword.setImageResource(R.drawable.ic_visibility_off_24dp);

        } else {
            // Show masked password
            txtPasswordLogin.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageButtonTogglePassword.setImageResource(R.drawable.ic_visibility_24dp);
        }

        txtPasswordLogin.setSelection(txtPasswordLogin.getText().toString().length());
    }

    /**
     * Forget password feature by opening URL in browser.
     */
    private void forgetPassword() {
        Utils.openInBrowser(getActivity(), BuildConfig.FORGET_PASSWORD_URL);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        values.cancelPendingRequests(FragmentLogin.class);
    }

    //////
    // S: Volley Result Listener Overidded Methods
    @Override
    public void onSuccess(final PojoBase clazz) {

        if (parent.isFinishing() || !FragmentLogin.this.isAdded()) return;

        // Login success
        if (clazz != null && clazz.success == 1) {
            Utils.Log(TAG, "Sign in success");
            // Proceed data when checkNewPackage is succeed
            ControllerPaket.getInstance().afterSignin(
                    parent,
                    null,
                    clazz,
                    new Realm.Transaction.Callback() {
                        @Override
                        public void onSuccess() {
                            super.onSuccess();

                            gotoMain();

                        }

                    },
                    this
            );
        } else {
            Utils.Log(TAG, "Sign in is not success");
        }
    }

    @Override
    public void onFinish(PojoBase clazz) {
        Utils.Log(TAG, "Volley onFinish: " + clazz);
    }

    @Override
    public void onStart(PojoBase clazz) {
        Utils.Log(TAG, "Volley onStart: " + clazz);
        try {
            if (!parent.isFinishing()) {
                if (progressDialog == null) {
                    progressDialog = controllerDialog.showLoadingNotCancelable(parent);
                }

                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onError(PojoBase clazz) {
        Utils.Log(TAG, "Volley onError: " + clazz);

        if (parent.isFinishing() || !FragmentLogin.this.isAdded()) return false;

        closeDialog();
        return false;
    }
    // E: Volley Result Listener Overidded Methods
    //////

    /**
     * Open main page for the first time.
     */
    private void gotoMain() {

        Intent intent = new Intent();
        parent.setResult(Activity.RESULT_OK, intent);
        parent.finish();
    }

    private void closeDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        closeDialog();
        super.onDestroy();
    }

    @Override
    public void publishUpdate(final String message) {
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(message);
            }
        });
    }

//    private boolean checkPlayServices() {
//        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
//        int resultCode = apiAvailability.isGooglePlayServicesAvailable(parent);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(parent, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
//                        .show();
//            } else {
//                Utils.Log(TAG, "Error: This device is not supported.");
//                parent.finish();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    private void autoLogin() {
//        // check auto checkNewPackage
//        String session = SPHelper.getInstance().getSharedPreferences(AppClass.SHARED_COOKIE, "");
//        String userEmail = SPHelper.getInstance().getSharedPreferences(AppClass.SHARED_EMAIL, "");
//
//        Utils.Log(TAG, "Auto Login with session: " + session + ", email: " + userEmail);
//        if (!StringUtils.isEmpty(session) && !StringUtils.isEmpty(userEmail)) {
//            gotoMain();
//        }
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        LocalBroadcastManager.getInstance(parent).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(AppClass.REGISTRATION_COMPLETE));
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        LocalBroadcastManager.getInstance(parent).unregisterReceiver(mRegistrationBroadcastReceiver);
//    }

}
