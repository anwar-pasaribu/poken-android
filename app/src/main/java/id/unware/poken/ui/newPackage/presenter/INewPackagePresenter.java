package id.unware.poken.ui.newPackage.presenter;

/**
 * Presenter for View to insteract with.
 *
 * @author Anwar Pasaribu
 * @since Feb 07 2017
 */

public interface INewPackagePresenter {

    void requestPackageServices();

    void requestAddressBook(boolean isForSender);

    void bookNewPackage();
}
