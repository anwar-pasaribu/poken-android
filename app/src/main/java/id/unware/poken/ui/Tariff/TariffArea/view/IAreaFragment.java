package id.unware.poken.ui.Tariff.TariffArea.view;

import java.util.List;

import id.unware.poken.pojo.PojoArea;
import id.unware.poken.ui.Tariff.TariffArea.view.adapter.AreaAdapter;

/**
 * Created by marzellaalfamega on 3/21/17.
 */

public interface IAreaFragment {
    String getQuery();

    void showMessage(String msg);

    void prepareList();

    void clearList();

    boolean getIsFromMoreListItem();

    AreaAdapter getAreaAdapter();

    void addToList(PojoArea data);

    void setFromMoreListItem(boolean b);

    void addAllToList(List<PojoArea> data);

    void refreshList();
}
