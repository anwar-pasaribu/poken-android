package id.unware.poken.ui.store.credits.model

import id.unware.poken.ui.store.credits.presenter.IStoreCreditsModelPresenter

/**
 * Created by Anwar Pasaribu on 16/02/2018.
 */
interface IStoreCreditsModel {
    fun getStoreCredits(presenter: IStoreCreditsModelPresenter)
    fun getMoreStoreCredits(presenter: IStoreCreditsModelPresenter, nextPage: Int)
}