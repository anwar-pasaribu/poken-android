package id.unware.poken.ui.jnews.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.ui.jnews.view.NewsFragment.OnListFragmentInteractionListener;
import id.unware.poken.ui.jnews.dummy.PojoNews.News;

import java.util.List;


public class MyNewsRecyclerViewAdapter extends RecyclerView.Adapter<MyNewsRecyclerViewAdapter.ViewHolder> {

    private final List<News> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyNewsRecyclerViewAdapter(List<News> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textVIewNewsPublishDate.setText(mValues.get(position).newsPublishDate);
        holder.textViewNewsTitle.setText(mValues.get(position).newsTitle);
        holder.textVIewNewsShortDescription.setText(mValues.get(position).newsDesc);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public News mItem;

        @BindView(R.id.textVIewNewsPublishDate) TextView textVIewNewsPublishDate;
        @BindView(R.id.textViewNewsTitle) TextView textViewNewsTitle;
        @BindView(R.id.textVIewNewsShortDescription) TextView textVIewNewsShortDescription;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }
}
