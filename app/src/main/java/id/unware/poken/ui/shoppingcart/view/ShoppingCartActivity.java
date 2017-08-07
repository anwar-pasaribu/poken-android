package id.unware.poken.ui.shoppingcart.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingcart.model.ShoppingCartModel;
import id.unware.poken.ui.shoppingcart.presenter.ShoppingCartPresenter;
import id.unware.poken.ui.shoppingcart.view.adapter.ShoppingCartAdapter;
import id.unware.poken.ui.shoppingorder.view.OrderActivity;

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
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupShoppingCartList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (presenter != null) {
                    presenter.getShoppingCartData();
                }
            }
        });

        btnContinueToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.startShoppingOrderScreen();
                }
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
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }

    @Override
    public void populateShoppingCarts(ArrayList<ShoppingCart> shoppingCarts) {
        Utils.Logs('i', TAG, "Shopping cart size: " + shoppingCarts.size());
        itemList.clear();
        itemList.addAll(shoppingCarts);
        shoppingCartAdapter.notifyDataSetChanged();

        // Clear selected shopping cart on repopulate list
        selectedShoppingCart.clear();
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
        if (selectedShoppingCart.size() > 0) {
            // Collect shopping cart id to send to order screen
            int selectedShoppingCartSize = selectedShoppingCart.size();
            long[] shoppingCartIds = new long[selectedShoppingCart.size()];
            for (int i = 0; i < selectedShoppingCartSize; i++) {
                shoppingCartIds[i] = selectedShoppingCart.get(i).id;
            }

            Utils.Logs('i', TAG, "Selected shopping carts: " + Arrays.toString(shoppingCartIds));

            // Convert ShoppingCart data to String
            Gson gson = new Gson();
            String strSelectedShoppingCarts = gson.toJson(selectedShoppingCart);

            Utils.Log(TAG, "JSON ArrayList<ShoppingCart>:\n" + strSelectedShoppingCarts);

            Intent shoppingOrderIntent = new Intent(this, OrderActivity.class);
            shoppingOrderIntent.putExtra(Constants.EXTRA_SELECTED_SHOPPING_CART_IDS, shoppingCartIds);
            shoppingOrderIntent.putExtra(Constants.EXTRA_SELECTED_SHOPPING_CART, strSelectedShoppingCarts);
            startActivity(shoppingOrderIntent);
        }
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
            // Remove from selected shopping cart when available
            ShoppingCart cartItem = itemList.get(deletedItemPos);
            if (cartItem != null) {
                // Remove selected item
                boolean isSelectedItemRemoved = selectedShoppingCart.remove(cartItem);
                if (isSelectedItemRemoved) {
                    Utils.Logs('i', TAG, "Selected item has deleted.");
                }

                // Remove item from original list
                itemList.remove(deletedItemPos);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        shoppingCartAdapter.notifyItemRemoved(deletedItemPos);

        // Recheck shopping cart grand total
        presenter.calculateSelectedShoppingCarts(selectedShoppingCart);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping_cart, menu);
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
