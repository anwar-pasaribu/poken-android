package id.unware.poken.httpConnection;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import id.unware.poken.BuildConfig;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import io.realm.RealmObject;

/**
 * Created by Alfa on 4/26/2015.
 * Request network (Web Service)
 *
 * @since Nov 11 2016 - Add Fabric logging report.
 */
public class MyRequest<T> extends Request<T> {

    // Tag to initilize where (Java file) log happen.
    private final String TAG = "MyRequest";

    private final Response.Listener<T> mListener;
    private Gson gson;
    private Class<T> clazz;
    private Map<String, String> params;

    public MyRequest(String url, Class<T> clazz, Map<String, String> params, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        this.mListener = listener;
        this.clazz = clazz;
        this.params = params;
        this.gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

    protected void deliverResponse(T response) {
        this.mListener.onResponse(response);
    }

    protected Response<T> parseNetworkResponse(NetworkResponse response) {

        String json = "";

        try {

            // Get COOKIE from HTTP Header
            String cookieNew = "";
            int apacheHeaderSize = response.apacheHeaders.length;
            for (int i = 0; i < apacheHeaderSize; i++) {
                String key = response.apacheHeaders[i].getName();
                String value = response.apacheHeaders[i].getValue();

                if (value.contains(BuildConfig.session_key)) {
                    Utils.Log(TAG, "VOLLEY_HEADERFIX - " + key + ": " + value);
                    cookieNew = value;
                    break;
                }
            }

            String cookie = SPHelper.getInstance().getSharedPreferences(Constants.SHARED_COOKIE, "");
            if (!StringUtils.isEmpty(cookieNew)) {
                if (!cookie.equals(cookieNew) || StringUtils.isEmpty(cookie)) {
                    Utils.Log(TAG, "Cookie catch: " + cookieNew);
                    SPHelper.getInstance().setPreferences(Constants.SHARED_COOKIE, cookieNew);
                }
            }

            Utils.Log(TAG, "Result json response is here");

            json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            Utils.Logs('i', TAG, "Result JSON Size : " + json.length() + ", " + (2 * json.getBytes("utf8").length + 4 + 4 + 4) + "bytes.");
            Utils.Log(TAG, "JSON String : " + json);

            // Convert JSON string to java object
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));

        } catch (UnsupportedEncodingException e) {

            MyLog.FabricLog(Log.ERROR, "UnsupportedEncodingException occours when [parseNetworkResponse]", e);

            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {

            MyLog.FabricLog(Log.ERROR, "JsonSyntaxException occours when [parseNetworkResponse]. JSON String: " + json, e);

            return Response.error(new ParseError(e));
        } catch (Exception e) {

            MyLog.FabricLog(Log.ERROR, "Exception (REGULAR) occours when [parseNetworkResponse]", e);

            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<>();
        String cookie = SPHelper.getInstance().getSharedPreferences(Constants.SHARED_COOKIE, "");
        if (!StringUtils.isEmpty(cookie)) {
            Utils.Log(TAG, "Cookie post:" + cookie);
            params.put("Cookie", cookie);
        }

        params.put("Content-Type", "application/x-www-form-urlencoded");
        return params;
    }
}
