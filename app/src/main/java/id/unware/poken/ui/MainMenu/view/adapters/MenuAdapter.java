package id.unware.poken.ui.MainMenu.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.tools.Utils;


/**
 * Created by PID-T420S on 20/05/2016.
 * Adapter for Main menu.
 */
public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private List<Integer> mDataset = new ArrayList<>();

    private List<String> mDatasetLables = new ArrayList<>();

    private Context mContext;
    // Listener
    private OnClickRecyclerItem mItemClickListener;


    public MenuAdapter(Context c, OnClickRecyclerItem listener) {
        this.mContext = c;

        this.mItemClickListener = listener;

        mDataset.add(R.drawable.ic_home_track_48_white);
        mDataset.add(R.drawable.ic_home_tariff_48_white);
        mDataset.add(R.drawable.ic_home_pacakge_48_white);
        mDataset.add(R.drawable.ic_home_pickup_48_white);
        mDataset.add(R.drawable.ic_home_nearby_48_white);
        mDataset.add(R.drawable.ic_home_wallet_48_white);

        // Add Home Menu Lable
        mDatasetLables.add("Tracker");
        mDatasetLables.add("Tariff");
        mDatasetLables.add("JOB");
        mDatasetLables.add("Pickup");
        mDatasetLables.add("Nearby");
        mDatasetLables.add("Wallet");

    }

    /**
     * Inner class to handle the View.
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.menu_icon) ImageView menuIcon;
        @BindView(R.id.home_menu_lable) TextView homeMenuLable;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Utils.Log("On click on view: ", view.toString());
            if (mItemClickListener != null)
                mItemClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_main_menu, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int curretDataSet = mDataset.get(position);
        final String currentDataSetLable = mDatasetLables.get(position);

        Utils.Log("MenuAdapter", "Bind pos: " + position);

        holder.menuIcon.setImageResource(curretDataSet);
        holder.homeMenuLable.setText(currentDataSetLable);
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public long getItemId(int position) {
        return mDataset.get(position);
    }

}
