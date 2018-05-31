package id.unware.poken.ui.shoppingorder.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.models.OrderStatus;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.address.view.AddressActivity;
import id.unware.poken.ui.payment.view.PaymentActivity;
import id.unware.poken.ui.shoppingorder.model.ShoppingOrderModel;
import id.unware.poken.ui.shoppingorder.presenter.ShoppingOrderPresenter;
import id.unware.poken.ui.shoppingorder.view.fragment.AddressBookDialogFragment;
import id.unware.poken.ui.shoppingorder.view.fragment.OrderedProductListDialogFragment;

public class OrderActivity extends BaseActivity implements IShoppingOrderView,
        AddressBookDialogFragment.Listener,
        OrderedProductListDialogFragment.Listener{

    private static final String TAG = "OrderActivity";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.parentClickableShippingAddress) ViewGroup parentClickableShippingAddress;

    // ORDER STATUS HEADER
    @BindView(R.id.orderParentHeader) RelativeLayout orderParentHeader;
    @BindView(R.id.orderViewFlipperStatusView) ViewFlipper orderViewFlipperStatusView;

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
    @BindView(R.id.tvProductQuantity) TextView tvProductQuantity;
    @BindView(R.id.tvProductTotalPrice) TextView tvProductTotalPrice;

    @BindView(R.id.tvSelectedShippingMethod) TextView tvSelectedShippingMethod;
    @BindView(R.id.tvSelectedShippingFee) TextView tvSelectedShippingFee;
    @BindView(R.id.ibSelectedShippingMethodMoreInfo) ImageButton ibSelectedShippingMethodMoreInfo;

    @BindView(R.id.tvTotalFee) TextView tvTotalFee;
    @BindView(R.id.orderDetailParentClickableOrderedProduct) RelativeLayout orderDetailParentClickableOrderedProduct;
    @BindView(R.id.orderDetailTvTotalOrderedProduct) TextView orderDetailTvTotalOrderedProduct;
    // Extra note (hide when no extra note)
    @BindView(R.id.orderTvExtraNoteLbl) TextView orderTvExtraNoteLbl;
    @BindView(R.id.orderTvExtraNote) TextView orderTvExtraNote;

    // SET TRACKING ID
    @BindView(R.id.parentContentOrderSellerSendOrder) RelativeLayout parentContentOrderSellerSendOrder;
    @BindView(R.id.etContentOrderSellerSendInputResi) EditText etContentOrderSellerSendInputResi;
    @BindView(R.id.btnContentOrderSellerSendSubmitResi) Button btnContentOrderSellerSendSubmitResi;

    // MAIN ACTION
    @BindView(R.id.tvTotalShoppingAmountLabel) TextView tvTotalShoppingAmountLabel;
    @BindView(R.id.tvTotalShoppingAmount) TextView tvTotalShoppingAmount;
    @BindView(R.id.btnContinueToPayment) Button btnContinueToPayment;

    // RESOURCES
    @BindDimen(R.dimen.clickable_size_64) int productImageSize;

    private Unbinder unbinder;

    private ShoppingOrderPresenter presenter;

    private boolean isSellerMode = false;

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

    private String orderRef = "";
    private long orderedProductId = -1;
    private long orderDetailsId = -1;
    private boolean isReadOnlyMode = false;
    private double totalShoppingCost = 0;
    private Date paymentDue;

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

            // When Order created before
            orderedProductId = getIntent().getExtras().getLong(Constants.EXTRA_ORDER_ID, -1);

            // Seller Mode different with Buyer mode
            isSellerMode = getIntent().getBooleanExtra(Constants.EXTRA_IS_SELLER_MODE_ORDER_PAGE, false);
        }

        presenter = new ShoppingOrderPresenter(new ShoppingOrderModel(), this /*View*/);

        if (orderedProductId != Constants.ID_NOT_AVAILABLE) {
            presenter.getShoppingOrderData(orderedProductId);

            isReadOnlyMode = true;
        } else {
            presenter.prepareOrderFromShoppingCart(shoppingCartArrayListJsonString);
        }

        initView();

        presenter.setupSellerMode(isSellerMode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Utils.Logs('w', TAG, "On activity result. Request code: " + requestCode + ", res: " + resultCode);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQ_CODE_ADDRESS_BOOK) {
                Utils.Logs('i', TAG, "Address book data found.");
                AddressBook addressBookResult = data.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS_BOOK);
                if (addressBookResult != null) {
                    setupSelectedAddressBook(addressBookResult);
                } else {
                    MyLog.FabricLog(Log.WARN, TAG + " - No parcelable address book found.");
                }
            }
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup read only mode
        if (isReadOnlyMode) {
            orderBtnChangeReceiverAddress.setVisibility(View.GONE);
        } else {
            orderBtnChangeReceiverAddress.setVisibility(View.VISIBLE);
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

        btnContentOrderSellerSendSubmitResi.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                if (presenter != null) {
                    presenter.setOrderDetailsTrackingId(orderDetailsId, etContentOrderSellerSendInputResi.getText().toString());
                }
            }
        });

        btnContinueToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // presenter.beginOrder();
                presenter.startPaymentScreen();
            }
        });
    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                showLoadingIndicator(true);
                break;
            case FINISHED:
                showLoadingIndicator(false);
                break;
            case INCOMPLETE:
                btnContinueToPayment.setEnabled(false);
                break;

        }

    }

    private void showLoadingIndicator(boolean isShow) {
        if (isShow) {
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }

            orderBtnChangeReceiverAddress.setEnabled(false);
            btnContinueToPayment.setEnabled(false);

            // Btn to add tracking id
            btnContentOrderSellerSendSubmitResi.setEnabled(false);

        } else {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }

            orderBtnChangeReceiverAddress.setEnabled(true);
            btnContinueToPayment.setEnabled(true);

            btnContentOrderSellerSendSubmitResi.setEnabled(true);
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
    public void openPaymentScreen(OrderDetail currentOrderDetails) {
        if (totalShoppingCost != 0
                && orderedProductId != -1
                && paymentDue != null
                && currentOrderDetails != null) {

            Utils.Log(TAG, "Open payment screen. Order ref: " + this.orderRef);
            Utils.Log(TAG, "Open payment screen. Order status: " + currentOrderDetails.order_status);

            Intent paymentIntent = new Intent(this, PaymentActivity.class);

            paymentIntent.putExtra(Constants.EXTRA_TOTAL_SHOPPING_COST, totalShoppingCost);
            paymentIntent.putExtra(Constants.EXTRA_ORDER_REF, this.orderRef);
            paymentIntent.putExtra(Constants.EXTRA_ORDER_ID, currentOrderDetails.id);
            paymentIntent.putExtra(Constants.EXTRA_ORDER_STATUS, currentOrderDetails.order_status);
            paymentIntent.putExtra(Constants.EXTRA_PAYMENT_DUE, paymentDue);
            this.startActivity(paymentIntent);
        } else {
            MyLog.FabricLog(Log.ERROR, "Payment activity is not ready to open.");
        }
    }

    @Override
    public void setupShippingReceiver(final AddressBook addressBook) {

        selectedAddressBookId = addressBook.id;

        tvShippingAddressName.setText(addressBook.name);
        tvShippingAddressPhone.setText(addressBook.phone);
        tvShippingAddress.setText(addressBook.address);

        if (isSellerMode) {
            tvShippingAddressPhone.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    MyLog.FabricLog(Log.INFO, "Open dialer using customer number.");
                    Utils.openDialer(OrderActivity.this, addressBook.phone);
                }
            });
        }
    }

    @Override
    public void setupSelectedProduct(ShoppingCart shoppingCart) {

        Product product = shoppingCart.product;
        int totalProductCount = shoppingCart.quantity;
        double productTotalPrice = shoppingCart.total_price;
        double shippingFee = shoppingCart.shipping_fee;
        String shippingMethod = shoppingCart.shipping.name;
        double grandTotal = productTotalPrice + shippingFee;

        Picasso.with(this)
                .load(product.images.get(0).path)
                .resize(productImageSize, productImageSize)
                .centerCrop()
                .into(ivSelectedProduct);

        tvSelectedProductName.setText(product.name);
        tvProductQuantity.setText(this.getString(R.string.lbl_quantity, totalProductCount));
        tvProductTotalPrice.setText(StringUtils.formatCurrency(String.valueOf(productTotalPrice)));
        tvSelectedShippingMethod.setText(shippingMethod);
        tvSelectedShippingFee.setText(StringUtils.formatCurrency(String.valueOf(shippingFee)));
        tvTotalFee.setText(StringUtils.formatCurrency(String.valueOf(grandTotal)));

        // Extra note
        if (StringUtils.isEmpty(shoppingCart.extra_note)) {
            orderTvExtraNoteLbl.setVisibility(View.GONE);
            orderTvExtraNote.setVisibility(View.GONE);
        } else {

            orderTvExtraNoteLbl.setVisibility(View.VISIBLE);
            orderTvExtraNote.setVisibility(View.VISIBLE);

            orderTvExtraNote.setText(shoppingCart.extra_note);
        }
    }

    @Override public void onShowPackageTrackingId(String shippingTrackingId) {
        etContentOrderSellerSendInputResi.setText(shippingTrackingId);
        etContentOrderSellerSendInputResi.setSelection(etContentOrderSellerSendInputResi.getText().length());
    }

    @Override
    public void setupSelectedProducts(ArrayList<ShoppingCart> shoppingCarts) {
        Utils.Logs('i', TAG, "Selected shopping carts size: " + shoppingCarts.size());
        this.selectedShoopingCarts = shoppingCarts;
    }

    @Override
    public void setupShippingMethod(final Shipping shipping) {
        Utils.Logs('i', TAG, "Setup shipping:" + shipping);
        if (ibSelectedShippingMethodMoreInfo == null) return;

        ibSelectedShippingMethodMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                ControllerDialog.getInstance().showDialogInfo(
                        getString(R.string.title_selected_shipping_service),
                        String.valueOf(shipping.service),
                        OrderActivity.this

                );
            }
        });

        tvSelectedShippingMethod.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                ControllerDialog.getInstance().showDialogInfo(
                        getString(R.string.title_selected_shipping_service),
                        String.valueOf(shipping.service),
                        OrderActivity.this

                );
            }
        });
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
    public void showOrderId(String orderDetailUniqueId, long orderDetailsId, long orderedProductId) {

        // #0 Ordered Product ID
        this.orderedProductId = orderedProductId;

        // #1 Order Detail Unique Identifier
        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(this.getString(R.string.lbl_order_ref_id, orderDetailUniqueId));
        }

        this.orderRef = orderDetailUniqueId;

        // #2 Order details ID
        this.orderDetailsId = orderDetailsId;
    }

    @Override
    public void populateAddressBookList(ArrayList<AddressBook> addressBookArrayList) {

        // Turn "EDIT" Address Book ON
        orderBtnChangeReceiverAddress.setEnabled(true);

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
            orderDetailTvTotalOrderedProduct.setText(
                    this.getString(R.string.btn_order_show_other_order, selectedProductSize - 1)
            );
        } else {
            orderDetailParentClickableOrderedProduct.setVisibility(View.GONE);
        }
    }

    @Override
    public void setupPaymentView(OrderDetail orderDetail) {
        this.paymentDue = orderDetail.payment_expiration_date;
    }

    @Override
    public void showPayNowView(boolean isShow) {
        Utils.Logs('i', TAG, "Show Pay Now View --> " + isShow);
        if (isShow) {
            btnContinueToPayment.setVisibility(View.VISIBLE);
        } else {
            btnContinueToPayment.setVisibility(View.GONE);
        }
    }

    @Override
    public void showViewStatusBooked() {
        orderViewFlipperStatusView.setDisplayedChild(OrderStatus.ORDERED);
    }

    @Override
    public void showViewStatusPaid() {
        orderViewFlipperStatusView.setDisplayedChild(OrderStatus.PAID);
    }

    @Override
    public void showViewStatusSent() {
        orderViewFlipperStatusView.setDisplayedChild(OrderStatus.SENT);

        // Setup view
        ViewGroup viewGroup = (ViewGroup) orderViewFlipperStatusView.getChildAt(OrderStatus.SENT);
        Button statusSentBtnConfirmAccepted = viewGroup.findViewById(R.id.statusSentBtnConfirmAccepted);
        Button statusSentBtnReturn = viewGroup.findViewById(R.id.statusSentBtnReture);
        statusSentBtnConfirmAccepted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControllerDialog.getInstance().showYesNoDialog(
                        getString(R.string.msg_order_confirm_order_received),
                        OrderActivity.this,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (DialogInterface.BUTTON_POSITIVE == i) {
                                    if (presenter != null) {
                                        presenter.confirmOrderReceived(orderDetailsId);
                                    }

                                    dialogInterface.dismiss();
                                }
                            }
                        }
                );
            }
        });

        statusSentBtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControllerDialog.getInstance().showYesNoDialog(
                        getString(R.string.msg_order_return_order),
                        OrderActivity.this,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (DialogInterface.BUTTON_POSITIVE == i) {
                                    if (presenter != null) {
                                        presenter.retureOrder(orderDetailsId);
                                    }

                                    dialogInterface.dismiss();
                                }
                            }
                        }, "Reture", "Tidak"
                );
            }
        });

        // Hide buttons on Seller mode
        if (isSellerMode) {
            statusSentBtnConfirmAccepted.setVisibility(View.GONE);
            statusSentBtnReturn.setVisibility(View.GONE);
        }
    }

    @Override
    public void showViewStatusReceived() {
        orderViewFlipperStatusView.setDisplayedChild(OrderStatus.RECEIVED);
    }

    @Override
    public void showViewStatusSuccess() {
        orderViewFlipperStatusView.setDisplayedChild(OrderStatus.SUCCESS);
    }

    @Override
    public void showViewStatusReturn() {
        orderViewFlipperStatusView.setDisplayedChild(OrderStatus.REFUND);
    }

    @Override public void showSellerProceedButton() {
        btnContinueToPayment.setVisibility(View.VISIBLE);
        btnContinueToPayment.setOnClickListener(null);
        btnContinueToPayment.setText(R.string.btn_proceed_send);
        btnContinueToPayment.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                // Send package
                if (presenter != null
                        && !StringUtils.isEmpty(etContentOrderSellerSendInputResi.getText().toString())) {
                    presenter.sendPackage(orderDetailsId);
                } else {
                    Utils.toast(OrderActivity.this, getString(R.string.msg_tracking_id_empty));
                    etContentOrderSellerSendInputResi.requestFocus();
                }
            }
        });
    }

    @Override public void showForSellerSection(boolean isShow) {
        if (isShow) {
            parentContentOrderSellerSendOrder.setVisibility(View.VISIBLE);
        } else {
            parentContentOrderSellerSendOrder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showAddressBookScreen(boolean isAddressBookAvailable) {
        Utils.Logs('v', TAG, "Show address book dialog.");

        Intent addressBookIntent = new Intent(this, AddressActivity.class);
        addressBookIntent.putExtra(Constants.EXTRA_ORDER_ID, this.orderDetailsId);
        startActivityForResult(addressBookIntent, Constants.REQ_CODE_ADDRESS_BOOK);

//        addressBookDialogFragment =
//                AddressBookDialogFragment.newInstance(
//                        0,
//                        selectedAddressBookIndex,
//                        isAddressBookAvailable  /* Open when address book not available*/
//                );
//
//        addressBookDialogFragment.show(
//                this.getSupportFragmentManager(),
//                "addressbook-dialog"
//        );
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

        // Add new address book to list in order to show on address book seletion
        if (!this.addressBookArrayList.contains(addressBook)) {
            this.addressBookArrayList.add(0, addressBook);
        }
    }

    @Override
    public void onViewReady() {
        addressBookDialogFragment.setListData(this.addressBookArrayList);
    }

    private void setupSelectedAddressBook(AddressBook addressBook) {
        this.selectedAddressBookId = addressBook.id;

        this.setupShippingReceiver(addressBook);
        this.showNoReceiverAddressView(false);

        // Update order detail with selected address book
        this.presenter.createOrUpdateOrderDetail(
                this.getSelectedShoppingCartIds(),
                addressBook
        );
    }
}
