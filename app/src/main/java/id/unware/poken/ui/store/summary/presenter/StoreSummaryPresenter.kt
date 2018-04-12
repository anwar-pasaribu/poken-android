package id.unware.poken.ui.store.summary.presenter

import id.unware.poken.domain.Product
import id.unware.poken.domain.Seller
import id.unware.poken.domain.SellerPromo
import id.unware.poken.models.UIState
import id.unware.poken.ui.store.summary.model.IStoreSummaryModel
import id.unware.poken.ui.store.summary.view.IStoreSummaryView

class StoreSummaryPresenter(
        private var model: IStoreSummaryModel,
        private var view: IStoreSummaryView
    ) : IStoreSummaryPresenter, IStoreSummaryModelPresenter {

    override fun onSellerPromoListResponse(promos: ArrayList<SellerPromo>) {
        view.populateSellerPromo(promos)
    }

    override fun onStoreDetailResponse(storeData: Seller) {
        view.showStoreInfo(storeData)
    }

    override fun startDetailScreen(product: Product) {
        view.showProductDetail(product)
    }

    override fun onLatestProductListResponse(products: ArrayList<Product>) {
        view.populateLatestProducts(products)
    }

    override fun updateViewState(uiState: UIState?) {
        view.showViewState(uiState)
    }

    override fun onStoreTotalAmount(formattedAmount: String) {
        view.showTotalAmount(formattedAmount)
    }

    override fun loadStoreSummary() {
        model.reqStoreSummary(this)
    }
}