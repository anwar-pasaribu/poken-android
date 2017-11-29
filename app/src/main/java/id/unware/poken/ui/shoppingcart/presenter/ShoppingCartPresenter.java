package id.unware.poken.ui.shoppingcart.presenter;

import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;

import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.shoppingcart.model.IShoppingCartModel;
import id.unware.poken.ui.shoppingcart.view.IShoppingCartView;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ShoppingCartPresenter implements IShoppingCartPresenter, IShoppingCartModelPresenter {

    private final static String TAG = "ShoppingCartPresenter";

    private final IShoppingCartModel model;
    private final IShoppingCartView view;
    private ArrayList<ShoppingCart> currentShoppingCarts = new ArrayList<>();
    private ArrayList<ShoppingCart> selectedShoppingCart = new ArrayList<>();

    public ShoppingCartPresenter(IShoppingCartModel model, IShoppingCartView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public void calculateSelectedShoppingCarts(ArrayList<ShoppingCart> shoppingCarts) {
        Utils.Log(TAG, "Selected item size: " + shoppingCarts.size());

        // Set selected shopping cart
        selectedShoppingCart.clear();
        selectedShoppingCart.addAll(shoppingCarts);

        SparseArray<ArrayList<ShoppingCart>> groupedCartByStore = new SparseArray<>();

        int totalQuantity = 0;
        double totalPrice = 0D;
        double originalProductPrice;
        // GROUPING SHOPPING CART BY STORE ID
        for (ShoppingCart item : shoppingCarts) {
            ArrayList<ShoppingCart> groupedSc = groupedCartByStore.get((int) item.product.seller.id);
            if (groupedSc == null) {
                groupedSc = new ArrayList<>();
            }
            groupedSc.add(item);
            groupedCartByStore.put((int) item.product.seller.id, groupedSc);

            totalQuantity = totalQuantity + item.quantity;
        }

        int groupedScSize = groupedCartByStore.size();
        for (int i = 0; i < groupedScSize; i++) {
            int key = groupedCartByStore.keyAt(i);
            ArrayList<ShoppingCart> val = groupedCartByStore.get(key);

            if (val == null) continue;

            Utils.Log(TAG, "Key: " + key);
            Utils.Log(TAG, "Val size: " + val.size());

            double lastShippingFee = 0;
            for (ShoppingCart item : val) {
                originalProductPrice = (item.product.price * item.quantity);
                totalPrice += (originalProductPrice - ((originalProductPrice * item.product.discount_amount) / 100));

                if (item.shipping_fee > lastShippingFee) {
                    totalPrice += item.shipping_fee;
                }

                lastShippingFee = item.shipping_fee;
            }
        }

        Utils.Log(TAG, "Total grouped shopping cart size: " + groupedCartByStore.size());
        Utils.Log(TAG, "Total price: " + StringUtils.formatCurrency(String.valueOf(totalPrice)));
        Utils.Log(TAG, "Total quantity: " + totalQuantity);

        view.updatePriceGrandTotal(totalPrice);

        view.toggleContinueOrderButton(totalPrice > 0D);
    }

    @Override
    public void getShoppingCartData() {
        // Request shopping cart online
        model.requestShoppingCartData(this);

        // Uncheck all selected item
        this.selectedShoppingCart.clear();
        this.calculateSelectedShoppingCarts(this.selectedShoppingCart);
    }

    @Override
    public void deleteShoppingCartItem(int itemPos, long shoppingCartId) {
        Utils.Logs('w', TAG, "Delete shopping cart ID: " + shoppingCartId + " pos: " + itemPos);
        // Remove shopping cart item
        model.deleteShoppingCartData(itemPos, shoppingCartId, this);
    }

    @Override
    public void onItemChecked(int itemPos, boolean isChecked, long shoppingCartId, int quantity, double price, ShoppingCart shoppingCart) {

        MyLog.FabricLog(Log.INFO, TAG + " - Shopping cart item " + (isChecked? "checked" : "unchecked") +
                ". ID: " + shoppingCartId + " pos: " + itemPos + ", quantity: " + quantity +
                ", price: " + price);

        view.onShoppingCartItemSelected(itemPos, isChecked, shoppingCart);
    }

    @Override
    public void onItemQuantityChanges(int itemPos, long shoppingCartId, int quantity, double price, ShoppingCart shoppingCart) {

        Utils.Logs('i', TAG, "Shopping cart item quantity change. ID: " + shoppingCartId +
                " pos: " + itemPos + ", quantity: " + quantity + ", price: " + price);

        model.updateItemQuantity(this, shoppingCart);

        view.onShoppingCartItemQuantityChanges(itemPos, shoppingCart);
    }

    @Override
    public void addExtraNote(int adapterPosition, ShoppingCart item) {
        Utils.Log(TAG, "Add extra note to shopping cart: \"" + String.valueOf(item.extra_note) + "\"");
        model.patchExtraNote(this, item);
    }

    @Override
    public boolean isItemSelected(ShoppingCart cartItem) {
        if (selectedShoppingCart.isEmpty()) return false;
        for (ShoppingCart item : selectedShoppingCart) {
            if (item.id == cartItem.id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void startShoppingOrderScreen() {
        Utils.Logs('i', TAG, "Start shopping order screen");
        view.openShoppingOrder();
    }

    @Override
    public void updateViewState(UIState uiState) {
        if (view.isActivityFinishing()) return;
        Utils.Logs('w', TAG, "Update view state: " + uiState);
        view.showViewState(uiState);
    }

    @Override
    public void onShoppingCartDataResponse(ArrayList<ShoppingCart> shoppingCarts) {

        if (view.isActivityFinishing()) return;

        this.currentShoppingCarts.clear();
        this.currentShoppingCarts.addAll(shoppingCarts);

        view.populateShoppingCarts(shoppingCarts);
        view.updatePriceGrandTotal(0D);
        view.toggleContinueOrderButton(false);
    }

    @Override
    public void onShoppingCartDeleted(int deletedItemPos) {

        if (view.isActivityFinishing()) return;

        view.deleteShoppingCartItem(deletedItemPos);
    }

    @Override
    public void onShoppingCartItemUpdated(ShoppingCart shoppingCart) {

        if (view.isActivityFinishing()) return;

        int updatedItemIndex = 0;
        for (ShoppingCart item : this.currentShoppingCarts) {
            if (item.id == shoppingCart.id) {
                Utils.Log(TAG, "Updated item found with id: " + shoppingCart.id + " on pos: " + updatedItemIndex);
                break;
            }

            updatedItemIndex++;
        }

        view.updateItem(updatedItemIndex, shoppingCart);

    }
}
