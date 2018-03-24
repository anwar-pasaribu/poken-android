package id.unware.poken.ui.store.manageproduct.presenter

import id.unware.poken.domain.Category
import id.unware.poken.domain.ProductImage
import id.unware.poken.domain.ProductInserted
import id.unware.poken.pojo.UIState
import id.unware.poken.ui.store.manageproduct.model.IManageProductModel
import id.unware.poken.ui.store.manageproduct.view.IManageProductView

class ManageProductPresenter(private val model: IManageProductModel,
                             private val view: IManageProductView) : IManageProductPresenter, IManageProductModelPresenter {

    override fun submitNewProductImage(productImage: ProductImage) {

        this.model.postProductImage(
                productImage,
                this.view.getImageMultipartBody(),
                this.view.getImageRequestBody(),
                this)

    }

    override fun onProductInserted(result: ProductInserted?) {
        view.proceedInsertedProduct(result)
    }

    override fun loadProductCategoryList() {
        view.showLoadingProductCategoryIndicator(true)
        model.getProductCategory(this)
    }

    override fun onProductCategoryResponse(productCategories: ArrayList<Category>) {
        view.showLoadingProductCategoryIndicator(false)
        view.populateProductCategoryList(productCategories)
    }

    override fun submitNewProduct(newProductData: ProductInserted) {
        model.postProduct(newProductData, this)
    }

    override fun updateViewState(uiState: UIState?) {
        this.view.showViewState(uiState)
    }

    override fun onProductImageResponse(productImage: ProductImage) {
        this.view.showUploadedProductImage(productImage)
    }
}
