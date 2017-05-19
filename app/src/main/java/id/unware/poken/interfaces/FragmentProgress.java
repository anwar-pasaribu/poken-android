package id.unware.poken.interfaces;

/**
 * Interface to give Fragment comunication with host Activity in case to show progress.
 *
 * @author Anwar Pasaribu
 * @since Nov 09 2016 - NEW!
 */

public interface FragmentProgress {

    // Trigger to show progress dialog that host Activity have.
    void showProgress(boolean isShow, Object progressTag);

}
