package id.unware.poken.pojo;

import android.support.annotation.NonNull;

/**
 * 0	Pending (Yellow)
 * -1	Cancelled (Red)
 *  1	Verified (Green)
 * -2	Expired (Red)
 * -3	Cancelled (Red)
 *
 * @author Anwar Pasaribu
 * @since Feb 07 2017
 */

public enum TransactionStatus {
    CANCELLED_ADMIN     (Status.CANCELLED_ADMIN),
    EXPIRED             (Status.EXPIRED),
    CANCELLED_USER      (Status.CANCELLED_USER),
    PENDING             (Status.PENDING),
    VERIFIED            (Status.VERIFIED);

    private final String statusVal;

    TransactionStatus(final String s) {
        this.statusVal = s;
    }

    public String getStrVal() {
        return statusVal;
    }

    /**
     * Get Integer representation of Status.
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

    @NonNull
    public static TransactionStatus fromString(String value) {
        for (TransactionStatus fname : TransactionStatus.values()) {
            if (fname.statusVal.equals(value)) {
                return fname;
            }
        }
        return TransactionStatus.PENDING;
    }

    private static class Status {
        public static final String CANCELLED_ADMIN = "-3";
        public static final String EXPIRED = "-2";
        public static final String CANCELLED_USER = "-1";
        public static final String PENDING = "0";
        public static final String VERIFIED = "1";
    }
}
