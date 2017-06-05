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

import id.unware.poken.R;
import id.unware.poken.domain.Category;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;

public class CategorySectionAdapter extends RecyclerView.Adapter<CategorySectionAdapter.SingleItemRowHolder> {

    private ArrayList<Category> itemsList;
    private Context mContext;

    public CategorySectionAdapter(Context context, ArrayList<Category> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card_category, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        Category singleItem = itemsList.get(i);

        holder.tvTitle.setText(singleItem.getName());

        if (singleItem.getImageResource() != 0) {
            holder.itemImage.setImageResource(singleItem.getImageResource());
        } else if (singleItem.getImageResource() == 0
                && !StringUtils.isEmpty(singleItem.getImageUrl())) {

            int categoryDimension = mContext.getResources().getDimensionPixelSize(R.dimen.clickable_size_64);

            Utils.Logs('i', "CategorySectionAdapter", "Category image dimension: " + categoryDimension);

            Picasso.with(mContext)
                    .load(singleItem.getImageUrl())
                    .resize(categoryDimension, categoryDimension)
                    .centerCrop()
                    .into(holder.itemImage);
        }
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvTitle;
        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), tvTitle.getText(), Toast.LENGTH_SHORT).show();

                }
            });


        }

    }

}