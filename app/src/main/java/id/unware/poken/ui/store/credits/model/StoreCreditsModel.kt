package id.unware.poken.ui.store.credits.model

import android.net.Uri
import id.unware.poken.connections.AdRetrofit
import id.unware.poken.connections.PokenRequest
import id.unware.poken.domain.OrderCredit
import id.unware.poken.helper.SPHelper
import id.unware.poken.models.UIState
import id.unware.poken.tools.Constants
import id.unware.poken.tools.PokenCredentials
import id.unware.poken.tools.StringUtils
import id.unware.poken.tools.Utils
import id.unware.poken.ui.store.credits.presenter.IStoreCreditsModelPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.HashMap
import javax.annotation.Nullable

class StoreCreditsModel : IStoreCreditsModel {

    private val tagq = "StoreCreditsModel"
    private val req: PokenRequest = AdRetrofit.getInstancePoken().create(PokenRequest::class.java)
    private var presenter: IStoreCreditsModelPresenter? = null

    private var storeTotalCredits: Double = 0.0
    private var storeOwnerName = ""
    private val storeOrderList = ArrayList<OrderCredit>()

    override fun composeWitdrawalRequest(presenter: IStoreCreditsModelPresenter) {
        Utils.Logs('i', "Store owner name: $storeOwnerName\nStore total credit: $storeTotalCredits")
        val formattedTotalCredit = StringUtils.formatCurrency(storeTotalCredits)
        val message = "Nama: $storeOwnerName\nJumlah Pencarian: $formattedTotalCredit\nBank Tujuan: ISI\nNo. Rek.: ISI\nAtas Nama: ISI"
        presenter.onWithdrawalRequestMessageReady(message, storeTotalCredits > 0)
    }

    override fun getMoreStoreCredits(presenter: IStoreCreditsModelPresenter, nextPage: Int) {

        if (this.presenter == null) {
            this.presenter = presenter
        }

        // Loading state to view
        this.presenter!!.updateViewState(UIState.LOADING_MORE)

        val queryParams = HashMap<String, String>()
        queryParams["page"] = nextPage.toString()

        this.req.reqStoreCredit(
                PokenCredentials.getInstance().credentialHashMap, queryParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Utils.Logs('i', tagq, "Result: $result")

                            result.next = if (result.next == null) { "" } else result.next
                            presenter.onNextStoreCreditListPage(
                                    result.next!!,
                                    parseNextPage(result.next!!)
                            )

                            // Add store credit for later use
                            storeOrderList.addAll(result.results)

                            presenter.onMoreStoreCreditsListResponse(result.results)
                            presenter.updateViewState(UIState.FINISHED)
                        },
                        {
                            error ->
                            Utils.Logs('e', tagq, "Error: $error")
                            presenter.updateViewState(UIState.ERROR)
                        }
                )

    }


    override fun getCreditSummary(presenter: IStoreCreditsModelPresenter) {
        presenter.onCreditSummaryViewState(UIState.LOADING)

        // "application/json"
        this.req.reqStoreSummary(PokenCredentials.getInstance().credentialHashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->

                            Utils.Log(tagq, "Result: $result")

                            SPHelper.getInstance().setPreferences(Constants.SP_SELLER_ID, result.results[0].id)
                            SPHelper.getInstance().setPreferences(Constants.SP_SELLER_OWNER_NAME, result.results[0].seller_detail.owner_name)


                            storeOwnerName = result.results[0].seller_detail.owner_name
                            storeTotalCredits = result.results[0].total_credits

                            presenter.onStoreTotalAmount(StringUtils.formatCurrency(result.results[0].total_credits))

                            presenter.onCreditSummaryViewState(UIState.FINISHED)
                        },
                        {
                            error ->
                            Utils.Log(tagq, "Error: $error")
                            presenter.onCreditSummaryViewState(UIState.ERROR)
                        }
                )
    }

    override fun getStoreCredits(presenter: IStoreCreditsModelPresenter) {
        if (this.presenter == null) {
            this.presenter = presenter
        }

        // Loading state to view
        this.presenter!!.updateViewState(UIState.LOADING)

        val queryParams = HashMap<String, String>()
        queryParams["page"] = "1"

        this.req.reqStoreCredit(PokenCredentials.getInstance().credentialHashMap, queryParams)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Utils.Logs('i', tagq, "Result: $result")

                            result.next = if (result.next == null) { "" } else result.next

                            presenter.onNextStoreCreditListPage(
                                    result.next!!,
                                    parseNextPage(result.next!!)
                            )

                            // Add store credit for later use
                            storeOrderList.addAll(result.results)

                            presenter.onStoreCreditsListResponse(result.results)
                            presenter.updateViewState(UIState.FINISHED)
                        },
                        {
                            error ->
                            Utils.Logs('e', tagq, "Error: $error")
                            presenter.updateViewState(UIState.ERROR)
                        }
                )
    }

    private fun parseNextPage(@Nullable nextPageUrl: String) : Int {

        if (StringUtils.isEmpty(nextPageUrl)) return Constants.STATE_NODATA

        var nextPage = Constants.STATE_NODATA
        if (!StringUtils.isEmpty(nextPageUrl)) {
            val uri = Uri.parse(nextPageUrl)
            nextPage = Integer.valueOf(uri.getQueryParameter("page"))!!
        }

        return nextPage
    }
}