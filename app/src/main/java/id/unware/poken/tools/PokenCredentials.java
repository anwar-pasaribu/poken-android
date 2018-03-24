package id.unware.poken.tools;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import id.unware.poken.PokenApp;
import id.unware.poken.domain.Customer;
import id.unware.poken.domain.PokenApiBase;
import id.unware.poken.domain.User;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.ui.pokenaccount.LoginActivity;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.reactivex.CompletableOnSubscribe;
import okhttp3.Credentials;

import static id.unware.poken.R.string.email;

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

        String username, password, email, token;

        if (user != null) {
            username = String.valueOf(user.username);
            password = String.valueOf(user.password);
            email = String.valueOf(user.email);
            token = String.valueOf(user.token);

        } else {
            username = "";
            password = "";
            email  = "";
            token = "";

            // Clear all data when null is provided
            SPHelper.getInstance().clearData();

            MyLog.FabricLog(Log.INFO, "Logout proceeded.");
        }

        this.spHelper.setPreferences(Constants.SP_AUTH_USERNAME, username);
        this.spHelper.setPreferences(Constants.SP_AUTH_PASSWORD, password);
        this.spHelper.setPreferences(Constants.SP_AUTH_EMAIL, email);
        this.spHelper.setPreferences(Constants.SP_AUTH_TOKEN, token);

        MyLog.FabricSetUserInformation();
    }

    public void setPokenCustomer(Customer customer) {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String customerStringData = gson.toJson(customer, Customer.class);
        Utils.Log("PokenCredentials", "Customer data: " + customerStringData);
        this.spHelper.setPreferences(Constants.SP_AUTH_CUSTOMER_DATA, customerStringData);

        if (customer.related_user != null) {
            this.spHelper.setPreferences(Constants.SP_AUTH_EMAIL, String.valueOf(customer.related_user.email));
        }
    }

    public void setPokenCustomer(String customerStringData) {
        Utils.Log("PokenCredentials", "Customer data: " + customerStringData);
        this.spHelper.setPreferences(Constants.SP_AUTH_CUSTOMER_DATA, customerStringData);
    }
}
