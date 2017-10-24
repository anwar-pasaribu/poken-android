package id.unware.poken.ui.customersubscription.view;

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
import id.unware.poken.domain.CustomerSubscription;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.customersubscription.model.CustomerSubscriptionModel;
import id.unware.poken.ui.customersubscription.presenter.CustomerSubscriptionPresenter;
import id.unware.poken.ui.customersubscription.view.adapter.CustomerSubscriptionAdapter;
import id.unware.poken.ui.pageseller.view.SellerActivity;

public class CustomerSubscriptionFragment extends BaseFragment implements ICustomerSubscriptionView {

    private static final String TAG = "CustomerSubscriptionFragment";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvOrderedItem) RecyclerView rvOrderedItem;

    private Unbinder unbinder;

    private CustomerSubscriptionPresenter presenter;

    private List<CustomerSubscription> orderList = new ArrayList<>();
    private CustomerSubscriptionAdapter adapter;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CustomerSubscriptionFragment() {
    }

    public static CustomerSubscriptionFragment newInstance(int columnCount) {
        CustomerSubscriptionFragment fragment = new CustomerSubscriptionFragment();
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

        presenter = new CustomerSubscriptionPresenter(new CustomerSubscriptionModel(), this /*View*/);

        adapter = new CustomerSubscriptionAdapter(parent, orderList, presenter);
        adapter.setHasStableIds(true);
        presenter.getCustomerSubscriptionData();

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
                presenter.getCustomerSubscriptionData();
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
    public void populateSubscriptionList(ArrayList<CustomerSubscription> subscriptions) {
        Utils.Log(TAG, "CustomerSubscription list size: " + subscriptions.size());
        orderList.clear();
        orderList.addAll(subscriptions);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void unsubscribe(CustomerSubscription subscription) {
        Utils.Log(TAG, "unsubscribe ID: " + subscription.id);
    }

    @Override
    public void openDetail(CustomerSubscription subscription) {
        Utils.Log(TAG, "Open subs detail ID: " + subscription.id);
        Intent sellerIntent = new Intent(this.getActivity(), SellerActivity.class);
        sellerIntent.putExtra(Constants.KEY_DOMAIN_ITEM_ID, subscription.seller_id);
        this.getActivity().startActivity(sellerIntent);
    }


    public interface OnCustomerSubscriptionFragmentListener {
    }
}
