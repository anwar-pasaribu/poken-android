package id.unware.poken.ui.pickup.view;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.pickup.history.view.PickupHistoryActivity;

public class PickupActivity extends BaseActivity implements FragmentPickupMap.FragmentPickupMapListener {

    private String TAG = "PickupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_fragment_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        Utils.changeFragment(this, R.id.frameLayoutFragmentContainer, new FragmentPickupMap());
    }

    //////
    // S: Option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m_pickup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                goToPickupHistory(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    // E: Option menu
    //////

    private void goToPickupHistory(boolean finishThis) {
        Utils.Log(TAG, "Open pickup history.");
        Intent intent = new Intent(this, PickupHistoryActivity.class);
        startActivity(intent);
        if (finishThis) {
            finish();
        }
    }

    @Override
    public void onSelectedAddress(Address address) {

    }

    @Override
    public void onRequestPickupSuccess() {

    }

    @Override
    public void onVerifyPhoneNumberNow(boolean isVerifyNow) {

    }
}
