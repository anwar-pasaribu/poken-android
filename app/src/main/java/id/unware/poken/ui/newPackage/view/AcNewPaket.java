package id.unware.poken.ui.newPackage.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import id.unware.poken.PokenApp;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.interfaces.FragmentProgress;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivityWithup;

public class AcNewPaket extends BaseActivityWithup implements FragmentProgress {

    private final String TAG = "AcNewPaket";

    private FragmentNewPackage2 mFragmentNewPackage;
    private boolean isTutorialMode = false;

    private ProgressDialog nonCancelableProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = null;

        if (savedInstanceState == null) {
            Utils.Logs('w', TAG, "Create New Package Fragment from scratch");
            mFragmentNewPackage = FragmentNewPackage2.newInstance();
            if (getIntent() != null) {
                bundle = getIntent().getExtras();
            }

            mFragmentNewPackage.setArguments(bundle);
            Utils.changeFragment(this, R.id.fragmentMain, mFragmentNewPackage);
        } else {
            Utils.Logs('i', TAG, "Reuse New Package Fragment from bundle");
            mFragmentNewPackage = (FragmentNewPackage2) Utils.getCurrentFragment(this, R.id.fragmentMain);
        }

        if (bundle != null && bundle.containsKey(Constants.EXTRA_IS_TUTORIAL)) {
            isTutorialMode = bundle.getBoolean(Constants.EXTRA_IS_TUTORIAL);
        }

        // When on tutorial mode, hide soft keyboard.
        Utils.Logs('i', TAG, "Hide keyboard!! --> " + isTutorialMode);
        if (isTutorialMode) {
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
        }
    }

    // Prevent Google dialog (fade out effect)
//    @Override
    protected boolean isAutoLogginEnabled() {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.Log(TAG, "Home menu clicked");
                mFragmentNewPackage.showPackageList();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        mFragmentNewPackage.showPackageList();
        super.onBackPressed();
    }

    /**
     * Provide Toolbar object for fragments
     *
     * @return Toolbar.
     */
    public Toolbar getToolBar() {
        return super.mToolbar;
    }

    @Override
    protected void onDestroy() {
        Utils.Logs('w', TAG, "On destroy!");
        closeLoading();
        super.onDestroy();
    }

    private void showLoading(final Object progressTag) {
        if (nonCancelableProgress == null) {

            // Generate progress dialog.
            nonCancelableProgress = ControllerDialog.getInstance().showLoading(
                    this,
                    null,  // Null title will not show any title on Progress Dialog.
                    this.getString(R.string.msg_creating_new_package));

            nonCancelableProgress.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Utils.Log(TAG, "Cancel request for: " + progressTag);
                    PokenApp.getInstance().cancelPendingRequests(progressTag);
                }
            });
        } else {
            nonCancelableProgress.show();
        }
    }

    private void closeLoading() {
        if (nonCancelableProgress != null) {
            nonCancelableProgress.dismiss();
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
