package id.unware.poken.ui.featured.presenter;

import id.unware.poken.domain.Featured;
import id.unware.poken.domain.Product;
import id.unware.poken.pojo.UIState;
import id.unware.poken.ui.featured.model.IFeaturedModel;
import id.unware.poken.ui.featured.view.IFeaturedView;

/**
 * @author Anwar Pasaribu
 * @since Aug 12 2017
 */

public class FeaturedPresenter implements IFeaturedPresenter, IFeaturedModelPresenter {

    final private IFeaturedModel model;
    final private IFeaturedView view;

    public FeaturedPresenter(IFeaturedModel model, IFeaturedView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void reqFeaturedDetail(long featuredId) {
        this.model.requestFeaturedItemDetail(this, featuredId);
    }

    @Override
    public void startProductDetail(Product product) {
        view.showProductDetail(product);
    }

    @Override
    public void updateViewState(UIState uiState) {
        if (view.isActivityFinishing()) return;
        view.showViewState(uiState);
    }

    @Override
    public void onFeaturedItemDetailRes(Featured featured) {
        if (view.isActivityFinishing()) return;

        view.setupFeaturedView(featured);

        if (featured.related_products != null
                && featured.related_products.size() > 0) {
            view.populateFeaturedRelatedProducts(featured.related_products);
        }
    }

    @Override
    public void showMessage(String msg, int msgStatus) {
    }
}
