package id.unware.poken.ui.pokenaccount;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.home.view.HomeActivity;
import id.unware.poken.ui.pokenaccount.login.view.fragment.FragmentLogin;
import id.unware.poken.ui.pokenaccount.register.view.fragment.FragmentRegisterEmail;
import id.unware.poken.ui.profile.view.ProfileActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements
        FragmentLogin.PokenLoginListener,
        FragmentRegisterEmail.PokenRegisterListener {

    private static final String TAG = "LoginActivity";
    public static int APP_REQUEST_CODE = 99;
    @BindView(R.id.loginFbAccountKitPhoneLogin) Button loginFbAccountKitPhoneLogin;
    /**
     * Requested page before open Login Page. In case user not login yet
     */
    private int requestedPageTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            requestedPageTag = getIntent().getIntExtra(Constants.EXTRA_REQUESTED_PAGE, -1);
        }

        if (savedInstanceState == null) {
            Utils.changeFragment(this, R.id.frameLogin, new FragmentRegisterEmail());
        }

        initView();
    }

    private void initView() {
        loginFbAccountKitPhoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneLogin(v);
            }
        });
    }

    public void phoneLogin(final View view) {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.CODE); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        configurationBuilder.setDefaultCountryCode("ID");
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
                // showErrorActivity(loginResult.getError());
                Utils.devModeToast(this, "LOgin gagal");
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));
                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                // goToMyLoggedInActivity();
                Utils.devModeToast(this, "LOgin berhasil");
            }

            // Surface the result to your user in an appropriate way.
            Toast.makeText(
                    this,
                    toastMessage,
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onRegisterRequested() {
        // Open Register screen
        Utils.changeFragment(this, R.id.frameLogin, new FragmentRegisterEmail());
    }

    @Override
    public void onLoginSuccess() {

        // Register app shortcuts for API > 25
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {

            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

            ShortcutInfo shortcut = new ShortcutInfo.Builder(this, getString(R.string.shortcuts_id_profile))
                    .setShortLabel(getString(R.string.shortcut_short_label))
                    .setLongLabel(getString(R.string.shortcut_long_label))
                    .setIcon(Icon.createWithResource(this, R.drawable.ic_person_black_24dp))
                    .setIntents(
                            new Intent[]{
                                    new Intent(Intent.ACTION_MAIN, Uri.EMPTY, this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK),
                                    new Intent(ProfileActivity.ACTION)
                            })
                    .build();

            if (shortcutManager != null) {
                shortcutManager.setDynamicShortcuts(Collections.singletonList(shortcut));
            } else {
                Utils.Logs('e', TAG, "Shortcut manager is empty");
            }
        }

        // Open page
        openPendingDesiredPage(this.requestedPageTag);
    }

    @Override
    public void onRegisterSuccess() {
        openPendingDesiredPage(this.requestedPageTag);
    }

    @Override
    public void onLoginScreenRequested() {
        Utils.changeFragment(this, R.id.frameLogin, new FragmentLogin());
    }

    private void openPendingDesiredPage(int pageTag) {

        Utils.Log(TAG, "Requested page tag: " + pageTag);

        Intent loginSuccessIntent = new Intent();
        loginSuccessIntent.putExtra(Constants.EXTRA_REQUESTED_PAGE, pageTag);
        this.setResult(Activity.RESULT_OK, loginSuccessIntent);
        this.finish();
    }
}

