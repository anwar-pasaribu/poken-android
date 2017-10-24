package id.unware.poken.ui.profile.view;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.BuildConfig;
import id.unware.poken.R;
import id.unware.poken.domain.Customer;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivityWithup;
import id.unware.poken.ui.customerorder.view.OrdersFragment;
import id.unware.poken.ui.home.view.HomeActivity;
import id.unware.poken.ui.profile.view.adapter.SectionsPagerAdapter;
import id.unware.poken.ui.profileedit.view.ProfileEditActivity;
import id.unware.poken.ui.shoppingorder.view.OrderActivity;

public class ProfileActivity extends BaseActivityWithup implements OrdersFragment.OnOrderFragmentListener {

    private static final String TAG = "ProfileActivity";

    public static final String ACTION = BuildConfig.APPLICATION_ID + ".OPEN_SHORTCUT_PROFILE";

    @BindView(R.id.parentProfileInfo) ViewGroup parentProfileInfo;
    @BindView(R.id.ivUserAvatar) ImageView ivUserAvatar;
    @BindView(R.id.tvProfileUser) TextView tvProfileUser;
    @BindView(R.id.tvProfileIdetifier) TextView tvProfileIdetifier;
    @BindView(R.id.profileIbEditProfile) Button profileIbEditProfile;

    @BindView(R.id.tabs) android.support.design.widget.TabLayout tabLayout;
    @BindView(R.id.container) android.support.v4.view.ViewPager mViewPager;

    private Customer currentCustomer;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private boolean isLaunchFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        // In case launch Favorite tab from Home screen
        if (getIntent().getExtras() != null) {
                isLaunchFavorite = getIntent().getExtras().getBoolean(Constants.EXTRA_IS_LAUNCH_FAVORITE, false);
            if (isLaunchFavorite) {
                mViewPager.setCurrentItem(1 /*Favorite tab*/);
            }
        }

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLoggedUserView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        profileIbEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentCustomer != null) {
                    showEditProfile(currentCustomer);
                }
            }
        });

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout.setupWithViewPager(mViewPager);
    }

    private void showEditProfile(Customer customer) {
        Intent editProfileIntent = new Intent(this, ProfileEditActivity.class);
        editProfileIntent.putExtra(Constants.KEY_DOMAIN_ITEM_ID, customer.id);
        editProfileIntent.putExtra(Constants.EXTRA_PARCELABLE_CUSTOMER, customer);
        startActivityForResult(editProfileIntent, Constants.TAG_EDIT_PROFILE);
    }

    private void setupLoggedUserView() {
        String strCustomerData = SPHelper.getInstance().getSharedPreferences(Constants.SP_AUTH_CUSTOMER_DATA, "");

        if (!StringUtils.isEmpty(strCustomerData)) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            Customer customer = gson.fromJson(strCustomerData, Customer.class);

            if (customer != null) {
                this.currentCustomer = customer;
                tvProfileIdetifier.setText(customer.related_user.email);
                tvProfileUser.setText(customer.related_user.getFullName());
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            proceedSignout();
            return true;
        } else if (id == android.R.id.home) {
            Utils.Log(TAG, "Home navigation cliked.");
            this.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void proceedSignout() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {

            ShortcutManager shortcutManager = getSystemService(ShortcutManager.class);

            if (shortcutManager != null) {
                shortcutManager.removeDynamicShortcuts(Collections.singletonList(getString(R.string.shortcuts_id_profile)));
            } else {
                Utils.Logs('e', TAG, "Shortcut manager is empty");
            }
        }

        PokenCredentials.getInstance().setCredential(null);
        this.finish();
    }

    @Override
    public void onListFragmentInteraction(ShoppingOrder item) {
        Utils.Log(TAG, "Interaction with order fragment.");
        openOrderScreen(item);
    }

    private void openOrderScreen(ShoppingOrder item) {
        Intent orderScreenIntent = new Intent(this, OrderActivity.class);
        orderScreenIntent.putExtra(Constants.EXTRA_ORDER_ID, item.id);
        this.startActivity(orderScreenIntent);
    }
}
