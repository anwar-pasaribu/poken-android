package id.unware.poken.ui.shoppingcart.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingcart.model.ShoppingCartModel;
import id.unware.poken.ui.shoppingcart.presenter.ShoppingCartPresenter;
import id.unware.poken.ui.shoppingcart.view.adapter.ShoppingCartAdapter;
import id.unware.poken.ui.shoppingorder.view.OrderActivity;

import static id.unware.poken.R.id.recyclerView;

public class ShoppingCartActivity extends AppCompatActivity implements IShoppingCartView {

    private final String TAG = "ShoppingCartActivity";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvShoppingCart) RecyclerView rvShoppingCart;

    @BindView(R.id.tvTotalShoppingAmount) TextView tvTotalShoppingAmount;
    @BindView(R.id.btnContinueToPayment) Button btnContinueToPayment;

    private Unbinder unbinder;

    private ShoppingCartPresenter presenter;

    private ArrayList<ShoppingCart> itemList = new ArrayList<>();
    private ShoppingCartAdapter shoppingCartAdapter;

    private long productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        unbinder = ButterKnife.bind(this);

        presenter = new ShoppingCartPresenter(new ShoppingCartModel(), this);

        if (getIntent().getExtras() != null) {
            productId = getIntent().getExtras().getLong(Product.KEY_PRODUCT_ID, -1);
            Utils.Log(TAG, "Product ID from intent: " + productId);
        }

        // Load shopping cart online
        presenter.getShoppingCartData();

        initView();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initView() {
        setupShoppingCartList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getShoppingCartData();
            }
        });

        btnContinueToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startShoppingOrderScreen();
            }
        });
    }

    private void setupShoppingCartList() {
        // Init shopping cart list
        shoppingCartAdapter = new ShoppingCartAdapter(this, itemList, presenter);
        shoppingCartAdapter.setHasStableIds(true);
        rvShoppingCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvShoppingCart.setHasFixedSize(true);
        rvShoppingCart.setAdapter(shoppingCartAdapter);
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
    public void populateShoppingCarts(ArrayList<ShoppingCart> shoppingCarts) {
        Utils.Logs('i', TAG, "Shopping cart size: " + shoppingCarts.size());
        itemList.clear();
        itemList.addAll(shoppingCarts);
        shoppingCartAdapter.notifyDataSetChanged();
    }

    @Override
    public void updatePriceGrandTotal(String formattedPrice) {

    }

    @Override
    public void openShoppingOrder() {
        Intent shoppingOrderIntent = new Intent(this, OrderActivity.class);
        startActivity(shoppingOrderIntent);
    }
}
