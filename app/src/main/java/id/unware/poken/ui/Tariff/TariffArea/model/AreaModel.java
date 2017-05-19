package id.unware.poken.ui.Tariff.TariffArea.model;

import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.TikiRequest;

/**
 * Created by marzellaalfamega on 3/21/17.
 */

public class AreaModel implements IAreaModel {

    private TikiRequest req;

    public AreaModel() {
        req = AdRetrofit.getInstanceTiki().create(TikiRequest.class);
    }

    @Override
    public void searchArea(String query, MyCallback callback) {
        req.searchArea(query).enqueue(callback);
    }
}
