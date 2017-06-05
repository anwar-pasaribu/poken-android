package id.unware.poken.httpConnection;

import id.unware.poken.tools.Utils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Poken req callback
 *
 */
public abstract class MyCallback implements retrofit2.Callback {

    @Override
    public void onResponse(Call call, Response response) {
        Utils.Log("MyCallback", "Network response code: " + response.code());

        if (response.isSuccessful()) {
            onSuccess(response);
        }

        onFinish();
    }

    @Override
    public void onFailure(Call call, Throwable t) {

        Utils.Log("failed", "response failed. Cause: " + String.valueOf(t.getMessage()));
        Utils.Log("failed", "response failed. Cause: " + String.valueOf(call));
        onMessage(t.getMessage());
        onFinish();

        t.printStackTrace();
    }

    public abstract void onSuccess(Response response);

    public abstract void onMessage(String msg);

    public abstract void onFinish();
}
