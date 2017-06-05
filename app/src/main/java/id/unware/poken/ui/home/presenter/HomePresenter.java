package id.unware.poken.ui.home.presenter;

import id.unware.poken.domain.HomeDataRes;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.home.model.IHomeModel;
import id.unware.poken.ui.home.view.IHomeView;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class HomePresenter implements IHomePresenter, IHomeModelPresenter {

    private static final String TAG = "HomePresenter";
    final private IHomeModel model;
    final private IHomeView view;

    public HomePresenter(IHomeModel model, IHomeView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void getHomeData() {
        model.requestHomeData(HomePresenter.this);
    }

    @Override
    public void onHomeDataResponse(HomeDataRes homeDataRes) {
        Utils.Logs('i', TAG, "Response res size: " + String.valueOf(homeDataRes));
        if (homeDataRes.results.isEmpty()) {
            Utils.Logs('i', TAG, "Response data empty");
            view.showViewState(UIState.ERROR);
        } else {
            view.populateHomeView(homeDataRes.results.get(0).featured_items, homeDataRes.results.get(0).sections);
        }

    }

    @Override
    public void updateViewState(UIState uiState) {
        view.showViewState(uiState);
    }
}
