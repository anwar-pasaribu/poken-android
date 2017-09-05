package id.unware.poken.ui.pokenaccount.register.model;

import android.util.Base64;

import java.util.HashMap;

import id.unware.poken.domain.Customer;
import id.unware.poken.domain.User;
import id.unware.poken.httpConnection.AdRetrofit;
import id.unware.poken.httpConnection.MyCallback;
import id.unware.poken.httpConnection.PokenRequest;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.pokenaccount.register.presenter.IRegisterEmailModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Sep 01 2017
 */

public class RegisterEmailModel extends MyCallback implements IRegisterEmailModel {

    private static final String TAG = "RegisterEmailModel";

    final private PokenRequest req;
    private IRegisterEmailModelPresenter presenter;

    private User pokenUser;

    public RegisterEmailModel() {
        this.req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void postRegister(User user, IRegisterEmailModelPresenter presenter) {

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.pokenUser = user;

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        HashMap<String, String> postBody = new HashMap<>();
        postBody.put("first_name", user.first_name);
        postBody.put("last_name", user.last_name);
        postBody.put("email", user.username);
        postBody.put("password", user.password);

        req.postPokenRegisterEmail(postBody).enqueue(this);

    }

    @Override
    public void getCustomerDataByToken(String userToken, IRegisterEmailModelPresenter presenter) {

    }

    @Override
    public void onSuccess(Response response) {
        Utils.Log(TAG, "Response: " + response.raw());

        Object o = response.body();

        if (o instanceof User) {
            User pokenUser = ((User) o);
            // Save poken user as logged in
            this.pokenUser.first_name = pokenUser.first_name;
            this.pokenUser.last_name = pokenUser.last_name;
            this.pokenUser.token = pokenUser.token;
            this.pokenUser.username = pokenUser.username;
            this.pokenUser.email = pokenUser.email;
            PokenCredentials.getInstance().setCredential(this.pokenUser);

            // Setup customer data but empty
            Customer newCust = new Customer();
            newCust.related_user = this.pokenUser;
            newCust.phone_number = "";
            newCust.location = null;

            PokenCredentials.getInstance().setPokenCustomer(newCust);

            Utils.Log(TAG, "User successfully register. Token: " + pokenUser.token);
            this.presenter.onRegisterSuccess(newCust);

        }
    }

    @Override
    public void onMessage(String msg, int status) {
        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            presenter.onRegisterError(msg, status);
        }
    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
