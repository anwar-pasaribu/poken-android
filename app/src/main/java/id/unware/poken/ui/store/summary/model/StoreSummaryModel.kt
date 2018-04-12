package id.unware.poken.ui.store.summary.model

import id.unware.poken.connections.AdRetrofit
import id.unware.poken.connections.PokenRequest
import id.unware.poken.helper.SPHelper
import id.unware.poken.models.UIState
import id.unware.poken.tools.Constants
import id.unware.poken.tools.PokenCredentials
import id.unware.poken.tools.StringUtils
import id.unware.poken.tools.Utils
import id.unware.poken.ui.store.summary.presenter.IStoreSummaryModelPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class StoreSummaryModel : IStoreSummaryModel {

    private val TAG = "StoreSummaryModel"
    private val req: PokenRequest = AdRetrofit.getInstancePoken().create(PokenRequest::class.java)

    lateinit var presenter: IStoreSummaryModelPresenter

    override fun reqStoreSummary(presenter: IStoreSummaryModelPresenter) {

        this.presenter = presenter

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING)

        // "application/json"
        this.req.reqStoreSummary(PokenCredentials.getInstance().credentialHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->

                            Utils.Log(TAG, "Result: $result")

                            SPHelper.getInstance().setPreferences(Constants.SP_SELLER_ID, result.results[0].id)
                            SPHelper.getInstance().setPreferences(Constants.SP_SELLER_OWNER_NAME, result.results[0].seller_detail.owner_name)

                            presenter.onStoreDetailResponse(result.results[0].seller_detail)

                            presenter.onSellerPromoListResponse(result.results[0].promotions)

                            presenter.onStoreTotalAmount(StringUtils.formatCurrency(result.results[0].total_credits))

                            presenter.onLatestProductListResponse(result.results[0].latest_products)

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