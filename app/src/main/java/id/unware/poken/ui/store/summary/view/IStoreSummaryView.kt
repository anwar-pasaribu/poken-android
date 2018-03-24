package id.unware.poken.ui.store.summary.view

import id.unware.poken.domain.Product
import id.unware.poken.domain.Seller
import id.unware.poken.domain.SellerPromo
import id.unware.poken.ui.view.BaseView

/**
 * Created by Anwar Pasaribu on 04/02/2018.
 */
interface IStoreSummaryView : BaseView{
    fun showTotalAmount(totalAmount: String)
    fun showStoreInfo(storeData: Seller)
    fun populateLatestProducts(products: List<Product>)
    fun populateSellerPromo(promos: List<SellerPromo>)
    fun showProductDetail(product: Product)
}