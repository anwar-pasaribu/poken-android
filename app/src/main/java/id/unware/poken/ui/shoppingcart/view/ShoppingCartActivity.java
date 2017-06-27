package id.unware.poken.ui.shoppingcart.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingcart.model.ShoppingCartModel;
import id.unware.poken.ui.shoppingcart.presenter.ShoppingCartPresenter;
import id.unware.poken.ui.shoppingcart.view.adapter.ShoppingCartAdapter;
import id.unware.poken.ui.shoppingorder.view.OrderActivity;

import static id.unware.poken.R.id.recyclerView;
import static id.unware.poken.R.id.select_dialog_listview;

public class ShoppingCartActivity extends AppCompatActivity implements IShoppingCartView {

    private final String TAG = "ShoppingCartActivity";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvShoppingCart) RecyclerView rvShoppingCart;

    @BindView(R.id.tvTotalShoppingAmount) TextView tvTotalShoppingAmount;
    @BindView(R.id.btnContinueToPayment) Button btnContinueToPayment;

    private Unbinder unbinder;

    private ShoppingCartPresenter presenter;

    private ArrayList<ShoppingCart> selectedShoppingCart = new ArrayList<>();

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
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        if (this.isFinishing()) return; // Abort view related operation when Activity is N/A

        switch (uiState) {
            case LOADING:
                btnContinueToPayment.setEnabled(false);
                swipeRefreshLayout.setRefreshing(true);
                break;
            case FINISHED:
                btnContinueToPayment.setEnabled(true);
                swipeRefreshLayout.setRefreshing(false);
                break;
            case ERROR:
                btnContinueToPayment.setEnabled(false);
                Utils.Logs('e', TAG, "Load data error.");
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
    public void updatePriceGrandTotal(double totalPrice) {
        Utils.Logs('i', TAG, "Update grand total: " + StringUtils.formatCurrency(String.valueOf(totalPrice)));
        tvTotalShoppingAmount.setText(StringUtils.formatCurrency(String.valueOf(totalPrice)));
    }

    @Override
    public void onShoppingCartItemSelected(int itemPos, boolean isChecked, ShoppingCart shoppingCart) {
        try {
            if (isChecked) {
                // Add selected item
                if (!selectedShoppingCart.contains(shoppingCart)) {
                    selectedShoppingCart.add(shoppingCart);
                } else {
                    Utils.Logs('e', TAG, "Shopping item " + shoppingCart.id + " already on list.");
                }
            } else {
                boolean unCheckItem = selectedShoppingCart.remove(shoppingCart);
                Utils.Logs('w', TAG, "Item un checked success --> " + unCheckItem);
            }
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }

        presenter.calculateSelectedShoppingCarts(selectedShoppingCart);
    }

    @Override
    public void onShoppingCartItemQuantityChanges(int itemPos, ShoppingCart shoppingCart) {
        boolean isItemChecked = selectedShoppingCart.contains(shoppingCart);
        try {
            if (isItemChecked) {
                int selectedItemPos = selectedShoppingCart.indexOf(shoppingCart);
                if (selectedItemPos != -1) {
                    selectedShoppingCart.set(selectedItemPos, shoppingCart);
                } else {
                    Utils.Logs('w', TAG, "Item id " + shoppingCart.id + " position is not available on selected list.");
                }
            } else {
                Utils.Logs('w', TAG, "Item id " + shoppingCart.id + " is not checked.");
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        presenter.calculateSelectedShoppingCarts(selectedShoppingCart);
    }

    @Override
    public void openShoppingOrder() {
        Intent shoppingOrderIntent = new Intent(this, OrderActivity.class);
        startActivity(shoppingOrderIntent);
    }

    @Override
    public void toggleContinueOrderButton(final boolean isActive) {
        Utils.Log(TAG, "Toggle begin order button --> " + isActive);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (btnContinueToPayment != null) {
                    btnContinueToPayment.setEnabled(isActive);
                }
            }
        }, this.getResources().getInteger(android.R.integer.config_shortAnimTime));
    }

    @Override
    public void deleteShoppingCartItem(int deletedItemPos) {

        try {
            itemList.remove(deletedItemPos);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        shoppingCartAdapter.notifyItemRemoved(deletedItemPos);

        if (itemList.isEmpty() || shoppingCartAdapter.getItemCount() <= 0) {
            toggleContinueOrderButton(false);
        }
    }
}
