package id.unware.poken.ui.store.product.model

import id.unware.poken.connections.AdRetrofit
import id.unware.poken.connections.PokenRequest
import id.unware.poken.models.UIState
import id.unware.poken.tools.PokenCredentials
import id.unware.poken.tools.Utils
import id.unware.poken.ui.store.product.presenter.IStoreProductListModelPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StoreProductListModel : IStoreProductListModel {

    private val TAG = "StoreProductListModel"
    private val req: PokenRequest = AdRetrofit.getInstancePoken().create(PokenRequest::class.java)
    private var presenter: IStoreProductListModelPresenter? = null

    override fun getProductCategory(presenter: IStoreProductListModelPresenter) {

        // pass

    }

    override fun getProductList(presenter: IStoreProductListModelPresenter) {

        if (this.presenter == null) {
            this.presenter = presenter
        }

        // Loading state to view
        this.presenter!!.updateViewState(UIState.LOADING)

        this.req.reqStoreProduct(
                PokenCredentials.getInstance().credentialHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Utils.Log(TAG, "Result: $result")
                            presenter.onProductListResponse(result.results)
                            presenter.updateViewState(UIState.FINISHED)
                        },
                        {
                            error ->
                            Utils.Log(TAG, "Error: $error")
                            presenter.updateViewState(UIState.ERROR)
                        }
                )
    }
}