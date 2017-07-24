package id.unware.poken.ui.shoppingorder.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.ShoppingCart;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;

public class OrderedProductListDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = "OrderedProductListDialogFragment";

    @BindView(R.id.addressBookIbClose) ImageButton addressBookIbClose;

    @BindView(R.id.orderedProductList) RecyclerView orderedProductList;

    private Unbinder unbinder;

    private ArrayList<ShoppingCart> listItem = new ArrayList<>();

    private Listener mListener;

    public static OrderedProductListDialogFragment newInstance() {
        final OrderedProductListDialogFragment fragment = new OrderedProductListDialogFragment();
        final Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_ordered_product_dialog, container, false);

        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        orderedProductList.setLayoutManager(new LinearLayoutManager(getContext()));
        orderedProductList.setAdapter(new ItemAdapter(this.listItem));

        if (mListener != null) {
            Utils.devModeToast(getContext(), "View address book ready");
            mListener.onProductListDialogViewReady();
        }
    }

    // Order activity responsible to handle this function.
    public void setListContent(ArrayList<ShoppingCart> listItem) {

        Utils.Log(TAG, "Set list content. Size: " + listItem.size());

        this.listItem.clear();
        this.listItem.addAll(listItem);
        orderedProductList.setAdapter(new ItemAdapter(this.listItem));

    }

    private void initView() {

        addressBookIbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        dialog.setTitle("Alamat Penerima");

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface Listener {

        // Show product list when dialog ready
        void onProductListDialogViewReady();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tvProductName;
        final TextView tvProductQuantity;
        final TextView tvProductTotalPrice;
        final TextView tvSelectedShippingMethod;
        final TextView tvShippingCost;
        final ImageView ivProductImage;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.list_order_selected_product, parent, false));

            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductQuantity = (TextView) itemView.findViewById(R.id.tvProductQuantity);
            tvProductTotalPrice = (TextView) itemView.findViewById(R.id.tvProductTotalPrice);
            tvSelectedShippingMethod = (TextView) itemView.findViewById(R.id.tvSelectedShippingMethod);
            tvShippingCost = (TextView) itemView.findViewById(R.id.tvShippingCost);
            ivProductImage = (ImageView) itemView.findViewById(R.id.ivProductImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.Log(TAG, "Selected Product to order clicked.");
                }
            });
        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private ArrayList<ShoppingCart> listItem = new ArrayList<>();

        public ItemAdapter(ArrayList<ShoppingCart> listItem) {
            this.listItem = listItem;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ShoppingCart item = this.listItem.get(position);
            String quantityString = holder.itemView.getContext().getString(R.string.lbl_quantity, item.quantity);
            double totalPrice = item.product.price * item.quantity;
            String shippingMethod = item.shipping.name;
            double shippingCost = item.shipping.fee;

            holder.tvProductName.setText(String.valueOf(item.product.name));
            holder.tvProductQuantity.setText(quantityString);
            holder.tvProductTotalPrice.setText(StringUtils.formatCurrency(String.valueOf(totalPrice)));
            holder.tvSelectedShippingMethod.setText(shippingMethod);
            holder.tvShippingCost.setText(StringUtils.formatCurrency(String.valueOf(shippingCost)));

            int clickableSize64 = holder.itemView.getResources().getDimensionPixelSize(R.dimen.clickable_size_64);
            Picasso.with(holder.itemView.getContext())
                    .load(item.product.images.get(0).path)
                    .resize(clickableSize64, clickableSize64)
                    .centerCrop()
                    .into(holder.ivProductImage);

        }

        @Override
        public int getItemCount() {
            return listItem.size();
        }

    }

}
