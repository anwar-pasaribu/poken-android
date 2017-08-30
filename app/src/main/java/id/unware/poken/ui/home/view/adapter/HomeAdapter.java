package id.unware.poken.ui.home.view.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.Category;
import id.unware.poken.domain.Featured;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Section;
import id.unware.poken.domain.Seller;
import id.unware.poken.models.SectionDataModel;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.home.presenter.IHomePresenter;

/**
 * Created by Anwar Pasaribu on June 2017
 * Main list adapter.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements GravitySnapHelper.SnapListener{

    private final int TYPE_HEADER = 1,
            TYPE_ITEM_CATEGORY = 2,
            TYPE_ITEM_TOP_SELLER = 3,
            TYPE_ITEM_REGULAR = 4;

    private final String TAG = "HomeAdapter";

    private Context mContext;
    private ArrayList<SectionDataModel> dataList;
    private GlideRequests glideRequests;
    private IHomePresenter homePresenter;

    public HomeAdapter(Context context,
                       ArrayList<SectionDataModel> dataList,
                       GlideRequests glideRequests,
                       IHomePresenter homePresenter) {
        this.mContext = context;
        this.dataList = dataList;
        this.glideRequests = glideRequests;
        this.homePresenter = homePresenter;

    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            case 1:
                return TYPE_ITEM_CATEGORY;
            case 2:
                return TYPE_ITEM_TOP_SELLER;
            default:
                return TYPE_ITEM_REGULAR;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        RecyclerView.ViewHolder vhItem;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;

        switch (itemType) {
            case TYPE_HEADER:
                view = inflater.inflate(R.layout.list_section_header, viewGroup, false);
                vhItem = new HeaderRowHolder(view);
                break;
            case TYPE_ITEM_CATEGORY:
                view = inflater.inflate(R.layout.list_section_category, viewGroup, false);
                vhItem = new CategoryRowHolder(view);
                break;
            case TYPE_ITEM_TOP_SELLER:
                view = inflater.inflate(R.layout.list_section_seller, viewGroup, false);
                vhItem = new TopSellerRowHolder(view);
                break;
            case TYPE_ITEM_REGULAR:
            default:
                view = inflater.inflate(R.layout.list_section_product, viewGroup, false);
                vhItem = new ItemRowHolder(view);
        }

        return vhItem;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        switch (holder.getItemViewType()) {
            case TYPE_HEADER:
                configureHeaderRow((HeaderRowHolder) holder, i);
                break;
            case TYPE_ITEM_CATEGORY:
                configureCategoryRow((CategoryRowHolder) holder, i);
                break;
            case TYPE_ITEM_TOP_SELLER:
                configureItemSellerRow((TopSellerRowHolder) holder, i);
                break;
            case TYPE_ITEM_REGULAR:
            default:
                configureItemProductRow((ItemRowHolder) holder, i);
        }
    }

    private void configureHeaderRow(HeaderRowHolder holder, int pos) {
        ArrayList<Featured> featuredItems = new ArrayList<>();

        if (dataList.get(pos).getFeaturedItems() != null) {
            featuredItems.addAll(dataList.get(pos).getFeaturedItems());
        }

        HeaderSectionAdapter adapter = new HeaderSectionAdapter(
                mContext,
                featuredItems,
                homePresenter,
                glideRequests);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        holder.recycler_view_list.setLayoutManager(layoutManager);
        holder.recycler_view_list.setHasFixedSize(true);

        // Snap on item
        SnapHelper snapHelper = new LinearSnapHelper();
        holder.recycler_view_list.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(holder.recycler_view_list);

//        RecyclerViewPreloader<Featured> preloader =
//                new RecyclerViewPreloader<>(glideRequests, adapter, adapter, 3);
//
//        holder.recycler_view_list.addOnScrollListener(preloader);
        holder.recycler_view_list.setAdapter(adapter);

        holder.recycler_view_list.setNestedScrollingEnabled(false);
    }

    private void configureCategoryRow(CategoryRowHolder holder, int pos) {
        ArrayList<Category> singleSectionItems = dataList.get(pos).getCategories();

        CategorySectionAdapter itemListDataAdapter = new CategorySectionAdapter(
                mContext,
                singleSectionItems,
                homePresenter,
                glideRequests);

        holder.recycler_view_list.setHasFixedSize(true);
        holder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        holder.recycler_view_list.setAdapter(itemListDataAdapter);

        holder.recycler_view_list.setNestedScrollingEnabled(false);
    }

    private void configureItemProductRow(ItemRowHolder holder, int pos) {

        // Product section found
        if (dataList.get(pos).getSection() != null
                && dataList.get(pos).getSection().section_action_id == Constants.HOME_SECTION_SALE_PRODUCT) {
            final Section section = dataList.get(pos).getSection();
            final String sectionName = section.name;
            final int itemPos = holder.getAdapterPosition();
            final ArrayList<Product> products = section.products;

            holder.itemTitle.setText(sectionName);

            ProductSectionAdapter itemListDataAdapter = new ProductSectionAdapter(
                    mContext,
                    products,
                    homePresenter,
                    glideRequests);
            holder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            SnapHelper snapHelper = new GravitySnapHelper(Gravity.START, false, this);
            holder.recycler_view_list.setOnFlingListener(null);
            snapHelper.attachToRecyclerView(holder.recycler_view_list);
            holder.recycler_view_list.setAdapter(itemListDataAdapter);

            holder.recycler_view_list.setNestedScrollingEnabled(false);

            holder.btnMore.setVisibility(View.VISIBLE);
            holder.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.Log(TAG, "Btn more context: " + String.valueOf(v.getContext()));
                    homePresenter.onSectionActionClick(itemPos, section);
                }
            });

            // SHOW APP VERSION
            try {
                PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                String versionNumber = pInfo.versionName;
                int versionCode = pInfo.versionCode;

                String strAppVersion = "V".concat(versionNumber).concat(" (").concat(String.valueOf(versionCode).concat(") "));
                holder.tvAppVersion.setText(strAppVersion);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Utils.Log(TAG, "Product section not available");
            holder.btnMore.setVisibility(View.GONE);
        }
    }

    private void configureItemSellerRow(TopSellerRowHolder holder, int pos) {

        // Seller section found
        if (dataList.get(pos).getSection() != null
                && dataList.get(pos).getSection().section_action_id == Constants.HOME_SECTION_TOP_SELLER) {
            final int itemPos = holder.getAdapterPosition();
            final Section section = dataList.get(pos).getSection();
            final String sectionName = section.name;

            ArrayList<Seller> singleSectionItems = section.top_sellers;

            holder.itemTitle.setText(sectionName);

            SellerSectionAdapter itemListDataAdapter = new SellerSectionAdapter(
                    mContext,
                    singleSectionItems,
                    homePresenter,
                    glideRequests
            );
            holder.recycler_view_list.setHasFixedSize(true);
            holder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            holder.recycler_view_list.setAdapter(itemListDataAdapter);

            holder.recycler_view_list.setNestedScrollingEnabled(false);

            holder.btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.Log(TAG, "Click event on more, " + sectionName);
                    homePresenter.onSectionActionClick(itemPos, section);
                }
            });
        } else {
            Utils.Log(TAG, "Product section not available");
        }
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    @Override
    public void onSnap(int position) {
        Utils.Log(TAG, "Snap to pos: " + String.valueOf(position));
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemTitle) TextView itemTitle;
        @BindView(R.id.recycler_view_list) RecyclerView recycler_view_list;
        @BindView(R.id.btnMore) Button btnMore;
        @BindView(R.id.tvAppVersion) TextView tvAppVersion;


        public ItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public class TopSellerRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemTitle) TextView itemTitle;
        @BindView(R.id.recycler_view_list) RecyclerView recycler_view_list;
        @BindView(R.id.btnMore) Button btnMore;


        public TopSellerRowHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

        }

    }

    public class HeaderRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recycler_view_list) RecyclerView recycler_view_list;

        public HeaderRowHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class CategoryRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recycler_view_list) RecyclerView recycler_view_list;

        public CategoryRowHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}