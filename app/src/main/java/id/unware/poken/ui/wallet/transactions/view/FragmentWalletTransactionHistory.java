package id.unware.poken.ui.wallet.transactions.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDate;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.tools.Constants;
import id.unware.poken.pojo.PojoUserTransaction;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.wallet.main.view.adapters.TransactionsAdapter;
import id.unware.poken.ui.wallet.transactions.model.TransactionsModel;
import id.unware.poken.ui.wallet.transactions.presenter.TransactionsPresenter;

public class FragmentWalletTransactionHistory extends BaseFragment implements
        ITransactionsView {

    private final String TAG = "FragmentWalletTransactionHistory";

    @BindView(R.id.parentTransactionHistoryHeader) ViewGroup parentTransactionHistoryHeader;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerViewTransactions) RecyclerView recyclerViewTransactions;
    @BindView(R.id.textViewStateMessage) TextView textViewStateMessage;

    private View parentView;
    private TransactionsAdapter mAdapter;
    private List<Object> mList;

    /**
     * Wallet Transaction presenter
     */
    private TransactionsPresenter mPresenter;

    public static FragmentWalletTransactionHistory newInstance(
            ArrayList<PojoUserTransaction> listTransactionHistory) {

        FragmentWalletTransactionHistory f = new FragmentWalletTransactionHistory();
        Bundle bndl = new Bundle();
        bndl.putParcelableArrayList(Constants.EXTRA_LIST_WALLET_TRANSACTION_HISTORY, listTransactionHistory);
        f.setArguments(bndl);
        return f;
    }

    public FragmentWalletTransactionHistory() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.Log(TAG, "On view created!");
        View view = inflater.inflate(R.layout.f_wallet_transaction_history, container, false);
        ButterKnife.bind(this, view);

        mPresenter = new TransactionsPresenter(this, new TransactionsModel());

        initView();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.loadWalletData();
    }

    private void initView() {

        Utils.Logs('w', TAG, "Init view......");
        setHasOptionsMenu(false);

        // Reload wallet data on swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Call API to reload data
                Utils.Log(TAG, "Reload Wallet data");
                listener.refreshFromHistory();
            }
        });

        parentTransactionHistoryHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int adapterCount = mAdapter != null? mAdapter.getItemCount() : -1;
                int listCount = mList != null? mList.size() : -1;
                Utils.Log(TAG, "Adapter size: " + adapterCount);
                Utils.Log(TAG, "List size: " + listCount);

            }
        });
    }

    /**
     * Update fragment interface based on package availability.
     *
     * @param uiState : {@link UIState} to indicate package availability.
     */
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                setupLoadingState(true);
                break;
            case NODATA:
                break;
            case FINISHED:
                // Hide loading state
                setupLoadingState(false);
                break;
            case ERROR:
                // Show error mode
                setupErrorScreen(true);
                break;
        }

    }

    private void setupLoadingState(boolean isLoading) {
        swipeRefreshLayout.setRefreshing(isLoading);
    }

    private void setupErrorScreen(boolean isError) {

        // Show error message
        textViewStateMessage.setVisibility(isError ? View.VISIBLE : View.GONE);
        Utils.Log(TAG, "Text message visibility: " + textViewStateMessage.getVisibility());

        // Hide swipe refresh loading indicator
        swipeRefreshLayout.setRefreshing(false);

    }

    private void setupWalletTransactionList(List<PojoUserTransaction> userTransactionList) {
        Utils.Log(TAG, "Setup trans list size: " + userTransactionList.size());

        mList = new ArrayList<>();
        mList.addAll(userTransactionList);

        mAdapter = new TransactionsAdapter(parent, mList, new OnClickRecyclerItem() {
            @Override
            public void onItemClick(View view, int position) {

                if (position < 0 || position >= mList.size()) return;

                Object clickedItem = mList.get(position);
                if (clickedItem != null && clickedItem instanceof PojoUserTransaction) {

                    // Copy formatted user transaction to clipboard.
                    PojoUserTransaction pojoUserTransaction = (PojoUserTransaction) clickedItem;

                    String strFormattedTransaction =
                            parent.getString(
                                    R.string.msg_user_transaction_snippet,
                                    pojoUserTransaction.getTransactionId(),
                                    ControllerDate.getInstance().toTransactionHistory(pojoUserTransaction.getCreatedOn()));

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.lbl_transaction_info));
                    sendIntent.putExtra(Intent.EXTRA_TEXT, strFormattedTransaction);
                    sendIntent.setType("text/plain");
                    parent.startActivity(Intent.createChooser(sendIntent, parent.getString(R.string.title_share_wallet_transaction_item)));
                }
            }
        });

        recyclerViewTransactions.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
        recyclerViewTransactions.setAdapter(mAdapter);
        recyclerViewTransactions.setHasFixedSize(true);

        Utils.Log(TAG, "Adapter size after notify: " + mAdapter.getItemCount());

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public View getParentView() {
        return parentView;
    }

    @Override
    public void showTransactionList(List<PojoUserTransaction> userTransactionList) {
        setupWalletTransactionList(userTransactionList);
    }

    @Override
    public void setParentView(View parentView) {
        this.parentView = parentView;
    }

    private FragmentTransaction hideDialog(String strTag) {

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        Fragment prev = getFragmentManager().findFragmentByTag(strTag);

        if (prev != null && prev instanceof DialogFragment) {
            Utils.Log(TAG, "Prev fragment is not null. Tag: " + strTag);

            ((DialogFragment) prev).dismiss();
            ft.remove(prev);
        }

        return ft;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private WalletHistoryListener listener;

    public void setListener(WalletHistoryListener listener) {
        this.listener = listener;
    }

    public interface WalletHistoryListener {
        void refreshFromHistory();
    }

}