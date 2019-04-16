package id.unware.poken.ui.store.product.view

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import id.unware.poken.R
import id.unware.poken.domain.Category
import id.unware.poken.domain.Product
import id.unware.poken.models.UIState
import id.unware.poken.tools.Utils
import id.unware.poken.tools.glide.GlideApp
import id.unware.poken.tools.glide.GlideRequests
import id.unware.poken.ui.store.product.model.StoreProductListModel
import id.unware.poken.ui.store.product.presenter.IStoreProductListPresenter
import id.unware.poken.ui.store.product.presenter.StoreProductListPresenter
import kotlinx.android.synthetic.main.fragment_store_product.*


class StoreProductListFragment : Fragment(), IStoreProductListView {

    private val tagq = "StoreProductListFragment"

    private lateinit var glideRequest: GlideRequests

    private var presenter: IStoreProductListPresenter? = null

    private val listItem: ArrayList<Product> = ArrayList()
    private lateinit var adapter: StoreProductListAdapter

    private var mColumnCount = 3
    private var mListener: OnStoreProductListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments!!.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // This fragment has option menu
        setHasOptionsMenu(true)
        activity?.title = getString(R.string.title_store_product_list)

        val view = inflater!!.inflate(R.layout.fragment_store_product, container, false)

        glideRequest = GlideApp.with(this)
        presenter = StoreProductListPresenter(StoreProductListModel(), this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        presenter?.loadProductList()
    }

    private fun initView() {

        srlStoreProduct.setOnRefreshListener {
            presenter?.loadProductList()
        }

        adapter = StoreProductListAdapter(
                listItem,
                mListener,
                glideRequest.asDrawable().fitCenter()
        )
        rvStoreProduct.layoutManager = LinearLayoutManager(context)
        rvStoreProduct.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_store_product, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity?.onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnStoreProductListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnStoreProductListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun showViewState(uiState: UIState?) {
        Utils.Logs('i', "$tagq - View state: $uiState")
        if (uiState == UIState.LOADING) {
            showLoadingIndicator(true)
        } else if (uiState == UIState.FINISHED) {
            showLoadingIndicator(false)
        } else if (uiState == UIState.ERROR) {
            showLoadingIndicator(false)
        }
    }

    private fun showLoadingIndicator(isLoading: Boolean) {
        if (srlStoreProduct == null) {
            return
        }

        if (isLoading) {
            if (!srlStoreProduct.isRefreshing) {
                srlStoreProduct?.isRefreshing = true
            }
        } else {
            if (srlStoreProduct.isRefreshing) {
                srlStoreProduct?.isRefreshing = false
            }
        }
    }

    override fun isActivityFinishing(): Boolean {
        return activity == null || activity?.isFinishing!! || isDetached
    }

    override fun populateProductCategoryList(productCategories: ArrayList<Category>) {
        Utils.Logs('i', "$tagq - Product categories: ${productCategories.size}")
    }

    override fun populateProductList(products: ArrayList<Product>) {
        Utils.Logs('i', "$tagq - Product size: ${products.size}")
        listItem.clear()
        listItem.addAll(products)
        adapter.notifyDataSetChanged()
    }


    interface OnStoreProductListener {
        fun onListFragmentInteraction(item: Product)
    }

    companion object {

        private const val ARG_COLUMN_COUNT = "column-count"

        fun newInstance(columnCount: Int): StoreProductListFragment {
            val fragment = StoreProductListFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
