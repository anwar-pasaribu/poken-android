package id.unware.poken.ui.wallet.main.view;

import android.location.Address;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.pickup.view.FragmentPickupMap;

public class WalletActivity extends BaseActivity implements FragmentPickupMap.FragmentPickupMapListener {

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

        Utils.changeFragment(this,R.id.frameLayoutFragmentContainer, new FragmentWallet());
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
