package id.unware.poken.ui.home.view.adapter;

import android.content.Context;
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
import id.unware.poken.ui.home.presenter.IHomePresenter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 * Category barang.
 */
public class CategorySectionAdapter extends RecyclerView.Adapter<CategorySectionAdapter.SingleItemRowHolder> {

    private ArrayList<Category> itemsList;
    private Context mContext;
    private IHomePresenter homePresenter;

    public CategorySectionAdapter(Context context, ArrayList<Category> itemsList, IHomePresenter homePresenter) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.homePresenter = homePresenter;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card_category, null);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {

        final Category singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.getName());

        if (singleItem.getImageResource() != 0) {
            holder.itemImage.setImageResource(singleItem.getImageResource());
        } else if (singleItem.getImageResource() == 0
                && !StringUtils.isEmpty(singleItem.getImageUrl())) {

            Utils.Logs('i', "CategorySectionAdapter", "Category image dimension: " + holder.clickableSize64);

            Picasso.with(mContext)
                    .load(singleItem.getImageUrl())
                    .resize(holder.clickableSize64, holder.clickableSize64)
                    .centerCrop()
                    .into(holder.itemImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homePresenter != null) {
                    homePresenter.onCategoryClick(holder.getAdapterPosition(), singleItem);
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
        @BindDimen(R.dimen.clickable_size_64) int clickableSize64;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }

}