package id.unware.poken.ui.product.detail.view.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductImage;
import id.unware.poken.domain.Seller;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.product.detail.presenter.IProductDetailPresenter;


public class NewlyShoppingCartDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = "NewlyShoppingCartDialogFragment";

    @BindView(R.id.addressBookIbClose) ImageButton addressBookIbClose;

    @BindView(R.id.cbSelectAllStoreItem) CheckBox cbSelectAllStoreItem;
    @BindView(R.id.ivStoreAvatar) ImageView ivStoreAvatar;
    @BindView(R.id.tvStoreName) TextView tvStoreName;
    @BindView(R.id.ivProductImage) ImageView ivProductImage;
    @BindView(R.id.tvProductName) TextView tvProductName;
    @BindView(R.id.btnDeleteCartItem) ImageButton btnDeleteCartItem;

    // PRODUCT PRICE
    @BindView(R.id.tvProductTotalPrice) TextView tvProductTotalPrice;
    @BindView(R.id.tvPrice2) TextView tvPrice2;
    @BindView(R.id.tvDiscountedPrice) TextView tvDiscountedPrice;
    @BindView(R.id.tvDiscountAmount) TextView tvDiscountAmount;
    @BindView(R.id.viewFlipperProductPrice) ViewFlipper viewFlipperProductPrice;

    // ITEM QUANTITY
    @BindView(R.id.parentQuantityControl) CardView parentQuantityControl;
    @BindView(R.id.btnAddQuantity) ImageButton btnAddQuantity;
    @BindView(R.id.btnSubstractQuantity) ImageButton btnSubstractQuantity;
    @BindView(R.id.textItemQuantity) TextView textItemQuantity;
    @BindView(R.id.tvSelectedShippingMethod) TextView tvSelectedShippingMethod;

    // Extra note
    @BindView(R.id.rowCartExtraNoteSeparator) View rowCartExtraNoteSeparator;
    @BindView(R.id.rowCartAddNoteIcon) ImageView rowCartAddNoteIcon;
    @BindView(R.id.rowCartAddNoteTextView) TextView rowCartAddNoteTextView;

    @BindView(R.id.tvNewShoppingCartTotalCost) TextView tvNewShoppingCartTotalCost;

    @BindView(R.id.btnContinueToPayment) Button btnContinueToPayment;
    @BindView(R.id.btnShopMore) Button btnShopMore;


    private Unbinder unbinder;

    private IProductDetailPresenter productDetailPresenter;

    private ShoppingCart item;


    public NewlyShoppingCartDialogFragment() {}

    @SuppressLint("ValidFragment")
    public NewlyShoppingCartDialogFragment (ShoppingCart shoppingCart, IProductDetailPresenter productDetailPresenter) {
        this.item = shoppingCart;
        this.productDetailPresenter = productDetailPresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_new_shopping_cart_dialog, container, false);

        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initView() {

        // Hide extra note field
        rowCartExtraNoteSeparator.setVisibility(View.GONE);
        rowCartAddNoteIcon.setVisibility(View.GONE);
        rowCartAddNoteTextView.setVisibility(View.GONE);

        addressBookIbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        Product product = item.product;
        if (product == null) return;

        Seller seller = product.seller;
        if (seller == null) return;

        ArrayList<ProductImage> images = product.images;
        if (images.isEmpty()) return;

        final long shoppingCartId = item.id;
        String storeName = seller.store_name;
        String productImage = images.get(0).path;
        String productName = product.name;
        String strShipping = item.shipping == null
                ? "Metode pengiriman ditentukan oleh Poken"
                : item.shipping.name;
        double  productPrice = product.price,
                discountAmount = product.discount_amount,
                discountedPrice = productPrice - ((productPrice * discountAmount) / 100);
        final int productStock = product.stock;

        cbSelectAllStoreItem.setVisibility(View.GONE);
        tvDiscountAmount.setVisibility(View.GONE);

        // Product image thumbnail size
        int imageSize = getContext().getResources().getDimensionPixelSize(R.dimen.clickable_size_64);
        tvStoreName.setText(storeName);
        Picasso.with(getContext())
                .load(productImage)
                .resize(imageSize, imageSize)
                .centerCrop()
                .into(ivProductImage);
        tvProductName.setText(productName);
        tvProductTotalPrice.setText(
                StringUtils.formatCurrency(String.valueOf(productPrice))
        );
        textItemQuantity.setText(
                String.valueOf(item.quantity)
        );
        tvSelectedShippingMethod.setText(strShipping);

        // tvPrice2 to show SALE item
        tvPrice2.setText(StringUtils.formatCurrency(String.valueOf(productPrice)));
        tvPrice2.setPaintFlags(tvPrice2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);  // Strike
        tvDiscountedPrice.setText(StringUtils.formatCurrency(String.valueOf(discountedPrice)));
        tvDiscountAmount.setText((int) discountAmount + "%");

        // Discount view
        if (discountAmount > 0D) {
            viewFlipperProductPrice.setDisplayedChild(0);
        } else {
            viewFlipperProductPrice.setDisplayedChild(1);
        }

        btnDeleteCartItem.setVisibility(View.GONE);
        cbSelectAllStoreItem.setVisibility(View.GONE);

        // Shopping initial price
        onItemQuantityChanges(item);

        btnAddQuantity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.quantity = controlItemQuantity(item.quantity, productStock, true);
                        Utils.Log(TAG, "[add] Q: " + item.quantity + ", stok: " + productStock);
                        textItemQuantity.setText(
                                String.valueOf(item.quantity)
                        );

                        // Change shopping cart counter on list page
                        onItemQuantityChanges(item);
                    }
                }
        );

        btnSubstractQuantity.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.quantity = controlItemQuantity(item.quantity, productStock, false);
                        Utils.Log(TAG, "[substract] Q: " + item.quantity + ", stok: " + productStock);
                        textItemQuantity.setText(
                                String.valueOf(item.quantity)
                        );

                        // Change shopping cart counter on list page
                        onItemQuantityChanges(item);
                    }
                }
        );

        btnContinueToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();

                if (productDetailPresenter != null) {
                    productDetailPresenter.startShoppingCartScreen(item);
                }

            }
        });

        btnShopMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
                if (productDetailPresenter != null) {
                    productDetailPresenter.onShopMoreClicked();
                }
            }
        });

    }

    private void onItemQuantityChanges(ShoppingCart item) {
        tvNewShoppingCartTotalCost.setText(StringUtils.formatCurrency(String.valueOf(item.quantity * item.product.price)));
    }

    private int controlItemQuantity(int currentQuantity, int maxQuantity, boolean isAdd) {
        if (isAdd && currentQuantity < maxQuantity) {
            currentQuantity = currentQuantity + 1;
        } else if (!isAdd && currentQuantity > 1) {
            currentQuantity = currentQuantity - 1;
        }

        return currentQuantity;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = d.findViewById(android.support.design.R.id.design_bottom_sheet);

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        dialog.setTitle("Barang baru di Troli");

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
