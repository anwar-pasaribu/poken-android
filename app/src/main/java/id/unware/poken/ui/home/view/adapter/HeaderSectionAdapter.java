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
import java.util.Locale;

import id.unware.poken.R;
import id.unware.poken.domain.Featured;

public class HeaderSectionAdapter extends RecyclerView.Adapter<HeaderSectionAdapter.SingleItemRowHolder> {

    private ArrayList<Featured> itemsList;
    private Context mContext;

    public HeaderSectionAdapter(Context context, ArrayList<Featured> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single_card_header, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {

        Featured singleItem = itemsList.get(position);

        holder.tvTitle.setText(String.format(Locale.ENGLISH, "%d/%d", position + 1, itemsList.size()));

        int featuredSlideWidth = mContext.getResources().getDimensionPixelSize(R.dimen.header_slide_width_m);
        int featuredSlideHeight = mContext.getResources().getDimensionPixelSize(R.dimen.header_slide_height_m);

        Picasso.with(mContext)
                .load(singleItem.image)
                .resize(featuredSlideWidth, featuredSlideHeight)
                .centerCrop()
                .into(holder.itemImage);
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