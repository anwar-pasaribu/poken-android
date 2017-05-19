package id.unware.poken.ui.nearby.view;

import java.util.List;

import id.unware.poken.pojo.PojoOfficeLocation;
import id.unware.poken.ui.view.BaseView;

/**
 * @author Anwar Pasaribu
 * @since Mar 22 2017
 */

public interface INearbyView extends BaseView {
    public void showNearbyBranches(List<PojoOfficeLocation> officeLocationList);
}
