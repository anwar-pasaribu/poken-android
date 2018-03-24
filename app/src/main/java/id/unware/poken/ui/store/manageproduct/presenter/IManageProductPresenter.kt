package id.unware.poken.ui.store.manageproduct.presenter

import id.unware.poken.domain.ProductImage
import id.unware.poken.domain.ProductInserted

/**
 * Created by Anwar Pasaribu on 16/02/2018.
 */
interface IManageProductPresenter {
    fun loadProductCategoryList()
    fun submitNewProductImage(productImage: ProductImage)
    fun submitNewProduct(newProductData: ProductInserted)
}