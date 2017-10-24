package id.unware.poken.ui.address.view.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.domain.AddressBook;
import id.unware.poken.ui.address.presenter.IAddressPresenter;

/**
 * Address Book item adapter.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private ArrayList<AddressBook> listItem = new ArrayList<>();

    private long selectedIndex = 0;
    private Context mContext;
    private IAddressPresenter presenter;

    public AddressAdapter(Context context, ArrayList<AddressBook> listItem, long selectedIndex, IAddressPresenter presenter) {
        this.listItem = listItem;
        this.selectedIndex = selectedIndex;
        this.mContext = context;
        this.presenter = presenter;
    }

    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.mContext).inflate(R.layout.list_address_book, parent, false);
        return new AddressAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AddressAdapter.ViewHolder holder, int position) {
        AddressBook item = this.listItem.get(position);

        holder.text.setText(String.valueOf(item.name));
        holder.phone.setText(String.valueOf(item.phone));
        holder.address.setText(String.valueOf(item.address));

        if (item.location == null) {
            holder.addressBookTvRegion.setText(Html.fromHtml(mContext.getString(R.string.lbl_no_zip_code)));
        } else {
            String strRegion = item.location.city.concat(" ").concat(item.location.zip);
            holder.addressBookTvRegion.setText(strRegion);
        }

        holder.rbIsSelected.setChecked(item.id == selectedIndex);
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text) TextView text;
        @BindView(R.id.phone) TextView phone;
        @BindView(R.id.address) TextView address;
        @BindView(R.id.addressBookTvRegion) TextView addressBookTvRegion;
        @BindView(R.id.rbIsSelected) RadioButton rbIsSelected;
        @BindView(R.id.addressBookItemIbMenu) ImageButton addressBookItemIbMenu;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            v.setOnClickListener(this);

            addressBookItemIbMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.addressBookItemIbMenu) {
                setupAddressBookContextMenu(view, getAdapterPosition(), listItem.get(getAdapterPosition()));
            } else {
                presenter.onAddressItemSelected(getAdapterPosition(), listItem.get(getAdapterPosition()));
            }
        }
    }

    private void setupAddressBookContextMenu(final View view, final int position, final AddressBook addressBook) {

        final int MENU_POS_DELETE = 1;

        // Do cancel pickup
        // Creating the instance of PopupMenu
        final PopupMenu popup = new PopupMenu(this.mContext, view, Gravity.START);
        // Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.context_menu_address_book, popup.getMenu());

        // noinspection deprecation
        popup.getMenu().getItem(MENU_POS_DELETE).setTitle(Html.fromHtml(mContext.getString(R.string.action_delete_address_book)));

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        // Edit address book
                        presenter.startEditAddressBook(position, addressBook);
                        return true;
                    case R.id.action_delete:
                        ControllerDialog.getInstance().showYesNoDialog(
                                "Data akan dihapus?",
                                view.getContext(),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (i == DialogInterface.BUTTON_POSITIVE) {
                                            // Delete address book
                                            presenter.startDeleteAddressBookItem(position, addressBook);
                                        }

                                        dialogInterface.dismiss();
                                    }
                                },
                                view.getContext().getString(R.string.btn_positive_delete),
                                view.getContext().getString(R.string.btn_negative_no)
                        );

                        return true;
                }

                return false;
            }
        });

        popup.show();  //showing popup menu
    }
}
