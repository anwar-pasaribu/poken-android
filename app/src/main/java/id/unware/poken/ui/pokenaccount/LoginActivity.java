package id.unware.poken.ui.pokenaccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import id.unware.poken.R;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.pokenaccount.login.view.fragment.FragmentLogin;
import id.unware.poken.ui.pokenaccount.register.view.fragment.FragmentRegisterEmail;
import okhttp3.Credentials;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements
        FragmentLogin.PokenLoginListener,
        FragmentRegisterEmail.PokenRegisterListener {

    private static final String TAG = "LoginActivity";

    /** Requested page before open Login Page. In case user not login yet */
    private int requestedPageTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (getIntent().getExtras() != null) {
            requestedPageTag = getIntent().getIntExtra(Constants.EXTRA_REQUESTED_PAGE, -1);
        }

        if (savedInstanceState == null) {
            Utils.changeFragment(this, R.id.frameLogin, new FragmentRegisterEmail());
        }
    }

    @Override
    public void onRegisterRequested() {
        // Open Register screen
        Utils.changeFragment(this, R.id.frameLogin, new FragmentRegisterEmail());
    }

    @Override
    public void onLoginSuccess() {
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
        Intent loginSuccessIntent = new Intent();
        loginSuccessIntent.putExtra(Constants.EXTRA_REQUESTED_PAGE, pageTag);
        this.setResult(Activity.RESULT_OK, loginSuccessIntent);
        this.finish();
    }
}

