package id.unware.poken.ui.pickup.history.view.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.pickup.history.view.adapters.PickupHistoryPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Page for Pickup History.
 */
public class FragmentPickupHistory extends BaseFragment
        implements TabLayout.OnTabSelectedListener {

    private final String TAG = "FragmentPickupHistory";

    @BindView(R.id.parentView) ViewGroup parentView;

    // Empty state view
    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    private PickupHistoryPagerAdapter mPagerAdapter;

    private FragmentPickupHistoryList mPickupHistoryListOnGoing;
    private FragmentPickupHistoryList mPickupHistoryListFinished;


    public FragmentPickupHistory() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pickup_history, container, false);
        ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {

        // Apply view pager to tabs
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(this);

        setupViewPager();
    }

    public void setupViewPager() {
        mPagerAdapter = new PickupHistoryPagerAdapter(parent, this.getChildFragmentManager());

        mPickupHistoryListOnGoing = FragmentPickupHistoryList.newInstance(true);
        mPickupHistoryListFinished = FragmentPickupHistoryList.newInstance(false);

        mPagerAdapter.addFragment(mPickupHistoryListOnGoing);
        mPagerAdapter.addFragment(mPickupHistoryListFinished);

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mPagerAdapter);
    }

    /**
     * Update fragment interface based on Address Book availability.
     *
     * @param stateNum : State number to indicate package availability.
     */
    private void interfaceState(int stateNum) {
        switch (stateNum) {
            case Constants.STATE_NODATA:
                //"No data"
                break;
            case Constants.STATE_FINISHED:
                //"Data available"
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        AppClass.getInstance().cancelPendingRequests(FragmentLogin.class);
//    }

    @Override
    public void onDestroy() {
        Utils.Log(TAG, "on destroy!");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Utils.Log(TAG, "on destroy view.");
        super.onDestroyView();
    }

    //////
    // S: Tab selection listener
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        Utils.Log(TAG, "[onTabSelected] View pager current item: " + tab.getPosition());

        // Set current pager (Fragment to show)
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    // E: Tab selection listener
    //////
}
