package id.unware.poken.ui.store.product.view

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import id.unware.poken.R
import id.unware.poken.domain.Product
import id.unware.poken.tools.glide.GlideRequest
import id.unware.poken.ui.store.product.view.StoreProductListFragment.OnStoreProductListener


class StoreProductListAdapter(
        private val mValues: List<Product>,
        private val mListener: OnStoreProductListener?,
        private val requestBuilder: GlideRequest<Drawable>
    ) : RecyclerView.Adapter<StoreProductListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_store_product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mItem = mValues[position]
        val productImageUrl = holder.mItem?.images?.get(0)?.thumbnail
        holder.tvStoreProductName.text = mValues[position].name
        holder.tvStoreProductStock.text = mValues[position].stock.toString()

        this.requestBuilder
                .clone()
                .load(productImageUrl)
                .error(R.drawable.ic_store_black_24dp)
                .placeholder(R.drawable.ic_circle_24dp)
                .circleCrop()
                .into(holder.ivStoreProductImage)

        holder.mView.setOnClickListener {
            mListener?.onListFragmentInteraction(holder.mItem!!)
        }

        if (mValues[position].is_ordered) {
            holder.ivStoreProductNotification.visibility = View.VISIBLE
        } else {
            holder.ivStoreProductNotification.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val ivStoreProductImage: ImageView = mView.findViewById(R.id.ivStoreProductImage) as ImageView
        val tvStoreProductName: TextView = mView.findViewById(R.id.tvStoreProductName) as TextView
        val tvStoreProductStock: TextView = mView.findViewById(R.id.tvStoreProductStock) as TextView
        val ivStoreProductNotification: ImageView = mView.findViewById(R.id.ivStoreProductNotification) as ImageView
        var mItem: Product? = null

        override fun toString(): String {
            return super.toString() + " '" + mItem + "'"
        }

    }
}
