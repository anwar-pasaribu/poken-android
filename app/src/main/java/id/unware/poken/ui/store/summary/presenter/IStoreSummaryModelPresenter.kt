package id.unware.poken.ui.store.summary.presenter

import id.unware.poken.domain.Product
import id.unware.poken.domain.Seller
import id.unware.poken.domain.SellerPromo
import id.unware.poken.ui.presenter.BasePresenter

/**
 * Created by Anwar Pasaribu on 06/02/2018.
 */
interface IStoreSummaryModelPresenter: BasePresenter {
    fun onStoreTotalAmount(formattedAmount: String)
    fun onStoreDetailResponse(storeData: Seller)
    fun onLatestProductListResponse(products: ArrayList<Product>)
    fun onSellerPromoListResponse(promos: ArrayList<SellerPromo>)
    fun onSellerNotAvailable()
}