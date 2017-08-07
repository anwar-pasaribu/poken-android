package id.unware.poken.ui.product.detail.view.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import id.unware.poken.domain.ProductImage;
import id.unware.poken.ui.product.detail.view.GestureSettingsSetupListener;

public class ProductImagesPagerAdapter extends RecyclePagerAdapter<ProductImagesPagerAdapter.ViewHolder> {

    private final ViewPager viewPager;
    private final ArrayList<ProductImage> paintings;
    private final GestureSettingsSetupListener setupListener;

    public ProductImagesPagerAdapter(ViewPager pager, ArrayList<ProductImage> paintings,
                                     GestureSettingsSetupListener listener) {
        this.viewPager = pager;
        this.paintings = paintings;
        this.setupListener = listener;
    }

    @Override
    public int getCount() {
        return paintings.size();
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
        Picasso.with(holder.itemView.getContext())
            .load(paintings.get(position).path)
            .into(holder.image);
    }

    public static GestureImageView getImage(RecyclePagerAdapter.ViewHolder holder) {
        return ((ViewHolder) holder).image;
    }


    static class ViewHolder extends RecyclePagerAdapter.ViewHolder {
        final GestureImageView image;

        ViewHolder(ViewGroup container) {
            super(new GestureImageView(container.getContext()));
            image = (GestureImageView) itemView;
        }
    }

}
