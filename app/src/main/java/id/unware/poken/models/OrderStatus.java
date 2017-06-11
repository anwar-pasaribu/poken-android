package id.unware.poken.models;

/**
 * @author Anwar Pasaribu
 * @since Jun 11 2017
 */

public class OrderStatus {
    public static final int ORDERED = 0;
    public static final int PAID = 1;
    public static final int SENT = 2;
    public static final int RECEIVED = 3;
    public static final int SUCCESS = 4;
    public static final int REFUND = 5;

    public OrderStatus() {
    }

    public static CharSequence getOrderStatusText(int statusNumber) {
        switch (statusNumber) {
            case ORDERED:
                return "Dipesan";
            case PAID:
                return "Dibayar";
            case SENT:
                return "Dikirim";
            case RECEIVED:
                return "Diterima";
            case SUCCESS:
                return "Pembelian berhasil";
            case REFUND:
                return "Pengembalian barang";
        }

        return "";
    }
}
