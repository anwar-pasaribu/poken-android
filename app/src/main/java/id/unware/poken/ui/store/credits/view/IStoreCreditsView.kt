package id.unware.poken.ui.store.credits.view

import id.unware.poken.domain.OrderCredit
import id.unware.poken.ui.view.BaseView

interface IStoreCreditsView : BaseView {
    fun populateStoreCreditList(credits: ArrayList<OrderCredit>)
    fun populateMoreStoreCreditList(results: ArrayList<OrderCredit>)
}