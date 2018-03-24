package id.unware.poken.ui.store.credits.view

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.unware.poken.R
import id.unware.poken.controller.ControllerDate
import id.unware.poken.domain.OrderCredit
import id.unware.poken.tools.StringUtils
import id.unware.poken.tools.glide.GlideRequest
import kotlinx.android.synthetic.main.list_store_credit_item.view.*


class StoreCreditsAdapter(
        private val mValues: List<OrderCredit>,
        private val mListener: StoreCreditsFragment.OnStoreCreditListener?,
        @Suppress("unused") private val requestBuilder: GlideRequest<Drawable>
    ) : RecyclerView.Adapter<StoreCreditsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_store_credit_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]

        mValues[position].let { credit ->
            holder.bind(credit)
        }

//        this.requestBuilder
//                .clone()
//                .load(productImageUrl)
//                .error(R.drawable.ic_store_black_24dp)
//                .placeholder(R.drawable.ic_circle_24dp)
//                .circleCrop()
//                .into(holder.ivStoreProductImage)

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(private val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: OrderCredit? = null

        fun bind(credit: OrderCredit) {
            val strDate = ControllerDate.getInstance().getShortDateWithHourFormat(credit.order_date)

            mView.tvStoreCreditOrderIndex.text = (adapterPosition + 1).toString()
            mView.tvStoreCreditOrderDate.text = strDate
            mView.tvStoreCreditName.text = credit.order_details
            mView.tvStoreCreditAmount.text = StringUtils.formatCurrency(credit.order_total_credits)

            mView.setOnClickListener {
                mListener?.onCreditItemInteraction(credit)
            }
        }

        override fun toString(): String {
            return super.toString() + " '" + mItem + "'"
        }
    }
}
