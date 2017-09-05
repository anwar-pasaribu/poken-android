package id.unware.poken.ui.home.view.adapter;


import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.signature.MediaStoreSignature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Featured;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideRequest;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.home.presenter.IHomePresenter;

/**
 * "Feature" or "Kolom Promosi"
 */
public class HeaderSectionAdapter extends RecyclerView.Adapter<HeaderSectionAdapter.SingleItemRowHolder>
        implements ListPreloader.PreloadSizeProvider<Featured>,
        ListPreloader.PreloadModelProvider<Featured>{

    private static final String TAG = "HeaderSectionAdapter";

    private final ArrayList<Featured> itemsList;
    private Context mContext;
    private IHomePresenter homePresenter;
    private final GlideRequest<Drawable> requestBuilder;
    private final int screenWidth;

    private int[] actualDimensions;

    public HeaderSectionAdapter(Context context,
                                ArrayList<Featured> itemsList,
                                IHomePresenter homePresenter,
                                GlideRequests glideRequest) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.homePresenter = homePresenter;

        requestBuilder = glideRequest.asDrawable().fitCenter();
        screenWidth = getScreenWidth(context);

    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        final View view = LayoutInflater.from(this.mContext).inflate(R.layout.list_single_card_header, viewGroup, false);

        if (actualDimensions == null) {
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (actualDimensions == null) {
                        actualDimensions = new int[] { view.getWidth(), view.getHeight() };

                        Utils.Log(TAG, "ACTUAL DIMEN: " + Arrays.toString(actualDimensions));
                    }
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        }
        return new SingleItemRowHolder(view);
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {

        Featured singleItem = itemsList.get(position);

        holder.tvTitle.setText(String.format(Locale.ENGLISH, "%d/%d", position + 1, itemsList.size()));

        requestBuilder
                .clone()
                .load(singleItem.thumbnail)
                .into(holder.itemImage);

        Utils.Logs('w', TAG, "Width: " + holder.featuredSlideWidth);
        Utils.Logs('w', TAG, "Height: " + holder.featuredSlideHeight);

        if (holder.getAdapterPosition() == 0) {
            RecyclerView.LayoutParams recyclerViewLayoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            recyclerViewLayoutParams.leftMargin = holder.itemGapL;
        } else if (holder.getAdapterPosition() == itemsList.size() - 1) {
            RecyclerView.LayoutParams recyclerViewLayoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            recyclerViewLayoutParams.rightMargin = holder.itemGapL;
        }
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    @Override
    public List<Featured> getPreloadItems(int position) {
        if (itemsList == null || position < 0 || position >= itemsList.size()) return null;
        return Collections.singletonList(itemsList.get(position));
    }

    @Override
    public RequestBuilder getPreloadRequestBuilder(Featured item) {
        MediaStoreSignature signature =
                new MediaStoreSignature(MediaStore.Images.Media.MIME_TYPE, 0, 0);
        return requestBuilder
                .clone()
                .signature(signature)
                .load(item.thumbnail);
    }

    @Nullable
    @Override
    public int[] getPreloadSize(Featured item, int adapterPosition, int perItemPosition) {
        return actualDimensions;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

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

    }

    // Display#getSize(Point)
    @SuppressWarnings("deprecation")
    private static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }


}