package id.unware.poken.ui.home.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Category;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideRequest;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.home.presenter.IHomePresenter;

import static java.lang.System.load;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 * Category barang.
 */
public class CategorySectionAdapter extends RecyclerView.Adapter<CategorySectionAdapter.SingleItemRowHolder> {

    private ArrayList<Category> itemsList;
    private Context mContext;
    private IHomePresenter homePresenter;
    private final GlideRequest<Drawable> requestBuilder;

    public CategorySectionAdapter(Context context,
                                  ArrayList<Category> itemsList,
                                  IHomePresenter homePresenter,
                                  GlideRequests glideRequest) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.homePresenter = homePresenter;

        requestBuilder = glideRequest.asDrawable().fitCenter();

    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card_category, null);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {

        holder.tvTitle.setText(itemsList.get(i).getName());

        if (itemsList.get(i).getImageResource() != 0) {
            holder.itemImage.setImageResource(itemsList.get(i).getImageResource());
        } else if (itemsList.get(i).getImageResource() == 0
                && !StringUtils.isEmpty(itemsList.get(i).getImageUrl())) {

            this.requestBuilder
                    .clone()
                    .load(itemsList.get(i).getImageUrl())
                    .dontTransform()
                    .into(holder.itemImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homePresenter != null) {
                    homePresenter.onCategoryClick(holder.getAdapterPosition(), itemsList.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.itemImage) ImageView itemImage;
        @BindDimen(R.dimen.clickable_size_64) int iconWidth;
        @BindDimen(R.dimen.clickable_size_64) int iconHeight;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }

}