package id.unware.poken.ui.store.product.model

import id.unware.poken.ui.store.product.presenter.IStoreProductListModelPresenter

/**
 * Created by Anwar Pasaribu on 16/02/2018.
 */
interface IStoreProductListModel {
    fun getProductCategory(presenter: IStoreProductListModelPresenter)
    fun getProductList(presenter: IStoreProductListModelPresenter)
}