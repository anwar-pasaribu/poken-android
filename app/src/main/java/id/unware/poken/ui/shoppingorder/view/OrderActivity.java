package id.unware.poken.ui.shoppingorder.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import okhttp3.internal.Util;

public class OrderActivity extends AppCompatActivity implements IShoppingOrderView {

    private static final String TAG = "OrderActivity";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.parentClickableShippingAddress) ViewGroup parentClickableShippingAddress;

    // ADDRESS BOOK SECTION
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


    private Unbinder unbinder;

    private ShoppingOrderPresenter presenter;

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
        int productImageSize = this.getResources().getDimensionPixelSize(R.dimen.clickable_size);
        Picasso.with(this)
                .load(product.images.get(0).path)
                .resize(productImageSize, productImageSize)
                .centerCrop()
                .into(ivSelectedProduct);

        tvSelectedProductName.setText(product.name);

    }

    @Override
    public void setupShippingMethod(Shipping shipping) {
        Utils.Logs('i', TAG, "Setup shipping method name: " + shipping.name + ", fee: " + shipping.fee);
        tvSelectedShippingMethodName.setText(shipping.name);
        tvSelectedShippingMethodFee.setText(StringUtils.formatCurrency(String.valueOf(shipping.fee)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
