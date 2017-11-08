package id.unware.poken.ui.pokenaccount.register.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.User;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.pokenaccount.register.model.RegisterEmailModel;
import id.unware.poken.ui.pokenaccount.register.presenter.RegisterEmailPresenter;
import id.unware.poken.ui.pokenaccount.register.view.IRegisterEmailView;


public class FragmentRegisterEmail extends BaseFragment implements
        View.OnClickListener,
        IRegisterEmailView {

    private final String TAG = "FragmentRegisterEmail";

    @BindView(R.id.parentView) ScrollView parentView;
    @BindView(R.id.loginTvTitle) TextView loginTvTitle;
    @BindView(R.id.txtFullNameLogin) AppCompatEditText txtFullNameLogin;
    @BindView(R.id.txtEmailLogin) AppCompatEditText txtEmailLogin;
    @BindView(R.id.txtPasswordLogin) AppCompatEditText txtPasswordLogin;
    @BindView(R.id.btnRegister) Button btnRegister;
    @BindView(R.id.registerEmailBtnLogin) Button registerEmailBtnLogin;

    @BindViews({R.id.txtFullNameLogin, R.id.txtEmailLogin, R.id.txtPasswordLogin}) List<AppCompatEditText> allInputText;

    // RESOURCE
    @BindString(R.string.lbl_register_title) String strDefaultText;
    @BindColor(R.color.red) int colorRed;
    @BindColor(R.color.colorPrimaryDark) int colorPrimaryDark;

    private FirebaseAuth mAuth;

    private Unbinder unbinder;

    private RegisterEmailPresenter presenter;

    private PokenRegisterListener pokenLoginListener;

    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            prepareRegistration(s);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ac_register_email, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter = new RegisterEmailPresenter(new RegisterEmailModel(), this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Utils.Log(TAG, "Current: " + currentUser);
    }

    @Override
    public void onResume() {
        super.onResume();

        txtFullNameLogin.addTextChangedListener(loginTextWatcher);
        txtEmailLogin.addTextChangedListener(loginTextWatcher);
        txtPasswordLogin.addTextChangedListener(loginTextWatcher);

        for (AppCompatEditText editText : allInputText) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        }

    }

    private void initView() {

        // Initially set sign button disabled
        btnRegister.setEnabled(false);

        // Button Register
        btnRegister.setOnClickListener(this);

        // Button to open sign in page
        registerEmailBtnLogin.setOnClickListener(this);

    }

    private void prepareRegistration(Editable s) {
        if (s.hashCode() == txtFullNameLogin.getText().hashCode()) {
            Utils.Log(TAG, "Typing on full name: " + String.valueOf(s));
        } else if (s.hashCode() == txtEmailLogin.getText().hashCode()) {
            Utils.Log(TAG, "Typing on email: " + String.valueOf(s));
        } else if (s.hashCode() == txtPasswordLogin.hashCode()) {
            Utils.Log(TAG, "Typing on password: " + String.valueOf(s));
        }

        if (!StringUtils.isEmpty(String.valueOf(txtFullNameLogin.getText()))
                && !StringUtils.isEmpty(String.valueOf(txtEmailLogin.getText()))
                && StringUtils.isValidEmail(String.valueOf(txtEmailLogin.getText()))
                && !StringUtils.isEmpty(String.valueOf(txtPasswordLogin.getText()))) {
            btnRegister.setEnabled(true);
        } else {
            btnRegister.setEnabled(false);
        }

        showMessage("", 0);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PokenRegisterListener) {
            pokenLoginListener = (PokenRegisterListener) context;
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
            case R.id.btnRegister:
                beginSignIn();
                break;
            case R.id.registerEmailBtnLogin:
                if (pokenLoginListener != null) {
                    pokenLoginListener.onLoginScreenRequested();
                }
                break;
        }

    }

    /**
     * Begin Sign In Process when all form is filled.
     */
    public void registerEmail() {

        try {
            Utils.hideKeyboardFrom(parent, txtPasswordLogin);
            String[] nameChunks = String.valueOf(txtFullNameLogin.getText()).split(" ", 2);
            Utils.Logs('i', TAG, "Name part: " + Arrays.asList(nameChunks));
            String firstName = String.valueOf(txtFullNameLogin.getText());
            String lastName = "";
            if (nameChunks.length == 2) {

                firstName = nameChunks[0];
                lastName = nameChunks[1];

            }

            final User pokenUser = new User();
            pokenUser.first_name = firstName;
            pokenUser.last_name = lastName;
            pokenUser.username = String.valueOf(txtEmailLogin.getText());
            pokenUser.email = String.valueOf(txtEmailLogin.getText());
            pokenUser.password = String.valueOf(txtPasswordLogin.getText());

            mAuth.createUserWithEmailAndPassword(pokenUser.email, pokenUser.password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                if (user != null) {
                                    Utils.Logs('i', TAG, "registered user email: " + user.getEmail());
                                    Utils.Logs('i', TAG, "registered user user U ID (Firebase ID): " + user.getUid());
                                    Utils.Logs('i', TAG, "registered user user token: " + user.getIdToken(true));

                                    if (!user.isEmailVerified()) {
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d(TAG, "Email sent.");
                                                        }
                                                    }
                                                });
                                    } else {
                                        Utils.Logs('i', TAG, "Email verified...");
                                    }
                                }

                            } else {
                                // If sign in fails, display a message to the user.
                                Utils.Logs('e', TAG, "createUserWithEmail:failure" + task.getException());
                            }

                        }
                    });

            // Digital Ocean
            presenter.startRegister(pokenUser);

        } catch (NullPointerException | IndexOutOfBoundsException e) {
            e.printStackTrace();
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
            registerEmail();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRegisterSuccess() {
        Utils.Log(TAG, "Register success.");

        // Track register by email event
        MyLog.FabricTrackRegister("Email Register", true);

        try {
            pokenLoginListener.onRegisterSuccess();
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
            btnRegister.setEnabled(false);
        } else {
            btnRegister.setEnabled(true);
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return parent == null || parent.isFinishing();
    }

    public interface PokenRegisterListener {
        void onRegisterSuccess();
        void onLoginScreenRequested();
    }


}
