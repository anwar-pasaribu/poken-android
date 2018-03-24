package id.unware.poken.ui.store.summary.presenter

import id.unware.poken.domain.Product

/**
 * Created by Anwar Pasaribu on 06/02/2018.
 */
interface IStoreSummaryPresenter {
    fun loadStoreSummary()
    fun startDetailScreen(product: Product)
}