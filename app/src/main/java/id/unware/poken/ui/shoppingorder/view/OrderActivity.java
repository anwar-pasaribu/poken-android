package id.unware.poken.ui.shoppingorder.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.payment.view.PaymentActivity;
import id.unware.poken.ui.shoppingorder.model.ShoppingOrderModel;
import id.unware.poken.ui.shoppingorder.presenter.ShoppingOrderPresenter;
import id.unware.poken.ui.shoppingorder.view.fragment.AddressBookDialogFragment;

import static id.unware.poken.pojo.UIState.LOADING;
import static id.unware.poken.ui.shoppingorder.view.fragment.AddressBookDialogFragment.newInstance;

public class OrderActivity extends AppCompatActivity implements IShoppingOrderView,
        AddressBookDialogFragment.Listener {

    private static final String TAG = "OrderActivity";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.parentClickableShippingAddress) ViewGroup parentClickableShippingAddress;

    // ADDRESS BOOK SECTION
    @BindView(R.id.parentNoShippingAddress) RelativeLayout parentNoShippingAddress;
    @BindView(R.id.orderBtnChangeReceiverAddress) Button orderBtnChangeReceiverAddress;
    @BindView(R.id.tvShippingAddressName) TextView tvShippingAddressName;
    @BindView(R.id.tvShippingAddressPhone) TextView tvShippingAddressPhone;
    @BindView(R.id.tvShippingAddress) TextView tvShippingAddress;

    // SELECTED PRODUCT SECTION
    @BindView(R.id.ivSelectedProduct) ImageView ivSelectedProduct;
    @BindView(R.id.tvSelectedProductName) TextView tvSelectedProductName;

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

    private AddressBookDialogFragment addressBookDialogFragment;
    private ArrayList<AddressBook> addressBookArrayList = new ArrayList<>();
    private long selectedAddressBookId = 0L;
    private int selectedAddressBookIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        unbinder = ButterKnife.bind(this);

        presenter = new ShoppingOrderPresenter(new ShoppingOrderModel(), this /*View*/);

        presenter.getShoppingOrderData();

        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // ADDRESS BOOK SCREEN TRIGGER
        orderBtnChangeReceiverAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {

                    if (addressBookArrayList.size() > 0) {
                        presenter.startAddressBookScreen();
                    } else {
                        orderBtnChangeReceiverAddress.setEnabled(false);
                        presenter.getAddressBookData();
                    }
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getShoppingOrderData();
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
                btnContinueToPayment.setEnabled(false);
                break;
            case FINISHED:
                swipeRefreshLayout.setRefreshing(false);
                btnContinueToPayment.setEnabled(true);
                break;
        }
    }

    @Override
    public void openPaymentScreen() {
        Utils.Log(TAG, "Open payment screen.");
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        this.startActivity(paymentIntent);
    }

    @Override
    public void setupShippingReceiver(AddressBook addressBook) {
        tvShippingAddressName.setText(addressBook.name);
        tvShippingAddressPhone.setText(addressBook.phone);
        tvShippingAddress.setText(addressBook.address);
    }

    @Override
    public void setupSelectedProduct(Product product) {

        Picasso.with(this)
                .load(product.images.get(0).path)
                .resize(productImageSize, productImageSize)
                .centerCrop()
                .into(ivSelectedProduct);

        tvSelectedProductName.setText(product.name);

    }

    @Override
    public void setupSelectedProducts(ArrayList<Product> products) {
        // TODO Show multi product
        Utils.Logs('i', TAG, "selected product size: " + products.size());
    }

    @Override
    public void setupShippingMethod(Shipping shipping) {
        Utils.Logs('i', TAG, "Setup shipping method name: " + shipping.name + ", fee: " + shipping.fee);
        tvSelectedShippingMethodName.setText(shipping.name);
        tvSelectedShippingMethodFee.setText(StringUtils.formatCurrency(String.valueOf(shipping.fee)));
    }

    @Override
    public void showTotalAmount(double grandTotal) {
        tvTotalShoppingAmount.setText(StringUtils.formatCurrency(String.valueOf(grandTotal)));
    }

    @Override
    public void showNoReceiverAddressView(boolean isShow) {
        parentNoShippingAddress.setVisibility(isShow? View.VISIBLE : View.GONE);

    }

    @Override
    public void showOrderId(String orderId) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("Order id: ".concat(orderId));
        }
    }

    @Override
    public void pupulateAddressBookList(ArrayList<AddressBook> addressBookArrayList) {

        // Turn "EDIT" Address Book ON
        if (orderBtnChangeReceiverAddress != null) {
            orderBtnChangeReceiverAddress.setEnabled(true);
        }

        this.addressBookArrayList.clear();
        this.addressBookArrayList.addAll(addressBookArrayList);

        if (this.addressBookArrayList.size() > 0) {
            presenter.startAddressBookScreen();
        }
    }

    @Override
    public void showAddressBookScreen() {
        Utils.Logs('v', TAG, "Show address book dialog.");

        addressBookDialogFragment =
                AddressBookDialogFragment.newInstance(0, selectedAddressBookIndex);
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
        this.setupShippingReceiver(this.addressBookArrayList.get(position));
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
