package id.unware.poken.ui.pokenaccount.login.model;

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
import id.unware.poken.ui.pokenaccount.login.presenter.ILoginModelPresenter;
import retrofit2.Response;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public class LoginModel extends MyCallback implements ILoginModel{

    private static final String TAG = "LoginModel";

    final private PokenRequest req;
    private ILoginModelPresenter presenter;

    private User pokenUser;

    public LoginModel() {
        this.req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void postLogin(User user, ILoginModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.pokenUser = user;

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        HashMap<String, String> postBody = new HashMap<>();
        postBody.put("username", user.username);
        postBody.put("password", user.password);

        req.postPokenLogin(postBody).enqueue(this);


    }

    @Override
    public void getCustomerDataByToken(String userToken, ILoginModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        req.getCustomerData(userToken).enqueue(this);
    }

    @Override
    public void onSuccess(Response response) {

        Object o = response.body();

        Utils.Log(TAG, "Response: " + response.raw());

        if (o instanceof User) {

            User pokenUser = ((User) o);
            Utils.Log(TAG, "Poken user token: " + pokenUser.token);
            presenter.onLoginTokenResponse(pokenUser.token);

            // Save poken user as logged in
            this.pokenUser.token = pokenUser.token;
            this.pokenUser.email = ""; // poken_rest/api-token-auth/ not return email
            PokenCredentials.getInstance().setCredential(this.pokenUser);

        } else if (o instanceof Customer) {

            // Second step on login process
            presenter.updateViewState(UIState.FINISHED);

            Customer customer = (Customer) o;
            PokenCredentials.getInstance().setPokenCustomer(customer);
            presenter.onLoginSuccess(customer);
        }
    }

    @Override
    public void onMessage(String msg, int status) {
        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            presenter.onLoginError(msg, status);
        }
    }

    @Override
    public void onFinish() {
        presenter.updateViewState(UIState.FINISHED);
    }
}
