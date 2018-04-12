package id.unware.poken.ui.profileedit.presenter;

import id.unware.poken.domain.Customer;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.ui.profileedit.model.IProfileEditModel;
import id.unware.poken.ui.profileedit.view.IProfileEditView;

/**
 * Created by PID-T420S on 10/13/2017.
 * Edit Profil.
 */
public class ProfileEditPresenter implements IProfileEditPresenter, IProfileEditModelPresenter {

    private IProfileEditModel model;
    private IProfileEditView view;

    private boolean isEditMode = false;


    public ProfileEditPresenter(IProfileEditModel model, IProfileEditView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void startEditProfile(long currentCustId, Customer customer) {
        this.isEditMode = true;
        model.patchProfileInfo(currentCustId, customer, this);
    }

    @Override
    public void loadPokenCustomerInfo(String currentCustId) {
        this.isEditMode = false;
        model.getPokenCustomerDataById(this, currentCustId);
    }

    @Override
    public void startEditProfilePassword(long custId, Customer customerData, String currentPassword, String newPassword) {
        this.isEditMode = true;

        model.patchProfileInfo(custId, customerData, currentPassword, newPassword, this);
    }

    @Override
    public void updateViewState(UIState uiState) {

        if (view.isActivityFinishing()) return;

        view.showViewState(uiState);
    }

    @Override
    public void onEditProfileSuccess(Customer customer) {

        if (view.isActivityFinishing()) return;

        view.showCustomerData(customer);

        if (isEditMode) {
            view.showMessage(Constants.STATE_FINISHED, "Berhasil mengubah data.");
        }
    }

    @Override
    public void startMessage(String msg, int msgState) {

        if (view.isActivityFinishing()) return;

        view.showMessage(msgState, msg);
    }
}
