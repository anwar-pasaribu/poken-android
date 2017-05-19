package id.unware.poken.ui.newPackage.view;

import android.widget.ScrollView;

import java.util.ArrayList;

import id.unware.poken.pojo.PojoAddressBook;
import id.unware.poken.pojo.PojoNewPackage;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Feb 07 2017
 */

public interface INewPackageView extends BaseView {

    void setParentView(ScrollView parentView);

    ScrollView getParentView();

    boolean isFormReady();

    PojoNewPackage getFormData();

    void populateAutoComplete(ArrayList<PojoAddressBook> pojoBookingArrayList);

    void initPackageServiceList(ArrayList<Object> packageServices);

    void saveLastSender(PojoNewPackage pojoNewPackage);

    void showAddressBook();

    void showNewPackageSummaryScreen(PojoNewPackage pojoNewPackage);

    void skipAllTutorial();

}
