package id.unware.poken.ui.shoppingsummary.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.home.view.HomeActivity;
import id.unware.poken.ui.payment.view.PaymentActivity;

public class ShoppingSummaryActivity extends BaseActivity {

    private static final String TAG = "ShoppingSummaryActivity";

    @BindView(R.id.parentView) ViewGroup parentView;
    @BindView(R.id.shoppingSummaryBtnShoppingAgain) Button shoppingSummaryBtnShoppingAgain;
    @BindView(R.id.shoppingSummaryBtnCopyPaymentConfirmText) Button shoppingSummaryBtnCopyPaymentConfirmText;

    private String orderRef = "";
    private double shoppingCost = 0;
    private String payTemplate = "";

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_summary);

        unbinder = ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            orderRef = getIntent().getStringExtra(Constants.EXTRA_ORDER_REF);
            shoppingCost = getIntent().getDoubleExtra(Constants.EXTRA_TOTAL_SHOPPING_COST, 0D);

            Utils.Logs('i', TAG, "Total shopping cost: " + shoppingCost);
        }

        payTemplate = getResources().getString(R.string.payment_confirmation_format_template_placehoder, orderRef, ((int) shoppingCost));

        Utils.Logs('i', TAG, "Template: " + payTemplate);

        initView();
    }

    private void initView() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setElevation(0F);
            getSupportActionBar().setTitle(R.string.title_activity_payment_confirm);
            getSupportActionBar().setSubtitle(this.getString(R.string.lbl_order_ref_id, orderRef));
        }

        shoppingSummaryBtnCopyPaymentConfirmText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ClipboardManager clipboard = (ClipboardManager) ShoppingSummaryActivity.this.getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("copied_payment_confirmation_template", payTemplate);
                        if (clipboard != null) {
                            clipboard.setPrimaryClip(clip);
                        }

                        Utils.snackBar(
                                parentView,
                                ShoppingSummaryActivity.this.getString(R.string.msg_info_string_copied, payTemplate),
                                Log.INFO
                        );
                    }
                }, 150);
            }
        });

        shoppingSummaryBtnShoppingAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainScreen();
            }
        });
    }

    private void openMainScreen() {
        Intent homeScreenIntent = new Intent(this, HomeActivity.class);
        homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(homeScreenIntent);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_summary, menu);
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
