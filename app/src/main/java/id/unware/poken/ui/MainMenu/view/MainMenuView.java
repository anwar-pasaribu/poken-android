package id.unware.poken.ui.MainMenu.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.MainMenu.view.adapters.MenuAdapter;
import id.unware.poken.ui.Tariff.TariffMain.view.RateActivity;
import id.unware.poken.ui.TrackPackage.TrackingMain.view.TrackingActivity;
import id.unware.poken.ui.nearby.view.NearbyActivity;
import id.unware.poken.ui.packages.view.AcMain;
import id.unware.poken.ui.pickup.view.PickupActivity;
import id.unware.poken.ui.wallet.main.view.WalletActivity;

public class MainMenuView extends BaseFragment implements OnClickRecyclerItem {

    private static final String TAG = "MainMenuView";

    private MenuAdapter homeMenuAdapter;

    /**
     * Main menu list view
     */
    @BindView(R.id.recyclerViewMainMenu) RecyclerView recyclerViewMainMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Utils.Log(TAG, "Create Main Menu");
        View view = inflater.inflate(R.layout.ac_main_page, container, false);
        ButterKnife.bind(this, view);
        initMainMenu();
        return view;
    }

    private void initMainMenu() {

        recyclerViewMainMenu.setHasFixedSize(true);
        recyclerViewMainMenu.setLayoutManager(new GridLayoutManager(parent, 2));

        homeMenuAdapter = new MenuAdapter(parent, this);

        recyclerViewMainMenu.setAdapter(homeMenuAdapter);

        int windowWidth = parent.findViewById(android.R.id.content).getWidth();
        int rvWidth = recyclerViewMainMenu.getWidth();

        Utils.Logs('i', TAG, "Window width: " + windowWidth);
        Utils.Logs('i', TAG, "RV width: " + rvWidth);

    }

    @Override
    public void onItemClick(View view, int position) {
        Utils.Log(TAG, "Clicked item pos: " + position);
        switch (position) {
            case 0:
                gotoTracker();
                break;
            case 1:
                gotoTariff();
                break;
            case 2:
                gotoBooking();
                break;
            case 3:
                gotoPickup();
                break;
            case 4:
                gotoNearby();
                break;
            case 5:
                gotoWallet();
                break;
        }
    }

    private void gotoBooking() {
        Intent intent = new Intent(parent, AcMain.class);
        startActivity(intent);
    }

    private void gotoNearby() {
        Intent intent = new Intent(parent, NearbyActivity.class);
        startActivity(intent);
    }

    private void gotoWallet() {
        Intent intent = new Intent(parent, WalletActivity.class);
        startActivity(intent);
    }

    private void gotoPickup() {
        Intent intent = new Intent(parent, PickupActivity.class);
        startActivity(intent);
    }

    private void gotoTariff() {
        Intent intent = new Intent(parent, RateActivity.class);
        startActivity(intent);
    }

    private void gotoTracker() {
        Intent intent = new Intent(parent, TrackingActivity.class);
        startActivity(intent);
    }
}
