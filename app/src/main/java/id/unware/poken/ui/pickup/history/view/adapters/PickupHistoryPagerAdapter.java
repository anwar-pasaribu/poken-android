package id.unware.poken.ui.pickup.history.view.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import id.unware.poken.R;
import id.unware.poken.tools.Utils;

/**
 * @author Anwar Pasaribu
 * @since Dec 27 2016
 */

public class PickupHistoryPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "WalletPagerAdapter";

    private List<Fragment> listFragment;

    private SparseArray<String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private Context mContext;

    public PickupHistoryPagerAdapter(Context context, FragmentManager fm) {
        super(fm);

        mFragmentManager = fm;
        mContext = context;
        mFragmentTags = new SparseArray<>();
        listFragment = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        Utils.Log(TAG, "Get item at pos: " + position);
        Fragment fragment = listFragment.get(position);

        return fragment;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position, tag);
        }
        return obj;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if (mContext == null) return "";

        Utils.Log(TAG, "Get page title at pos: " + position);
        switch (position) {
            case 0:
                return mContext.getString(R.string.subtitle_pickup_history_on_going);
            case 1:
                return mContext.getString(R.string.subtitle_pickup_history_finished);

        }

        return null;
    }

    /**
     * Return the attached Fragment that is associated with the given position
     * @param position : Position of target Fragmet
     * @return : Attached Fragment on Pager
     */
    public Fragment getFragment(int position) {
        String tag = mFragmentTags.get(position);
        if (tag == null)
            return null;
        return mFragmentManager.findFragmentByTag(tag);
    }

    public void addFragment(Fragment fragment){
        listFragment.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}
