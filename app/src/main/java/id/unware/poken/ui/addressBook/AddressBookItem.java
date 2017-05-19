package id.unware.poken.ui.addressBook;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractSectionableItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.utils.Utils;
import eu.davidea.viewholders.FlexibleViewHolder;
import id.unware.poken.R;
import id.unware.poken.pojo.PojoAddressBook;
import id.unware.poken.tools.BitmapUtil;

public class AddressBookItem extends AbstractSectionableItem<AddressBookItem.AddressBookItemViewHolder, AddressBookHeaderItem> implements IFilterable {

    private String id;
    private String addressBookName;
    private String addressBookPhone;
    private String addressBookAddress;
    private PojoAddressBook pojoAddressBook;

    public AddressBookItem(String id, AddressBookHeaderItem header) {
        super(header);
        this.id = id;
        this.header = header;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof AddressBookItem) {
            AddressBookItem inItem = (AddressBookItem) inObject;
            return this.id.equals(inItem.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public AddressBookItem withName(String name) {
        this.addressBookName = name;
        getHeader().setTitle(name);
        return this;
    }

    public AddressBookItem withPhone(String strPhoneNumber) {
        this.addressBookPhone = strPhoneNumber;
        return this;
    }

    public AddressBookItem withAddress(String address) {
        this.addressBookAddress = address;
        return this;
    }

    public AddressBookItem withPojoAddressBook(PojoAddressBook pojoAddressBook) {
        this.pojoAddressBook = pojoAddressBook;
        return this;
    }

    public String getId() {
        return id != null ? id : "";
    }

    /**
     * Return formatted Sender or Receiver data.
     *
     * @return Formatted String for Sender or Receiver.
     * @since (Sep 19th 2016) - Version 41
     */
    public String getAddressDetail() {
        return String.format("%s %s %s",
                addressBookName != null ? addressBookName : "",
                addressBookPhone != null ? addressBookPhone : "",
                addressBookAddress != null ? addressBookAddress : "");
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_address_book;
    }

    @Override
    public AddressBookItemViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new AddressBookItemViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(final FlexibleAdapter adapter, AddressBookItemViewHolder holder, int position, List payloads) {

        final Context context = holder.itemView.getContext();
        final int intColor = this.pojoAddressBook.ismIsSenderData() ?
                R.color.myAccentColor
                : R.color.myPrimaryColor;

        holder.imgProfileAddressBook.setColorFilter(BitmapUtil.getDrawableFilter(
                context, intColor));

        if (adapter.hasSearchText()) {

            Utils.highlightText(context, holder.txtNameAddressBook, addressBookName, adapter.getSearchText(),
                    ContextCompat.getColor(context, R.color.myAccentColor));

            Utils.highlightText(context, holder.txtPhoneAddressBook, addressBookPhone, adapter.getSearchText(),
                    ContextCompat.getColor(context, R.color.myAccentColor));

            Utils.highlightText(context, holder.txtAddressAddressBook, addressBookAddress, adapter.getSearchText(),
                    ContextCompat.getColor(context, R.color.myAccentColor));
        } else {
            holder.txtNameAddressBook.setText(addressBookName);
            holder.txtPhoneAddressBook.setText(addressBookPhone);
            holder.txtAddressAddressBook.setText(addressBookAddress);
        }

    }

    @Override
    public boolean filter(String constraint) {
        return addressBookName != null && addressBookName.toLowerCase().trim().contains(constraint)
                || addressBookPhone != null && addressBookPhone.trim().contains(constraint)
                || addressBookAddress != null && addressBookAddress.toLowerCase().trim().contains(constraint);
    }

    static final class AddressBookItemViewHolder extends FlexibleViewHolder {

        @BindView(R.id.imgProfileAddressBook) ImageView imgProfileAddressBook;
        @BindView(R.id.txtNameAddressBook) TextView txtNameAddressBook;
        @BindView(R.id.txtPhoneAddressBook) TextView txtPhoneAddressBook;
        @BindView(R.id.txtAddressAddressBook) TextView txtAddressAddressBook;

        public AddressBookItemViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public String toString() {
        return "AddressBookItem[" + super.toString() + "]";
    }

}