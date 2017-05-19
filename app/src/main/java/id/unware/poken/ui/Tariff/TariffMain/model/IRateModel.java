package id.unware.poken.ui.Tariff.TariffMain.model;

import id.unware.poken.httpConnection.MyCallback;

/**
 * Created by marzellaalfamega on 3/21/17.
 */

public interface IRateModel {
    void checkTariff(String origin, String dest, String weight, MyCallback callback);
}
