package id.unware.poken.ui.customerorder.view;

import android.content.Context;
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
import id.unware.poken.domain.ShoppingOrder;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.customerorder.model.OrdersModel;
import id.unware.poken.ui.customerorder.presenter.OrdersPresenter;
import id.unware.poken.ui.customerorder.view.adapter.OrdersAdapter;


public class OrdersFragment extends BaseFragment implements IOrdersView {

    private static final String TAG = "OrdersFragment";

    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvOrderedItem) RecyclerView rvOrderedItem;

    private Unbinder unbinder;

    private OrdersPresenter presenter;

    private List<ShoppingOrder> orderList = new ArrayList<>();
    private OrdersAdapter adapter;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnOrderFragmentListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrdersFragment() {
    }

    @SuppressWarnings("unused")
    public static OrdersFragment newInstance(int columnCount) {
        OrdersFragment fragment = new OrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_orderedproduct_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter = new OrdersPresenter(new OrdersModel(), this /*View*/);

        adapter = new OrdersAdapter(orderList, presenter);
        adapter.setHasStableIds(true);
        presenter.getOrdersData();

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
                presenter.getOrdersData();
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrderFragmentListener) {
            mListener = (OnOrderFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public void populateOrdersList(ArrayList<ShoppingOrder> orders) {
        Utils.Log(TAG, "New order list: " + orders.size());
        orderList.clear();
        orderList.addAll(orders);
        adapter.notifyDataSetChanged();


    }

    @Override
    public void openOrderDetail(ShoppingOrder shoppingOrder) {
        Utils.Log(TAG, "Open order detail ID: " + shoppingOrder.id);
        try {
            mListener.onListFragmentInteraction(shoppingOrder);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }


    public interface OnOrderFragmentListener {
        void onListFragmentInteraction(ShoppingOrder item);
    }
}
