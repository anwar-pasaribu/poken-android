package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Nov 30 2016
 */

public class PojoHistoryData extends PojoBase {
    @SerializedName("booking_history") private PojoHistory[] bookingHistories;

    public PojoHistoryData() {
    }

    public PojoHistory[] getBookingHistories() {
        return bookingHistories;
    }

    public void setBookingHistories(PojoHistory[] bookingHistories) {
        this.bookingHistories = bookingHistories;
    }
}
