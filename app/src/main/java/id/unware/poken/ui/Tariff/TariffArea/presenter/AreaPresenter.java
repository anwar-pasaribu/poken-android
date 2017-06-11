package id.unware.poken.ui.Tariff.TariffArea.presenter;

import android.util.Log;

import java.util.List;

import id.unware.poken.R;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.pojo.PojoArea;
import id.unware.poken.pojo.PojoAreaData;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.Tariff.TariffArea.model.IAreaModel;
import id.unware.poken.ui.Tariff.TariffArea.view.AreaFragment;
import retrofit2.Response;

import static android.R.id.message;
import static android.content.ContentValues.TAG;

/**
 * Created by marzellaalfamega on 3/21/17.
 */

public class AreaPresenter extends MyCallback implements IAreaPresenter {

    private AreaFragment view;
    private IAreaModel model;

    public AreaPresenter(AreaFragment view, IAreaModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void searchArea() {
        if (view.getActivity() != null && view.isAdded()) {
            view.showLoadingIndicator(true);
        }
        model.searchArea(view.getQuery(),this);
    }

    @Override
    public void onSuccess(Response response) {
        if (view.getActivity() != null && view.isAdded()) {

            // Return when holder activity is finishing
            if (view.getActivity().isFinishing()) return;
            PojoAreaData pojoAreaData = (PojoAreaData) response.body();
            List<PojoArea> data = pojoAreaData.area;
            // Provide "No Data" state when no response from server.
            if (data.isEmpty()) {
                Utils.Log(TAG, "AREA DATA EMPTY");

                view.prepareList();

                view.clearList();

            } else if (view.getIsFromMoreListItem()) {

                // Append data into list

                if (view.getAreaAdapter() != null && view.getAreaAdapter().getItemCount() > 0) {
                    Utils.Log("TRIGGER", "Adapter sudah ada.");
//                    Utils.Log("TRIGGER", "Total occurencelist data: " + occurenceList.size());
//                    Utils.Log("TRIGGER", "Total adapter data: " + areaAdapter.getItemCount());

                    int headerDataPos = view.getAreaAdapter().getItemCount() - 1;

                    // Add server data to adapater
                    // when the data is not available offline.
                    for (PojoArea areaItem : data) {
                        if(!view.areaDataAlreadyAvailable(areaItem.daerah))
                            view.addToList(areaItem);
                    }

                    // Change "More" text to "District"
                    if (headerDataPos >= 0) {
                        Utils.Log("TRIGGER", "Header pos: " + headerDataPos);
                        view.getAreaAdapter().setHeaderText(headerDataPos, view.getString(R.string.lbl_rate_district));
                    } else {
                        Utils.Log("TRIGGER", "Masalah Header pos: " + headerDataPos);
                    }

                    view.clearList();

                    view.showLoadingIndicator(false);

                    // Set "isFromMoreListItem" to false
                    // in order to enable list items recreation
                    view.setFromMoreListItem(!view.getIsFromMoreListItem());
                }

            } else {
                view.addAllToList(data);
                view.refreshList();

            }
        }
    }

    @Override
    public void onMessage(String msg, int status) {
        MyLog.FabricLog(Log.ERROR, "Failed to rates check. Message : " + message);

        if (view.getActivity() != null && view.isAdded()) {

            if (view.getActivity().isFinishing()) return;

            // Show snackbar when request is failed
            // the snackbar contain failed string.
//            if (parent != null && !Util.isEmpty(message))
            view.showMessage(msg);
        }
    }

    @Override
    public void onFinish() {
        if (view.getActivity() != null && view.isAdded()) {

            if (view.getActivity().isFinishing()) return;

            view.showLoadingIndicator(false);

        }
    }
}
