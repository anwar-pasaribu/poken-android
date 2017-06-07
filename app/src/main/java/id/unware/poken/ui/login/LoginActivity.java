package id.unware.poken.ui.login;

import android.os.Bundle;

import id.unware.poken.R;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import okhttp3.Credentials;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SPHelper spHelper = SPHelper.getInstance();

        String credential = Credentials.basic("anwar", "anwar_poken17");
        spHelper.setPreferences(Constants.SP_AUTH_TOKEN, credential);

        if (savedInstanceState == null) {
            Utils.changeFragment(this, R.id.frameLogin, new FragmentLogin());
        }
    }
}

