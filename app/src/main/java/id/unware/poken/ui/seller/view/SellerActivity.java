package id.unware.poken.ui.seller.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;
import id.unware.poken.ui.seller.model.SellerPageModel;
import id.unware.poken.ui.seller.presenter.SellerPagePresenter;
import id.unware.poken.ui.seller.view.adapter.SellerProductAdapter;

import static id.unware.poken.R.id.rvOrderedItem;

public class SellerActivity extends BaseActivity implements ISellerPageView {

    private static final String TAG = "SellerActivity";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvSellerProduct) RecyclerView rvSellerProduct;

    private Unbinder unbinder;

    private SellerPagePresenter presenter;

    private SellerProductAdapter adapter;
    private ArrayList<Product> listItem = new ArrayList<>();

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);

        unbinder = ButterKnife.bind(this);

        presenter = new SellerPagePresenter(new SellerPageModel(), this);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Utils.Log(TAG, "Begin gitting seller page data.");
                presenter.getSellerPageProductData();
            }
        }, this.getResources().getInteger(android.R.integer.config_mediumAnimTime));

        initView();

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getSellerPageProductData();
            }
        });

        initSellerPageList();

    }

    private void initSellerPageList() {
        adapter = new SellerProductAdapter(listItem, presenter);
        adapter.setHasStableIds(true);
        rvSellerProduct.setLayoutManager(new GridLayoutManager(this, 2));
        rvSellerProduct.setHasFixedSize(true);
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
            Utils.Log(TAG, "Home navigation cliked.");
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
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
}
