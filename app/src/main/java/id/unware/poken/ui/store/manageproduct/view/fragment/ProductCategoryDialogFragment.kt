package id.unware.poken.ui.store.manageproduct.view.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import id.unware.poken.R
import id.unware.poken.domain.Category
import id.unware.poken.tools.Constants
import kotlinx.android.synthetic.main.fragment_product_category_dialog.*
import kotlinx.android.synthetic.main.list_product_category.view.*
import android.view.animation.AnimationUtils



class ProductCategoryDialogFragment : BottomSheetDialogFragment() {

    private var listItem: ArrayList<Category> = ArrayList()

    private val handler: Handler = Handler()

    private var helperTextAnimationRunnable: Runnable = Runnable {
        // Create shake effect from xml resource
        val shake = AnimationUtils.loadAnimation(activity, R.anim.anim_shaking)
        // Perform animation
        tvproductCategoryChooserCloseHelper?.startAnimation(shake)
        tvproductCategoryChooserCloseHelper?.animate()?.setDuration(Constants.DURATION_SUPER_LONG.toLong())?.alpha(0F)
    }


    private var mListener: Listener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null)
        listItem = arguments!!.getParcelableArrayList(Constants.EXTRA_DOMAIN_PARCELABLE_DATA)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_category_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        ibClose.setOnClickListener { dismissAllowingStateLoss() }

        rvProductCategoryChooser.layoutManager = LinearLayoutManager(context)
        rvProductCategoryChooser.adapter = ItemAdapter(listItem)

        // SHAKE HOW TO DISMISS SHEET HELPER TEXT
        handler.postDelayed(helperTextAnimationRunnable, Constants.DURATION_SUPER_LONG.toLong())

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        mListener = if (parent != null) {
            parent as Listener
        } else {
            context as Listener
        }
    }

    override fun onDetach() {
        mListener = null
        handler.removeCallbacks(helperTextAnimationRunnable)
        super.onDetach()
    }

    interface Listener {
        fun onProductCategoryItemClicked(position: Int, category: Category)
    }

    private inner class ViewHolder internal constructor(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.list_product_category, parent, false)) {

        internal val text: TextView = itemView.text
        internal lateinit var itemVal: Category

        init {
            text.setOnClickListener {
                mListener?.let {
                    it.onProductCategoryItemClicked(adapterPosition, itemVal)
                    dismiss()
                }
            }
        }
    }

    private inner class ItemAdapter internal constructor(private var listItem: ArrayList<Category>) : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemVal = listItem[position]
            holder.text.text = listItem[position].name
        }

        override fun getItemCount(): Int {
            return listItem.size
        }
    }

    companion object {

        fun newInstance(productCategoryList: ArrayList<Category>): ProductCategoryDialogFragment =
                ProductCategoryDialogFragment().apply {
                    arguments = Bundle().apply {
                        putParcelableArrayList(Constants.EXTRA_DOMAIN_PARCELABLE_DATA, productCategoryList)
                    }
                }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        dialog.setOnShowListener { dialogOnShowListener ->
            val d = dialogOnShowListener as BottomSheetDialog

            val bottomSheet = d.findViewById<FrameLayout>(android.support.design.R.id.design_bottom_sheet)
            if (bottomSheet != null && context != null) {
                bottomSheet.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent))
                BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        dialog.setTitle("Pilih Kategory Produk")

        // Do something with your dialog like setContentView() or whatever
        return dialog
    }
}
