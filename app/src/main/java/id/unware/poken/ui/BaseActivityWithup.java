package id.unware.poken.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import id.unware.poken.R;

/**
 * Created by marzellaalfamega on 6/29/15.
 * Activity to hadle fragments include:
 * - FragmentPackageDetail2()
 * - FragmentAddressBookDetail()
 * - FragmentPickupHistory()
 */
public class BaseActivityWithup extends BaseActivity
        //extends BaseActivityWithGoogleSignIn
        {

    protected Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_up);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }

//    @Override
//    protected boolean isAutoLogginEnabled() {
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
