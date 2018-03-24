package id.unware.poken.ui.store.summary.model;

import id.unware.poken.ui.store.summary.presenter.IStoreSummaryModelPresenter

/**
 * Created by Anwar Pasaribu on 04/02/2018.
 */

interface IStoreSummaryModel {
    fun reqStoreSummary(presenter: IStoreSummaryModelPresenter)
}
