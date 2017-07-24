package id.unware.poken.ui.wallet.main.view;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.pojo.PojoDeposit;
import id.unware.poken.pojo.PojoWalletData;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.MyLog;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;
import id.unware.poken.ui.wallet.main.model.WalletModel;
import id.unware.poken.ui.wallet.main.presenter.WalletPresenter;
import id.unware.poken.ui.wallet.main.view.adapters.WalletPagerAdapter;
import id.unware.poken.ui.wallet.topup.view.FragmentWalletTopup;
import id.unware.poken.ui.wallet.transactions.view.FragmentWalletTransactionHistory;
import id.unware.poken.ui.wallet.withdrawal.view.FragmentWalletWithdrawal;



public class FragmentWallet extends BaseFragment implements
        TabLayout.OnTabSelectedListener,
        FragmentWalletWithdrawal.WalletWithdrawalListener,
        FragmentWalletTransactionHistory.WalletHistoryListener,
        IWalletView {

    private final String TAG = "FragmentWallet";

    @BindView(R.id.textViewTotalBalanceLable) TextView textViewTotalBalanceLable;
    @BindView(R.id.textViewBalance) TextView textViewBalance;
    @BindView(R.id.progressBarWalletStatus) ProgressBar progressBarWalletStatus;
    @BindView(R.id.container) ViewPager mViewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.parentView) ViewGroup parentView;

    /**
     * Hold all wallet info data. This object replaced by newly downloaded data.
     */
    private PojoWalletData mPojoWalletData;

    private WalletPagerAdapter mSectionsPagerAdapter;

    private WalletPresenter presenter;

    private FragmentWalletTransactionHistory fragmentWalletTransactionHistory;
    private FragmentWalletTopup fragmentWalletTopup;
    private FragmentWalletWithdrawal fragmentWalletWithdrawal;

    /**
     * Decimal format for Indonesia
     */
    private DecimalFormat mMoneyFormatIndo = new DecimalFormat(
            "#,##0", new DecimalFormatSymbols(new Locale("id", "ID")));

    public static FragmentWallet newInstance() {
        return new FragmentWallet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.Log(TAG, "On view created!");
        View view = inflater.inflate(R.layout.f_wallet, container, false);
        ButterKnife.bind(this, view);
        presenter = new WalletPresenter(this, new WalletModel());
        initView();
        return view;
    }

    private void initView() {
        // Apply view pager to tabs
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.addOnTabSelectedListener(this);

        setHasOptionsMenu(true);

        presenter.loadWalletData();

    }

    @Override
    public void setupViewPager(PojoWalletData pojoWalletData) {

        mPojoWalletData = pojoWalletData;

        Utils.Log(TAG, "View pager adapter. ");
        if (mSectionsPagerAdapter == null) {
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new WalletPagerAdapter(parent, this.getChildFragmentManager());

            Utils.Log(TAG, "User transaction size: " + mPojoWalletData.getUserTransaction().size());

            // User transaction
            fragmentWalletTransactionHistory = FragmentWalletTransactionHistory.newInstance(
                    mPojoWalletData.getUserTransaction()
            );
            fragmentWalletTransactionHistory.setParentView(getParentView());
            fragmentWalletTransactionHistory.setListener(this);

            // Top Up
            fragmentWalletTopup = FragmentWalletTopup.newInstance(
                    mPojoWalletData.getDeposit()
            );
            fragmentWalletTopup.setParentView(getParentView());

            // Withdrawal
            fragmentWalletWithdrawal = FragmentWalletWithdrawal.newInstance(
                    mPojoWalletData.getUserBalance(),
                    mPojoWalletData.getWithdraw(),
                    mPojoWalletData.getWalletConfig(),
                    mPojoWalletData.getUserBank(),
                    mPojoWalletData.getBankList());
            fragmentWalletWithdrawal.setParentView(getParentView());
            fragmentWalletWithdrawal.setListener(this);

            // Add fragments to tab
            mSectionsPagerAdapter.addFragment(fragmentWalletTransactionHistory);
            mSectionsPagerAdapter.addFragment(fragmentWalletTopup);
            mSectionsPagerAdapter.addFragment(fragmentWalletWithdrawal);

            // Set up the ViewPager with the sections adapter.
            mViewPager.setAdapter(mSectionsPagerAdapter);

        } else {

            Utils.Logs('i', TAG, "Pager adapter is not null. And select tab pos: " + tabLayout.getSelectedTabPosition());

            if (tabLayout.getSelectedTabPosition() == 0) {
                // Update User Transaction history section content
                fragmentWalletTransactionHistory.showTransactionList(mPojoWalletData.getUserTransaction());
            } else if (tabLayout.getSelectedTabPosition() == 1) {

                // Update Top Up section content
                PojoDeposit pojoDeposit = mPojoWalletData.getDeposit().getDeposit();
                if (pojoDeposit != null) {
                    // Decide show waiting to transfer state or show input
                    fragmentWalletTopup.showPendingPage(pojoDeposit);
                }

            } else if (tabLayout.getSelectedTabPosition() == 2) {
                // Update Withdarawal section content
                fragmentWalletWithdrawal.setUserBanks(mPojoWalletData.getUserBank());
                fragmentWalletWithdrawal.refreshBankList();
            }
        }

    }

    private CharSequence makeCharSequence(String strBalance) {

        if (parent == null || parent.isFinishing()) return null;

        String prefix = parent.getString(R.string.lbl_idr);
        String sequence = prefix + " " + strBalance;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(Typeface.NORMAL), 0, prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(Typeface.BOLD), prefix.length(), sequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    public void reloadWalletInfo() {
        if (presenter != null) {
            presenter.loadWalletData();
        }
    }

    /**
     * Update fragment interface based on package availability.
     *
     * @param uiState : {@link UIState} to indicate data response availability.
     */
    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                showLoadingState(true);
                break;
            case NODATA:

                break;
            case FINISHED:
                // Hide loading progress
                showLoadingState(false);
                break;
        }

    }

    @Override
    public boolean isActivityFinishing() {
        return parent.isFinishing();
    }

    //////
    // S: Fragment Wallet (Main) View impl.
    @Override
    public View getParentView() {
        return parentView;
    }

    @Override
    public void setBalance(final String balance) {

        if (textViewTotalBalanceLable == null || textViewBalance == null) return;

        double parsedBalance = Utils.getParsedDouble(balance);
        final String formattedBalance = mMoneyFormatIndo.format(parsedBalance);

        // Animate balance label and value.
        textViewTotalBalanceLable.animate().alpha(1).withEndAction(new Runnable() {
            @Override
            public void run() {
                textViewBalance.setText(makeCharSequence(formattedBalance));
                textViewBalance.animate().alpha(1);
            }
        });
    }

    private void showLoadingState(boolean isLoading) {

        if (progressBarWalletStatus == null) return;

        if (isLoading) {
            progressBarWalletStatus.setVisibility(View.VISIBLE);

            // Hide balance info while loading
            textViewTotalBalanceLable.setAlpha(0);
            textViewBalance.setAlpha(0);
        } else {
            progressBarWalletStatus.setVisibility(View.GONE);
        }
    }

    //////
    // S: Tab selection listener
    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        Utils.Log(TAG, "[onTabSelected] View pager current item: " + tab.getPosition());

        // Set current pager (Fragment to show)
        mViewPager.setCurrentItem(tab.getPosition());

        try {
            // Withdrawal
            if (tab.getPosition() == 2 && mPojoWalletData != null) {

                fragmentWalletWithdrawal.checkWithdrawStatus();

                // S: Update Withdarawal section content

                // Decide view state, pending or no
                if (mPojoWalletData.getWithdraw().isPending()) {
                    fragmentWalletWithdrawal.showTransferSummary(
                            mPojoWalletData.getWithdraw().getWithdraw()
                    );
                }

                // Show withdrawal min max amount.
                fragmentWalletWithdrawal.showWithdrawalAmount(
                        mPojoWalletData.getUserBalance(),
                        String.valueOf(mPojoWalletData.getWalletConfig().getWalletMinWithdraw())
                );

                // Update user banks spinner
                fragmentWalletWithdrawal.setUserBanks(mPojoWalletData.getUserBank());
                fragmentWalletWithdrawal.refreshBankList();
            }
        } catch (NullPointerException e) {
            MyLog.FabricLog(Log.ERROR, TAG + " - Tab selected with null object.", e);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
    // E: Tab selection listener
    //////

    @Override
    public void refreshFromWithdraw() {
        presenter.loadWalletData();
    }

    @Override
    public void refreshFromHistory() {
        presenter.loadWalletData();
    }
}
