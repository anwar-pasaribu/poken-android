package id.unware.poken.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Dec 06 2016
 */

public class PojoBookingData extends PojoBase {
    
    @SerializedName("booking") private PojoBooking[] bookings;

    public PojoBooking[] getBookings() {
        return bookings;
    }

    public void setBookings(PojoBooking[] bookings) {
        this.bookings = bookings;
    }
}
