package id.unware.poken.ui.featured.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Featured;
import id.unware.poken.domain.Product;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideApp;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.BaseActivityWithup;
import id.unware.poken.ui.featured.model.FeaturedModel;
import id.unware.poken.ui.featured.presenter.FeaturedPresenter;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;

public class FeaturedActivity extends BaseActivityWithup implements IFeaturedView {

    private static final String TAG = "FeaturedActivity";

    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.featuredHeaderImage) ImageView featuredHeaderImage;
    @BindView(R.id.featuredProgress) ProgressBar featuredProgress;
    @BindView(R.id.featuredTvMain) TextView featuredTvMain;

    private FeaturedPresenter presenter;

    private Featured currentFeatured;

    private GlideRequests glideRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured);
        ButterKnife.bind(this);

        glideRequests = GlideApp.with(this);

        if (getIntent().getExtras() != null) {
            String strFeatured = getIntent().getExtras().getString(Constants.EXTRA_DOMAIN_SERIALIZED_STRING);
            if (!StringUtils.isEmpty(strFeatured)) {
                Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
                currentFeatured = gson.fromJson(strFeatured, Featured.class);
            }
        }

        presenter = new FeaturedPresenter(new FeaturedModel(), this);

        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentFeatured != null) {
            presenter.reqFeaturedDetail(currentFeatured.id);
        } else {
            Utils.Logs('e', TAG, "Featured data not available!");
        }

    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle(null);

        if (currentFeatured != null) {

            loadFeaturedHeaderImage(currentFeatured.image);

            // Prevent status bar solid color on collapsed status
            toolbar_layout.setStatusBarScrimColor(ContextCompat.getColor(this, android.R.color.transparent));

        } else {
            Utils.Logs('e', TAG, "Current feture not available anymore");
        }

    }

    @Override
    public void showViewState(UIState uiState) {
        Utils.Log(TAG, "UIState: " + uiState);
        switch (uiState) {
            case LOADING:
                showLoadingIndicator(true);
                break;
            case FINISHED:
                showLoadingIndicator(false);
                break;
            case ERROR:
                showLoadingIndicator(false);
                break;
        }
    }

    private void showLoadingIndicator(boolean isShow) {
        if (isShow) {
            featuredProgress.animate().alpha(1F);
        } else {
            featuredProgress.animate().alpha(0F);
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }

    @Override
    public void setupFeaturedView(Featured featured) {
        loadFeaturedHeaderImage(featured.image);
    }

    private void loadFeaturedHeaderImage(String imgUrl) {

        glideRequests.asDrawable().load(imgUrl).into(featuredHeaderImage);
    }

    @Override
    public void populateFeaturedRelatedProducts(String featured_text) {
        Utils.Log(TAG, "Realted product size: " + featured_text);

        featuredTvMain.setText(Html.fromHtml(featured_text));

    }

    @Override
    public void showProductDetail(Product product) {
        Utils.Logs('i', TAG, "Show product detail with id: " + product.id);
        Intent productDetailIntent = new Intent(this, ProductDetailActivity.class);
        productDetailIntent.putExtra(Product.KEY_PRODUCT_ID, product.id);
        this.startActivityForResult(productDetailIntent, Constants.TAG_PRODUCT_DETAIL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_featured, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share_featured) {
            openShareScreen();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openShareScreen() {
        String strShare = currentFeatured.name;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Poken Psp");
        sendIntent.putExtra(Intent.EXTRA_TEXT, strShare);
        sendIntent.setType("text/plain");
        this.startActivity(Intent.createChooser(sendIntent, "Bagikan promo Poken"));
    }
}
