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
import java.util.Locale;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Featured;

public class HeaderSectionAdapter extends RecyclerView.Adapter<HeaderSectionAdapter.SingleItemRowHolder> {

    private ArrayList<Featured> itemsList;
    private Context mContext;

    public HeaderSectionAdapter(Context context, ArrayList<Featured> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.mContext).inflate(R.layout.list_single_card_header, viewGroup, false);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {

        Featured singleItem = itemsList.get(position);

        holder.tvTitle.setText(String.format(Locale.ENGLISH, "%d/%d", position + 1, itemsList.size()));

        Picasso.with(mContext)
                .load(singleItem.image)
                .resize(holder.featuredSlideWidth, holder.featuredSlideHeight)
                .centerCrop()
                .into(holder.itemImage);

        if (position == 0) {
            RecyclerView.LayoutParams recyclerViewLayoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            recyclerViewLayoutParams.leftMargin = holder.itemGapL;
        } else if (position == itemsList.size() - 1) {
            RecyclerView.LayoutParams recyclerViewLayoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            recyclerViewLayoutParams.rightMargin = holder.itemGapL;
        }
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.itemImage) ImageView itemImage;

        @BindDimen(R.dimen.header_slide_width_m) int featuredSlideWidth;
        @BindDimen(R.dimen.header_slide_height_m) int featuredSlideHeight;
        @BindDimen(R.dimen.item_gap_l) int itemGapL;


        public SingleItemRowHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();
        }
    }

}