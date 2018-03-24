package id.unware.poken.ui.store.manageproduct.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.yalantis.ucrop.UCrop
import id.unware.poken.R
import id.unware.poken.domain.Category
import id.unware.poken.domain.Product
import id.unware.poken.domain.ProductImage
import id.unware.poken.domain.ProductInserted
import id.unware.poken.helper.SPHelper
import id.unware.poken.pojo.UIState
import id.unware.poken.tools.Constants
import id.unware.poken.tools.MyLog
import id.unware.poken.tools.StringUtils
import id.unware.poken.tools.Utils
import id.unware.poken.tools.glide.GlideApp
import id.unware.poken.tools.glide.GlideRequests
import id.unware.poken.ui.BaseActivity
import id.unware.poken.ui.store.manageproduct.model.ManageProductModel
import id.unware.poken.ui.store.manageproduct.presenter.ManageProductPresenter
import id.unware.poken.ui.store.manageproduct.view.fragment.ProductCategoryDialogFragment
import kotlinx.android.synthetic.main.activity_manage_product.*
import kotlinx.android.synthetic.main.comp_bottom_toolbar_single_button.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class ManageProductActivity : BaseActivity(), IManageProductView, ProductCategoryDialogFragment.Listener  {

    private val TAG = "ManageProductActivity"

    private val REQUEST_SELECT_PICTURE = 0x01
    private val SAMPLE_CROPPED_IMAGE_NAME = "PRODUCT_IMG"

    private val newProductData = ProductInserted()

    private var presenter: ManageProductPresenter? = null

    private lateinit var productCategoryList: ArrayList<Category>

    private lateinit var imageViews: ArrayList<ImageView>
    private var productToEdit: Product? = null
    private val productImages: SparseArray<ProductImage> = SparseArray()
    private var currentWorkingImgIndex = 0

    private var currentImageBody: MultipartBody.Part? = null
    private var currentImageRequestBody: RequestBody? = null

    private lateinit var glideRequest: GlideRequests

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_product)

        productToEdit = intent.extras?.getParcelable(Constants.EXTRA_DOMAIN_PARCELABLE_DATA)

        Utils.Logs('i', "$TAG - Product data to edit: ${productToEdit}")

        this.presenter = ManageProductPresenter(ManageProductModel(), this)

        // Load Product Category immediately
        presenter?.loadProductCategoryList()

        glideRequest = GlideApp.with(this)

        initView()

    }

    private fun initView() {

        // Collect all image views
        imageViews = ArrayList()

        imageViews.add(ivManageProduct1)
        imageViews.add(ivManageProduct2)
        imageViews.add(ivManageProduct3)
        imageViews.add(ivManageProduct4)
        imageViews.add(ivManageProduct5)

        Utils.Logs('i', TAG, "Image views: $imageViews")

        for ((index, iv) in imageViews.withIndex()) {
            iv.setOnClickListener {
                MyLog.FabricLog(Log.INFO, "Click $index image")
                currentWorkingImgIndex = index
                pickFromGallery()
            }
        }

        parentClickableManageProductCategory.setOnClickListener {
            openProductCategoryChooser()
        }

        etManageProductStorePrice.addTextChangedListener(onTextChangedListener())

        includeBottomToolbar.btnRight.text = getString(R.string.btn_save_product)
        includeBottomToolbar.btnRight.setOnClickListener { view: View? ->
            Utils.Log("Clicked view: ", view.toString())
            proceedNewProductData()
        }

        parentManageProductGeneratedPokenPrice.setOnClickListener {
            openGeneratedPokenPriceInfo()
        }

        // Restore product data to edit it.
        if (productToEdit != null)
            restoreProductData(productToEdit)

    }

    private fun restoreProductData(productToEdit: Product?) {

        // Restore image
        var imgIndex = 0
        productToEdit?.images?.forEach { productImage ->
            Utils.Logs('i', "Product image ${productImage.thumbnail}")
            glideRequest.asDrawable()
                    .clone()
                    .load(productImage.thumbnail)
                    .centerInside()
                    .into(imageViews[imgIndex])

            imgIndex++
        }

        etManageProductName.setText(productToEdit?.name)
        etManageProductDescription.setText(productToEdit?.description)
        etManageProductStock.setText(productToEdit?.stock.toString())
        etManageProductWeight.setText(productToEdit?.weight.toString())
        etManageProductStorePrice.setText(productToEdit?.price.toString())

        // Restore product category
        // When product category downloaded
    }


    private fun onTextChangedListener(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                etManageProductStorePrice.removeTextChangedListener(this)

                try {
                    var originalString = s.toString()

                    val longval: Long?
                    if (originalString.contains(".")) {
                        originalString = originalString.replace(".", "")
                    }
                    longval = java.lang.Long.parseLong(originalString)

                    val formatter = NumberFormat.getInstance(Locale.GERMANY) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString = formatter.format(longval)

                    //setting text after format to EditText
                    etManageProductStorePrice.setText(formattedString)
                    etManageProductStorePrice.setSelection(etManageProductStorePrice.text.length)

                    afterProductPriceEntered(originalString)

                } catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                etManageProductStorePrice.addTextChangedListener(this)
            }
        }
    }

    private fun openGeneratedPokenPriceInfo() {

        showAlertDialog(
                "Harga Poken",
                "Harga jual di Poken adalah harga produk + 5%",
                { dialogInterface: DialogInterface, i: Int ->
                    Utils.Logs('i', "$TAG - Dialog pressed button: $i")
                    dialogInterface.dismiss()
                },
                "OK",
                { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }, "Tutup"

        )
    }

    private fun openProductCategoryChooser() {

        MyLog.FabricLog(Log.INFO, "Open product category chooser.")

        val f: ProductCategoryDialogFragment = ProductCategoryDialogFragment.newInstance(productCategoryList)
        f.show(supportFragmentManager, "dialog-product-category-chooser")

    }

    private fun afterProductPriceEntered(priceString: String) {
        Utils.Logs('i', "Entered price $priceString")

        val nullSafePriceString: String = if (priceString.isEmpty())
            "0"
        else
            priceString

        // Add 5% from original price
        val pokenPrice: Double = nullSafePriceString.toDouble() * 0.005 + nullSafePriceString.toDouble()

        newProductData.price = pokenPrice

        Utils.Logs('i', "After Poken price $pokenPrice")
        tvManageProductGeneratedProkenPrice.text = StringUtils.formatCurrency(pokenPrice)

    }

    override fun proceedInsertedProduct(result: ProductInserted?) {
        Utils.Log(TAG, "New inserted product: $result")
        val productDetailIntent = Intent()
        productDetailIntent.putExtra(Product.KEY_PRODUCT_ID, result?.id)
        productDetailIntent.putExtra(Constants.EXTRA_DOMAIN_PARCELABLE_DATA, result)
        productDetailIntent.putExtra(Constants.EXTRA_PRODUCT_DETAIL_IS_EDIT, true)
        setResult(Activity.RESULT_OK, productDetailIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Utils.Log(TAG, "On activity result. Res: $resultCode, req: $requestCode, data: $data")

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                val selectedUri = data.data
                if (selectedUri != null) {
                    startCropActivity(data.data)
                } else {
                    Toast.makeText(this, R.string.toast_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data)
            }
        } else {
            Utils.Logs('e', "Result not OK and data NULL")
        }

        if (resultCode == UCrop.RESULT_ERROR && data != null) {
            Utils.Logs('e', TAG, "Handle ERROR")
            handleCropError(data)
        }
    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            try {

                // Set MultipartBody.Part
                val filePath = File(resultUri.path).absolutePath
                val file = File(filePath)
                val mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)

                this.currentImageRequestBody = mFile
                this.currentImageBody = MultipartBody.Part.createFormData("path", file.name, mFile)

                this.presenter?.submitNewProductImage(ProductImage(0, "", ""))

                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true

                glideRequest.asDrawable()
                        .clone()
                        .load(File(resultUri.path).absolutePath)
                        .centerInside()
                        .into(imageViews[currentWorkingImgIndex])

                Utils.Logs('i', TAG, "Image size w: " + options.outWidth + ", h: " + options.outHeight)

            } catch (e: Exception) {
                Log.e(TAG, "setImageUri", e)
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                MyLog.FabricLog(Log.ERROR, "Error while preparing cropped image URI to Upload.", e)
            }

        } else {
            Toast.makeText(this@ManageProductActivity, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show()
            MyLog.FabricLog(Log.ERROR, "URI of cropped image is NULL.")
        }

    }

    private fun handleCropError(result: Intent) {
        val cropError = UCrop.getError(result)
        if (cropError != null) {
            Toast.makeText(this@ManageProductActivity, cropError.message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@ManageProductActivity, R.string.toast_unexpected_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCropActivity(uri: Uri) {
        val destinationFileName = SAMPLE_CROPPED_IMAGE_NAME + java.util.Calendar.getInstance().timeInMillis + ".jpg"

        var uCrop = UCrop.of(uri, Uri.fromFile(File(cacheDir, destinationFileName)))

        val options = UCrop.Options()

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.setCompressionQuality(50 /* IMAGE QUALITY*/)

        options.setHideBottomControls(true)

        // Color palette
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorAccent))
        options.setToolbarWidgetColor(ContextCompat.getColor(this, R.color.colorAccent))

        uCrop.withOptions(options)

        uCrop = uCrop
                .withAspectRatio(1F, 1F)
                .withMaxResultSize(1024, 1024)

        uCrop.start(this)
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_STORAGE_READ_ACCESS_PERMISSION -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickFromGallery()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun pickFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION)
        } else {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(intent, getString(R.string.label_select_picture)), REQUEST_SELECT_PICTURE)
        }
    }

    private fun proceedNewProductData() {
        MyLog.FabricLog(Log.INFO, "Begin collecting new product data")
        val parentChildCount = parentManageProductRoot.childCount
        var isFormReady = true
        for (childIndex in 0..parentChildCount) {
            if (parentManageProductRoot.getChildAt(childIndex) is android.support.design.widget.TextInputLayout) {
                val enteredVal = (parentManageProductRoot.getChildAt(childIndex) as TextInputLayout).editText?.text
                Utils.Log(TAG, "Entered val: $enteredVal")
                if (enteredVal.isNullOrEmpty()) {
                    (parentManageProductRoot.getChildAt(childIndex) as TextInputLayout).error = getString(R.string.msg_generic_form_field_invalid)
                    isFormReady = false
                }
            }
        }

        if (productImages.size() == 0) {
            isFormReady = false
            Utils.toast(this, "Gambar produk tidak boleh kosong.")
        }

        if (isFormReady) {
            Utils.Logs('i', TAG, "Begin submitting new product")
            MyLog.FabricLog(Log.INFO, "$TAG - Begin submitting new product")

            val imagesId = (0..productImages.size())
                    .map { productImages.keyAt(it) }
                    .map { productImages.get(it).id }

            newProductData.seller = SPHelper.getInstance().getSharedPreferences(Constants.SP_SELLER_ID, 3L)
            newProductData.brand = 1
            newProductData.category = 5
            newProductData.images = LongArray(imagesId.size, { i -> imagesId[i] })
            newProductData.size = 5
            newProductData.is_new = true
            newProductData.name = etManageProductName.text.toString()
            newProductData.description = etManageProductDescription.text.toString()
            newProductData.stock = try {
                etManageProductStock.text.toString().toInt()
            } catch (e: NumberFormatException) {
                0
            }
            newProductData.weight = try {
                etManageProductWeight.text.toString().toDouble()
            } catch (e: NumberFormatException) {
                0.0
            }
            Utils.Logs('i', TAG, "New product data ready: $newProductData")
            presenter?.submitNewProduct(newProductData)
        } else {
            Utils.Logs('e', "$TAG Form not ready")
        }
    }

    override fun showViewState(uiState: UIState?) {
        if (uiState == UIState.LOADING) {
            Utils.Log(TAG, "Show loading")
            toggleLoadingState(true)
        } else if (uiState == UIState.FINISHED) {
            toggleLoadingState(false)
            Utils.Log(TAG, "Show finished")
        }
    }

    private fun toggleLoadingState(isLoading: Boolean) {
        includeBottomToolbar?.btnRight?.isEnabled = !isLoading
    }

    override fun isActivityFinishing(): Boolean {
        return isFinishing
    }

    override fun showUploadedProductImage(productImage: ProductImage) {
        Utils.Log(TAG, "Working img index: $currentWorkingImgIndex Product image uploaded: $productImage")
        productImages.put(currentWorkingImgIndex, productImage)
        Utils.Logs('i', "$TAG collected images: $productImages")
    }

    override fun getImageMultipartBody(): MultipartBody.Part? {
        return this.currentImageBody
    }

    override fun getImageRequestBody(): RequestBody? {
        return this.currentImageRequestBody
    }

    override fun showLoadingProductCategoryIndicator(isLoading: Boolean) {

        if (isLoading) {
            parentClickableManageProductCategory.isEnabled = false
            parentClickableManageProductCategory.alpha = 0.5F
        } else {
            parentClickableManageProductCategory.isEnabled = true
            parentClickableManageProductCategory.alpha = 1F
        }

    }

    override fun populateProductCategoryList(productCategories: ArrayList<Category>) {
        Utils.Logs('i', "$TAG - Product category list size: ${productCategories.size}")
        productCategoryList = productCategories

        if (productToEdit != null) {
            // Restore product category
            if (productCategoryList.size > 0) {
                val productCategoryName = productToEdit?.category
                val productCategoryIndex = productCategoryList.binarySearch {
                    String.CASE_INSENSITIVE_ORDER.compare(it.name, productCategoryName)
                }

                Utils.Logs('i', "$TAG - Find product category: $productCategoryName found at index $productCategoryIndex")

                if (productCategoryIndex >= 0) {
                    onProductCtaegoryItemClicked(productCategoryIndex, productCategoryList[productCategoryIndex])
                }
            }
        }
    }

    override fun onProductCtaegoryItemClicked(position: Int, category: Category) {

        Utils.Logs('i', "Selected category pos: $position item: $category")

        newProductData.category = category.id

        tvManageProductProductCategory.text = getString(R.string.lbl_selected_product_category, category.name)

    }

}
