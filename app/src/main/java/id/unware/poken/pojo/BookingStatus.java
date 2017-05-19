package id.unware.poken.pojo;

/**
 * Package Booking Status. <br />
 * <pre>
 *  0    Booked
 *  1    Sent
 *  2    Delivered
 *  3    Picked
 * -1    Expired
 * -2    Void
 * -3    Returned
 * -4    Deleted
 * </pre>
 *
 * @author Anwar Pasaribu
 * @since (Sep 20, 2016) Version 41
 * @see PojoBooking
 */
public enum BookingStatus {
    DELETED     ("-4"),
    RETURNED    ("-3"),  // Visible
    VOID        ("-2"),  // Visible
    EXPIRED     ("-1"),
    BOOKED      ("0"),
    SENT        ("1"),
    DELIVERED   ("2"),
    PICKED      ("3");

    private final String statusVal;

    BookingStatus(final String s) {
        this.statusVal = s;
    }

    public String getStrVal() {
        return statusVal;
    }

    /**
     * Get Integer representation of Booking status.
     *
     * @return <code>int</code> Status number.
     */
    public int getIntVal() {
        try {
            return Integer.parseInt(statusVal);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            return 99;
        }
    }
}
