package id.unware.poken.ui.profile.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import id.unware.poken.tools.Utils;
import id.unware.poken.ui.customerorder.view.OrdersFragment;
import id.unware.poken.ui.customersubscription.view.CustomerSubscriptionFragment;

/**
 * @author Anwar Pasaribu
 * @since Jul 29 2017
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final int POS_ORDER = 0,
            POS_COLLECTION = -1,
            POS_SUBSCRIPTION = 1,
            TOTAL_PAGE = 2;

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
        Utils.Log("SectionsPagerAdapter", "Total page: " + this.TOTAL_PAGE);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == this.POS_ORDER) {
            return OrdersFragment.newInstance(1 /*Column count*/);

        /*} else if (position == POS_COLLECTION) {
            return CustomerCollectionFragment.newInstance(2);

        }*/
        } else if (position == this.POS_SUBSCRIPTION) {
            return CustomerSubscriptionFragment.newInstance(1);
        }

        return null;
    }

    @Override
    public int getCount() {
        return this.TOTAL_PAGE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case POS_ORDER:
                return "Pesanan";
//                case POS_COLLECTION:
//                    return "Favorit";
            case POS_SUBSCRIPTION:
                return "Toko Langganan";
        }
        return null;
    }
}
