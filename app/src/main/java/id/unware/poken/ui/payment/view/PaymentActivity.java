package id.unware.poken.ui.payment.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDate;
import id.unware.poken.domain.UserBank;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyTagHandler;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideApp;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.payment.model.PaymentModel;
import id.unware.poken.ui.payment.presenter.PaymentPresenter;
import id.unware.poken.ui.payment.view.adapter.PokenBankAdapter;
import id.unware.poken.ui.shoppingsummary.view.ShoppingSummaryActivity;

public class PaymentActivity extends BaseActivity implements IPaymentView {

    private static final String TAG = "PaymentActivity";

    @BindView(R.id.parentView) ViewGroup parentView;
    @BindView(R.id.btnConfirmPayment) Button btnConfirmPayment;
    @BindView(R.id.textViewAmountToTransfer) TextView textViewAmountToTransfer;
    @BindView(R.id.textViewInstructions) TextView textViewInstructions;

    @BindView(R.id.pbPaymentBankList) ProgressBar pbPaymentBankList;
    @BindView(R.id.rvBankList) RecyclerView rvBankList;

    // LOADING INDICATOR
    @BindView(R.id.loadingOverlayParentView) ViewGroup loadingOverlayParentView;

    // Bank account number
    @BindString(R.string.poken_bank_account_number_bri) String poken_bank_account_number_bri;
    @BindString(R.string.poken_bank_account_number_bni) String poken_bank_account_number_bni;
    @BindString(R.string.poken_bank_account_number_mandiri) String poken_bank_account_number_mandiri;

    private PaymentPresenter presenter;

    private GlideRequests glideRequests;

    private Unbinder unbinder;

    private long orderId;
    private String orderRef = "";
    private double shoppingCost = 0;
    private int orderStatus = 0;
    private Date paymentDue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        if (getIntent().getExtras() != null) {
            orderId = getIntent().getLongExtra(Constants.EXTRA_ORDER_ID, 0);
            orderRef = getIntent().getStringExtra(Constants.EXTRA_ORDER_REF);
            shoppingCost = getIntent().getDoubleExtra(Constants.EXTRA_TOTAL_SHOPPING_COST, 0D);
            orderStatus = getIntent().getIntExtra(Constants.EXTRA_ORDER_STATUS, 0);
            paymentDue = (Date) getIntent().getSerializableExtra(Constants.EXTRA_PAYMENT_DUE);
        }

        presenter = new PaymentPresenter(new PaymentModel(), this);

        glideRequests = GlideApp.with(this);

        unbinder = ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.loadAvailablePokenBankList();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setSubtitle(this.getString(R.string.lbl_order_ref_id, orderRef));
        }

        // Shopping cost
        textViewAmountToTransfer.setText(StringUtils.formatCurrency(String.valueOf(shoppingCost)));

        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.beginOrder(orderId, orderStatus);
                // openShoppingSummaryScreen();
            }
        });

        String strRules = this.getString(R.string.top_up_rules,
                ControllerDate.getInstance().getShortDateWithHourFormat(paymentDue));
        //noinspection deprecation
        textViewInstructions.setText(Html.fromHtml(strRules, null, new MyTagHandler()));
    }

    @Override
    public void openShoppingSummaryScreen() {
        Intent intentShoppingSummary = new Intent(this, ShoppingSummaryActivity.class);
        intentShoppingSummary.putExtra(Constants.EXTRA_ORDER_REF, orderRef);
        intentShoppingSummary.putExtra(Constants.EXTRA_TOTAL_SHOPPING_COST, shoppingCost);
        this.startActivity(intentShoppingSummary);
    }

    @Override
    public void populateBankList(ArrayList<UserBank> userBankArrayList) {

        PokenBankAdapter adapter = new PokenBankAdapter(this, userBankArrayList, presenter, glideRequests);
        rvBankList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvBankList.setAdapter(adapter);

    }

    @Override
    public void copyBankAccountNumber(String accountNumber) {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                String clearBankAccNumber = poken_bank_account_number_mandiri.replace("-", "");
                ClipboardManager clipboard = (ClipboardManager) PaymentActivity.this.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copied_bank_account_number", clearBankAccNumber);
                clipboard.setPrimaryClip(clip);

                Utils.snackBar(
                        parentView,
                        PaymentActivity.this.getString(R.string.msg_info_string_copied, clearBankAccNumber),
                        Log.INFO
                );
            }
        }, 150);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            Utils.Log(TAG, "Home navigation clicked.");
            this.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void showLoadingIndicator(boolean isShow) {
        if (isShow) {
            loadingOverlayParentView.animate().withStartAction(new Runnable() {
                @Override
                public void run() {
                    loadingOverlayParentView.setVisibility(View.VISIBLE);
                    btnConfirmPayment.setEnabled(false);
                }
            }).alpha(1F);

            pbPaymentBankList.setVisibility(View.VISIBLE);
        } else {

            loadingOverlayParentView.animate().withEndAction(new Runnable() {
                @Override
                public void run() {
                    loadingOverlayParentView.setVisibility(View.GONE);
                    btnConfirmPayment.setEnabled(true);
                }
            }).alpha(0F);

            pbPaymentBankList.setVisibility(View.GONE);
        }
    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                showLoadingIndicator(true);
                break;
            case FINISHED:
                showLoadingIndicator(false);
                break;
            case INCOMPLETE:
                break;

        }
    }

    @Override
    public boolean isActivityFinishing() {
        return isFinishing();
    }
}
