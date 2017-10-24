package id.unware.poken.connections;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import id.unware.poken.domain.PokenApiBase;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Poken req callback <br/>
 * Handle SERVER Response.
 */
public abstract class MyCallback implements retrofit2.Callback {

    private static final String TAG = "MyCallback";

    @Override
    public void onResponse(Call call, Response response) {
        Utils.Log("MyCallback", "Network response code: " + response.code());
        Utils.Log("MyCallback", "Network response body: " + response.body());


        // HEADERS CONTENT
//        int headerSize = response.headers().size();
//        for (int i = 0; i < headerSize; i++) {
//            Utils.Logs('v', TAG, i + " - "
//                    .concat(response.headers().name(i))
//                    .concat(" : ")
//                    .concat(response.headers().value(i))
//            );
//        }

        if (response.isSuccessful()) {
            onSuccess(response);
            onFinish();
        } else {
            String errorMsg = "Request jaringan bermasalah.";
            try {
                String strErrorResponse = response.errorBody().string();
                Utils.Log("MyCallback", "Network response error body: " + strErrorResponse);
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                PokenApiBase apiBase = gson.fromJson(strErrorResponse, PokenApiBase.class);

                if (apiBase != null) {
                    try {
                        errorMsg = apiBase.detail != null
                                ? apiBase.detail
                                : apiBase.non_field_errors.length != 0
                                ? String.valueOf(apiBase.non_field_errors[0])
                                : errorMsg;
                    } catch (NullPointerException npe) {
                        npe.printStackTrace();
                        MyLog.FabricLog(Log.ERROR, "Error message from server.", npe);
                    }


                } else {
                    Utils.Logs('e', "MyCallback", "no poken api base");
                    errorMsg = response.code() + ": " + response.message();
                }
            } catch (IOException | com.google.gson.JsonSyntaxException e) {
                e.printStackTrace();
                errorMsg = e.getMessage();
            }

            Utils.Logs('w', TAG, "Response errorBody: " + String.valueOf(response.errorBody()));
            Utils.Logs('w', TAG, "Response body: " + String.valueOf(response.raw().toString()));
            Utils.Logs('w', TAG, "Raw response body: " + String.valueOf(response));

            MyLog.FabricLog(Log.ERROR, "Response failed. Body: " + String.valueOf(response.errorBody().toString()));

            this.onFinish();
            this.onMessage(errorMsg, Constants.NETWORK_CALLBACK_FAILURE);
        }
    }

    @Override
    public void onFailure(Call call, Throwable t) {

        Utils.Log(TAG, "response failed. Cause: " + String.valueOf(t.getMessage()));
        Utils.Log(TAG, "response failed. Cause: " + String.valueOf(call));
        onFinish();

        // Send message to client
        onMessage(t.getMessage(), Constants.NETWORK_CALLBACK_FAILURE);

        t.printStackTrace();
    }

    public abstract void onSuccess(Response response);

    /**
     * Message when network request failed. Message status <code>Constants.NETWORK_CALLBACK_FAILURE</code>
     * indicate server response failed.
     * @param msg Message to show.
     * @param status Response status.
     */
    public abstract void onMessage(String msg, int status);

    public abstract void onFinish();
}
