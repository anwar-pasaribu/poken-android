package id.unware.poken.ui.wallet.main.view;

import android.view.View;

import id.unware.poken.pojo.PojoWalletData;
import id.unware.poken.ui.view.BaseView;


/**
 * @author Anwar Pasaribu
 * @since Feb 02 2017
 */

public interface IWalletView extends BaseView {

    View getParentView();

    void setBalance(String balance);

    void setupViewPager(PojoWalletData pojoWalletData);
}
