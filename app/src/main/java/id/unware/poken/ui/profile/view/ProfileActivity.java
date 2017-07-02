package id.unware.poken.ui.profile.view;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import id.unware.poken.R;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivityWithup;
import id.unware.poken.ui.customercollection.view.CustomerCollectionFragment;
import id.unware.poken.ui.customerorder.view.OrdersFragment;
import id.unware.poken.ui.customerorder.view.dummy.DummyContent;
import id.unware.poken.ui.customersubscription.view.CustomerSubscriptionFragment;

public class ProfileActivity extends BaseActivityWithup implements OrdersFragment.OnOrderFragmentListener {

    private static final String TAG = "ProfileActivity";

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private boolean isLaunchFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // In case launch Favorite tab from Home screen
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey(Constants.EXTRA_IS_LAUNCH_FAVORITE)) {
                isLaunchFavorite = bundle.getBoolean(Constants.EXTRA_IS_LAUNCH_FAVORITE, false);
                if (isLaunchFavorite) {
                    mViewPager.setCurrentItem(1 /*Favorite tab*/);
                }
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
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            Utils.Log(TAG, "Home navigation cliked.");
            this.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(ShoppingOrder item) {
        Utils.Log(TAG, "Interaction with order fragment.");
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            int sectionNo = getArguments().getInt(ARG_SECTION_NUMBER);
            String str = "";
            if (sectionNo == 1) {
                str = "Bagian Pesanan";
            } else if (sectionNo == 2) {
                str = "Bagian barang favorit";
            } else if (sectionNo == 3) {
                str = "Bagian Toko Langganan";
            }
            textView.setText(str);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final int POS_ORDER = 0,
                    POS_SUBSCRIPTION = 1,
                    TOTAL_PAGE = 2;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            if (position == this.POS_ORDER) {
                return OrdersFragment.newInstance(1 /*Column count*/);

            /*} else if (position == 1) {
                return CustomerCollectionFragment.newInstance(2);
            */

            } else if (position == this.POS_SUBSCRIPTION) {
                return CustomerSubscriptionFragment.newInstance(1);
            }

            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return this.TOTAL_PAGE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case POS_ORDER:
                    return "Pesanan";
//                case 1:
//                    return "Favorit";
                case POS_SUBSCRIPTION:
                    return "Toko Langganan";
            }
            return null;
        }
    }
}
