package id.unware.poken.ui.Tariff.TariffMain.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import id.unware.poken.R;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.pojo.PojoRateItem;
import id.unware.poken.pojo.PojoTariffCheck;
import id.unware.poken.tools.MyLog;
import id.unware.poken.ui.Tariff.TariffMain.model.IRateModel;
import id.unware.poken.ui.Tariff.TariffMain.view.RateActivity;
import retrofit2.Response;

/**
 * Created by marzellaalfamega on 3/21/17.
 */

public class RatePresenter extends MyCallback implements IRatePresenter {

    private RateActivity view;
    private IRateModel model;

    public RatePresenter(RateActivity view, IRateModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void checkTariff() {
        MyLog.FabricLog(Log.INFO, "Rate checker started");
        view.showProgressDialog();
        model.checkTariff(view.getOrigin(), view.getDest(), view.getWeight(), this);
    }

    @Override
    public void onSuccess(Response response) {

        PojoTariffCheck data = (PojoTariffCheck) response.body();
        MyLog.FabricLog(Log.INFO, "Rate checker success with tariff data: " + ((data == null) ? "NULL" : "DATA AVAILABLE"));

        if (data == null) return;

        // Detect when no delivery provided by check when
        // Tariff is zero
        List<PojoRateItem> listRateItem = new ArrayList<>();
        for (PojoRateItem rateItem : data.getTariff()) {
            if (rateItem.getTariff() != 0) {
                listRateItem.add(rateItem);
            }
        }

        if (listRateItem.isEmpty()) {
            // Provide empty message
            listRateItem.clear();
            String strMessage;

            // Display message from server
            if (data.getMsg() != null && data.getMsg().length() > 0) {
                strMessage = data.getMsg();
            } else {
                // Display no-delivery message
                strMessage = view
                        .getString(
                                R.string.lbl_rate_no_delivery,
                                view.getOrigin(),
                                view.getDest());
            }

            view.setListAdapter(listRateItem);
            view.setEmptyRate(strMessage);


        } else {
            view.setEmptyRate(view.getString(R.string.lbl_empty_string));
//            ((TextView) findViewById(R.id.tvEmptyRate)).setText();
            view.setListAdapter(listRateItem);
//            mListView.setAdapter(new RateAdapter(RateActivity.this, listRateItem));
        }
    }

    @Override
    public void onMessage(String msg, int status) {
        view.showMessage(msg);
    }

    @Override
    public void onFinish() {
        view.dismissProgressDialog();
    }
}
