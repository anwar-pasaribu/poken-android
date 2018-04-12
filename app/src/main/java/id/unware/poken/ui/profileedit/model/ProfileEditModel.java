package id.unware.poken.ui.profileedit.model;

import java.util.HashMap;

import id.unware.poken.connections.AdRetrofit;
import id.unware.poken.connections.MyCallback;
import id.unware.poken.connections.PokenRequest;
import id.unware.poken.domain.Customer;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.ui.profileedit.presenter.IProfileEditModelPresenter;
import retrofit2.Response;

/**
 * Created by PID-T420S on 10/13/2017.
 */
public class ProfileEditModel extends MyCallback implements IProfileEditModel {

    private static final String TAG = "RegisterEmailModel";

    final private PokenRequest req;
    private IProfileEditModelPresenter presenter;

    private Customer pokenUser;

    public ProfileEditModel() {
        this.req = AdRetrofit.getInstancePoken().create(PokenRequest.class);
    }

    @Override
    public void patchProfileInfo(long customerId, Customer customer, IProfileEditModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.pokenUser = customer;

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        HashMap<String, String> postBody = new HashMap<>();
        postBody.put("related_user.first_name", customer.related_user.first_name);
        postBody.put("related_user.last_name", customer.related_user.last_name);
        postBody.put("related_user.email", customer.related_user.email);
        postBody.put("phone_number", customer.phone_number);

        req.patchCustomerProfile(
                customerId,
                PokenCredentials.getInstance().getCredentialHashMap(),
                postBody).enqueue(this);
    }

    @Override
    public void patchProfileInfo(long customerId, Customer customer, String currentPassword, String newPassword, IProfileEditModelPresenter presenter) {
        if (this.presenter == null) {
            this.presenter = presenter;
        }

        this.pokenUser = customer;

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        HashMap<String, String> postBody = new HashMap<>();
        postBody.put("related_user.first_name", customer.related_user.first_name);
        postBody.put("related_user.last_name", customer.related_user.last_name);
        postBody.put("related_user.email", customer.related_user.email);
        postBody.put("phone_number", customer.phone_number);
        postBody.put("current_password", currentPassword);
        postBody.put("new_password", newPassword);

        req.patchCustomerProfile(
                customerId,
                PokenCredentials.getInstance().getCredentialHashMap(),
                postBody).enqueue(this);
    }

    @Override
    public void getPokenCustomerDataById(IProfileEditModelPresenter presenter, String custIdentifier) {

        if (this.presenter == null) {
            this.presenter = presenter;
        }

        // Loading state to view
        this.presenter.updateViewState(UIState.LOADING);

        req.getCustomerData(custIdentifier)
                .enqueue(this);

    }

    @Override
    public void onSuccess(Response response) {

        if (response.body() instanceof Customer) {
            presenter.updateViewState(UIState.FINISHED);

            presenter.onEditProfileSuccess(((Customer) response.body()));

            PokenCredentials.getInstance().setPokenCustomer(((Customer) response.body()));
        }

    }

    @Override
    public void onMessage(String msg, int status) {
        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            presenter.updateViewState(UIState.ERROR);
        }

        presenter.startMessage(msg, status);
    }

    @Override
    public void onFinish() {

    }
}
