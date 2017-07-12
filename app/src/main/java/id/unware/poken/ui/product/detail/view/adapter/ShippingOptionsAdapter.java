package id.unware.poken.ui.product.detail.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Shipping;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.tools.StringUtils;

import static com.google.android.gms.internal.zznu.is;

/**
 * Adapter to show simple purpose Package list. The adapter
 */
public class ShippingOptionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "ShippingOptionsAdapter";

    private final int TYPE_ITEM = 0;

    private ArrayList<Boolean> selectedIndex = new ArrayList<>();
    private ArrayList<Shipping> listPackage = new ArrayList<>();
    private OnClickRecyclerItem listener;

    private Context mContext;


    public ShippingOptionsAdapter(Context context,
                                  ArrayList<Shipping> listPromotion,
                                  ArrayList<Boolean> selectedIndex,
                                  OnClickRecyclerItem listener) {
        this.selectedIndex = selectedIndex;
        this.listPackage = listPromotion;
        this.listener = listener;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.rowShippingCourierName) TextView rowShippingCourierName;
        @BindView(R.id.rowShippingFee) TextView rowShippingFee;
        @BindView(R.id.rowShippingIsActive) RadioButton rowShippingIsActive;

        public ItemViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(v, getAdapterPosition());
            }
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vhItem;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            default:
                View regularItemView = inflater.inflate(R.layout.row_shippings, parent, false);
                vhItem = new ItemViewHolder(regularItemView);
        }

        return vhItem;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            default:
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                configureRegularItem(itemViewHolder, position);
        }

    }

    private void configureRegularItem(ItemViewHolder holder, int position) {
        try {

            boolean isSelected = selectedIndex.get(position);

            Shipping item = listPackage.get(position);
            String  strCourierName = item.name,
                    strShippingFee = StringUtils.formatCurrency(String.valueOf(item.fee));

            holder.rowShippingCourierName.setText(strCourierName);
            holder.rowShippingFee.setText(strShippingFee);

            holder.rowShippingIsActive.setChecked(isSelected);

        } catch (IndexOutOfBoundsException | IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listPackage != null ? listPackage.size() : -1;
    }

}