package id.unware.poken.ui.seller.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.MemoryCategory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Seller;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideApp;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;
import id.unware.poken.ui.seller.model.SellerPageModel;
import id.unware.poken.ui.seller.presenter.SellerPagePresenter;
import id.unware.poken.ui.seller.view.adapter.SellerProductAdapter;

import static android.R.attr.bitmap;
import static id.unware.poken.R.id.itemImage;
import static id.unware.poken.R.id.rvOrderedItem;

public class SellerActivity extends BaseActivity implements ISellerPageView {

    private static final String TAG = "SellerActivity";

    @BindView(R.id.ivUserAvatar) ImageView ivUserAvatar;

    @BindView(R.id.tvSellerUser) TextView tvSellerUser;

    @BindView(R.id.sellerIconLocation) ImageView sellerIconLocation;
    @BindView(R.id.tvSellerIdetifier) TextView tvSellerIdetifier;

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvSellerProduct) RecyclerView rvSellerProduct;

    @BindDimen(R.dimen.profile_avatar_m) int profile_avatar_m;

    private Unbinder unbinder;

    private SellerPagePresenter presenter;

    private SellerProductAdapter adapter;
    private ArrayList<Product> listItem = new ArrayList<>();

    private long sellerId;

    private Handler handler = new Handler();
    private Runnable reqSellerPageRun = new Runnable() {
        @Override
        public void run() {
            Utils.Log(TAG, "Begin gitting seller page data.");
            presenter.getSellerPageProductData(sellerId);
        }
    };

    private GlideRequests glideRequests;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        GlideApp.get(this).setMemoryCategory(MemoryCategory.HIGH);

        glideRequests = GlideApp.with(this);

        unbinder = ButterKnife.bind(this);

        presenter = new SellerPagePresenter(new SellerPageModel(), this);

        if (getIntent().getExtras() != null) {
            sellerId = getIntent().getExtras().getLong(Constants.KEY_DOMAIN_ITEM_ID, -1L);
            Utils.Logs('i', TAG, "Seller id to show: " + sellerId);
        }

        handler.postDelayed(reqSellerPageRun, this.getResources().getInteger(android.R.integer.config_mediumAnimTime));

        initView();

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getSellerPageProductData(sellerId);
            }
        });

        initSellerPageList();

    }

    private void initSellerPageList() {
        adapter = new SellerProductAdapter(listItem, presenter, glideRequests);
        adapter.setHasStableIds(true);
        rvSellerProduct.setLayoutManager(new GridLayoutManager(this, 2));
        rvSellerProduct.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_seller, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Utils.Log(TAG, "Home navigation clicked.");
            this.onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_toggle_subscribe) {
            Utils.Log(TAG, "Toggle berlangganan: " + item.getTitle());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(reqSellerPageRun);
        unbinder.unbind();

    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                swipeRefreshLayout.setRefreshing(true);
                break;
            case FINISHED:
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }

    @Override
    public void pupolateSellerProductList(ArrayList<Product> products) {
        Utils.Log(TAG, "Product response size: " + products.size());
        listItem.clear();
        listItem.addAll(products);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProductDetail(Product product) {
        Utils.Log(TAG, "Open collection detail ID: " + product.id);
        Intent productDetailIntent = new Intent(this, ProductDetailActivity.class);
        productDetailIntent.putExtra(Product.KEY_PRODUCT_ID, product.id);
        this.startActivityForResult(productDetailIntent, Constants.TAG_PRODUCT_DETAIL);
    }

    @Override
    public void showSellerInfo(Seller seller) {

        tvSellerUser.setText(seller.store_name);

        tvSellerIdetifier.setText(seller.location);  // Alpha still 0
        sellerIconLocation.animate().alpha(1F).withEndAction(new Runnable() {
            @Override
            public void run() {
                tvSellerIdetifier.animate().alpha(1F);
            }
        });


        Utils.Log(TAG, "Seller img url: " + seller.store_avatar);

        if (!StringUtils.isEmpty(seller.store_avatar)) {
            glideRequests.asDrawable()
                    .clone()
                    .load(seller.store_avatar)
                    .circleCrop()
                    .placeholder(R.drawable.ic_circle_24dp)
                    .into(ivUserAvatar);
        }
    }
}
