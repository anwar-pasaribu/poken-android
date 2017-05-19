package id.unware.poken.ui.wallet.withdrawal.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoBank;
import id.unware.poken.pojo.PojoUserBank;
import id.unware.poken.pojo.PojoWalletConfig;
import id.unware.poken.pojo.PojoWithdraw;
import id.unware.poken.pojo.PojoWithdrawPending;
import id.unware.poken.tools.MyTagHandler;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.wallet.addBankAccount.view.FragmentDialogBankAccount;
import id.unware.poken.ui.wallet.main.view.adapters.BanksSpinnerAdapter;
import id.unware.poken.ui.wallet.withdrawal.model.WithdrawalModel;
import id.unware.poken.ui.wallet.withdrawal.presenter.IWithdrawalPresenter;
import id.unware.poken.ui.wallet.withdrawal.presenter.WithdrawalPresenter;
import io.realm.Realm;

public class FragmentWalletWithdrawal extends BaseFragment implements
        IWithdrawalView {

    private final String TAG = "FragmentWalletWithdrawal";

    //////
    // S: Input withdrawal
    @BindView(R.id.parentWithdrawalInput) ViewGroup parentWithdrawalInput;
    @BindView(R.id.buttonClearText) ImageButton buttonClearText;
    @BindView(R.id.editTextAmount) EditText editTextAmount;
    @BindView(R.id.textViewTerm) TextView textViewTerm;
    @BindView(R.id.textViewWithdrawalMinMax) TextView textViewWithdrawalMinMax;
    @BindView(R.id.spinnerBanks) Spinner spinnerBanks;
    @BindView(R.id.editTextPassword) EditText editTextPassword;
    @BindView(R.id.buttonAddBank) Button buttonAddBank;
    // E: Input withdrawal
    //////

    //////
    // S: Withdrawal is pending
    @BindView(R.id.parentWithdrawalSummary) ViewGroup parentWithdrawalSummary;
    @BindView(R.id.textViewTransferedBalance) TextView textViewTransferedBalance;
    @BindView(R.id.textViewBankName) TextView textViewBankName;
    @BindView(R.id.textViewBankAccountName) TextView textViewBankAccountName;
    @BindView(R.id.textViewBankAccountNumber) TextView textViewBankAccountNumber;

    @BindView(R.id.buttonComposeEmail) Button buttonComposeEmail;
    @BindView(R.id.buttonCallSupport) Button buttonCallSupport;

    @BindView(R.id.buttonCancelTopUp) Button buttonCancelTopUp;  // Cancel withdrawal
    // E: Withdrawal is pending
    //////

    private View parentView;

    private IWithdrawalPresenter presenter;
    private BanksSpinnerAdapter banksSpinnerAdapter;

    /**
     * Current typed text on amount edit text
     */
    private String mCurrentTypedStr = "";

    /**
     * Object list for user banks spinner
     */
    private List<Object> mList;

    /**
     * Decimal format for Indonesia
     */
    private DecimalFormat mMoneyFormatIndo = new DecimalFormat(
            "#,##0", new DecimalFormatSymbols(new Locale("id", "ID")));

    private Realm mRealm;
    private WalletWithdrawalListener listener;

    public FragmentWalletWithdrawal() {
    }

    public static FragmentWalletWithdrawal newInstance(
            String strUserBalance,
            PojoWithdrawPending withdraw,
            PojoWalletConfig walletConfig,
            ArrayList<PojoUserBank> userBank,
            ArrayList<PojoBank> bankList) {

        FragmentWalletWithdrawal fragmentWalletWithdrawal = new FragmentWalletWithdrawal();
        Bundle bndl = new Bundle();
        bndl.putString(Constants.EXTRA_WALLET_USER_BALANCE, strUserBalance);  // Put user balance as max withdrawal
        bndl.putParcelable(Constants.EXTRA_WITHDRAWAL_STATUS, withdraw);
        bndl.putParcelable(Constants.EXTRA_WALLET_CONFIG, walletConfig);  // Put PojoWalletConfig as args.
        bndl.putParcelableArrayList(Constants.EXTRA_WITHDRAWAL_USER_BANK_LIST, userBank);
        bndl.putParcelableArrayList(Constants.EXTRA_WITHDRAWAL_BANK_LIST, bankList);
        fragmentWalletWithdrawal.setArguments(bndl);
        return fragmentWalletWithdrawal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.Log(TAG, "On view created!");
        View view = inflater.inflate(R.layout.f_wallet_withdrawal, container, false);
        ButterKnife.bind(this, view);

        mRealm = Realm.getDefaultInstance();

        presenter = new WithdrawalPresenter(this, new WithdrawalModel());

        initView();

        return view;
    }

    private void initView() {

        setupUserBanks();
        setHasOptionsMenu(false);

        // Spinner event on item selected
        spinnerBanks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Utils.Logs('e', TAG, "Selected spinner -. ID: " + id + ", pos: " + position);

                if (id == Constants.HEADER_ITEM_ID) {
                    // Open add bank account screen
                    onClick(buttonAddBank);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Utils.Logs('w', TAG, "Do nothing with spinner");
            }
        });

        // Text formatting while typing amount (format to Indo decimal)
        editTextAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals(mCurrentTypedStr) && s.length() > 0) {
                    editTextAmount.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[%s,.\\\\s]", "");
                    double parsed = Utils.getParsedDouble(cleanString);
                    String formatted = mMoneyFormatIndo.format(parsed);

                    mCurrentTypedStr = formatted;
                    editTextAmount.setText(formatted);
                    editTextAmount.setSelection(formatted.length());

                    editTextAmount.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Utils.Log(TAG, "After text changed: " + editable.toString());

                if (editable.length() > 0) {
                    toggleClearEditTextButton(true);
                } else {
                    toggleClearEditTextButton(false);
                }
            }
        });

        setupRules();

        presenter.getUserBanks();
        presenter.getWalletInfo();

    }

    private void setupUserBanks() {

        // Create dummy data for bank list
        mList = new ArrayList<>();

        banksSpinnerAdapter = new BanksSpinnerAdapter(this.parent, mList);

        spinnerBanks.setAdapter(banksSpinnerAdapter);

        verifiedSpinner();
    }

    private void verifiedSpinner() {

        // Show spinner when user bank is available
        spinnerBanks.setVisibility(this.mList.isEmpty() ? View.GONE : View.VISIBLE);

        // Show/Hide add bank button
        // Hide when user bank is available or show when no user bank
        buttonAddBank.setVisibility(this.mList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private CharSequence makeCharSequence(String strBalance) {

        if (parent == null || parent.isFinishing()) return null;

        String prefix = parent.getString(R.string.lbl_idr);
        String sequence = prefix + " " + strBalance;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(Typeface.NORMAL), 0, prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), prefix.length(), sequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    private void setupWithdrawalAmountLimit(String strMinAmount, String strMaxAmount) {
        String strMinMax = parent.getString(
                R.string.lbl_withdrawal_limit,
                makeCharSequence(strMinAmount),
                makeCharSequence(strMaxAmount));
        //noinspection deprecation
        textViewWithdrawalMinMax.setText(Html.fromHtml(strMinMax));
    }

    private void setupRules() {
        String strWithdrawalRules = parent.getString(R.string.withdrawal_rules);
        //noinspection deprecation
        textViewTerm.setText(Html.fromHtml(strWithdrawalRules, null, new MyTagHandler()));
    }

    private void toggleClearEditTextButton(boolean isShow) {
        if (isShow) {
            buttonClearText.animate().scaleX(1).scaleY(1);
        } else {
            buttonClearText.animate().scaleX(0).scaleY(0);
        }
    }

    private void requestForgotPassword() {
        Utils.Logs('i', TAG, "Request forget ");
    }

    private void clearAmountText() {
        editTextAmount.setText(null);
    }

    private FragmentTransaction hideDialog(String strTag) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        Fragment prev = getFragmentManager().findFragmentByTag(strTag);

        if (prev != null && prev instanceof DialogFragment) {
            Utils.Log(TAG, "Prev fragment is not null. Tag: " + strTag);

            ((DialogFragment) prev).dismiss();
            ft.remove(prev);
        }

        return ft;
    }

    public void setupInputAmountState() {
        parentWithdrawalSummary.animate().alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                parentWithdrawalSummary.setVisibility(View.GONE);

                parentWithdrawalInput.setVisibility(View.VISIBLE);

                parentWithdrawalInput.animate().translationY(0).alpha(1);
            }
        });
    }

    private void setupTransferInfo(PojoWithdraw pojoWithdraw) {

        parentWithdrawalSummary.animate().alpha(0);
        parentWithdrawalSummary.setVisibility(View.VISIBLE);
        parentWithdrawalSummary.animate().alpha(1);

        PojoUserBank pojoUserBank = ControllerRealm.getInstance().getUserBankAccount(pojoWithdraw.user_bank_id);
        double longBalanceAmount = Double.parseDouble(pojoWithdraw.amount);
        String strUserName = pojoUserBank.getUserBankAccName(),
                strBankName = pojoUserBank.getBankName(),
                strBankAccountNumber = pojoUserBank.getUserBankAccNo();

        // Format balance amount
        String formattedAmount;
        DecimalFormat mMoneyFormatIndo = new DecimalFormat(
                "#,##0", new DecimalFormatSymbols(new Locale("id", "ID")));
        formattedAmount = mMoneyFormatIndo.format(longBalanceAmount);

        textViewTransferedBalance.setText(makeCharSequence(formattedAmount));
        textViewBankName.setText(strBankName);
        textViewBankAccountName.setText(strUserName);
        textViewBankAccountNumber.setText(strBankAccountNumber);
    }

    private void proceedCancellationWithConfirmation() {
        ControllerDialog.getInstance().showYesNoDialog(
                this.getString(R.string.msg_withdrawal_cancellation),
                this.parent,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == Dialog.BUTTON_POSITIVE) {
                            Utils.Log(TAG, "User sure to cancel withdrawal.");
                            FragmentWalletWithdrawal.this.presenter.requestCancelWithdrawal();
                        } else if (which == Dialog.BUTTON_NEGATIVE) {
                            Utils.Log(TAG, "USer not sure to cancel.");
                        }

                        dialog.dismiss();
                    }
                },
                this.parent.getString(R.string.btn_cancel_withdraw),
                this.parent.getString(R.string.btn_negative_no)
        );
    }

    @OnClick(R.id.buttonCallSupport)
    public void callSupport() {

        if (parent == null || parent.isFinishing()) return;

        Utils.Log(TAG, "Open dialer with support number attached");
        Utils.openDialer(parent, parent.getString(R.string.support_mobile_phone));
    }

    @OnClick(R.id.buttonComposeEmail)
    public void composeEmail() {

        if (parent == null || parent.isFinishing()) return;

        Utils.Log(TAG, "Open email composer");
        Utils.composeEmail(parent, parent.getString(R.string.email_title_support));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (!mRealm.isClosed()) {
            mRealm.close();
        }
    }

    @OnClick({R.id.btnContinueWithdrawal, R.id.buttonClearText, R.id.buttonForgetPassword, R.id.buttonAddBank, R.id.buttonCancelTopUp})
    public void onClick(View v) {
        Utils.Log("view clicked", "button clicked. View: " + v);
        switch (v.getId()) {
            case R.id.btnContinueWithdrawal:
                presenter.requestWithdrawal();
                break;
            case R.id.buttonClearText:
                clearAmountText();
                break;
            case R.id.buttonForgetPassword:
                requestForgotPassword();
                break;
            case R.id.buttonAddBank:
                presenter.getBankList();
                break;
            case R.id.buttonCancelTopUp:
                proceedCancellationWithConfirmation();
                break;
        }
    }

    //////
    // S: Wallet Withdrawal view
    // FragmentWallet have control over this method
    @Override
    public void setUserBanks(ArrayList<PojoUserBank> userBankList) {
        Utils.Log(TAG, "Set user bank list on spinner. Size: " + userBankList.size());

        if (userBankList.size() != 0) {
            mList.clear();
            mList.addAll(userBankList);
            addUserAddBank();
        } else {
            verifiedSpinner();
        }
    }

    @Override
    public boolean isWithdrawReady() {
        if (TextUtils.isEmpty(editTextAmount.getText())) {
            editTextAmount.requestFocus();
            editTextAmount.setError(this.parent.getString(R.string.msg_error_amount_empty));
        } else if (TextUtils.isEmpty(editTextPassword.getText())) {
            editTextPassword.requestFocus();
            editTextPassword.setError(this.parent.getString(R.string.msg_password_blank));
        } else {
            editTextAmount.setError(null);
            editTextPassword.setError(null);
            int nominal = Integer.parseInt(getAmount());
            if (nominal >= Constants.MIN_WITHDRAW_VALUE && nominal <= Constants.MAX_WITHDRAW_VALUE) {
                return true;
            } else {
                editTextAmount.setError(this.parent.getString(R.string.msg_amount_not_valid));
            }
        }
        return false;
    }

    @Override
    public String getAmount() {
        return editTextAmount.getText().toString().replaceAll("[%s,.\\\\s]", "");
    }

    @Override
    public String getSelectedUserBankId() {
        return ((PojoUserBank) mList.get(spinnerBanks.getSelectedItemPosition())).getUserBankId();
    }

    @Override
    public String getPassword() {
        return editTextPassword.getText().toString();
    }

    @Override
    public void checkWithdrawStatus() {
        presenter.checkWithdrawStatus();
    }

    // FragmentWallet have control over this method
    @Override
    public void refreshBankList() {
        Utils.Log(TAG, "Refresh list bank spinner count: " + banksSpinnerAdapter.getCount());
        banksSpinnerAdapter.notifyDataSetChanged();
        spinnerBanks.setAdapter(banksSpinnerAdapter);
        verifiedSpinner();
    }

    @Override
    public void addUserAddBank() {
        mList.add(this.parent.getString(R.string.lbl_add_bank));
    }

    @Override
    public View getParentView() {
        return parentView;
    }

    @Override
    public void setParentView(View parentView) {
        this.parentView = parentView;
    }

    @Override
    public void showAddBankAccount(ArrayList<PojoBank> listBank) {
        Utils.Log(TAG, "Begin to show add bank account screen.");

        final String bankAccountTag = "dialog-bank-account";

        FragmentTransaction ft = hideDialog(bankAccountTag);
        ft.addToBackStack(null);
        FragmentDialogBankAccount bankAccount = FragmentDialogBankAccount.newInstance(listBank);
        bankAccount.setListener(presenter);
        bankAccount.show(ft, bankAccountTag);
    }

    @Override
    public void showWithdrawalAmount(String strMaxAmount, String strMinAmount) {

        double parsedMaxAmount = Utils.getParsedDouble(strMaxAmount);
        double parsedMinAmount = Utils.getParsedDouble(strMinAmount);

        setupWithdrawalAmountLimit(
                mMoneyFormatIndo.format(parsedMinAmount),
                mMoneyFormatIndo.format(parsedMaxAmount));

    }

    @Override
    public void showTransferSummary(final PojoWithdraw pojoWithdraw) {

        // Show pending withdrawal info
        // Hide Input form
        int parentTopUpInputHeight = parentWithdrawalInput.getHeight();

        parentWithdrawalInput
                .animate()
                .translationY(parentTopUpInputHeight * -1)
                .alpha(0)
                .withEndAction(new Runnable() {
            @Override
            public void run() {
                parentWithdrawalInput.setVisibility(View.GONE);
                setupTransferInfo(pojoWithdraw);

            }
        });

    }

    @Override
    public void showInputWithdrawal() {
        setupInputAmountState();
    }

    @Override
    public void refreshBankListRemote() {
        listener.refreshFromWithdraw();
    }
    // E: Wallet Withdrawal view
    //////

    public void setListener(WalletWithdrawalListener listener) {
        this.listener = listener;
    }

    public interface WalletWithdrawalListener {
        void refreshFromWithdraw();
    }

}