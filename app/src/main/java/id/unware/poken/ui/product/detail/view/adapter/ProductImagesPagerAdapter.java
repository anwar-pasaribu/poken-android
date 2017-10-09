package id.unware.poken.ui.product.detail.view.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.RequestBuilder;

import java.util.ArrayList;

import id.unware.poken.domain.ProductImage;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideRequest;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.product.detail.view.GestureSettingsSetupListener;

public class ProductImagesPagerAdapter extends RecyclePagerAdapter<ProductImagesPagerAdapter.ViewHolder> {

    private final ViewPager viewPager;
    private final ArrayList<ProductImage> itemList;
    private final GestureSettingsSetupListener setupListener;
    private GlideRequest<Drawable> requestBuilder;

    public ProductImagesPagerAdapter(ViewPager pager,
                                     ArrayList<ProductImage> itemList,
                                     GlideRequests glideRequests,
                                     GestureSettingsSetupListener listener) {
        this.viewPager = pager;
        this.itemList = itemList;
        this.setupListener = listener;

        requestBuilder = glideRequests.asDrawable().fitCenter();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
        ViewHolder holder = new ViewHolder(container);
        holder.image.getController().enableScrollInViewPager(viewPager);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (setupListener != null) {
            setupListener.onSetupGestureView(holder.image);
        }
        requestBuilder.clone()
                .load(itemList.get(position).path)
                .into(holder.image);
    }

    public static GestureImageView getImage(RecyclePagerAdapter.ViewHolder holder) {
        return ((ViewHolder) holder).image;
    }


    static class ViewHolder extends RecyclePagerAdapter.ViewHolder implements View.OnClickListener {
        final GestureImageView image;

        ViewHolder(ViewGroup container) {
            super(new GestureImageView(container.getContext()));
            image = (GestureImageView) itemView;
        }

        @Override
        public void onClick(View v) {
            Utils.Log("ProductImagesPagerAdapter", "Item click." );
        }
    }

}
