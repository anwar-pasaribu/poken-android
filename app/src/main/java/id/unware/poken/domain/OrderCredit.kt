package id.unware.poken.domain

import java.util.*

/**
 * Created by Anwar Pasaribu on 3/16/2018.
 * Store income per Order.
 */
class OrderCredit(
        val id: Long,
        val order_details: String,
        val order_date: Date,
        val order_total_credits: Double,
        val order_total_ordered_item: Int) {

    override fun toString(): String {
        return "OrderCredit(id=$id, order_details='$order_details', order_date=$order_date, order_total_credits=$order_total_credits, order_total_ordered_item=$order_total_ordered_item)"
    }
}