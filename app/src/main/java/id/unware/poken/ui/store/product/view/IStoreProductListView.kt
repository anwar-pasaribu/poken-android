package id.unware.poken.ui.store.product.view

import id.unware.poken.domain.Category
import id.unware.poken.domain.Product
import id.unware.poken.domain.ProductImage
import id.unware.poken.domain.ProductInserted
import id.unware.poken.ui.view.BaseView
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Anwar Pasaribu on 16/02/2018.
 */
interface IStoreProductListView : BaseView {
    fun populateProductCategoryList(productCategories: ArrayList<Category>)
    fun populateProductList(products: ArrayList<Product>)
}