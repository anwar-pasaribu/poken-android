package id.unware.poken.ui.store.manageproduct.model

import id.unware.poken.connections.AdRetrofit
import id.unware.poken.connections.MyCallback
import id.unware.poken.connections.PokenRequest
import id.unware.poken.domain.Product
import id.unware.poken.domain.ProductImage
import id.unware.poken.domain.ProductInserted
import id.unware.poken.models.UIState
import id.unware.poken.tools.PokenCredentials
import id.unware.poken.tools.Utils
import id.unware.poken.ui.store.manageproduct.presenter.IManageProductModelPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import kotlin.math.roundToInt

class ManageProductModel : MyCallback(), IManageProductModel {

    private val TAG = "ManageProductModel"
    private val req: PokenRequest = AdRetrofit.getInstancePoken().create(PokenRequest::class.java)
    private var presenter: IManageProductModelPresenter? = null


    override fun getProductCategory(presenter: IManageProductModelPresenter) {
        if (this.presenter == null) {
            this.presenter = presenter
        }

        // Loading state to view
        this.presenter!!.updateViewState(UIState.LOADING)

        this.req.reqProductCategory(PokenCredentials.getInstance().credentialHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Utils.Log(TAG, "Result: $result")
                            presenter.onProductCategoryResponse(result.results)
                            presenter.updateViewState(UIState.FINISHED)
                        },
                        {
                            error ->
                            Utils.Log(TAG, "Error: $error")
                            presenter.updateViewState(UIState.ERROR)
                        }
                )

    }

    override fun postProduct(productInserted: ProductInserted, presenter: IManageProductModelPresenter) {

        if (this.presenter == null) {
            this.presenter = presenter
        }

        // Loading state to view
        this.presenter!!.updateViewState(UIState.LOADING)

        // "application/json"
        this.req.postNewProduct(
                "application/json",
                PokenCredentials.getInstance().credentialHashMap,
                productInserted)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Utils.Log(TAG, "Result: " + result)
                            presenter.onProductInserted(result)
                            presenter.updateViewState(UIState.FINISHED)
                        },
                        {
                            error ->
                            Utils.Log(TAG, "Error: " + error)
                            presenter.updateViewState(UIState.ERROR)
                        }
                )

    }

    override fun patchProductData(presenter: IManageProductModelPresenter, modifiedProduct: Product, enteredNewProduct: ProductInserted) {
        // Loading state to view
        presenter.updateViewState(UIState.LOADING)

        val productImages = LongArray(enteredNewProduct.images.size)
        for ((imgIndex, imgId) in enteredNewProduct.images.withIndex()) {
            productImages[imgIndex] = imgId
        }

        val patchBody = ProductInserted()
        patchBody.name = enteredNewProduct.name
        patchBody.description = enteredNewProduct.description
        patchBody.seller = enteredNewProduct.seller
        patchBody.is_new = enteredNewProduct.is_new
        patchBody.brand = 1  // Other
        patchBody.category = enteredNewProduct.category
        patchBody.images = productImages
        patchBody.size = 5  // Satu Ukuran
        patchBody.stock = enteredNewProduct.stock
        patchBody.original_price = enteredNewProduct.original_price
        patchBody.price = enteredNewProduct.price
        patchBody.weight = enteredNewProduct.weight


        // "application/json"
        this.req.patchProduct(
                PokenCredentials.getInstance().credentialHashMap,
                "application/json",
                modifiedProduct.id,
                patchBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Utils.Log(TAG, "Result: $result")
                            presenter.onProductInserted(result)
                            presenter.updateViewState(UIState.FINISHED)
                        },
                        {
                            error ->
                            Utils.Log(TAG, "Error: $error")
                            presenter.updateViewState(UIState.ERROR)
                        }
                )
    }

    override fun postProductImage(productImage: ProductImage,
                                  imagePart: MultipartBody.Part?,
                                  imageReqBody: RequestBody?,
                                  presenter: IManageProductModelPresenter) {
        if (this.presenter == null) {
            this.presenter = presenter
        }

        // Loading state to view
        this.presenter!!.updateViewState(UIState.LOADING)

        val title = RequestBody.create(MediaType.parse("multipart/form-data"), "TEST_UPLOAD_ANDROID_TITLE")
        val description = RequestBody.create(MediaType.parse("multipart/form-data"), "PRODUCTIMAGE.DESCRIPTION")

        this.req.uploadProductImage(
                PokenCredentials.getInstance().credentialHashMap,
                imagePart, imageReqBody, title, description)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Utils.Log(TAG, "Result: " + result)
                            presenter.onProductImageResponse(result)
                            presenter.updateViewState(UIState.FINISHED)
                        },
                        {
                            error ->
                            Utils.Log(TAG, "Error: " + error)
                            presenter.updateViewState(UIState.ERROR)
                        }
                )

    }

    override fun onSuccess(response: Response<*>?) {
        Utils.Logs('i', TAG, "Response: " + response?.body().toString())
    }

    override fun onMessage(msg: String?, status: Int) {
    }

    override fun onFinish() {
    }

}

