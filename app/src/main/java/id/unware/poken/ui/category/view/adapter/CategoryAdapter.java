package id.unware.poken.ui.category.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Category;
import id.unware.poken.domain.FeaturedCategoryProduct;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.ProductImage;
import id.unware.poken.ui.category.presenter.ICategoryPresenter;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.SingleItemRowHolder> {

    private ArrayList<FeaturedCategoryProduct> itemsList;
    private Context mContext;
    private ICategoryPresenter homePresenter;

    public CategoryAdapter(Context context, ArrayList<FeaturedCategoryProduct> itemsList, ICategoryPresenter homePresenter) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.homePresenter = homePresenter;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.mContext).inflate(R.layout.list_product_category, viewGroup, false);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int position) {

        FeaturedCategoryProduct singleItem = itemsList.get(position);
        ArrayList<Product> products = singleItem.products;
        holder.tvTitle.setText(singleItem.product_category.getName());

        for (int i = 0; i < holder.imgs.size(); i++) {
            if (!products.isEmpty()
                    && !products.get(i).images.isEmpty()) {
                Picasso.with(mContext)
                        .load(products.get(i).images.get(0).thumbnail)
                        .into(holder.imgs.get(i));

            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePresenter.onCategoryClick(
                        holder.getAdapterPosition(),
                        itemsList.get(holder.getAdapterPosition()).product_category
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindViews({R.id.img1, R.id.img2, R.id.img3}) List<ImageView> imgs;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }

}