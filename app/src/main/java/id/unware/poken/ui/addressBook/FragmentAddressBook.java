package id.unware.poken.ui.addressBook;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.IFlexible;
import id.unware.poken.PokenApp;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.itemdecoration.ItemDecorationDivider;
import id.unware.poken.pojo.PojoAddressBook;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;

public class FragmentAddressBook extends Fragment implements
        FlexibleAdapter.OnUpdateListener,
        FlexibleAdapter.OnItemClickListener,
        SearchView.OnQueryTextListener {

    private final String TAG = "FragmentAddressBook";

    private final PokenApp values = PokenApp.getInstance();

    private View rootView;

    // Empty state view
    @BindView(R.id.emptyStateParent) RelativeLayout emptyStateParent;
    @BindView(R.id.emptyStateImage) ImageView emptyStateImage;
    @BindView(R.id.txtEmptyPackage) TextView txtEmptyPackage;

    @BindView(R.id.rvPackageHistory) RecyclerView lvAddressBook;
    @BindView(R.id.fabCreatePackage) FloatingActionButton fabCreatePackage;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;

    // Flexible list
    // private FlexibleAdapter<IFlexible> adapter;
    private FlexibleAdapter<IFlexible> adapter;
    List<IFlexible> listFlexibleAddressBooks = new ArrayList<IFlexible>();
    List<IFlexible> listFlexibleAddressBooksSearchCopy = new ArrayList<IFlexible>();

    private Context mContext;

    /**
     * Indicate that this fragment is called from New Package attach address
     */
    private boolean fromNewPackage = false;

    /**
     * Determine which empty state view will be showed.
     */
    private boolean inSearchMode = false;


    //////
    // S: SearchView.OnQueryTextListener
    @Override
    public boolean onQueryTextSubmit(String query) {
        Utils.Log(TAG, "On query text submit: " + query);
        return onQueryTextChange(query);
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        newText = newText.trim();

        if (adapter.hasNewSearchText(newText)) {
            Utils.Log(TAG, "onQueryTextChange newText: " + newText);
            if (adapter != null) {
                adapter.setSearchText(newText);

                // Fill and Filter mItems with your custom list and automatically animate the changes
                // Watch out! The original list must be a copy
                adapter.filterItems(getCopyOfListData(), 300L);

            } else {
                adapter.setSearchText("");
                adapter.setNotifyChangeOfUnfilteredItems(true);
            }

        }

        //Disable SwipeRefresh if search is active!!
        swipeRefreshLayout.setEnabled(!adapter.hasSearchText());

        adapter.setNotifyChangeOfUnfilteredItems(true);

        return true;
    }
    // E: SearchView.OnQueryTextListener
    //////

    @Override
    public void onUpdateEmptyView(int size) {

        Log.d(TAG, "onUpdateEmptyView size=" + size);
        Log.d(TAG, "List size=" + listFlexibleAddressBooks.size());

        TextView emptyView = (TextView) rootView.findViewById(R.id.empty);
        emptyView.setText(mContext.getString(R.string.no_items));

        if (inSearchMode) {

            if (size > 0) {
                emptyView.setVisibility(View.GONE);
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }

        } else {

            if (size > 0) {
                emptyView.setVisibility(View.GONE);
                interfaceState(Constants.STATE_FINISHED);
            } else {
                interfaceState(Constants.STATE_NODATA);
            }

        }

    }

    @Override
    public boolean onItemClick(int position) {

        if (adapter.getItem(position) instanceof AddressBookHeaderItem) {
            Utils.Log(TAG, "Header Item Click on pos: " + position);
            Utils.Log(TAG, "Item: " + adapter.getItem(position));
        } else {
            Utils.Log(TAG, "On Item Click pos: " + adapter.getItem(position));
            AddressBookItem addressBookItem = (AddressBookItem) adapter.getItem(position);
            Utils.Log(TAG, "Addressbook item id: " + addressBookItem.getId());
            returnData(addressBookItem.getAddressDetail());
        }

        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.f_address_book, container, false);
        ButterKnife.bind(this, rootView);

        mContext = getActivity();

        initView();

        // Flexible RV
        initFlexibleRecyclerview();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (mContext == null) {
            mContext = context;
        }
    }

    private void initView() {
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            fromNewPackage = getArguments().getBoolean(Constants.EXTRA_FROM_NEW_PACKAGE, false);
            Utils.Log(TAG, "From New Package -> " + fromNewPackage);
        }

        // Remove FAB when Address Book fragment is called from New Package
        if (fromNewPackage) {
            fabCreatePackage.hide();
        }
    }

    private void initFlexibleRecyclerview() {
        //Initialize the Adapter with AddressBook Flexible item and listener
        adapter = new FlexibleAdapter<>(getFlexiblePojoBooking(), FragmentAddressBook.this);
        adapter.initializeListeners(this)
                .setDisplayHeadersAtStartUp(true)
                .setRemoveOrphanHeaders(false)
                .setNotifyMoveOfFilteredItems(false)
                .setNotifyChangeOfUnfilteredItems(true)
                .enableStickyHeaders();

        /** Init RecyclerView with it's adapter (AdapterAddressBook) */
        lvAddressBook.setLayoutManager(new SmoothScrollLinearLayoutManager(getActivity()));
        lvAddressBook.setAdapter(adapter);
        lvAddressBook.setHasFixedSize(true);
        lvAddressBook.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
                //NOTE: This allows to receive Payload objects on notifyItemChanged called by the Adapter!!!
                Utils.Log(TAG, "canReuseUpdatedViewHolder");
                return true;
            }
        });

        lvAddressBook.addItemDecoration(new ItemDecorationDivider(mContext, LinearLayoutManager.VERTICAL));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public List<IFlexible> getFlexiblePojoBooking() {
        /*
        HOW TO CREATE HEADER

        A --> charHeader = A --> New Header H#1
        A --> charHeader == A ? Yes --> Use H#1
        A --> charHeader == A ? Yes --> Use H#1
        B --> charHeader == A ? No  --> charHeader = B --> New Header H#2
        B --> charHeader == B ? Yes --> Use H#2
        C --> charHeader == C ? No  --> charHeader = C --> New Header H#3
        */

        char charHeader = '!';
        AddressBookHeaderItem header = new AddressBookHeaderItem("H1");
        AddressBookItem addressBookItem;

        /** Get all PojoBooking data from Realm and populate to IFlexible list item.*/
        int index = 0;
        for (PojoAddressBook pojoAddressBook : ControllerRealm.getInstance().getAllAddressBook()) {

            if (pojoAddressBook.getName().trim().isEmpty())
                continue;

            final char c = Character.toUpperCase(pojoAddressBook.getName().trim().charAt(0));

            if (charHeader != (!(c >= 65 && c <= 90) ? '#' : c)) {
                header = new AddressBookHeaderItem("H-" + index);

                addressBookItem = new AddressBookItem(header.getId() + "I-" + index, header)
                        .withName(pojoAddressBook.getName().trim())
                        .withPhone(pojoAddressBook.getPhoneNumber().trim())
                        .withAddress(pojoAddressBook.getAddress().trim())
                        .withPojoAddressBook(pojoAddressBook);

                listFlexibleAddressBooks.add(addressBookItem);
                listFlexibleAddressBooksSearchCopy.add(addressBookItem);

            } else {
                addressBookItem = new AddressBookItem(header.getId() + "I-" + index, header)
                        .withName(pojoAddressBook.getName().trim())
                        .withPhone(pojoAddressBook.getPhoneNumber().trim())
                        .withAddress(pojoAddressBook.getAddress().trim())
                        .withPojoAddressBook(pojoAddressBook);

                listFlexibleAddressBooks.add(addressBookItem);
                listFlexibleAddressBooksSearchCopy.add(addressBookItem);
            }

            charHeader = !(c >= 65 && c <= 90) ? '#' : c;

            index++;
        }

        return listFlexibleAddressBooks;
    }

    private List<IFlexible> getCopyOfListData() {
        Utils.Log(TAG, "Search List size: " + listFlexibleAddressBooksSearchCopy.size());
        return new ArrayList<>(listFlexibleAddressBooksSearchCopy);
    }

    /**
     * Update fragment interface based on Address Book availability.
     *
     * @param stateNum : State number to indicate package availability.
     */
    private void interfaceState(int stateNum) {
        switch (stateNum) {
            case Constants.STATE_LOADING:
                Utils.Log(TAG, "Loading address book data");
                emptyStateParent.setVisibility(View.VISIBLE);
                txtEmptyPackage.setText(mContext.getString(R.string.msg_loading_data));
                emptyStateImage.setVisibility(View.GONE);
                break;
            case Constants.STATE_NODATA:
                Utils.Log(TAG, "No data");
                emptyStateParent.setVisibility(View.VISIBLE);
                txtEmptyPackage.setText(mContext.getString(R.string.lbl_no_address_book));
                emptyStateImage.setImageResource(R.drawable.img_empty_state_address_book);
                break;
            case Constants.STATE_FINISHED:
                Utils.Log(TAG, "Data available");
                emptyStateParent.setVisibility(View.GONE);
                break;

        }

    }

    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Prepare and return data to predecessor Activity.
     * Data is fetched from Realm database based on booking_id.
     *
     * @param booking_id : String booking_id
     */
    private void returnData(String booking_id) {

        /** In case list item return complete data instead booking_id*/
        if (booking_id.length() >= 11) {
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_ADDRESS_DETAIL, booking_id);

            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();

            return;
        }

        long longBookingId = 0;
        try {
            longBookingId = Long.parseLong(booking_id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        final PojoBooking contact = ControllerRealm.getInstance().getBookingById(longBookingId);

        Intent intent = new Intent();
        intent.putExtra("to_name", contact.getTo_name());
        intent.putExtra("to_address", contact.getTo_address());
        intent.putExtra("to_city",
                Utils.getCityById(getActivity(), contact.getTo_area_id())
                        .getCity());
        intent.putExtra("to_zip_code", contact.getTo_zip_code());
        intent.putExtra("to_phone", contact.getTo_phone());
        intent.putExtra("to_email", contact.getTo_email());

        // Single data for Sender and Receiver
        String sAddress = String.format("%s %s %s",
                contact.getTo_name(),
                contact.getTo_phone(),
                contact.getTo_address());

        Utils.Log(TAG, "Address book data: " + sAddress);

        intent.putExtra(Constants.EXTRA_ADDRESS_DETAIL, sAddress);

        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (menu != null) menu.clear();

        inflater.inflate(R.menu.m_address_book, menu);

        initSearchView(menu);

    }

    private void initSearchView(final Menu menu) {

        //Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search_address_book);

        if (searchItem != null) {
            MenuItemCompat.setOnActionExpandListener(
                    searchItem, new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionExpand(MenuItem item) {
                            inSearchMode = true;
                            Utils.Log(TAG, "Search view expanded. Then begin search mode!");
                            return true;
                        }

                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            inSearchMode = false;
                            Utils.Log(TAG, "Search view collapsed. Then end search mode.");
                            return true;
                        }
                    });

            SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            mSearchView.setInputType(InputType.TYPE_TEXT_VARIATION_FILTER);
            mSearchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH | EditorInfo.IME_FLAG_NO_FULLSCREEN);
            mSearchView.setQueryHint(mContext.getString(R.string.hint_search_address_book));
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            mSearchView.setOnQueryTextListener(this);

        }
    }
}