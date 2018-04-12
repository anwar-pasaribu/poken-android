package id.unware.poken.ui.store.credits.presenter

interface IStoreCreditsPresenter {
    fun loadStoreCreditList()
    fun loadNextStoreCreditList(nextPage: Int)
    fun loadCreditSummary()
    fun prepareWitdhdrawal()
}