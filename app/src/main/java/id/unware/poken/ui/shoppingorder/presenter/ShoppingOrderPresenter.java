package id.unware.poken.ui.shoppingorder.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.domain.ShoppingOrderInserted;
import id.unware.poken.models.OrderStatus;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingorder.model.IShoppingOrderModel;
import id.unware.poken.ui.shoppingorder.view.IShoppingOrderView;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ShoppingOrderPresenter implements IShoppingOrderPresenter, IShoppingOrderModelPresenter {

    private final static String TAG = "ShoppingOrderPresenter";

    private final IShoppingOrderModel model;
    private final IShoppingOrderView view;

    /** Save active order id */
    private long previousOrderDetailId = -1L;

    private OrderDetail currentOrderDetails;
    private boolean isPaymentScreenRequested = false;
    private int previousOrderStatusNumber = OrderStatus.INITIALIZE;
    private boolean isSellerMode = false;

    public ShoppingOrderPresenter(IShoppingOrderModel model, IShoppingOrderView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void addNewAddressBook(AddressBook addressBook) {
        model.postNewAddressBook(this, addressBook);
    }

    @Override
    public void getAddressBookData() {
        model.getAddressBookData(this);
    }

    @Override
    public void getShoppingOrderData(long orderedProductId) {

        MyLog.FabricLog(Log.INFO, TAG + " - Get sopping order data by id: " + orderedProductId);

        if (orderedProductId == Constants.ID_NOT_AVAILABLE) {
            model.requestShoppingOrderData(this);
        } else {
            model.requestShoppingOrderDataById(this, orderedProductId);
        }
    }

    @Override
    public void startPaymentScreen() {
        if (view.isOrderReady() && currentOrderDetails != null) {
            Utils.Logs('i', TAG, "Start Payment screen");
            view.openPaymentScreen(currentOrderDetails);
        } else {
            Utils.Logs('e', TAG, "Start Payment screen not ready.");

        }
    }

    @Override
    public void startAddressBookScreen(boolean isAddressBookAvailable) {
        view.showAddressBookScreen(isAddressBookAvailable);
    }

    @Override
    public void startSelectedProductScreen() {
        view.showSelectedProductDialog();
    }

    @Override public void setOrderDetailsTrackingId(long orderDetailsId, String trackingId) {
        Utils.Log(TAG, "Update tracking id. ID: " + trackingId);
        this.model.patchOrderDetailsTrackingId(this, orderDetailsId, trackingId);
    }

    @Override
    public void createOrUpdateOrderDetail(long[] selectedShoppingCartIds, AddressBook addressBook) {

        Utils.Log(TAG, "Create Or Update Order Detail with address book id: " + addressBook.id);
        Utils.Log(TAG, "Selected shopping cart ids: " + Arrays.toString(selectedShoppingCartIds));
        Utils.Log(TAG, "Previous order id: " + previousOrderDetailId);

        model.postOrUpdateOrderDetails(
                this,
                addressBook,
                previousOrderDetailId
        );
    }

    @Override
    public void prepareOrderFromShoppingCart(String shoppingCartArrayListJsonString) {

        // Load address book online
        model.getAddressBookData(this);

        // Parse String of ArrayList<ShoppingCart> then show on view.
        model.parseSelectedShoppingCarts(
                this,
                shoppingCartArrayListJsonString
        );
    }

    @Override
    public void confirmOrderReceived(long orderDetailsId) {
        model.patchOrderDetailsStatus(this, orderDetailsId, OrderStatus.RECEIVED);
    }

    @Override
    public void retureOrder(long orderDetailsId) {
        model.patchOrderDetailsStatus(this, orderDetailsId, OrderStatus.REFUND);
    }

    @Override
    public void beginOrder() {

        this.isPaymentScreenRequested = true;

        // Trigger Slack Notif
        if (this.previousOrderStatusNumber == OrderStatus.INITIALIZE) {
            model.patchOrderDetailsStatus(
                    this,
                    this.previousOrderDetailId,
                    OrderStatus.ORDERED
            );
        }
    }

    @Override public void setupSellerMode(boolean isSellerMode) {
        if (view.isActivityFinishing()) return;
        this.isSellerMode = isSellerMode;
        view.showSellerProceedButton();
        view.showForSellerSection(true);
    }

    @Override public void sendPackage(long orderDetailsId) {
        Utils.Log(TAG, "Setup order details as sent. Order details id: " + orderDetailsId);
        // SELLER ACTION - SELLER BEGIN SEND ORDER
        model.patchOrderDetailsStatus(this, orderDetailsId, OrderStatus.SENT);
    }

    @Override
    public void updateViewState(UIState uiState) {

        if (view.isActivityFinishing()) return;

        Utils.Logs('w', TAG, "Update view state: " + uiState);
        view.showViewState(uiState);
    }

    @Override
    public void onShoppingOrderDataResponse(ArrayList<ShoppingOrder> shoppingOrders) {
        Utils.Logs('i', TAG, "onShoppingOrderDataResponse. Size: " + shoppingOrders.size());

        if (view.isActivityFinishing()) return;

        if (shoppingOrders.size() > 0) {

            view.showNoReceiverAddressView(false);

            setupOrderDetailView(shoppingOrders.get(0));

        } else {

            view.showNoReceiverAddressView(true);
        }
    }

    @Override
    public void onAddressBookCreated(AddressBook newlyCreatedAddressBook) {

        if (view.isActivityFinishing()) return;

        // Make sure "no address book" view hidden
        view.showNoReceiverAddressView(false);

        view.setupShippingReceiver(getFormattedAddress(newlyCreatedAddressBook));

        // Each new address book created, create/update order details
        Utils.Log(TAG, "New address book created. Now, create order detail with address  book id: " + newlyCreatedAddressBook.id);
        model.postOrUpdateOrderDetails(
                this,
                newlyCreatedAddressBook,
                previousOrderDetailId);
    }

    @Override
    public void onAddressBookContentResponse(ArrayList<AddressBook> addressBookArrayList) {

        if (view.isActivityFinishing()) return;

        if (addressBookArrayList.isEmpty()) {
            view.showNoReceiverAddressView(true);
        } else {
            view.showNoReceiverAddressView(false);

            // Format Addres with complete with postal code and show on view
            view.setupShippingReceiver(getFormattedAddress(addressBookArrayList.get(0)));

            view.populateAddressBookList(addressBookArrayList);

            model.postOrUpdateOrderDetails(
                    this,
                    addressBookArrayList.get(0),
                    previousOrderDetailId);
        }

    }

    @Override
    public void onNetworkMessage(String msg, int messageStatus) {

        if (view.isActivityFinishing()) return;

        view.showMessage(msg, messageStatus);
    }

    @Override
    public void onOrderDetailCreatedOrUpdated(OrderDetail orderDetail) {

        if (view.isActivityFinishing()) return;

        Utils.Logs('i', TAG, "Created/updated order detail id: " + orderDetail.id + ", status: " + orderDetail.order_status);
        Utils.Logs('i', TAG, "Created/updated order detsil.address_book_id: " + orderDetail.address_book_id);

        // Current order details
        this.currentOrderDetails = orderDetail;

        // Save previous order status number
        this.previousOrderStatusNumber = orderDetail.order_status;

        // Begin add ordered product
        if (previousOrderDetailId == -1) {

            // Save previous order detail id
            this.previousOrderDetailId = orderDetail.id;

            // Begin to create new Ordered Product
            long[] shoppingCartIds = view.getSelectedShoppingCartIds();

            Utils.Logs('w', TAG, "BEGIN POST NEW ORDERED PRODUCT. Shopping cart ids: " + Arrays.toString(shoppingCartIds));
            model.postOrUpdateOrderedProduct(
                    this,
                    orderDetail,
                    shoppingCartIds
            );
        } else {
            Utils.Logs('w', TAG, "ORDER DETAIL DATA UPDATED.");

            int orderStatusNo = orderDetail.order_status;

            //
            if (orderStatusNo == OrderStatus.ORDERED && this.isPaymentScreenRequested) {
                view.openPaymentScreen(currentOrderDetails);
            } else {
                setupOrderDetailsViewByOrderStatus(orderDetail);
            }
        }

    }

    @Override
    public void onOrderedProductInserted(ShoppingOrderInserted shoppingOrderInserted) {
        // Load actual ordered data when insertion completed
        model.requestShoppingOrderData(this);
    }

    @Override
    public void onOrderDetailResponse(ShoppingOrder shoppingOrder) {

        if (view.isActivityFinishing()) return;

        this.currentOrderDetails = shoppingOrder.order_details;

        setupOrderDetailView(shoppingOrder);
    }

    @Override
    public void onShoppingCartsParseResponse(ArrayList<ShoppingCart> shoppingCartArrayList) {

        if (view.isActivityFinishing()) return;

        setupSelectedProduct(shoppingCartArrayList);
    }

    private void setupSelectedProduct(ArrayList<ShoppingCart> shoppingCarts) {

        // Count Product Grand total + shipping fee
        double grandTotal = 0D;
        for (ShoppingCart item : shoppingCarts) {
            grandTotal += calculateShoppingItemGrandTotalPrice(item);
        }

        view.showTotalAmount(grandTotal);

        view.showMultiSelectedProduct(shoppingCarts.size());
        if (shoppingCarts.size() <= 1) {
            shoppingCarts.get(0).total_price = calculateShoppingItemTotalPrice(shoppingCarts.get(0));
            view.setupSelectedProduct(shoppingCarts.get(0));
        } else {
            shoppingCarts.get(0).total_price = calculateShoppingItemTotalPrice(shoppingCarts.get(0));
            view.setupSelectedProduct(shoppingCarts.get(0));
            view.setupSelectedProducts(shoppingCarts);
        }
    }

    /**
     * Calculate product times quantity. Without additional fee (shipping, tax, etc.)
     * @param shoppingCart Shopping Item
     * @return total price without additional fee.
     */
    private double calculateShoppingItemTotalPrice(ShoppingCart shoppingCart) {
        double originalProductPrice = shoppingCart.product.price * shoppingCart.quantity;
        double discountAmount = shoppingCart.product.discount_amount;
        shoppingCart.total_price =
                (originalProductPrice - ((originalProductPrice * discountAmount) / 100));

        return shoppingCart.total_price;
    }

    /**
     * Calculation include all additional fee (ex. tax, shipping, etc)
     * @param shoppingCart Shopping item
     * @return Grand total price (double type)
     */
    private double calculateShoppingItemGrandTotalPrice(ShoppingCart shoppingCart) {
        double originalProductPrice = shoppingCart.product.price * shoppingCart.quantity;
        double discountAmount = shoppingCart.product.discount_amount;
        double shippingFee = shoppingCart.shipping_fee;
        shoppingCart.total_price =
                (originalProductPrice - ((originalProductPrice * discountAmount) / 100)) + shippingFee;

        return shoppingCart.total_price;
    }

    private void setupOrderDetailView(ShoppingOrder orderedProduct) {

        ArrayList<ShoppingCart> shoppingCarts = orderedProduct.shopping_carts;
        OrderDetail orderDetail = orderedProduct.order_details;
        Shipping shipping = shoppingCarts.get(0).shipping;

        // Save current order detail id to prevent recreate order detail
        previousOrderDetailId = orderDetail.id;

        // Save previous order status number
        this.previousOrderStatusNumber = orderDetail.order_status;

        // Set payment due time
        view.setupPaymentView(orderDetail);

        // Show pay now when order still unpaid
        if (orderDetail.order_status == OrderStatus.ORDERED
                || orderDetail.order_status == OrderStatus.INITIALIZE
                || isSellerMode) {
            view.showPayNowView(true);
        } else {
            view.showPayNowView(false);
        }

        view.showOrderId(orderDetail.order_id, orderDetail.id, orderedProduct.id);

        view.setupShippingReceiver(getFormattedAddress(orderDetail.address_book));

        view.onShowPackageTrackingId(orderDetail.shipping_tracking_id);

        setupSelectedProduct(shoppingCarts);

        // Set Shipping Service which is available on each Shopping Carts
        shipping.service = shoppingCarts.get(0).shipping_service;
        view.setupShippingMethod(shipping);

        setupOrderDetailsViewByOrderStatus(orderDetail);

    }

    private void setupOrderDetailsViewByOrderStatus(OrderDetail orderDetail) {
        // DECIDE ORDER STATUS VIEW
        if (orderDetail.order_status == OrderStatus.ORDERED) {
            view.showViewStatusBooked();
        } else if (orderDetail.order_status == OrderStatus.PAID) {
            view.showViewStatusPaid();
        } else if (orderDetail.order_status == OrderStatus.SENT) {
            view.showViewStatusSent();
        } else if (orderDetail.order_status == OrderStatus.RECEIVED) {
            view.showViewStatusReceived();
        } else if (orderDetail.order_status == OrderStatus.REFUND) {
            view.showViewStatusReturn();
        } else if (orderDetail.order_status == OrderStatus.SUCCESS) {
            view.showViewStatusSuccess();
        }
    }

    private AddressBook getFormattedAddress(AddressBook addressBook) {

        String strDistrict = addressBook.location != null? addressBook.location.district : "";
        String strCity = addressBook.location != null? addressBook.location.city : "";
        String strZip = addressBook.location != null? addressBook.location.zip : "";
        addressBook.address = String.format("%s, %s, %s %s", addressBook.address, strDistrict, strCity, strZip);

        return addressBook;

    }
}
