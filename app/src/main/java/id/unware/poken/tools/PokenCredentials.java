package id.unware.poken.tools;

import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import id.unware.poken.domain.User;
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

        String username = this.spHelper.getSharedPreferences(Constants.SP_AUTH_USERNAME, "");
        String password = this.spHelper.getSharedPreferences(Constants.SP_AUTH_PASSWORD, "");

        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return null;
        }

        String credential = Credentials.basic(username, password);
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", credential);

        return headerMap;
    }

    public void setCredential(User user) {

        String username, password, token;

        if (user != null) {
            username = user.username;
            password = user.password;
            token = user.token;
        } else {
            username = "";
            password = "";
            token = "";
        }

        this.spHelper.setPreferences(Constants.SP_AUTH_USERNAME, username);
        this.spHelper.setPreferences(Constants.SP_AUTH_PASSWORD, password);
        this.spHelper.setPreferences(Constants.SP_AUTH_TOKEN, token);
    }
}
