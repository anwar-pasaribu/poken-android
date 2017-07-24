package id.unware.poken.ui.pokenaccount;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import id.unware.poken.R;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.pokenaccount.login.view.fragment.FragmentLogin;
import okhttp3.Credentials;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements FragmentLogin.PokenLoginListener {

    private static final String TAG = "LoginActivity";

    /** Requested page before open Login Page. In case user not login yet */
    private int requestedPageTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getIntent().getExtras() != null) {
            requestedPageTag = getIntent().getIntExtra(Constants.EXTRA_REQUESTED_PAGE, -1);
        }

        SPHelper spHelper = SPHelper.getInstance();

        String credential = Credentials.basic("anwar", "anwar_poken17");
        spHelper.setPreferences(Constants.SP_AUTH_TOKEN, credential);

        if (savedInstanceState == null) {
            Utils.changeFragment(this, R.id.frameLogin, new FragmentLogin());
        }
    }

    @Override
    public void onRegisterRequested() {
        // TODO Open Register screen
    }

    @Override
    public void onLoginSuccess() {
        Intent loginSuccessIntent = new Intent();
        loginSuccessIntent.putExtra(Constants.EXTRA_REQUESTED_PAGE, requestedPageTag);
        this.setResult(Activity.RESULT_OK, loginSuccessIntent);
        this.finish();
    }
}

