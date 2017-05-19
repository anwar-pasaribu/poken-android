package id.unware.poken.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.ui.MainPage.MainPage;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.ivSplash) ImageView ivSplash;
    int idx = 0;
    private long splashDelay = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        ButterKnife.bind(this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoMainPage();
            }
        }, splashDelay);
    }

    private void gotoMainPage() {
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
        finish();
    }
}
