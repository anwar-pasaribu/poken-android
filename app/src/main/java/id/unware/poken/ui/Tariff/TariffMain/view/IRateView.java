package id.unware.poken.ui.Tariff.TariffMain.view;

import java.util.List;

import id.unware.poken.pojo.PojoRateItem;

/**
 * Created by marzellaalfamega on 3/21/17.
 */

public interface IRateView {
    String getOrigin();

    String getDest();

    String getWeight();

    void setEmptyRate(String strMessage);

    void setListAdapter(List<PojoRateItem> listRateItem);

    void showMessage(String msg);
}
