package id.unware.poken.ui.pokenaccount.login.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.BuildConfig;
import id.unware.poken.R;
import id.unware.poken.domain.User;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
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

    @BindView(R.id.loginTvTitle) TextView loginTvTitle;
    @BindView(R.id.txtEmailLogin) AppCompatEditText txtEmailLogin;
    @BindView(R.id.txtPasswordLogin) AppCompatEditText txtPasswordLogin;
    @BindView(R.id.btnSignIn) Button btnSignIn;
    @BindView(R.id.loginBtnOpenRegisterEmail) Button loginBtnOpenRegisterEmail;
    @BindView(R.id.parentView) FrameLayout parentView;

    // RESOURCE
    @BindString(R.string.lbl_login_title) String strDefaultText;
    @BindColor(R.color.red) int colorRed;
    @BindColor(R.color.colorPrimaryDark) int colorPrimaryDark;

    private Unbinder unbinder;

    private LoginPresenter presenter;

    private PokenLoginListener pokenLoginListener;

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            prepareLogin(s);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ac_login_email, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter = new LoginPresenter(new LoginModel(), this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        txtEmailLogin.addTextChangedListener(loginTextWatcher);
        txtPasswordLogin.addTextChangedListener(loginTextWatcher);
    }

    private void initView() {
        Utils.Log(TAG, "Init view");

        txtPasswordLogin.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    beginSignIn();
                    return true;
                }
                return false;
            }
        });

        // Initially set sign button disabled
        btnSignIn.setEnabled(false);

        // Button Sign In
        btnSignIn.setOnClickListener(this);
        // Button Register
        loginBtnOpenRegisterEmail.setOnClickListener(this);

    }

    private void prepareLogin(Editable s) {
        if (s.hashCode() == txtEmailLogin.getText().hashCode()) {
            Utils.Log(TAG, "Typing on email: " + String.valueOf(s));
        } else if (s.hashCode() == txtPasswordLogin.hashCode()) {
            Utils.Log(TAG, "Typing on password: " + String.valueOf(s));
        }

        if (!StringUtils.isEmpty(String.valueOf(txtEmailLogin.getText()))
                && !StringUtils.isEmpty(String.valueOf(txtPasswordLogin.getText()))) {
            btnSignIn.setEnabled(true);
        } else {
            btnSignIn.setEnabled(false);
        }

        showMessage("", 0);
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
            case R.id.loginBtnOpenRegisterEmail:
                registerAccount();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoginSuccess() {
        Utils.Log(TAG, "Login success.");

        // Track user successfully logged in
        MyLog.FabricTrackLogin("Email Login", true);

        try {
            pokenLoginListener.onLoginSuccess();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    @Override
    public void showMessage(String msg, int status) {
        Utils.Log(TAG, "Show message: " + msg + ", status: " + status);
        loginTvTitle.setText(msg);

        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            loginTvTitle.setTextColor(colorRed);
        } else {
            loginTvTitle.setText(strDefaultText);
            loginTvTitle.setTextColor(colorPrimaryDark);
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
