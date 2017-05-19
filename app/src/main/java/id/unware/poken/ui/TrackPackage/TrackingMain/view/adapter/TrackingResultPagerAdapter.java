package id.unware.poken.ui.TrackPackage.TrackingMain.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.util.ArrayList;

import id.unware.poken.tools.Utils;
import id.unware.poken.ui.TrackPackage.TrackingResult.view.TrackingResultFragment;

/**
 * @author Anwar Pasaribu
 * @since Dec 27 2016
 */

public class TrackingResultPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "TrackingResultPagerAdapter";

    private ArrayList<String> mTrackingCnnoList;
    private int mPageCount;

    private SparseArray<String> mFragmentTags;
    private FragmentManager mFragmentManager;
    private Context mContext;

    public TrackingResultPagerAdapter(Context context, FragmentManager fm) {
        super(fm);

        mFragmentManager = fm;
        mContext = context;
        mFragmentTags = new SparseArray<>();
    }

    public void setPageContent(ArrayList<String> trackingCnnoList) {
        mTrackingCnnoList = trackingCnnoList;
        mPageCount = trackingCnnoList.size();
    }

    @Override
    public Fragment getItem(int position) {
        Utils.Log(TAG, "Get item at pos: " + position);

        return TrackingResultFragment.newInstance(mTrackingCnnoList.get(position));
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
        return mTrackingCnnoList.get(position);
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

    @Override
    public int getCount() {
        return mTrackingCnnoList != null? mTrackingCnnoList.size() : 0;
    }
}
