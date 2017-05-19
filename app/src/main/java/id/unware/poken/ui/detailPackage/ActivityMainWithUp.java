package id.unware.poken.ui.detailPackage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import id.unware.poken.PokenApp;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.interfaces.FragmentProgress;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivityWithup;
import id.unware.poken.ui.pickup.history.view.fragments.FragmentPickupHistory;

/**
 * Created by marzellaalfamega on 6/29/15.
 *
 * Activity to hadle fragments include:
 * - FragmentPackageDetail2() : Package detail
 * - FragmentPickupHistory() : Fragment to show list of pickup history.
 *
 * -Unused-
 * - FragmentAddressBookDetail() : Address Book detail
 * - FragmentPickupHistory : Pickup history
 */
public class ActivityMainWithUp extends BaseActivityWithup implements FragmentProgress {

    private final String TAG = "ActivityMainWithUp";

    private Fragment fragment;

    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bndl = getIntent().getExtras();

        Utils.Log(TAG, "Main fragment. Fragment: " + bndl.getInt(Constants.USE_FRAGMENT));

        // Determine fragment to open.
        switch (bndl.getInt(Constants.USE_FRAGMENT)) {
            case Constants.TAG_PACKAGE_DETAIL:
                setTitle(getString(R.string.title_package_detail));
                fragment = new FragmentPackageDetail2();
                break;
            case Constants.TAG_PICKUP_HISTORY:
                setTitle(getString(R.string.title_pickup_history));
                fragment = new FragmentPickupHistory();
                break;
        }

        fragment.setArguments(bndl);
        Utils.changeFragment(this, R.id.fragmentMain, fragment);

    }

    @Override
    protected void onDestroy() {
        closeLoading();
        super.onDestroy();
    }

    // Prevent Google dialog (fade out effect)
//    @Override
//    protected boolean isAutoLogginEnabled() {
//        return false;
//    }

    private void showLoading(final Object progressTag) {
        if (loading == null) {
            loading = ControllerDialog.getInstance().showLoadingNotCancelable(this);
            loading.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Utils.devModeToast(ActivityMainWithUp.this, "Request cancelled.");
                    Utils.Log(TAG, "Cancel request for: " + progressTag);
                    PokenApp.getInstance().cancelPendingRequests(progressTag);
                }
            });

            loading.show();
        } else {
            loading.show();
        }
    }

    private void closeLoading() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void showProgress(boolean isShow, Object progressTag) {
        if (isShow) {
            showLoading(progressTag);
        } else {
            closeLoading();
        }
    }
}
