package id.unware.poken.ui.store.product.presenter;

import id.unware.poken.domain.Category
import id.unware.poken.domain.Product
import id.unware.poken.domain.ProductImage
import id.unware.poken.domain.ProductInserted
import id.unware.poken.ui.presenter.BasePresenter


interface IStoreProductListModelPresenter : BasePresenter {

    fun onProductListResponse(products: ArrayList<Product>)

    fun onProductCategoryResponse(productCategories: ArrayList<Category>)

}
