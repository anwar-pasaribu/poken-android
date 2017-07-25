package id.unware.poken.ui.shoppingorder.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.payment.view.PaymentActivity;
import id.unware.poken.ui.shoppingorder.model.ShoppingOrderModel;
import id.unware.poken.ui.shoppingorder.presenter.ShoppingOrderPresenter;
import id.unware.poken.ui.shoppingorder.view.fragment.AddressBookDialogFragment;
import id.unware.poken.ui.shoppingorder.view.fragment.OrderedProductListDialogFragment;

public class OrderActivity extends AppCompatActivity implements IShoppingOrderView,
        AddressBookDialogFragment.Listener,
        OrderedProductListDialogFragment.Listener{

    private static final String TAG = "OrderActivity";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.parentClickableShippingAddress) ViewGroup parentClickableShippingAddress;

    // ADDRESS BOOK SECTION
    @BindView(R.id.parentNoShippingAddress) RelativeLayout parentNoShippingAddress;
    @BindView(R.id.orderDetailNoAddressBook) TextView orderDetailNoAddressBook;
    @BindView(R.id.orderBtnChangeReceiverAddress) Button orderBtnChangeReceiverAddress;
    @BindView(R.id.tvShippingAddressName) TextView tvShippingAddressName;
    @BindView(R.id.tvShippingAddressPhone) TextView tvShippingAddressPhone;
    @BindView(R.id.tvShippingAddress) TextView tvShippingAddress;

    // SELECTED PRODUCT SECTION
    @BindView(R.id.ivSelectedProduct) ImageView ivSelectedProduct;
    @BindView(R.id.tvSelectedProductName) TextView tvSelectedProductName;
    @BindView(R.id.orderDetailParentClickableOrderedProduct) RelativeLayout orderDetailParentClickableOrderedProduct;
    @BindView(R.id.orderDetailTvTotalOrderedProduct) TextView orderDetailTvTotalOrderedProduct;

    // PAYMENT TYPE BUTTON
    @BindView(R.id.tvSelectedShippingMethodName) TextView tvSelectedShippingMethodName;
    @BindView(R.id.tvSelectedShippingMethodFee) TextView tvSelectedShippingMethodFee;

    // MAIN ACTION
    @BindView(R.id.tvTotalShoppingAmountLabel) TextView tvTotalShoppingAmountLabel;
    @BindView(R.id.tvTotalShoppingAmount) TextView tvTotalShoppingAmount;
    @BindView(R.id.btnContinueToPayment) Button btnContinueToPayment;

    // RESOURCES
    @BindDimen(R.dimen.clickable_size) int productImageSize;

    private Unbinder unbinder;

    private ShoppingOrderPresenter presenter;

    private long[] shoppingCartIds;
    private String shoppingCartArrayListJsonString;

    // Address book dialog
    private AddressBookDialogFragment addressBookDialogFragment;
    private ArrayList<AddressBook> addressBookArrayList = new ArrayList<>();

    // Shopping carts dialog
    private OrderedProductListDialogFragment orderedProductListDialogFragment;
    private ArrayList<ShoppingCart> selectedShoopingCarts;

    private long selectedAddressBookId = -1L;
    private int selectedAddressBookIndex = 0;

    private long orderedProductId = -1;
    private boolean isReadOnlyMode = false;
    private double totalShoppingCost = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        unbinder = ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {

            // Get shopping cart ids from shopping cart screen
            shoppingCartIds = getIntent().getLongArrayExtra(Constants.EXTRA_SELECTED_SHOPPING_CART_IDS);

            // Get ShoppingCart arrayList JSON String
            shoppingCartArrayListJsonString = getIntent().getStringExtra(Constants.EXTRA_SELECTED_SHOPPING_CART);

            orderedProductId = getIntent().getExtras().getLong(Constants.EXTRA_ORDER_ID, -1);
            if (orderedProductId != -1) {
                isReadOnlyMode = true;
            }

        }

        presenter = new ShoppingOrderPresenter(new ShoppingOrderModel(), this /*View*/);

        // Load address book first
        presenter.getAddressBookData();

        if (orderedProductId != -1) {
            presenter.getShoppingOrderData(orderedProductId);
        } else {
            presenter.prepareOrderFromShoppingCart(shoppingCartArrayListJsonString);
        }

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup read only mode
        if (isReadOnlyMode) {
            orderBtnChangeReceiverAddress.setVisibility(View.GONE);
            btnContinueToPayment.setVisibility(View.GONE);
        } else {
            orderBtnChangeReceiverAddress.setVisibility(View.VISIBLE);
            btnContinueToPayment.setVisibility(View.VISIBLE);
        }

        // ADDRESS BOOK SCREEN TRIGGER
        orderBtnChangeReceiverAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {

                    presenter.startAddressBookScreen(true);

                }
            }
        });

        orderDetailParentClickableOrderedProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.startSelectedProductScreen();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getShoppingOrderData(orderedProductId);
            }
        });

        btnContinueToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startPaymentScreen();
            }
        });
    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                swipeRefreshLayout.setRefreshing(true);
                orderBtnChangeReceiverAddress.setEnabled(false);
                btnContinueToPayment.setEnabled(false);
                break;
            case FINISHED:
                swipeRefreshLayout.setRefreshing(false);
                orderBtnChangeReceiverAddress.setEnabled(true);
                btnContinueToPayment.setEnabled(true);
                break;
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }

    @Override
    public boolean isOrderReady() {

        if (selectedAddressBookId == -1) {
            this.showMessage(this.getString(R.string.msg_order_receiver_empty), Log.WARN);
            return false;
        }

        return true;
    }

    @Override
    public void openPaymentScreen() {
        Utils.Log(TAG, "Open payment screen.");
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        paymentIntent.putExtra(Constants.EXTRA_TOTAL_SHOPPING_COST, totalShoppingCost);
        paymentIntent.putExtra(Constants.EXTRA_ORDER_ID, orderedProductId);
        this.startActivity(paymentIntent);
    }

    @Override
    public void setupShippingReceiver(AddressBook addressBook) {

        selectedAddressBookId = addressBook.id;

        tvShippingAddressName.setText(addressBook.name);
        tvShippingAddressPhone.setText(addressBook.phone);
        tvShippingAddress.setText(addressBook.address);
    }

    @Override
    public void setupSelectedProduct(ShoppingCart shoppingCart) {

        Product product = shoppingCart.product;

        Picasso.with(this)
                .load(product.images.get(0).path)
                .resize(productImageSize, productImageSize)
                .centerCrop()
                .into(ivSelectedProduct);

        tvSelectedProductName.setText(product.name);

    }

    @Override
    public void setupSelectedProducts(ArrayList<ShoppingCart> shoppingCarts) {
        Utils.Logs('i', TAG, "Selected shopping carts size: " + shoppingCarts.size());
        this.selectedShoopingCarts = shoppingCarts;
    }

    @Override
    public void setupShippingMethod(Shipping shipping) {
        Utils.Logs('i', TAG, "Setup shipping method name: " + shipping.name + ", fee: " + shipping.fee);
        tvSelectedShippingMethodName.setText(shipping.name);
        tvSelectedShippingMethodFee.setText(StringUtils.formatCurrency(String.valueOf(shipping.fee)));
    }

    @Override
    public void showTotalAmount(double grandTotal) {
        this.totalShoppingCost = grandTotal;
        tvTotalShoppingAmount.setText(StringUtils.formatCurrency(String.valueOf(grandTotal)));
    }

    @Override
    public void showNoReceiverAddressView(boolean isShow) {
        parentNoShippingAddress.setVisibility(isShow? View.VISIBLE : View.GONE);
        // noinspection deprecation
        orderDetailNoAddressBook.setText(Html.fromHtml(this.getString(R.string.msg_order_detail_no_receiver_address)));

        parentClickableShippingAddress.setVisibility(isShow? View.INVISIBLE : View.VISIBLE);

    }

    @Override
    public void showOrderId(String orderDetailUniqueId, long orderedProductId) {

        // #1 Order Detail Unique Identifier
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("Order id: ".concat(orderDetailUniqueId));
        }

        // #2 Ordered Product
        this.orderedProductId = orderedProductId;
    }

    @Override
    public void populateAddressBookList(ArrayList<AddressBook> addressBookArrayList) {

        // Turn "EDIT" Address Book ON
        if (orderBtnChangeReceiverAddress != null) {
            orderBtnChangeReceiverAddress.setEnabled(true);
        }

        this.addressBookArrayList.clear();
        this.addressBookArrayList.addAll(addressBookArrayList);

        // presenter.startAddressBookScreen(this.addressBookArrayList.size() > 0);
    }

    @Override
    public void showMessage(String msg, int messageStatus) {
        if (messageStatus == Constants.NETWORK_CALLBACK_FAILURE) {
            Utils.snackBar(swipeRefreshLayout, msg, Log.ERROR);
        }

        Utils.snackBar(swipeRefreshLayout, msg, messageStatus);
    }

    @Override
    public long[] getSelectedShoppingCartIds() {
        return shoppingCartIds;
    }

    @Override
    public void showSelectedProductDialog() {
        Utils.Logs('v', TAG, "Show selected product dialog.");

        orderedProductListDialogFragment =
                OrderedProductListDialogFragment.newInstance();
        orderedProductListDialogFragment.show(
                this.getSupportFragmentManager(),
                "selected-product-dialog"
        );
    }

    @Override
    public void showMultiSelectedProduct(int selectedProductSize) {
        Utils.Logs('v', TAG, "Is multi selected product: " + selectedProductSize);

        if (selectedProductSize > 1) {
            orderDetailParentClickableOrderedProduct.setVisibility(View.VISIBLE);
            orderDetailTvTotalOrderedProduct.setText(this.getString(R.string.btn_order_show_other_order, selectedProductSize - 1));
        } else {
            orderDetailParentClickableOrderedProduct.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAddressBookScreen(boolean isAddressBookAvailable) {
        Utils.Logs('v', TAG, "Show address book dialog.");

        addressBookDialogFragment =
                AddressBookDialogFragment.newInstance(
                        0,
                        selectedAddressBookIndex,
                        isAddressBookAvailable  /* Open when address book not available*/
                );

        addressBookDialogFragment.show(
                this.getSupportFragmentManager(),
                "addressbook-dialog"
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_order, menu);
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
            Utils.Log(TAG, "Home navigation cliked.");
            this.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onItemClicked(int position) {
        Utils.Log(TAG, "Address book clicked item on pos: " + position);

        AddressBook addressBook = this.addressBookArrayList.get(position);

        this.selectedAddressBookIndex = position;
        this.selectedAddressBookId = addressBook.id;

        this.setupShippingReceiver(addressBook);
        this.showNoReceiverAddressView(false);

        // Update order detail with selected address book
        this.presenter.createOrUpdateOrderDetail(
                this.getSelectedShoppingCartIds(),
                addressBook
        );
    }

    @Override
    public void onProductListDialogViewReady() {
        orderedProductListDialogFragment.setListContent(this.selectedShoopingCarts);
    }

    @Override
    public void onNewAddressBook(AddressBook addressBook) {
        Utils.Logs('i', TAG, "Begin to add: " + addressBook);
        if (presenter != null) {
            presenter.addNewAddressBook(addressBook);
        }
    }

    @Override
    public void onViewReady() {
        addressBookDialogFragment.setListData(this.addressBookArrayList);
    }
}
