package id.unware.poken.ui.home.presenter;

import id.unware.poken.domain.Category;
import id.unware.poken.domain.Featured;
import id.unware.poken.domain.HomeDataRes;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Section;
import id.unware.poken.domain.Seller;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
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
    public void onSectionActionClick(int position, Section section) {
        Utils.Log(TAG, "Section main action click. Pos: " + position + ", sectionId: " + section.section_action_id);
        if (section.section_action_id == Constants.HOME_SECTION_SALE_PRODUCT) {
            view.startProductCategoryScreen(section);
        }
    }

    @Override
    public void onCategoryClick(int position, Category category) {
        view.showCategoryDetailScreen(category);
    }

    @Override
    public void onProductClick(int position, Product product) {
        Utils.Log(TAG, "Product on Sale section clicked. Pos: " + position + ", product ID: " + product.id);
        view.showProductDetailScreen(product);
    }

    @Override
    public void onFeaturedItemClicked(int position, Featured featured) {
        view.showFeaturedScreen(position, featured);
    }

    @Override
    public void onSellerClick(int position, Seller seller) {
        view.showSellerDetailScreen(position, seller);
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
    public void showMessage(String msg, int msgStatus) {
        view.showMessage(msg, msgStatus);
    }

    @Override
    public void updateViewState(UIState uiState) {
        view.showViewState(uiState);
    }
}
