package id.unware.poken.ui.home.view.adapter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
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

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private ArrayList<Product> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, ArrayList<Product> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card_product, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        Product singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.name);
        holder.tvPrice.setText(String.valueOf(singleItem.price));

        int productDimension = mContext.getResources().getDimensionPixelSize(R.dimen.img_grid_m);

        Picasso.with(mContext)
                .load(singleItem.images.get(0).path)
                .resize(productDimension, productDimension)
                .centerCrop()
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvPrice) TextView tvPrice;

        @BindView(R.id.itemImage) ImageView itemImage;


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