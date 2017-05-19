package id.unware.poken.ui.wallet.withdrawalStatus.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoUserBank;
import id.unware.poken.pojo.PojoWithdraw;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.wallet.withdrawalStatus.model.WithdrawSummaryModel;
import id.unware.poken.ui.wallet.withdrawalStatus.presenter.IWithdrawSummaryPresenter;
import id.unware.poken.ui.wallet.withdrawalStatus.presenter.WithdrawSummaryPresenter;


public class FragmentDialogTransferSummary extends DialogFragment implements
        View.OnClickListener, IDialogWithdrawSummaryView {

    private final String TAG = "FragmentDialogTransferSummary";

    @BindView(R.id.textViewTransferedBalance) TextView textViewTransferedBalance;
    @BindView(R.id.textViewBankName) TextView textViewBankName;
    @BindView(R.id.textViewBankAccountName) TextView textViewBankAccountName;
    @BindView(R.id.textViewBankAccountNumber) TextView textViewBankAccountNumber;

    @BindView(R.id.buttonComposeEmail) Button buttonComposeEmail;
    @BindView(R.id.buttonCallSupport) Button buttonCallSupport;
    @BindView(R.id.buttonDoneDialog) Button buttonDoneDialog;
    @BindView(R.id.parentView) LinearLayout parentView;

    private PojoWithdraw pojoWithdraw;

    private AppCompatActivity parent;

    private IWithdrawSummaryPresenter presenter;

    public FragmentDialogTransferSummary() {

    }

    public static FragmentDialogTransferSummary newInstance(PojoWithdraw pojoWithdraw) {
        FragmentDialogTransferSummary fragment = new FragmentDialogTransferSummary();
        Bundle args = new Bundle();

        args.putParcelable(Constants.EXTRA_POJO_WITHDRAW, pojoWithdraw);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyLog.FabricLog(Log.INFO, "Dimension dialog created.");
        this.setCancelable(false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            pojoWithdraw = bundle.getParcelable(Constants.EXTRA_POJO_WITHDRAW);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_balance_transfer_summary, null, false);
        ButterKnife.bind(this, view);
        presenter = new WithdrawSummaryPresenter(this, new WithdrawSummaryModel());
        initView();

        return view;
    }

    private void initView() {

        if (getDialog() != null && getDialog().getWindow() != null) {
            // request a window without the title
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            getDialog().setCancelable(true);
        }

        buttonComposeEmail.setOnClickListener(this);
        buttonCallSupport.setOnClickListener(this);
        buttonDoneDialog.setOnClickListener(this);

        setupTransferInfo();

    }

    private void setupTransferInfo() {
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

    private CharSequence makeCharSequence(String strBalance) {
        String prefix = parent.getString(R.string.lbl_idr);
        String sequence = prefix + " " + strBalance;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(Typeface.NORMAL), 0, prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), prefix.length(), sequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonComposeEmail:
                composeEmail();
                break;
            case R.id.buttonCallSupport:
                callSupport();
                break;
            case R.id.buttonDoneDialog:
                presenter.cancelWithdraw();
                break;
        }
    }

    public void closeDialog() {
        if (getDialog() != null) {
            Utils.Log(TAG, "Close dialog.");
            getDialog().dismiss();
        }
    }

    private void callSupport() {

        if (parent == null || parent.isFinishing()) return;

        Utils.Log(TAG, "Open dialer with support number attached");
        Utils.openDialer(parent, parent.getString(R.string.support_mobile_phone));
    }

    private void composeEmail() {

        if (parent == null || parent.isFinishing()) return;

        Utils.Log(TAG, "Open email composer");
        Utils.composeEmail(parent, parent.getString(R.string.email_title_support));
    }

    @Override
    public void onStop() {
        Utils.Log(TAG, "On stop FragmentDialogArea");

        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (AppCompatActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        parent = null;
    }

    @Override
    public View getParentView() {
        return parentView;
    }
}
