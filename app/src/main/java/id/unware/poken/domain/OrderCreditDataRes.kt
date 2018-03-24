package id.unware.poken.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by Anwar Pasaribu on 3/16/2018.
 * Store income per Order.
 */
class OrderCreditDataRes(
        @SerializedName("results")
        val results: ArrayList<OrderCredit>) : PokenApiBase() {
}