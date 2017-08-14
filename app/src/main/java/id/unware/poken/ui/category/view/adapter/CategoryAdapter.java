package id.unware.poken.ui.category.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Category;
import id.unware.poken.ui.category.presenter.ICategoryPresenter;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.SingleItemRowHolder> {

    private ArrayList<Category> itemsList;
    private Context mContext;
    private ICategoryPresenter homePresenter;

    public CategoryAdapter(Context context, ArrayList<Category> itemsList, ICategoryPresenter homePresenter) {
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

        Category singleItem = itemsList.get(position);

        holder.tvTitle.setText(singleItem.getName());
        holder.tvDescription.setText(String.valueOf(singleItem.getId()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homePresenter.onCategoryClick(
                        holder.getAdapterPosition(),
                        itemsList.get(holder.getAdapterPosition())
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
        @BindView(R.id.tvDescription) TextView tvDescription;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }

}