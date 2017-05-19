package id.unware.poken.httpConnection;

import android.text.TextUtils;

import id.unware.poken.pojo.PojoBase;
import id.unware.poken.tools.Utils;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by marzellamega on 4/26/16.
 *
 */
public abstract class MyCallback implements retrofit2.Callback {

    @Override
    public void onResponse(Call call, Response response) {
        Utils.Log("success", "response success ");

        if (response.body() instanceof PojoBase) {

            PojoBase pojoBase = (PojoBase) response.body();

            if (pojoBase.success == 1) {
                onSuccess(response);
            }

            if (!TextUtils.isEmpty(pojoBase.msg)) {
                onMessage(pojoBase.msg);
            }

        }

        onFinish();
    }

    @Override
    public void onFailure(Call call, Throwable t) {

        Utils.Log("failed", "response failed. Cause: " + String.valueOf(t.getMessage()));
        Utils.Log("failed", "response failed. Cause: " + String.valueOf(call));
        onMessage(t.getMessage());
        onFinish();
    }

    public abstract void onSuccess(Response response);

    public abstract void onMessage(String msg);

    public abstract void onFinish();
}
