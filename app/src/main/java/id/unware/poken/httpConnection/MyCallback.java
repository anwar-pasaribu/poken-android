package id.unware.poken.httpConnection;

import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Poken req callback
 *
 */
public abstract class MyCallback implements retrofit2.Callback {

    private static final String TAG = "MyCallback";

    @Override
    public void onResponse(Call call, Response response) {
        Utils.Log("MyCallback", "Network response code: " + response.code());

        if (response.isSuccessful()) {
            onSuccess(response);
        } else {
            Utils.Logs('w', TAG, "Response body: " + String.valueOf(response.body()));
            Utils.Logs('w', TAG, "Raw response body: " + String.valueOf(response));
        }

        onFinish();
    }

    @Override
    public void onFailure(Call call, Throwable t) {

        Utils.Log(TAG, "response failed. Cause: " + String.valueOf(t.getMessage()));
        Utils.Log(TAG, "response failed. Cause: " + String.valueOf(call));
        onMessage(t.getMessage(), Constants.NETWORK_CALLBACK_FAILURE);
        onFinish();

        t.printStackTrace();
    }

    public abstract void onSuccess(Response response);

    public abstract void onMessage(String msg, int status);

    public abstract void onFinish();
}
