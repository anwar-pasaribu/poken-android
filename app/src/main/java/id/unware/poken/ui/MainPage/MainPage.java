package id.unware.poken.ui.MainPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.MainMenu.view.MainMenuView;
import id.unware.poken.ui.jnews.view.NewsFragment;
import id.unware.poken.ui.jnews.dummy.PojoNews;
import id.unware.poken.ui.pokenaccount.LoginActivity;
import id.unware.poken.ui.profile.ProfileFragment;

public class MainPage extends AppCompatActivity implements
        ProfileFragment.OnFragmentProfileInteractionListener,
        NewsFragment.OnListFragmentInteractionListener {

    private String TAG = "MainPage";

    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.frameMain) FrameLayout frameMain;

    private int TAB_MENU = 0, TAB_JNEWS = 1, TAB_PROFILE = 2;

    private SPHelper mSpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mSpHelper = SPHelper.getInstance();

        showMainMenu();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == TAB_MENU) {
                    showMainMenu();
                } else if (tab.getPosition() == TAB_JNEWS) {
                    showJneNews();
                } else if (tab.getPosition() == TAB_PROFILE) {
                    showProfile();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void showJneNews() {
        TabLayout.Tab tab = tabLayout.getTabAt(TAB_JNEWS);
        tab.select();

        Utils.Log(TAG, "Show JNEWS");
        Utils.changeFragment(this, R.id.frameMain, NewsFragment.newInstance(1));
    }

    public void showMainMenu() {
        TabLayout.Tab tab = tabLayout.getTabAt(TAB_MENU);
        tab.select();

        Utils.changeFragment(this, R.id.frameMain, new MainMenuView());
    }

    public void showProfile() {
        TabLayout.Tab tab = tabLayout.getTabAt(TAB_PROFILE);
        tab.select();

        Utils.Log(TAG, "Show profile");
        String strName = mSpHelper.getSharedPreferences(Constants.SHARED_PROFILE_NAME, "");
        String strEmail = mSpHelper.getSharedPreferences(Constants.SHARED_EMAIL, "");

        Utils.Log(TAG, "User. name:\"" + strName + "\" email: \"" + strEmail + "\"");

        if (StringUtils.isEmpty(strName) && StringUtils.isEmpty(strEmail)) {
            // Open login
            Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, Constants.TAG_LOGIN);
        } else {
            Utils.changeFragment(this, R.id.frameMain, ProfileFragment.newInstance(strName, strEmail));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.TAG_LOGIN) {
                Utils.Logs('i', TAG, "Login success, data: " + data);
                showProfile();
            }
        }
    }

    @Override
    public void onLogOut() {
        // [WARNING] Delete all Realm content and
        // Shared Preferences
        ControllerRealm.getInstance().removeAllData();
        SPHelper.getInstance().clearData();

        showMainMenu();
    }

    @Override
    public void onListFragmentInteraction(PojoNews.News item) {
        Utils.Logs('i', TAG, "Click news. Title: " + item.newsUrl);
        Utils.openInBrowser(this, item.newsUrl);
    }
}
