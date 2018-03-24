package id.unware.poken.ui.store.credits.presenter

import id.unware.poken.domain.OrderCredit
import id.unware.poken.pojo.UIState
import id.unware.poken.tools.Constants
import id.unware.poken.tools.Utils
import id.unware.poken.ui.store.credits.model.IStoreCreditsModel
import id.unware.poken.ui.store.credits.view.IStoreCreditsView

class StoreCreditsPresenter(
        val model: IStoreCreditsModel,
        val view: IStoreCreditsView
) : IStoreCreditsPresenter, IStoreCreditsModelPresenter {

    private val tagq: String = "StoreSummaryPresenter"

    private var isLoadMore = false
    private var nextPage: Int = 0

    override fun loadStoreCreditList() {

        // Make false to prevent append data again
        this.isLoadMore = false

        model.getStoreCredits(this)
    }

    override fun onNextStoreCreditListPage(nextPage: String, nextPageNumber: Int) {
        this.nextPage = nextPageNumber
    }

    override fun updateViewState(uiState: UIState?) {
        if (!view.isActivityFinishing) {
            view.showViewState(uiState)
        }
    }

    override fun onStoreCreditsListResponse(credits: ArrayList<OrderCredit>) {
        if (view.isActivityFinishing) return

        view.populateStoreCreditList(credits)
    }

    override fun loadNextStoreCreditList(nextPage: Int) {
        this.isLoadMore = true

        if (this.nextPage != Constants.STATE_NODATA) {
            model.getMoreStoreCredits(this, nextPage)
        } else {
            Utils.Log(tagq, "Last page reached...")
        }
    }

    override fun onMoreStoreCreditsListResponse(results: ArrayList<OrderCredit>) {
        if (view.isActivityFinishing) return

        view.populateMoreStoreCreditList(results)
    }
}