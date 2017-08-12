package id.unware.poken.ui.home.view.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Featured;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.home.presenter.IHomePresenter;

/**
 * "Feature" or "Kolom Promosi"
 */
public class HeaderSectionAdapter extends RecyclerView.Adapter<HeaderSectionAdapter.SingleItemRowHolder> {

    private static final String TAG = "HeaderSectionAdapter";
    private ArrayList<Featured> itemsList;
    private Context mContext;
    private IHomePresenter homePresenter;

    public HeaderSectionAdapter(Context context, ArrayList<Featured> itemsList, IHomePresenter homePresenter) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.homePresenter = homePresenter;

        // Preloaded featured images
        for (Featured i : this.itemsList) {
            Picasso.with(this.mContext)
                    .load(i.image)
                    .fetch(new Callback() {
                        @Override
                        public void onSuccess() {
                            Utils.Log(TAG, "FETCH SUCCESS");
                        }

                        @Override
                        public void onError() {
                            Utils.Log(TAG, "FETCH ERROR");

                        }
                    });
        }
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
                .error(R.drawable.bg_gradient_poken)
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

    public class SingleItemRowHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, com.squareup.picasso.Target{

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
            if (homePresenter != null) {
                homePresenter.onFeaturedItemClicked(getAdapterPosition(), itemsList.get(getAdapterPosition()));
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Utils.Logs('i', TAG, "onBitmapLoaded. Form: " + from.name() + ", from int: " + from);
            itemImage.setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Utils.Logs('e', TAG, "onBitmapFailed");
            itemImage.setImageResource(R.drawable.bg_gradient_poken);

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Utils.Logs('v', TAG, "onPrepareLoad");
            itemImage.setImageResource(R.drawable.bg_gradient_poken);

        }
    }

}