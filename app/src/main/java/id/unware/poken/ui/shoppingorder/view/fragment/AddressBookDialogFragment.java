package id.unware.poken.ui.shoppingorder.view.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.unware.poken.R;
import id.unware.poken.domain.AddressBook;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;

/**
 * <p>A fragment that shows a list of items as a modal bottom sheet.</p>
 * <p>You can show this modal bottom sheet from your activity like this:</p>
 * <pre>
 *     ItemListDialogFragment.newInstance(30).show(getSupportFragmentManager(), "dialog");
 * </pre>
 * <p>You activity (or fragment) needs to implement {@link AddressBookDialogFragment.Listener}.</p>
 */
public class AddressBookDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.addressBookIbClose) ImageButton addressBookIbClose;
    @BindView(R.id.addressBookBtnAdd) Button addressBookBtnAdd;

    // View Flipper for List or Input mode
    @BindView(R.id.addressBookParentViewFlipper) ViewFlipper addressBookParentViewFlipper;
    // View #1
    @BindView(R.id.addressBookRecyclerView) RecyclerView addressBookRecyclerView;
    // View #2
    @BindView(R.id.addressBookTilName) TextInputLayout addressBookTilName;
    @BindView(R.id.editTextAddressBookName) EditText editTextAddressBookName;
    @BindView(R.id.addressBookTilPhone) TextInputLayout addressBookTilPhone;
    @BindView(R.id.editTextPhone) EditText editTextPhone;
    @BindView(R.id.addressBookTilFullAddress) TextInputLayout addressBookTilFullAddress;
    @BindView(R.id.editTextFullAddress) EditText editTextFullAddress;
    @BindView(R.id.btnAddAddressBook) Button btnAddAddressBook;

    private Unbinder unbinder;

    private ArrayList<AddressBook> listItem = new ArrayList<>();
    private int selectedAddressBookIndex = 0;
    private boolean isAddressBookAvailable = true;

    private boolean isListMode = true;  // List mode as default
    private int DISPLAY_MODE_LIST = 0;
    private int DISPLAY_MODE_INPUT = 1;

    private static final String ARG_ITEM_COUNT = "item_count";
    private static final String ARG_SELECTED_ITEM_INDEX = "selected_item_index";
    private static final String ARG_IS_ADDRESS_BOOK_AVAILABLE = "is_add_available";
    private Listener mListener;

    public static AddressBookDialogFragment newInstance(int itemCount, int selectedAddressBookIndex, boolean isAddressBookAvailable) {
        final AddressBookDialogFragment fragment = new AddressBookDialogFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_ITEM_COUNT, itemCount);
        args.putInt(ARG_SELECTED_ITEM_INDEX, selectedAddressBookIndex);
        args.putBoolean(ARG_IS_ADDRESS_BOOK_AVAILABLE, isAddressBookAvailable);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListData(ArrayList<AddressBook> addressBookArrayList) {
        this.listItem.clear();
        this.listItem.addAll(addressBookArrayList);
        this.addressBookRecyclerView.setAdapter(new ItemAdapter(this.listItem, this.selectedAddressBookIndex));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedAddressBookIndex = getArguments().getInt(ARG_SELECTED_ITEM_INDEX, 0);
            isAddressBookAvailable = getArguments().getBoolean(ARG_IS_ADDRESS_BOOK_AVAILABLE, true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_item_list_dialog, container, false);

        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        addressBookRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addressBookRecyclerView.setAdapter(new ItemAdapter(this.listItem, 0));

        if (mListener != null) {
            Utils.devModeToast(getContext(), "View address book ready");
            mListener.onViewReady();
        }
    }

    private void initView() {

        // Show List Mode first
        if (isListMode && isAddressBookAvailable) {
            addressBookParentViewFlipper.setDisplayedChild(DISPLAY_MODE_LIST);
        } else if (!isAddressBookAvailable) {
            addressBookParentViewFlipper.setDisplayedChild(DISPLAY_MODE_INPUT);
        }

        addressBookBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListMode) {
                    addressBookParentViewFlipper.setDisplayedChild(DISPLAY_MODE_INPUT);
                    setupAddressBookVisibleView(DISPLAY_MODE_INPUT);
                } else {
                    addressBookParentViewFlipper.setDisplayedChild(DISPLAY_MODE_LIST);
                    setupAddressBookVisibleView(DISPLAY_MODE_LIST);
                }

                isListMode = !isListMode;

            }
        });

        int activeChildIndex = addressBookParentViewFlipper.getDisplayedChild();
        setupAddressBookVisibleView(activeChildIndex);

        addressBookIbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAddAddressBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedNewAddressBook();

            }
        });
    }

    private void setupAddressBookVisibleView(int viewChildIndex) {
        if (viewChildIndex == DISPLAY_MODE_INPUT) {
            addressBookBtnAdd.setText("Batal");
        } else if (viewChildIndex == DISPLAY_MODE_LIST) {
            addressBookBtnAdd.setText("Tambah");
        }
    }

    private void proceedNewAddressBook() {
        if (isAddressBookFormReady()) {
            if (mListener != null) {
                AddressBook addressBook = new AddressBook();
                addressBook.name = String.valueOf(editTextAddressBookName.getText());
                addressBook.phone = String.valueOf(editTextPhone.getText());
                addressBook.address = String.valueOf(editTextFullAddress.getText());
                mListener.onNewAddressBook(addressBook);
                dismiss();
            }
        }
    }

    private boolean isAddressBookFormReady() {
        if (StringUtils.isEmpty(String.valueOf(editTextAddressBookName.getText()))) {
            addressBookTilName.setError("Nama penerima tidak boleh kosong.");
            return false;
        }

        if (StringUtils.isEmpty(String.valueOf(editTextPhone.getText()))) {
            addressBookTilPhone.setError("Nomor penerima tidak boleh kosong.");
            return false;
        }

        if (StringUtils.isEmpty(String.valueOf(editTextFullAddress.getText()))) {
            addressBookTilFullAddress.setError("Alamat lengkap tidak boleh kosong.");
            return false;
        }

        return true;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(android.support.design.R.id.design_bottom_sheet);

                if (bottomSheet != null) {
                    BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        dialog.setTitle("Alamat Penerima");

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mListener = (Listener) parent;
        } else {
            mListener = (Listener) context;
        }
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public interface Listener {
        void onItemClicked(int position);

        void onNewAddressBook(AddressBook addressBook);

        void onViewReady();
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        final TextView text;
        final TextView phone;
        final TextView address;
        final RadioButton rbIsSelected;

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.fragment_item_list_dialog_item, parent, false));

            text = (TextView) itemView.findViewById(R.id.text);
            phone = (TextView) itemView.findViewById(R.id.phone);
            address = (TextView) itemView.findViewById(R.id.address);
            rbIsSelected = (RadioButton) itemView.findViewById(R.id.rbIsSelected);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClicked(getAdapterPosition());
                        dismiss();
                    }
                }
            });
        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {

        private ArrayList<AddressBook> listItem = new ArrayList<>();
        private int selectedIndex = 0;

        public ItemAdapter(ArrayList<AddressBook> listItem, int selectedIndex) {
            this.listItem = listItem;
            this.selectedIndex = selectedIndex;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            AddressBook item = this.listItem.get(position);
            holder.text.setText(String.valueOf(item.name));
            holder.phone.setText(String.valueOf(item.phone));
            holder.address.setText(String.valueOf(item.address));

            holder.rbIsSelected.setChecked(holder.getAdapterPosition() == selectedIndex);
        }

        @Override
        public int getItemCount() {
            return listItem.size();
        }

    }

}
