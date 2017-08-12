package id.unware.poken.ui.home.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.Category;
import id.unware.poken.domain.Featured;
import id.unware.poken.domain.Product;
import id.unware.poken.domain.Section;
import id.unware.poken.domain.Seller;
import id.unware.poken.models.SectionDataModel;
import id.unware.poken.models.SingleItemModel;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.PokenCredentials;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.browse.view.BrowseActivity;
import id.unware.poken.ui.featured.view.FeaturedActivity;
import id.unware.poken.ui.home.model.HomeModel;
import id.unware.poken.ui.home.presenter.HomePresenter;
import id.unware.poken.ui.home.view.adapter.HomeAdapter;
import id.unware.poken.ui.pokenaccount.LoginActivity;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;
import id.unware.poken.ui.profile.view.ProfileActivity;
import id.unware.poken.ui.search.view.SearchActivity;
import id.unware.poken.ui.seller.view.SellerActivity;
import id.unware.poken.ui.shoppingcart.view.ShoppingCartActivity;
import io.realm.Realm;

public class HomeActivity extends AppCompatActivity implements IHomeView {

    private final String TAG = "HomeAcivity";

    // View states for home screen
    private final int HOME_VIEW_DEFAULT = 0;
    private final int HOME_VIEW_ERROR = 1;

    @BindView(R.id.vFHome) ViewFlipper vFHome;
    @BindView(R.id.swipeRefreshLayoutMain) SwipeRefreshLayout swipeRefreshLayoutMain;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    // ERROR VIEW WIDGETS
    @BindView(R.id.tvErrorTitle) TextView tvErrorTitle;
    @BindView(R.id.tvErrorContent) TextView tvErrorContent;
    @BindView(R.id.btnErrorRetry) Button btnErrorRetry;


    private Unbinder unbinder;
    private Realm realm;

    private HomePresenter presenter;

    private HomeAdapter adapter;
    private ArrayList<SectionDataModel> listHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        unbinder = ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();

        HomeModel model = new HomeModel(realm);
        presenter = new HomePresenter(model, HomeActivity.this);

        listHome = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createInitialHomeData();

        adapter = new HomeAdapter(this, listHome, presenter /* Presenter to trigger click event on items*/);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayoutMain.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getHomeData();
            }
        });

        // Get initial data
        presenter.getHomeData();

    }

    public void createInitialHomeData() {
        for (int i = 1; i <= 4; i++) {

            SectionDataModel dm = new SectionDataModel();

            dm.setHeaderTitle("Section " + i);

            ArrayList<SingleItemModel> singleItem = new ArrayList<SingleItemModel>();
            for (int j = 0; j <= 5; j++) {
                singleItem.add(new SingleItemModel("Item " + j, "URL " + j));
            }

            dm.setAllItemsInSection(singleItem);

            listHome.add(dm);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            openSearchScreen();
        } else if (id == R.id.action_shopping_cart) {
            openShoppingCart();
        } else if (id == R.id.action_profile) {
            openProfile();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSearchScreen() {
        Intent searchIntent = new Intent(this, SearchActivity.class);
        this.startActivity(searchIntent);
    }

    private void openProfile() {
        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {

            MyLog.FabricTrackContentView("Poken Account", "Page", "06");

            Intent profileIntent = new Intent(this, ProfileActivity.class);
            this.startActivity(profileIntent);

        } else {
            Intent accountIntent = new Intent(this, LoginActivity.class);
            accountIntent.putExtra(Constants.EXTRA_REQUESTED_PAGE, Constants.TAG_PROFILE);
            this.startActivityForResult(accountIntent, Constants.TAG_LOGIN);
        }
    }

    private void openShoppingCart() {

        MyLog.FabricTrackContentView("Shopping Cart", "Page", "05");

        if (PokenCredentials.getInstance().getCredentialHashMap() != null) {

            Intent intent = new Intent(this, ShoppingCartActivity.class);
            this.startActivity(intent);

        } else {
            Intent accountIntent = new Intent(this, LoginActivity.class);
            accountIntent.putExtra(Constants.EXTRA_REQUESTED_PAGE, Constants.TAG_SHOPPING_CART);
            this.startActivityForResult(accountIntent, Constants.TAG_LOGIN);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Utils.Log(TAG, "Activity result. Req: " + requestCode + ", res: " + resultCode + ", data: " + data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.TAG_LOGIN) {

                int requestedPageTag = data.getIntExtra(Constants.EXTRA_REQUESTED_PAGE, -1);

                if (PokenCredentials.getInstance().getCredentialHashMap() != null) {
                    if (requestedPageTag == Constants.TAG_PROFILE) {
                        openProfile();
                    } else if (requestedPageTag == Constants.TAG_SHOPPING_CART) {
                        openShoppingCart();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        Utils.Log(TAG, "Destroy home view.");
        super.onDestroy();
        unbinder.unbind();
        realm.close();
    }

    @Override
    public void showMessage(String msg, int status) {
        if (status == Constants.NETWORK_CALLBACK_FAILURE) {
            tvErrorTitle.setText(R.string.msg_title_network_error);
            tvErrorContent.setText(String.valueOf(status).concat(msg));
            setupHomeViewErrorState(true);
        }
    }

    @Override
    public void populateHomeView(ArrayList<Featured> featured_items, ArrayList<Section> sections) {
        for (Featured featured : featured_items) {
            Utils.Log(TAG, "Feature : " + featured.image);
        }

        // ADD HEADER ITEM
        listHome.remove(0);
        SectionDataModel dm = new SectionDataModel();
        dm.setFeaturedItems(featured_items);
        listHome.add(0, dm);
        adapter.notifyItemChanged(0);

        // POPULATE CATEGORY SECTION
        listHome.remove(1);
        listHome.add(1, createCategoryItems());
        adapter.notifyItemChanged(1);

        for (Section section : sections) {

            Utils.Log(TAG, "Section action ID: " + section.section_action_id);

            if (section.section_action_id == Constants.HOME_SECTION_TOP_SELLER) {

                // POPULATE SELLER
                listHome.remove(2); // Section 2 after category
                SectionDataModel dmTopSeller = new SectionDataModel();
                dmTopSeller.setSection(section);
                listHome.add(2, dmTopSeller);
                adapter.notifyItemChanged(2);

            } else if (section.section_action_id == Constants.HOME_SECTION_SALE_PRODUCT) {

                // POPULATE SALE PRODUCTS
                listHome.remove(3); // Section 3 after seller
                SectionDataModel dmPopularStore = new SectionDataModel();
                dmPopularStore.setSection(section);
                listHome.add(3, dmPopularStore);
                adapter.notifyItemChanged(3);

            }
        }
    }

    @Override
    public void startProductCategoryScreen(Section sectionItem) {
        Utils.Logs('i', TAG, "Start Product Category Screen with intention to show Sale item. Intent id: " + sectionItem.section_action_id);
        Intent browseSaleProducts = new Intent(this, BrowseActivity.class);
        browseSaleProducts.putExtra(Constants.EXTRA_GENERAL_INTENT_ID, sectionItem.section_action_id);
        browseSaleProducts.putExtra(Constants.EXTRA_GENERAL_INTENT_VALUE, sectionItem.name);
        this.startActivity(browseSaleProducts);
    }

    @Override
    public void showCategoryDetailScreen(Category category) {
        Utils.Logs('i', TAG, "Start Browse by Category. id: " + category.getId() + ", name: " + category.getName());
        if (category.getName().equals("Favorit")) {
            Intent profileFavorite = new Intent(this, ProfileActivity.class);
            profileFavorite.putExtra(Constants.EXTRA_IS_LAUNCH_FAVORITE, true /*Launch favorite tab*/);
            this.startActivity(profileFavorite);

        } else {
            Intent browsePage = new Intent(this, BrowseActivity.class);
            browsePage.putExtra(Constants.EXTRA_GENERAL_INTENT_ID, Constants.INTENT_BROWSE_BY_CATEGORY);
            browsePage.putExtra(Constants.EXTRA_GENERAL_INTENT_VALUE, category.getName());
            browsePage.putExtra(Constants.EXTRA_IS_BROWSE_BY_CATEGORY, true);
            browsePage.putExtra(Constants.EXTRA_CATEGORY_ID, category.getId());
            browsePage.putExtra(Constants.EXTRA_CATEGORY_NAME, category.getName());
            this.startActivity(browsePage);
        }
    }

    @Override
    public void showProductDetailScreen(Product product) {
        Utils.Logs('i', TAG, "Start Product Detail Screen. Product id: " + product.id);
        Intent productDetailIntent = new Intent(this, ProductDetailActivity.class);
        productDetailIntent.putExtra(Product.KEY_PRODUCT_ID, product.id);
        this.startActivityForResult(productDetailIntent, Constants.TAG_PRODUCT_DETAIL);
    }

    @Override
    public void showSellerDetailScreen(int position, Seller seller) {
        Utils.Logs('i', TAG, "Open seller detail with id: " + seller.id);
        Intent sellerIntent = new Intent(this, SellerActivity.class);
        sellerIntent.putExtra(Constants.KEY_DOMAIN_ITEM_ID, seller.id);
        this.startActivity(sellerIntent);
    }

    @Override
    public void showFeaturedScreen(int position, Featured featured) {
        MyLog.FabricTrackContentView("Featured", Constants.FABRIC_CONTENT_TYPE, String.valueOf(Constants.TAG_FEATURED));

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String featuredString = gson.toJson(featured, Featured.class);
        Utils.Log(TAG, "Featured data: " + featuredString);

        Intent featuredIntent = new Intent(this, FeaturedActivity.class);
        featuredIntent.putExtra(Constants.KEY_DOMAIN_ITEM_ID, featured.id);
        featuredIntent.putExtra(Constants.EXTRA_DOMAIN_SERIALIZED_STRING, featuredString);
        this.startActivity(featuredIntent);
    }

    private SectionDataModel createCategoryItems() {
        SectionDataModel dmCategory = new SectionDataModel();
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("Favorit", 0, "", R.drawable.ic_favo));
        categories.add(new Category("Baju", 4, "", R.drawable.ic_clothes));
        categories.add(new Category("Sepatu", 3, "", R.drawable.ic_shoes));
        categories.add(new Category("Topi", 5, "", R.drawable.ic_hat));
        categories.add(new Category("Semua", 6, "", R.drawable.ic_category));
        dmCategory.setCategories(categories);
        return dmCategory;
    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                showLoadingIndicator(true);
                break;
            case FINISHED:
                showLoadingIndicator(false);
                break;
            case ERROR:
                setupHomeViewErrorState(true);
                break;
        }
    }

    private void showLoadingIndicator(boolean isLoading) {

        if (vFHome.getDisplayedChild() == this.HOME_VIEW_ERROR) {
            vFHome.setDisplayedChild(this.HOME_VIEW_DEFAULT);
        }

        if (isLoading) {
            swipeRefreshLayoutMain.setRefreshing(true);
        } else {
            swipeRefreshLayoutMain.setRefreshing(false);
        }
    }

    private void setupHomeViewErrorState(boolean isError) {

        if (isError) {
            vFHome.setDisplayedChild(this.HOME_VIEW_ERROR);
            btnErrorRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (presenter != null) {
                        presenter.getHomeData();
                    }
                }
            });
        } else {
            vFHome.setDisplayedChild(this.HOME_VIEW_DEFAULT);
        }

    }

    @Override
    public boolean isActivityFinishing() {
        return this.isFinishing();
    }
}
