package id.unware.poken.ui.store.credits.model

import android.net.Uri
import id.unware.poken.connections.AdRetrofit
import id.unware.poken.connections.PokenRequest
import id.unware.poken.domain.ProductDataRes
import id.unware.poken.pojo.UIState
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