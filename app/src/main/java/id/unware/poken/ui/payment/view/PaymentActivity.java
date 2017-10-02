package id.unware.poken.ui.payment.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDate;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyTagHandler;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingsummary.view.ShoppingSummaryActivity;

public class PaymentActivity extends AppCompatActivity {

    private static final String TAG = "PaymentActivity";

    @BindView(R.id.parentView) ViewGroup parentView;
    @BindView(R.id.btnConfirmPayment) Button btnConfirmPayment;
    @BindView(R.id.textViewAmountToTransfer) TextView textViewAmountToTransfer;
    @BindView(R.id.textViewInstructions) TextView textViewInstructions;
    @BindView(R.id.buttonCopyBankAccountNumber) Button buttonCopyBankAccountNumber;

    // Bank account number
    @BindString(R.string.poken_bank_account_number_bri) String poken_bank_account_number_bri;

    private Unbinder unbinder;

    private String orderRef = "";
    private double shoppingCost = 0;
    private Date paymentDue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        if (getIntent().getExtras() != null) {
            orderRef = getIntent().getStringExtra(Constants.EXTRA_ORDER_REF);
            shoppingCost = getIntent().getDoubleExtra(Constants.EXTRA_TOTAL_SHOPPING_COST, 0D);
            paymentDue = (Date) getIntent().getSerializableExtra(Constants.EXTRA_PAYMENT_DUE);
        }

        unbinder = ButterKnife.bind(this);

        initView();
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
                openShoppingSummaryScreen();
            }
        });

        buttonCopyBankAccountNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String clearBankAccNumber = poken_bank_account_number_bri.replace("-", "");
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
        });

        String strRules = this.getString(R.string.top_up_rules,
                ControllerDate.getInstance().getShortDateWithHourFormat(paymentDue));
        //noinspection deprecation
        textViewInstructions.setText(Html.fromHtml(strRules, null, new MyTagHandler()));
    }

    private void openShoppingSummaryScreen() {
        Intent intentShoppingSummary = new Intent(this, ShoppingSummaryActivity.class);
        intentShoppingSummary.putExtra(Constants.EXTRA_ORDER_REF, orderRef);
        this.startActivity(intentShoppingSummary);
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

}
