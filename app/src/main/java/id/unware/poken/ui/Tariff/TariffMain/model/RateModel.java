package id.unware.poken.ui.Tariff.TariffMain.model;

import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.TikiRequest;

/**
 * Created by marzellaalfamega on 3/21/17.
 */

public class RateModel implements IRateModel {

    private TikiRequest req;

    public RateModel() {
        req = AdRetrofit.getInstanceTiki().create(TikiRequest.class);
    }

    @Override
    public void checkTariff(String origin, String dest, String weight, MyCallback callback) {
        req.checkTariff(origin, dest, weight).enqueue(callback);
    }
}
