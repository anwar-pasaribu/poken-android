package id.unware.poken.ui.wallet.addBankAccount.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.unware.poken.BuildConfig;
import id.unware.poken.R;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoBank;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.wallet.addBankAccount.model.DialogBankAccountModel;
import id.unware.poken.ui.wallet.addBankAccount.presenter.DialogBankAccountPresenter;
import id.unware.poken.ui.wallet.addBankAccount.presenter.IDialogBankAccountPresenter;


public class FragmentDialogBankAccount extends DialogFragment implements
        IDialogBankAccountView {

    private final String TAG = "FragmentDialogBankAccount";

    @BindView(R.id.textViewTitle) TextView textViewTitle;
    @BindView(R.id.spinnerBanks) Spinner spinnerBanks;
    @BindView(R.id.editTextAccountName) EditText editTextAccountName;
    @BindView(R.id.editTextAccountNumber) EditText editTextAccountNumber;
    @BindView(R.id.editTextPaketIdPassword) EditText editTextPaketIdPassword;
    @BindView(R.id.parentView) ScrollView parentView;

    private IDialogBankAccountPresenter presenter;

    private ArrayList<PojoBank> listBank;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listBankString;

    public FragmentDialogBankAccount() {

    }

    public static FragmentDialogBankAccount newInstance(ArrayList<PojoBank> pojoBanks) {
        FragmentDialogBankAccount fragment = new FragmentDialogBankAccount();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.EXTRA_WITHDRAWAL_BANK_LIST, pojoBanks);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.fragment_bank_account, null, false);
        ButterKnife.bind(this, view);
        presenter = new DialogBankAccountPresenter(this, new DialogBankAccountModel());
        initView();

        return view;
    }

    private void initView() {

        if (getDialog() != null && getDialog().getWindow() != null) {
            // request a window without the title
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            getDialog().setCancelable(true);
        }

        setupSpinnerBankList();
        presenter.getBankList();

    }

    private void setupSpinnerBankList() {
        listBank = new ArrayList<>();
        listBankString = new ArrayList<>();
        listBankString.add("coba");
        listBankString.add("coba");
        listBankString.add("coba");
        listBankString.add("coba");
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listBankString);
        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setNotifyOnChange(true);
        spinnerBanks.setAdapter(adapter);
    }

    @OnClick({R.id.buttonForgetPassword, R.id.buttonCreate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonForgetPassword:
                openForgotPassword();
                break;
            case R.id.buttonCreate:
                presenter.createBankAccount();
                break;
        }
    }

    private void openForgotPassword() {
        Utils.Log(TAG, "openForgotPassword.");
        Utils.openInBrowser(getActivity(), BuildConfig.FORGET_PASSWORD_URL);
    }

    private void closeDialog() {
        this.dismiss();
    }


    /**
     * Prevent dialog to show it's native title.
     *
     * @param savedInstanceState : Default bundle data.
     * @return Dialog which gonna be displayed.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Utils.Log(TAG, "On create dialog.");

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        dialog.setCancelable(true);
        return dialog;
    }

    @Override
    public void onStop() {
        Utils.Log(TAG, "On stop FragmentDialogArea");

        super.onStop();
    }

    @Override
    public Bundle getMyArguments() {
        return getArguments();
    }

    @Override
    public void setupBankList(ArrayList<PojoBank> listBank) {
        this.listBank.clear();
        this.listBank.addAll(listBank);
    }

    @Override
    public void setupBankListString() {
        listBankString.clear();
        for (PojoBank pojoBank : listBank) {
            listBankString.add(pojoBank.getBankName());
        }
    }

    @Override
    public void refreshBankList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public String getBankId() {
        return listBank.get(spinnerBanks.getSelectedItemPosition()).getBankId();
    }

    @Override
    public String getUserBankAccountName() {
        return editTextAccountName.getText().toString();
    }

    @Override
    public String getUserBankAccountNumber() {
        return editTextAccountNumber.getText().toString();
    }

    @Override
    public String getPassword() {
        return editTextPaketIdPassword.getText().toString();
    }

    @Override
    public View getParentView() {
        return parentView;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void successAddBank() {
        closeDialog();
        listener.onSuccessAddBank();
    }

    @Override
    public void dismissLoading() {

    }

    private DialogBankAccountListener listener;

    public void setListener(DialogBankAccountListener listener) {
        this.listener = listener;
    }

    public interface DialogBankAccountListener{
        void onSuccessAddBank();
    }
}
