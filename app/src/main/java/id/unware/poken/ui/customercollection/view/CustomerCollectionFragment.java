package id.unware.poken.ui.customercollection.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.CustomerCollection;
import id.unware.poken.domain.Product;
import id.unware.poken.models.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.customercollection.model.CustomerCollectionModel;
import id.unware.poken.ui.customercollection.presenter.CustomerCollectionPresenter;
import id.unware.poken.ui.customercollection.view.adapter.CustomerCollectionAdapter;
import id.unware.poken.ui.product.detail.view.ProductDetailActivity;

public class CustomerCollectionFragment extends BaseFragment implements ICustomerCollectionView {

    private static final String TAG = "CustomerCollectionFragment";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvOrderedItem) RecyclerView rvOrderedItem;

    private Unbinder unbinder;

    private CustomerCollectionPresenter presenter;

    private List<CustomerCollection> orderList = new ArrayList<>();
    private CustomerCollectionAdapter adapter;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CustomerCollectionFragment() {
    }

    public static CustomerCollectionFragment newInstance(int columnCount) {
        CustomerCollectionFragment fragment = new CustomerCollectionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_collection_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter = new CustomerCollectionPresenter(new CustomerCollectionModel(), this /*View*/);

        adapter = new CustomerCollectionAdapter(orderList, presenter);
        adapter.setHasStableIds(true);
        presenter.getCustomerCollectionData();

        initView();

        if (mColumnCount <= 1) {
            rvOrderedItem.setLayoutManager(new LinearLayoutManager(parent));
        } else {
            rvOrderedItem.setLayoutManager(new GridLayoutManager(parent, mColumnCount));
        }
        rvOrderedItem.setHasFixedSize(true);
        rvOrderedItem.setAdapter(adapter);

        return view;
    }

    private void initView() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getCustomerCollectionData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
            case ERROR:
                Toast.makeText(parent, "Tidak bisa mengambil data", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return parent == null || parent.isFinishing();
    }

    @Override
    public void populateCollectionList(ArrayList<CustomerCollection> collections) {
        Utils.Log(TAG, "Collection list size: " + collections.size());
        orderList.clear();
        orderList.addAll(collections);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void openDetail(CustomerCollection collection) {
        Utils.Log(TAG, "Open collection detail ID: " + collection.product_id);
        Intent productDetailIntent = new Intent(getActivity(), ProductDetailActivity.class);
        productDetailIntent.putExtra(Product.KEY_PRODUCT_ID, collection.product_id);
        getActivity().startActivityForResult(productDetailIntent, Constants.TAG_PRODUCT_DETAIL);
    }


    public interface OnCustomerCollectionFragmentListener {
    }
}
