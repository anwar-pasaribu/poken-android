package id.unware.poken.pojo;

/**
 * @author Anwar Pasaribu
 * @since Dec 22 2016
 */

public enum ListType {
    PACKAGE_HEADER_REQUEST_PICKUP  (-128),
    PACKAGE_FOOTER_LOAD_MORE_ITEM_ID  (-127);

    private int intVal;


    ListType(int i) {
        this.intVal = i;
    }

    public int getIntVal() {
        return intVal;
    }
}
