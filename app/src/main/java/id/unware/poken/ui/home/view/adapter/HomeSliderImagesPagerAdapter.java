package id.unware.poken.ui.home.view.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.commons.RecyclePagerAdapter;
import com.alexvasilkov.gestures.views.GestureImageView;

import java.util.ArrayList;

import id.unware.poken.domain.Featured;
import id.unware.poken.tools.glide.GlideRequest;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.home.presenter.IHomePresenter;

public class HomeSliderImagesPagerAdapter extends RecyclePagerAdapter<HomeSliderImagesPagerAdapter.ViewHolder> {

    private ViewPager viewPager;
    private ArrayList<Featured> itemList;
    private IHomePresenter homePresenter;
    private GlideRequest<Drawable> requestBuilder;

    public HomeSliderImagesPagerAdapter(ViewPager pager,
                                        ArrayList<Featured> itemList,
                                        GlideRequests glideRequests,
                                        IHomePresenter listener) {
        this.viewPager = pager;
        this.itemList = itemList;
        this.homePresenter = listener;

        requestBuilder = glideRequests.asDrawable().fitCenter();

    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
        ViewHolder holder = new ViewHolder(container);
        // holder.image.getController().enableScrollInViewPager(viewPager);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        requestBuilder.clone()
                .load(itemList.get(position).thumbnail)
                .into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homePresenter != null) {
                    // homePresenter.onFeaturedItemClicked(holder.getAdapterPosition(), itemsList.get(getAdapterPosition()));
                }
            }
        });
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
