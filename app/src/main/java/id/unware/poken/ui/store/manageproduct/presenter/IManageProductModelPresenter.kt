package id.unware.poken.ui.store.manageproduct.presenter;

import id.unware.poken.domain.Category
import id.unware.poken.domain.ProductImage
import id.unware.poken.domain.ProductInserted
import id.unware.poken.ui.presenter.BasePresenter


interface IManageProductModelPresenter: BasePresenter {

    fun onProductImageResponse(productImage: ProductImage)
    fun onProductInserted(result: ProductInserted?)

    fun onProductCategoryResponse(productCategories: ArrayList<Category>)

}
