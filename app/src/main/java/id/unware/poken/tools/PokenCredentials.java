package id.unware.poken.tools;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import id.unware.poken.helper.SPHelper;
import okhttp3.Credentials;

/**
 * @author Anwar Pasaribu
 * @since Jun 20 2017
 */

public class PokenCredentials {
    private SPHelper spHelper;

    public static PokenCredentials getInstance() {
        return new PokenCredentials();
    }

    public PokenCredentials() {
        this.spHelper = SPHelper.getInstance();
    }

    @Nullable
    public Map<String, String> getCredentialHashMap() {

        String credential = Credentials.basic("anwar", "anwar_poken17");
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", credential);

        return headerMap;
    }
}
