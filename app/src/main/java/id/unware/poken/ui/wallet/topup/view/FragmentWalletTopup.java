package id.unware.poken.ui.wallet.topup.view;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoDeposit;
import id.unware.poken.pojo.PojoDepositPending;
import id.unware.poken.tools.MyTagHandler;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.wallet.topup.model.WalletTopupModel;
import id.unware.poken.ui.wallet.topup.presenter.IWalletTopupPresenter;
import id.unware.poken.ui.wallet.topup.presenter.WalletTopupPresenter;

public class FragmentWalletTopup extends BaseFragment implements View.OnClickListener,
        IWalletTopupView {

    private final String TAG = "FragmentWalletTopup";

    // S: Input amount of balance top up form
    @BindView(R.id.parentTopUpInput) ViewGroup parentTopUpInput;
    @BindView(R.id.editTextAmount) EditText editTextAmount;
    @BindView(R.id.buttonClearText) ImageButton buttonClearText;
    @BindView(R.id.buttonContinueTopUp) Button buttonContinueTopUp;
    // E: Input amount of balance top up form

    // S: Waiting for payment
    @BindView(R.id.parentWaitingTransfer) ViewGroup parentWaitingTransfer;
    @BindView(R.id.textViewAmountToTransfer) TextView textViewAmountToTransfer;  // Show amount to transfer (with unique id)
    @BindView(R.id.buttonAccountNumber) Button buttonAccountNumber;  // Bank Account Number
    @BindView(R.id.buttonCopyBankAccountNumber) Button buttonCopyBankAccountNumber;  // Copy Bank Account Number
    @BindView(R.id.buttonCancelTopUp) Button buttonCancelTopUp;  // Cancel top up
    @BindView(R.id.textViewInstructions) TextView textViewInstructions;
    // E: Waiting for payment

    private View parentView;

    /**
     * Current typed text on amount edit text
     */
    private String mCurrentTypedStr = "";

    /**
     * Decimal format for Indonesia
     */
    private DecimalFormat mMoneyFormatIndo = new DecimalFormat(
            "#,##0", new DecimalFormatSymbols(new Locale("id", "ID")));

    /**
     * Amount to transfer include unique number
     */
//    private long mLongAmountToTransfer = 0L;
    public static FragmentWalletTopup newInstance(PojoDepositPending deposit) {
        FragmentWalletTopup fragmentWalletTopup = new FragmentWalletTopup();
        Bundle bndl = new Bundle();
        bndl.putParcelable(Constants.EXTRA_TOPUP_STATUS, deposit);
        fragmentWalletTopup.setArguments(bndl);

        return fragmentWalletTopup;
    }

    public FragmentWalletTopup() {
    }

    private IWalletTopupPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.Log(TAG, "On view created!");
        View view = inflater.inflate(R.layout.f_wallet_topup, container, false);
        ButterKnife.bind(this, view);
        presenter = new WalletTopupPresenter(this, new WalletTopupModel());

        initView();

        return view;
    }

    private void initView() {

        setHasOptionsMenu(false);

        // Decide whether show Input amount form or Waiting for transfer
        presenter.checkStatus();
        // Disable continue top up button when no amount entered
        buttonContinueTopUp.setEnabled(isValidAmount());

        // Show or hide clear edit text button
        toggleClearEditTextButton(isValidAmount());

        // Init on click event
        buttonClearText.setOnClickListener(this);
        buttonContinueTopUp.setOnClickListener(this);
        buttonCancelTopUp.setOnClickListener(this);

        // Button copy account number
        buttonCopyBankAccountNumber.setOnClickListener(this);

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

                buttonContinueTopUp.setEnabled(isValidAmount());

                if (editable.length() > 0) {
                    toggleClearEditTextButton(true);
                } else {
                    toggleClearEditTextButton(false);
                }
            }
        });

        // On long click on amount to transfer to copy it.
        textViewAmountToTransfer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                presenter.copyAmountToClipboard();
                return false;
            }
        });
    }

    public void setupInputAmountState() {
        parentWaitingTransfer.animate().alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                parentWaitingTransfer.setVisibility(View.GONE);

                parentTopUpInput.setVisibility(View.VISIBLE);

                parentTopUpInput.animate().translationY(0).alpha(1);
            }
        });
    }

    private void setupWaitingTransferState(PojoDeposit pojoDeposit) {

        parentWaitingTransfer.animate().alpha(0);
        parentWaitingTransfer.setVisibility(View.VISIBLE);
        parentWaitingTransfer.animate().alpha(1);

        // Show amount to transfer with unique id
        setupAmountToTransfer(pojoDeposit.amount);

        // Setup rule with generated unique id and due date time inside.
        setupRules(pojoDeposit.unique_id, pojoDeposit.expired_on);
    }

    private void setupRules(String strUniqueNumber, String dueDateTime) {
        String strRules = parent.getString(R.string.top_up_rules, strUniqueNumber, dueDateTime);
        //noinspection deprecation
        textViewInstructions.setText(Html.fromHtml(strRules, null, new MyTagHandler()));
    }

    private void setupAmountToTransfer(String strAmountToTransfer) {

        double parsed = Utils.getParsedDouble(strAmountToTransfer);
        String formatted = mMoneyFormatIndo.format(parsed);

        textViewAmountToTransfer.setText(makeCharSequence(formatted));
    }

    private CharSequence makeCharSequence(String strBalance) {
        String prefix = parent.getString(R.string.lbl_idr);
        String sequence = prefix + " " + strBalance;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(Typeface.NORMAL), 0, prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), prefix.length(), sequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    private void toggleClearEditTextButton(boolean isShow) {
        if (isShow) {
            buttonClearText.animate().scaleX(1).scaleY(1);
        } else {
            buttonClearText.animate().scaleX(0).scaleY(0);
        }
    }

    public boolean isValidAmount() {

        String strAmount = getAmountString();
        double doubleAmount = Utils.getParsedDouble(strAmount);

        return doubleAmount >= 10000;
    }

    private void proceedCancellationWithConfirmation() {
        ControllerDialog.getInstance().showYesNoDialog(
                this.parent.getString(R.string.msg_top_up_cancellation),
                this.parent,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (which == Dialog.BUTTON_POSITIVE) {
                            Utils.Log(TAG, "User sure to cancel withdrawal.");
                            FragmentWalletTopup.this.presenter.requestCancelTopUp();
                        } else if (which == Dialog.BUTTON_NEGATIVE) {
                            Utils.Log(TAG, "USer not sure to cancel.");
                        }

                        dialog.dismiss();
                    }
                },
                this.parent.getString(R.string.btn_cancel_top_up),
                this.parent.getString(R.string.btn_negative_no)
        );
    }

    @Override
    public void showMessage(String msg) {
        Utils.snackBar(buttonContinueTopUp, msg, Log.ERROR);
    }

    @Override
    public void setLoadingState() {
        buttonContinueTopUp.setEnabled(false);
        buttonContinueTopUp.setText(parent.getString(R.string.msg_change_data_loading));
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
    public void showPendingPage(final PojoDeposit deposit) {

        // Do nothing if deposit data is null
        if (deposit == null) return;

        Utils.Log(TAG, "Show waiting for transfer state.");

        // Hide Input form
        int parentTopUpInputHeight = parentTopUpInput.getHeight();

        parentTopUpInput.animate().translationY(parentTopUpInputHeight * -1).alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                parentTopUpInput.setVisibility(View.GONE);
                setupWaitingTransferState(deposit);

            }
        });
    }

    @Override
    public String getAmountString() {
        return String.valueOf(editTextAmount.getText()).replaceAll("\\.", "");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonContinueTopUp:
                presenter.requestTopUp();
                break;
            case R.id.buttonClearText:
                presenter.clearAmountText();
                break;
            case R.id.buttonCancelTopUp:
                proceedCancellationWithConfirmation();
                break;
            case R.id.buttonCopyBankAccountNumber:
                presenter.copyAccountNumberToClipboard();
                break;
        }

    }

    @Override
    public String getAccountNumber() {
        return buttonAccountNumber.getText().toString();
    }

    @Override
    public ClipboardManager getClipboardManager() {
        return (ClipboardManager) parent.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Override
    public void setAmount(String amount) {
        editTextAmount.setText(null);
    }

    @Override
    public Context getParentActivity() {
        return parent;
    }

    @Override
    public void showToast(String string) {
        Utils.toast(parent, string);
    }
}
