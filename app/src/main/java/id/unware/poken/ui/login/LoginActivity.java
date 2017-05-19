package id.unware.poken.ui.login;

import android.os.Bundle;

import id.unware.poken.R;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (savedInstanceState == null) {
            Utils.changeFragment(this, R.id.frameLogin, new FragmentLogin());
        }
    }
}

