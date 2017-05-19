package id.unware.poken.ui.newPackage.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import id.unware.poken.R;
import id.unware.poken.pojo.PojoAddressBook;

/**
 * Adapter for Sender/Receiver auto complete on New Package page.
 *
 * @author Anwar Pasaribu
 * @since Feb 14 2017
 */

public class AddressBookAutocompleteAdapter extends ArrayAdapter<PojoAddressBook> {
    private final int MAX_RESULT = 3;
    private final List<PojoAddressBook> mList;
    public List<PojoAddressBook> mFilteredList = new ArrayList<>();

    public AddressBookAutocompleteAdapter(Context context, List<PojoAddressBook> list) {
        super(context, 0, list);
        this.mList = list;
    }

    @Override
    public int getCount() {
        return Math.min(MAX_RESULT, mFilteredList.size());
    }

    @Nullable
    @Override
    public PojoAddressBook getItem(int position) {

        if (position < 0 || position >= mFilteredList.size()) return null;

        return mFilteredList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        ItemViewholder holder;

        if (v == null) {

            // Inflate your custom row layout as usual.
            LayoutInflater inflater = LayoutInflater.from(super.getContext());
            v = inflater.inflate(R.layout.row_address_book_autocomplete_item, parent, false);

            holder = new ItemViewholder();

            holder.textViewName = (TextView) v.findViewById(R.id.textViewTitle);
            holder.textViewAddress = (TextView) v.findViewById(R.id.textViewSubtitle);

            v.setTag(holder);

        } else {
            holder = (ItemViewholder) v.getTag();
        }

        PojoAddressBook item = mFilteredList.get(position);

        holder.textViewName.setText(item.getName());
        holder.textViewAddress.setText(item.getAddress());

        // [V49] Different color for Sender/Receiver
        int intTextColor = ContextCompat.getColor(
                super.getContext(),
                item.ismIsSenderData()
                        ? R.color.myAccentColor
                        : R.color.black_90);
        holder.textViewName.setTextColor(intTextColor);

        return v;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new AddressBookAutocompleteFilter(this, mList);
    }

    private class ItemViewholder {
        TextView textViewName, textViewAddress;
    }
}
