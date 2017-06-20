package id.unware.poken.ui.browse.view;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.Product;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.browse.model.BrowseModel;
import id.unware.poken.ui.browse.presenter.BrowsePresenter;
import id.unware.poken.ui.browse.view.adapter.BrowseProductAdapter;

public class BrowseActivity extends AppCompatActivity implements IBrowseView {

    private static final String TAG = "BrowseActivity";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvProductBrowsing) RecyclerView rvProductBrowsing;

    private Unbinder unbinder;

    private List<Product> listItem = new ArrayList<>();
    private BrowseProductAdapter adapter;

    private BrowsePresenter presenter;

    private int actionId = -1;
    private String actionName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        unbinder = ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            actionId = getIntent().getExtras().getInt(Constants.GENERAL_INTENT_ID, -1);
            actionName = getIntent().getExtras().getString(Constants.GENERAL_INTENT_VALUE, "");
        }

        presenter = new BrowsePresenter(new BrowseModel(), this);
        presenter.getProductDataByIntentId(actionId);

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void initView() {

        setupToolbarView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getProductDataByIntentId(actionId);
            }
        });

        initBrowseListView();
    }

    private void setupToolbarView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(actionName);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initBrowseListView() {
        adapter = new BrowseProductAdapter(listItem, presenter);
        adapter.setHasStableIds(true);
        rvProductBrowsing.setHasFixedSize(true);
        rvProductBrowsing.setLayoutManager(new GridLayoutManager(this, 2));
        rvProductBrowsing.setAdapter(adapter);
    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                swipeRefreshLayout.setRefreshing(true);
                break;
            case FINISHED:
                swipeRefreshLayout.setRefreshing(false);
                break;
        }
    }

    @Override
    public void pupolateSellerProductList(ArrayList<Product> products) {
        listItem.clear();
        listItem.addAll(products);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProductDetail(Product product) {
        Utils.Logs('i', TAG, "Show product detail with id: " + product.id);
    }
}
