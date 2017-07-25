package id.unware.poken.ui.shoppingsummary.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.home.view.HomeActivity;

public class ShoppingSummaryActivity extends BaseActivity {

    private static final String TAG = "ShoppingSummaryActivity";

    @BindView(R.id.shoppingSummaryBtnShoppingAgain) Button shoppingSummaryBtnShoppingAgain;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_summary);

        unbinder = ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setElevation(0F);
        getSupportActionBar().setTitle(null);

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
