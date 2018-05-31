package id.unware.poken.ui.store.summary.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.*
import com.daimajia.slider.library.SliderTypes.DefaultSliderView
import id.unware.poken.R
import id.unware.poken.domain.Product
import id.unware.poken.domain.Seller
import id.unware.poken.domain.SellerPromo
import id.unware.poken.models.UIState
import id.unware.poken.tools.Constants
import id.unware.poken.tools.MyLog
import id.unware.poken.tools.Utils
import id.unware.poken.tools.glide.GlideApp
import id.unware.poken.tools.glide.GlideRequests
import id.unware.poken.ui.product.detail.view.ProductDetailActivity
import id.unware.poken.ui.store.summary.model.StoreSummaryModel
import id.unware.poken.ui.store.summary.presenter.StoreSummaryPresenter
import id.unware.poken.ui.store.summary.view.adapter.StoreSummaryLatestProductAdapter
import kotlinx.android.synthetic.main.fragment_store_summary.*


class StoreSummaryFragment : Fragment(), IStoreSummaryView {

    private var TAG: String = "StoreSummaryFragment"

    private var mParam1: String? = null
    private var mParam2: String? = null

    private lateinit var glideRequest: GlideRequests

    private var mListener: OnFragmentInteractionListener? = null

    private var presenter: StoreSummaryPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // This fragment has option menu
        setHasOptionsMenu(true)
        activity.title = getString(R.string.title_store_summary)

        val view = inflater!!.inflate(R.layout.fragment_store_summary, container, false)
        // Inflate the layout for this fragment
        presenter = StoreSummaryPresenter(StoreSummaryModel(), this)
        glideRequest = GlideApp.with(this)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.loadStoreSummary()

        initView()
    }

    private fun initView() {

        srlStoreSummary.setOnRefreshListener {
            presenter?.loadStoreSummary()
        }

        btnStoreSummaryAllProducts.setOnClickListener {
            mListener?.openAllProductListScreen()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Utils.Logs('i', "$TAG - Activity result. Req: $requestCode result code: $resultCode, data: $data")

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.TAG_STORE_SUMMARY) {
                presenter?.loadStoreSummary()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater?.inflate(R.menu.menu_store_summary, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_notification -> openNotification()
            android.R.id.home -> activity.onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun openNotification() {

        MyLog.FabricLog(Log.INFO, "Open notification screen")


    }

    override fun populateLatestProducts(products: List<Product>) {
        Utils.Log(TAG, "Latest product size: " + products.size)
        ViewCompat.setNestedScrollingEnabled(rvStoreSummaryLatestProduct, false);
        rvStoreSummaryLatestProduct.layoutManager = GridLayoutManager(context, 3)
        rvStoreSummaryLatestProduct.adapter = StoreSummaryLatestProductAdapter(products, presenter, glideRequest)
    }

    override fun showProductDetail(product: Product) {
        val productDetailIntent = Intent(context, ProductDetailActivity::class.java)
        productDetailIntent.putExtra(Product.KEY_PRODUCT_ID, product.id)
        productDetailIntent.putExtra(Constants.EXTRA_PRODUCT_DETAIL_IS_EDIT, true)
        startActivity(productDetailIntent)
    }

    /** User not registered as Seller */
    override fun showSellerNotRegisteredStatus() {
        mListener?.showSellerNotRegisteredView()
    }

    override fun showStoreInfo(storeData: Seller) {

        Utils.Log(TAG, "Store info: $storeData")

        tvStoreSummaryStoreName?.text = storeData.store_name

        glideRequest.asDrawable()
                .clone()
                .load(storeData.store_avatar)
                .error(R.drawable.ic_store_black_24dp)
                .placeholder(R.drawable.ic_circle_24dp)
                .circleCrop()
                .into(ivStoreSummaryStoreAvatar)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun showTotalAmount(totalAmount: String) {
        Utils.Log(TAG, "Total amount: $totalAmount")
        tvStoreSummaryCreditAmount?.text = totalAmount
    }

    override fun populateSellerPromo(promos: List<SellerPromo>) {

        for (item in promos) {
            val sliderView = DefaultSliderView(this.context)
            sliderView.image(item.thumbnail)
            sliderView.error(R.drawable.ic_image_black_24dp)
            sliderView.bundle(Bundle())
            sliderView.bundle.putParcelable(Constants.EXTRA_DOMAIN_PARCELABLE_DATA, item)
            sliderView.setOnSliderClickListener { slider ->
                val featured = slider.bundle.getParcelable<SellerPromo>(Constants.EXTRA_DOMAIN_PARCELABLE_DATA)
                Utils.Logs('i', "Clicked slide item: $featured")
            }
            slStoreSummaryPromotion.addSlider(sliderView)
        }

        slStoreSummaryPromotion.setCustomIndicator(piStoreSummarySectionHeaderCustomIndicator)

    }

    override fun showViewState(uiState: UIState?) {
        if (uiState == UIState.LOADING) {
            srlStoreSummary?.isRefreshing = true
        } else if (uiState == UIState.FINISHED) {
            Utils.Log(TAG, "Finished")
            srlStoreSummary?.isRefreshing = false
        } else if (uiState == UIState.ERROR) {
            srlStoreSummary?.isRefreshing = false
        }
    }

    override fun isActivityFinishing(): Boolean {
        // 'or' operator still check other
        return isDetached || activity.isFinishing
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
        fun openAllProductListScreen()
        fun showSellerNotRegisteredView()
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        fun newInstance(param1: String, param2: String): StoreSummaryFragment {
            val fragment = StoreSummaryFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}
