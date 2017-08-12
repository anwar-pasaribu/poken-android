package id.unware.poken.ui.featured.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Featured;
import id.unware.poken.domain.Product;
import id.unware.poken.httpConnection.UrlComposer;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivityWithup;
import id.unware.poken.ui.featured.model.FeaturedModel;
import id.unware.poken.ui.featured.presenter.FeaturedPresenter;
import id.unware.poken.ui.featured.view.adapter.FeaturedRelatedProductAdapter;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;

public class FeaturedActivity extends BaseActivityWithup implements IFeaturedView {

    private static final String TAG = "FeaturedActivity";

    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbar_layout;
    @BindView(R.id.featuredProgress) ProgressBar featuredProgress;
    @BindView(R.id.featuredRv) RecyclerView featuredRv;

    private FeaturedPresenter presenter;

    private Featured currentFeatured;

    private ArrayList<Product> listItem = new ArrayList<>();
    private FeaturedRelatedProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured);
        ButterKnife.bind(this);

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adapter = new FeaturedRelatedProductAdapter(listItem, presenter);
        adapter.setHasStableIds(true);
        featuredRv.setHasFixedSize(true);
        featuredRv.setLayoutManager(new GridLayoutManager(this, 2));
        featuredRv.setAdapter(adapter);

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
        Drawable drawable = toolbar_layout.getBackground();
        Utils.Log(TAG, "Current drawable: " + String.valueOf(drawable));


        Picasso.with(this).load(imgUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                Utils.Log(TAG, "Bitmap loaded.");

                toolbar_layout.setBackground(new BitmapDrawable(
                        FeaturedActivity.this.getResources(),
                        bitmap
                ));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                MyLog.FabricLog(Log.ERROR, "Picasso error to load Featured image");
                Utils.Logs('e', TAG, "Picasso error to load Featured image");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Utils.Log(TAG, "Prepare loading featured image.");
            }
        });
    }

    @Override
    public void populateFeaturedRelatedProducts(ArrayList<Product> related_products) {
        Utils.Log(TAG, "Realted product size: " + related_products.size());

        listItem.clear();
        listItem.addAll(related_products);
        adapter.notifyDataSetChanged();

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
