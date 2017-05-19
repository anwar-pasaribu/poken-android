package id.unware.poken.interfaces;

/**
 * Interface to handle Realm process.
 *
 * @author Anwar Pasaribu
 * @since Oct 30 2016
 */

public interface OnRealmListener<T> {
    void onDatabaseProcessStarted(String msg);
    void onDatabaseProcessFinished(T dataResult);
    void onDatabaseProcessFailed(String msg);
}
