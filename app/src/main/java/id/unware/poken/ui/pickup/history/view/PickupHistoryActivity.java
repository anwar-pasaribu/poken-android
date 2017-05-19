package id.unware.poken.ui.pickup.history.view;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.pickup.history.view.fragments.FragmentPickupHistory;
import id.unware.poken.ui.pickup.history.view.fragments.FragmentPickupHistoryList;
import id.unware.poken.ui.pickup.view.FragmentPickupMap;
import id.unware.poken.ui.pickup.view.PickupActivity;

public class PickupHistoryActivity extends BaseActivity implements FragmentPickupMap.FragmentPickupMapListener, FragmentPickupHistoryList.PickupHistoryListListener {

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

        Utils.changeFragment(this,R.id.frameLayoutFragmentContainer, new FragmentPickupHistory());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            backToPickupMap();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        backToPickupMap();
        super.onBackPressed();
    }

    private void backToPickupMap() {
        Intent intent = new Intent(this, PickupActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRepickup(String pickupId) {
        Utils.Logs('i', "PickupHistoryActivity", "Repickup pickup id: " + pickupId);
    }
}
