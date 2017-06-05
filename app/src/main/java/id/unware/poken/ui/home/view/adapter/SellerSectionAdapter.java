package id.unware.poken.ui.home.view.adapter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 * Seller item on home screen.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Seller;

public class SellerSectionAdapter extends RecyclerView.Adapter<SellerSectionAdapter.SingleItemRowHolder> {

    private ArrayList<Seller> itemsList;
    private Context mContext;

    public SellerSectionAdapter(Context context, ArrayList<Seller> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card_seller, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        Seller singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.store_name);
        holder.tvDescription.setText(singleItem.tag_line);
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvDescription) TextView tvDescription;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

}