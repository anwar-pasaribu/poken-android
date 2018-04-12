package id.unware.poken.ui.store.manageproduct.view

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
interface IManageProductView: BaseView {
    fun showUploadedProductImage(productImage: ProductImage)
    fun getImageMultipartBody(): MultipartBody.Part?
    fun getImageRequestBody(): RequestBody?
    fun proceedInsertedProduct(result: ProductInserted?)
    fun showLoadingProductCategoryIndicator(isLoading: Boolean)
    fun populateProductCategoryList(productCategories: ArrayList<Category>)
    fun showUpdateProductButton()
    fun getModifiedProduct(): Product
    fun generateNewProductData(): ProductInserted
}