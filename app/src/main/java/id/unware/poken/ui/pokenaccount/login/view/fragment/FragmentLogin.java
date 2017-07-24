package id.unware.poken.ui.pokenaccount.login.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.BuildConfig;
import id.unware.poken.PokenApp;
import id.unware.poken.R;
import id.unware.poken.domain.User;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.pokenaccount.login.model.LoginModel;
import id.unware.poken.ui.pokenaccount.login.presenter.LoginPresenter;
import id.unware.poken.ui.pokenaccount.login.view.ILoginView;


public class FragmentLogin extends BaseFragment implements
        View.OnClickListener,
        ILoginView {

    private static final String TAG = "FragmentLogin";

    @BindView(R.id.txtEmailLogin) AppCompatEditText txtEmailLogin;
    @BindView(R.id.txtPasswordLogin) AppCompatEditText txtPasswordLogin;
    @BindView(R.id.btnSignIn) Button btnSignIn;
    @BindView(R.id.btnRegister) Button btnRegister;
    @BindView(R.id.btnResetPassword) Button btnResetPassword;
    @BindView(R.id.parentView) FrameLayout parentView;

    private Unbinder unbinder;

    private LoginPresenter presenter;

    private final PokenApp values = PokenApp.getInstance();

    private PokenLoginListener pokenLoginListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ac_login, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter = new LoginPresenter(new LoginModel(), this);

        initView();

        return view;
    }


    private void initView() {
        Utils.Log(TAG, "Init view");

        // Fill checkNewPackage form with tester account in DEV_MODE
        if (BuildConfig.DEV_MODE) {
            txtEmailLogin.setText(BuildConfig.USER_EMAIL);
            txtPasswordLogin.setText(BuildConfig.USER_PASSWORD);
        }

        // Button Sign In
        btnSignIn.setOnClickListener(this);
        // Button Register
        btnRegister.setOnClickListener(this);
        // Reset password
        btnResetPassword.setOnClickListener(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PokenLoginListener) {
            pokenLoginListener = (PokenLoginListener) context;
        } else {
            throw new ClassCastException(context.toString() + " should implement PokenLoginListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        pokenLoginListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSignIn:
                beginSignIn();
                break;
            case R.id.btnRegister:
                registerAccount();
                break;
            case R.id.btnResetPassword:
                forgetPassword();
                break;
        }

    }

    private void registerAccount() {
        try {
            pokenLoginListener.onRegisterRequested();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    /**
     * Begin Sign In Process when all form is filled.
     */
    public void login() {

        try {
            Utils.hideKeyboardFrom(parent, txtPasswordLogin);

            User pokenUser = new User();
            pokenUser.username = String.valueOf(txtEmailLogin.getText());
            pokenUser.password = String.valueOf(txtPasswordLogin.getText());

            presenter.startLogin(pokenUser);

        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    /**
     * Validate checkNewPackage form in order to continue checkNewPackage
     */
    private boolean isLoginReady() {

        if (StringUtils.isEmpty(txtEmailLogin.getText().toString())) {
            Utils.snackBar(parentView, parent.getString(R.string.please_insert_your_email), Log.WARN);
            txtEmailLogin.requestFocus();
        } else if (StringUtils.isEmpty(txtPasswordLogin.getText().toString())) {
            Utils.snackBar(parentView, parent.getString(R.string.please_insert_password), Log.WARN);
            txtPasswordLogin.requestFocus();
        } else {
            return true;
        }

        return false;
    }


    /**
     * Begin Sign In when clicking on btnSignIn
     */
    public void beginSignIn() {

        if (isLoginReady()) {
            login();
        }
    }

    /**
     * Forget password feature by opening URL in browser.
     */
    private void forgetPassword() {
        Utils.openInBrowser(getActivity(), BuildConfig.FORGET_PASSWORD_URL);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        values.cancelPendingRequests(FragmentLogin.class);
        unbinder.unbind();
    }

    @Override
    public void onLoginSuccess() {
        Utils.Log(TAG, "Login success.");
        try {
            pokenLoginListener.onLoginSuccess();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                showLoadingState(true);
                break;
            case FINISHED:
                showLoadingState(false);
                break;
        }
    }

    private void showLoadingState(boolean isLoading) {
        if (isLoading) {
            btnSignIn.setEnabled(false);
        } else {
            btnSignIn.setEnabled(true);
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return parent == null || parent.isFinishing();
    }

    public interface PokenLoginListener {
        void onRegisterRequested();
        void onLoginSuccess();
    }


}
