package id.unware.poken.ui.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.models.SectionDataModel;
import id.unware.poken.models.SingleItemModel;
import id.unware.poken.domain.Category;
import id.unware.poken.domain.Featured;
import id.unware.poken.domain.Section;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.home.model.HomeModelImpl;
import id.unware.poken.ui.home.presenter.HomePresenter;
import id.unware.poken.ui.home.view.adapter.HomeAdapter;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;
import id.unware.poken.ui.profile.view.ProfileActivity;
import id.unware.poken.ui.shoppingcart.view.ShoppingCartActivity;
import io.realm.Realm;

public class HomeActivity extends AppCompatActivity implements IHomeView {

    private final String TAG = "HomeAcivity";

    @BindView(R.id.swipeRefreshLayoutMain) SwipeRefreshLayout swipeRefreshLayoutMain;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;

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

        HomeModelImpl model = new HomeModelImpl(realm);
        presenter = new HomePresenter(model, HomeActivity.this);

        listHome = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createDummyData();

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

    public void createDummyData() {
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
            return true;
        } else if (id == R.id.action_shopping_cart) {
            openShoppingCart();
        } else if (id == R.id.action_profile) {
            openProfile();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openProfile() {
        Intent profileIntent = new Intent(this, ProfileActivity.class);
        this.startActivity(profileIntent);
    }

    private void openShoppingCart() {
        Intent intent = new Intent(this, ShoppingCartActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        Utils.Log(TAG, "Destroy home view.");
        super.onDestroy();
        unbinder.unbind();
        realm.close();
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

        // POPULATE CATEGORY SECTION
        listHome.remove(1);
        listHome.add(1, createCategoryItems());
        adapter.notifyItemChanged(1);
    }

    @Override
    public void startProductCategoryScreen(Section sectionItem) {
        Utils.Logs('i', TAG, "Start Product Category Screen with intention to show Sale item. Intent id: " + sectionItem.section_action_id);
    }

    @Override
    public void startProductDetailScreen(Product product) {
        Utils.Logs('i', TAG, "Start Product Detail Screen. Product id: " + product.id);
        Intent productDetailIntent = new Intent(this, ProductDetailActivity.class);
        productDetailIntent.putExtra(Product.KEY_PRODUCT_ID, product.id);
        this.startActivityForResult(productDetailIntent, Constants.TAG_PRODUCT_DETAIL);
    }

    private SectionDataModel createCategoryItems() {
        SectionDataModel dmCategory = new SectionDataModel();
        ArrayList<Category> categories = new ArrayList<>(5);
        categories.add(new Category("Favorit", 0, "", R.drawable.ic_favo));
        categories.add(new Category("Baju", 1, "", R.drawable.ic_clothes));
        categories.add(new Category("Sepatu", 2, "", R.drawable.ic_shoes));
        categories.add(new Category("Topi", 3, "", R.drawable.ic_hat));
        categories.add(new Category("Semua", 4, "", R.drawable.ic_category));
        dmCategory.setCategories(categories);
        return dmCategory;
    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                swipeRefreshLayoutMain.setRefreshing(true);
                break;
            case FINISHED:
                swipeRefreshLayoutMain.setRefreshing(false);
                break;
        }
    }
}
