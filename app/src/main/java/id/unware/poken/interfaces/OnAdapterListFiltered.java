package id.unware.poken.interfaces;


import java.util.List;

/**
 * @author Anwar Pasaribu
 * @since Oct 29 2016
 */

public interface OnAdapterListFiltered<T> {
    void onFilterFinished(List<T> adapterList);
}
