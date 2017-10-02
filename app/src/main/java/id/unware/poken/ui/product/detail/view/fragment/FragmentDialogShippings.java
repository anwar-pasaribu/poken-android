package id.unware.poken.ui.product.detail.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;

import id.unware.poken.R;
import id.unware.poken.domain.Shipping;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.product.detail.view.adapter.ShippingOptionsAdapter;

import static id.unware.poken.R.id.container;

/**
 * Display picked package(s).
 */
public class FragmentDialogShippings extends DialogFragment implements OnClickRecyclerItem {

    private final String TAG = "FragmentDialogShippings";

    private OnShippingOptionDialogListener listener;

    private AppCompatActivity parent;
    private static final String ARG_IS_COD = "is_cod";
    private static final String ARG_PRODUCT_ID = "product_id";
    private static final String ARG_SELECTED_INDEX = "selected_index";

    private boolean isCod = false;
    private long productId = -1;
    private int selectedIndex = -1;
    private ArrayList<Boolean> selectedIndexMapping = new ArrayList<>();
    private ArrayList<Shipping> listItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentDialogShippings() {}

    public static FragmentDialogShippings newInstance(boolean isCod, long productId, int selectedIndex) {
        FragmentDialogShippings fragment = new FragmentDialogShippings();
        Bundle args = new Bundle();

        args.putBoolean(ARG_IS_COD, isCod);
        args.putLong(ARG_PRODUCT_ID, productId);
        args.putInt(ARG_SELECTED_INDEX, selectedIndex);

        fragment.setArguments(args);

        return fragment;
    }

    public void setupListener(OnShippingOptionDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            isCod = getArguments().getBoolean(ARG_IS_COD, false);
            productId = getArguments().getLong(ARG_PRODUCT_ID);
            selectedIndex = getArguments().getInt(ARG_SELECTED_INDEX);
        }

        listItem = getPojoBookingLocalData(isCod, selectedIndex);
    }

    private ArrayList<Shipping> getPojoBookingLocalData(boolean isCod, int selectedIndex) {

        ArrayList<Shipping> shippings = new ArrayList<>();
        shippings.add(new Shipping(1, "POS Indonesia", 0));

        if (isCod) {
            shippings.add(new Shipping(2, "COD", 15000));
        }

        // Create selected index mapping
        for (int i = 0; i < shippings.size(); i++) {
            selectedIndexMapping.add(i, i == selectedIndex);
            Utils.Log(TAG, "Index - " + i + (i == selectedIndex ? "SELECTED" : "NOT SELECTED"));
        }

        return shippings;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.f_dialog_shippings, container, false);

        TextView textViewTitle = view.findViewById(R.id.txtViewDialogPackagesTitle);
        RecyclerView recyclerView = view.findViewById(R.id.list);

        textViewTitle.setText(R.string.title_shipping_options);

        // Adaper with empty list
        ShippingOptionsAdapter adapterPackage = new ShippingOptionsAdapter(
                getContext(),
                listItem,
                selectedIndexMapping,
                this);

        recyclerView.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterPackage);
        recyclerView.setHasFixedSize(true);

        if (listItem.isEmpty()) {
            view.findViewById(R.id.textViewNoData).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.textViewNoData).setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (AppCompatActivity) context;
    }

    @Override
    public void onItemClick(View view, int position) {

        if (position < 0
                || listItem == null
                || position >= listItem.size()) return;

        String selectedCourierName = listItem.get(position).name;
        Utils.Log(TAG, "Item clicked at pos: " + position + " with selectedCourierName: " + selectedCourierName);

        if (listener != null) {
            listener.onShippingOptionSelected(position, listItem.get(position));

            getDialog().dismiss();
        }

    }

    /**
     * Prevent dialog to show it's native title.
     *
     * @param savedInstanceState : Default bundle data.
     * @return Dialog which gonna be displayed.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        if (dialog.getWindow() != null)
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        return dialog;
    }

    public interface OnShippingOptionDialogListener {
        void onShippingOptionSelected(int pos, Shipping shipping);
    }


}
