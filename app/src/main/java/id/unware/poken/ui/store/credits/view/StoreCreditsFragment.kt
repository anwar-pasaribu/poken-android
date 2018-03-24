package id.unware.poken.ui.store.credits.view

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import id.unware.poken.R
import id.unware.poken.domain.OrderCredit
import id.unware.poken.pojo.UIState
import id.unware.poken.tools.Utils
import id.unware.poken.tools.glide.GlideApp
import id.unware.poken.tools.glide.GlideRequests
import id.unware.poken.ui.browse.view.adapter.EndlessRecyclerViewScrollListener
import id.unware.poken.ui.store.credits.model.StoreCreditsModel
import id.unware.poken.ui.store.credits.presenter.StoreCreditsPresenter
import kotlinx.android.synthetic.main.fragment_store_credit.*
import kotlinx.android.synthetic.main.list_store_credit_item.*
import kotlinx.android.synthetic.main.list_store_credit_item.view.*


class StoreCreditsFragment : Fragment(), IStoreCreditsView {

    private val tagq = "StoreCreditsFragment"

    private lateinit var glideRequest: GlideRequests

    private var presenter: StoreCreditsPresenter? = null

    private val listItem: ArrayList<OrderCredit> = ArrayList()
    private lateinit var adapter: StoreCreditsAdapter

    private var mColumnCount = 3
    private var mListener: OnStoreCreditListener? = null

    // Store a member variable for the listener
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            mColumnCount = arguments.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // This fragment has option menu
        setHasOptionsMenu(true)
        activity.title = getString(R.string.title_store_credit_list)

        val view = inflater!!.inflate(R.layout.fragment_store_credit, container, false)

        glideRequest = GlideApp.with(this)
        presenter = StoreCreditsPresenter(StoreCreditsModel(), this)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        presenter?.loadStoreCreditList()
    }

    private fun initView() {

        // Init credit list header
        tvStoreCreditOrderIndex.text = "No"
        tvStoreCreditOrderIndex.setTypeface(null, Typeface.BOLD)

        tvStoreCreditOrderDate.text = "Tgl."
        tvStoreCreditOrderDate.setTypeface(null, Typeface.BOLD)

        tvStoreCreditName.text = "No. Order"
        tvStoreCreditName.setTypeface(null, Typeface.BOLD)

        tvStoreCreditAmount.text = "Rp"
        tvStoreCreditAmount.setTypeface(null, Typeface.BOLD)

        srlStoreCredits.setOnRefreshListener {
            presenter?.loadStoreCreditList()
        }

        adapter = StoreCreditsAdapter(
                listItem,
                mListener,
                glideRequest.asDrawable().fitCenter()
        )
        val linearLayoutManager = LinearLayoutManager(context)
        rvStoreCredit.layoutManager = linearLayoutManager

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page)
            }
        }
        // Adds the scroll listener to RecyclerView
        rvStoreCredit.addOnScrollListener(scrollListener)

        rvStoreCredit.adapter = adapter

    }

    private fun loadNextDataFromApi(nextPage: Int) {
        Utils.Logs('i', "$tagq - Next page: $nextPage")
        presenter?.loadNextStoreCreditList(nextPage)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_store_credit, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> activity.onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnStoreCreditListener) {
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
        } else {
            showLoadingIndicator(false)
        }
    }

    private fun showLoadingIndicator(isLoading: Boolean) {
        if (isLoading) {
            if (!srlStoreCredits.isRefreshing) {
                srlStoreCredits.isRefreshing = true
            }
        } else {
            if (srlStoreCredits.isRefreshing) {
                srlStoreCredits.isRefreshing = false
            }
        }
    }

    override fun isActivityFinishing(): Boolean {
        return activity == null || activity.isFinishing || isDetached
    }

    override fun populateStoreCreditList(credits: ArrayList<OrderCredit>) {
        Utils.Logs('i', "$tagq - Credit size: ${credits.size}")
        listItem.clear()
        listItem.addAll(credits)
        adapter.notifyDataSetChanged()
    }

    override fun populateMoreStoreCreditList(results: ArrayList<OrderCredit>) {
        val moreSellerSize = results.size
        val currectSellerListSize = listItem.size

        Utils.Logs('i', tagq, "More seller list size: $moreSellerSize")
        Utils.Logs('i', tagq, "Current seller size $currectSellerListSize")

        listItem.addAll(results)
        adapter.notifyItemRangeInserted(currectSellerListSize, moreSellerSize)

    }

    interface OnStoreCreditListener {
        fun onCreditItemInteraction(item: OrderCredit)
    }

    companion object {

        private const val ARG_COLUMN_COUNT = "column-count"

        fun newInstance(columnCount: Int): StoreCreditsFragment {
            val fragment = StoreCreditsFragment()
            val args = Bundle()
            args.putInt(ARG_COLUMN_COUNT, columnCount)
            fragment.arguments = args
            return fragment
        }
    }
}
