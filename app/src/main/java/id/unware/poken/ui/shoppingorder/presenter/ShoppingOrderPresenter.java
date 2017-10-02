package id.unware.poken.ui.shoppingorder.presenter;

import java.util.ArrayList;
import java.util.Arrays;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.domain.ShoppingOrderInserted;
import id.unware.poken.models.OrderStatus;
import id.unware.poken.pojo.UIState;
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
        Utils.Log(TAG, "Get sopping order data by id: " + orderedProductId);
        if (orderedProductId == -1) {
            model.requestShoppingOrderData(this);
        } else {
            model.requestShoppingOrderDataById(this, orderedProductId);
        }
    }

    @Override
    public void startPaymentScreen() {
        if (view.isOrderReady()) {
            Utils.Logs('i', TAG, "Start Payment screen");
            view.openPaymentScreen();
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

        view.setupShippingReceiver(newlyCreatedAddressBook);

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
            view.setupShippingReceiver(addressBookArrayList.get(0));
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

        // Begin add ordered product
        if (previousOrderDetailId == -1) {

            // Save previous order detail id
            previousOrderDetailId = orderDetail.id;

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
            setupOrderDetailsViewByOrderStatus(orderDetail);
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
     * Calculate product times quantity.
     * @param shoppingCart Shopping Item
     * @return total price without additional fee.
     */
    private double calculateShoppingItemTotalPrice(ShoppingCart shoppingCart) {
        double originalProductPrice = shoppingCart.product.price * shoppingCart.quantity;
        double discountAmount = shoppingCart.product.discount_amount;
        shoppingCart.total_price = (originalProductPrice - ((originalProductPrice * discountAmount) / 100));

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
        double shippingFee = shoppingCart.shipping.fee;
        shoppingCart.total_price = (originalProductPrice - ((originalProductPrice * discountAmount) / 100)) + shippingFee;

        return shoppingCart.total_price;
    }

    private void setupOrderDetailView(ShoppingOrder shoppingOrder) {
        ArrayList<ShoppingCart> shoppingCarts = shoppingOrder.shopping_carts;
        OrderDetail orderDetail = shoppingOrder.order_details;
        AddressBook addressBook = orderDetail.address_book;
        Shipping shipping = shoppingCarts.get(0).shipping;

        // Save current order detail id to prevent recreate order detail
        previousOrderDetailId = orderDetail.id;

        // Set payment due time
        view.setupPaymentView(orderDetail);

        // Show pay now when order still unpaid
        if (orderDetail.order_status == OrderStatus.ORDERED) {
            view.showPayNowView(true);
        } else {
            view.showPayNowView(false);
        }

        view.showOrderId(orderDetail.order_id, orderDetail.id, shoppingOrder.id);

        view.setupShippingReceiver(addressBook);

        setupSelectedProduct(shoppingCarts);

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
        }
    }
}
