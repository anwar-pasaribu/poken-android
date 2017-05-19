package id.unware.poken.ui.packages.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;

/**
 * Main base activity to handle Navigation Drawer.
 *
 * @since V49 - Rename file name.
 */
public abstract class AcMainBase extends BaseActivity
//        implements NavigationDrawerCallbacks
{

    private String TAG = "AcMainBase";

    @BindView(R.id.fragmentMain) FrameLayout parentView;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.toolbar_actionbar) Toolbar baseToolbar;

    //    protected NavigationDrawerFragment mNavigationDrawerFragment;
    protected ImageButton hamburgerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_topdrawer);
        ButterKnife.bind(this);

        if (baseToolbar != null) {

            setSupportActionBar(baseToolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            ViewTreeObserver vto = baseToolbar.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    Constants.toolbarHeight = getSupportActionBar().getHeight();

                    baseToolbar.getViewTreeObserver().removeOnPreDrawListener(this);
                    return false;
                }
            });
        }

//        mNavigationDrawerFragment = (NavigationDrawerFragment)
//                getSupportFragmentManager().findFragmentById(R.id.fragment_drawer);
//
//        mNavigationDrawerFragment.setup(
//                R.id.fragment_drawer,
//                (DrawerLayout) findViewById(R.id.drawer),
//                baseToolbar);

        if (getDrawerView(baseToolbar) != null) {
            hamburgerButton = (ImageButton) getDrawerView(baseToolbar);
        }
    }

    /**
     * Get hamburger button from toolbar
     */
    private View getDrawerView(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            if (toolbar.getChildAt(i) instanceof ImageButton)
                return toolbar.getChildAt(i);
        }
        return null;
    }

    /**
     * Action when user click on drawer list item.
     *
     * @param itemId Navigation item ID.
     *
     * @since Nov 8 2016 - Waiting for certain time (config_mediumAnimTime)
     *                     before open desire fragment on host activity
     *                     and prevent reload page when user re-click on the same item.
     */
//    @Override
//    public void onNavigationDrawerSelectedItemId(final int itemId) {
//        Utils.Log(TAG, "Nav. drawer selected item pos: " + itemId);
//
//        onNavigationDrawerItemSelected(itemId, false);
//    }

    /*
     * Bridge click item from list item on drawer, so subclass of {@link AcMainBase} could
     * use the event.
     *
     * @param itemId      : Position of the item
     * @param forceChange : UNKNOW
     */
//    public abstract void onNavigationDrawerItemSelected(int itemId, boolean forceChange);

    @Override
    public void onBackPressed() {
        Utils.Log(TAG, "Super onBackPressed is called.");
//        if (mNavigationDrawerFragment.isDrawerOpen())
//            mNavigationDrawerFragment.closeDrawer();
//        else
        super.onBackPressed();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        removeActivityTransition();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        removeActivityTransition();
    }

    @Override
    public void finish() {
        super.finish();
        removeActivityTransition();

        // Clear naviagtion instance when finishing the activity
//        mNavigationDrawerFragment = null;
    }

    private void removeActivityTransition() {
        overridePendingTransition(0, 0);
    }
}
