package id.unware.poken.ui.store.product.presenter

import id.unware.poken.domain.Category
import id.unware.poken.domain.Product
import id.unware.poken.models.UIState
import id.unware.poken.ui.store.product.model.IStoreProductListModel
import id.unware.poken.ui.store.product.view.IStoreProductListView

class StoreProductListPresenter(val model: IStoreProductListModel,
                                val view: IStoreProductListView) :
        IStoreProductListPresenter,
        IStoreProductListModelPresenter{

    override fun updateViewState(uiState: UIState?) {
        view.showViewState(uiState)
    }

    override fun onProductListResponse(products: ArrayList<Product>) {
        view.populateProductList(products)
    }

    override fun onProductCategoryResponse(productCategories: ArrayList<Category>) {
        view.populateProductCategoryList(productCategories)
    }

    override fun loadProductList() {
        model.getProductList(this)
    }
}