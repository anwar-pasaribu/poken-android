package id.unware.poken.ui.shoppingorder.presenter;

import java.util.ArrayList;
import java.util.Arrays;

import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.OrderDetail;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Shipping;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.domain.ShoppingOrderInserted;
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
        Utils.Logs('i', TAG, "Start Payment screen");
        view.openPaymentScreen();
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

        model.postOrUpdateOrderDetails(
                this,
                addressBook
        );
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
                newlyCreatedAddressBook
        );
    }

    @Override
    public void onAddressBookContentResponse(ArrayList<AddressBook> addressBookArrayList) {

        if (view.isActivityFinishing()) return;

        view.populateAddressBookList(addressBookArrayList);
    }

    @Override
    public void onNetworkMessage(String msg, int messageStatus) {

        if (view.isActivityFinishing()) return;

        view.showMessage(msg, messageStatus);
    }

    @Override
    public void onOrderDetailCreatedOrUpdated(OrderDetail orderDetail) {
        Utils.Logs('i', TAG, "Created/updated order detail id: " + orderDetail.id);
        Utils.Logs('i', TAG, "Created/updated order detsil.address_book_id: " + orderDetail.address_book_id);

        // Begin add ordered product
        if (previousOrderDetailId == -1) {
            // Begin to create new Ordered Product
            long[] shoppingCartIds = view.getSelectedShoppingCartIds();

            Utils.Logs('w', TAG, "BEGIN POST NEW ORDERED PRODUCT. Shopping cart ids: " + Arrays.toString(shoppingCartIds));
            model.postOrUpdateOrderedProduct(
                    this,
                    orderDetail,
                    shoppingCartIds
            );
        }

    }

    @Override
    public void onOrderedProductInserted(ShoppingOrderInserted shoppingOrderInserted) {
        // Load actual ordered data when insertion completed
        model.requestShoppingOrderData(this);
    }

    @Override
    public void onOrderDetailResponse(ShoppingOrder shoppingOrder) {
        setupOrderDetailView(shoppingOrder);
    }

    private void setupOrderDetailView(ShoppingOrder shoppingOrder) {
        ArrayList<ShoppingCart> shoppingCarts = shoppingOrder.shopping_carts;
        OrderDetail orderDetail = shoppingOrder.order_details;
        AddressBook addressBook = orderDetail.address_book;
        Shipping shipping = shoppingOrder.shopping_carts.get(0).shipping;
        Product product = shoppingOrder.shopping_carts.get(0).product;

        // Save current order detail id to prevent recreate order detail
        previousOrderDetailId = orderDetail.id;

        double grandTotal = 0D;
        ArrayList<Product> productArrayList = new ArrayList<>();
        for (ShoppingCart item : shoppingCarts) {
            productArrayList.add(item.product);

            // Count Product Grand total + shipping fee
            grandTotal += (item.product.price * item.quantity) + item.shipping.fee;
        }

        view.showOrderId(orderDetail.order_id, shoppingOrder.id);

        view.showTotalAmount(grandTotal);

        view.setupShippingReceiver(addressBook);

        Utils.Log(TAG, "All selected product size: " + productArrayList.size());
        view.showMultiSelectedProduct(shoppingCarts.size() > 1);
        if (shoppingCarts.size() <= 1) {
            view.setupSelectedProduct(shoppingCarts.get(0));
        } else {
            view.setupSelectedProduct(shoppingCarts.get(0));
            view.setupSelectedProducts(shoppingCarts);
        }

        view.setupShippingMethod(shipping);
    }
}
