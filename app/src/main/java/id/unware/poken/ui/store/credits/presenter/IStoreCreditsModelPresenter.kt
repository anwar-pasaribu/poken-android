package id.unware.poken.ui.store.credits.presenter;

import id.unware.poken.domain.OrderCredit
import id.unware.poken.ui.presenter.BasePresenter


interface IStoreCreditsModelPresenter : BasePresenter {

    fun onStoreCreditsListResponse(credits: ArrayList<OrderCredit>)
    fun onMoreStoreCreditsListResponse(results: ArrayList<OrderCredit>)

    fun onNextStoreCreditListPage(nextPage: String, nextPageNumber: Int)

}
