package id.unware.poken.ui.store.manageproduct.model

import id.unware.poken.domain.Product
import id.unware.poken.domain.ProductImage
import id.unware.poken.domain.ProductInserted
import id.unware.poken.ui.store.manageproduct.presenter.IManageProductModelPresenter
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by Anwar Pasaribu on 16/02/2018.
 */
interface IManageProductModel {
    fun getProductCategory(presenter: IManageProductModelPresenter)
    fun postProduct(productInserted: ProductInserted, presenter: IManageProductModelPresenter)
    fun postProductImage(productImage: ProductImage, imagePart: MultipartBody.Part?, imageReqBody: RequestBody?, presenter: IManageProductModelPresenter)
    fun patchProductData(presenter: IManageProductModelPresenter, modifiedProduct: Product, enteredNewProduct: ProductInserted)
}