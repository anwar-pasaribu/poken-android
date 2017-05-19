package id.unware.poken.ui.newPackage.view.adapters;

import android.widget.Filter;
import java.util.ArrayList;
import java.util.List;

import id.unware.poken.pojo.PojoAddressBook;

/**
 * @author Anwar Pasaribu
 * @since Feb 14 2017
 */

class AddressBookAutocompleteFilter extends Filter {

    private final String TAG = "AddressBookAutocompleteFilter";

    AddressBookAutocompleteAdapter adapter;
    List<PojoAddressBook> originalList;
    List<PojoAddressBook> filteredList;

    public AddressBookAutocompleteFilter(AddressBookAutocompleteAdapter adapter, List<PojoAddressBook> originalList) {
        super();
        this.adapter = adapter;
        this.originalList = originalList;
        this.filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults results = new FilterResults();

        if (constraint == null || constraint.length() == 0) {
            filteredList.addAll(originalList);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();

            // Search by name
            for (final PojoAddressBook addressBook : originalList) {
                if (addressBook.getName().toLowerCase().startsWith(filterPattern, 0)) {
                    filteredList.add(addressBook);
                }
            }
        }
        results.values = filteredList;
        results.count = filteredList.size();
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.mFilteredList.clear();
        adapter.mFilteredList.addAll((List) results.values);
        adapter.notifyDataSetChanged();
    }
}