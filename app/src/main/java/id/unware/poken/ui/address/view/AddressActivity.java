package id.unware.poken.ui.address.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDialog;
import id.unware.poken.domain.AddressBook;
import id.unware.poken.domain.Location;
import id.unware.poken.pojo.UIState;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivity;
import id.unware.poken.ui.address.model.AddressModel;
import id.unware.poken.ui.address.presenter.AddressPresenter;
import id.unware.poken.ui.address.view.adapter.AddressAdapter;

public class AddressActivity extends BaseActivity implements IAddressView {

    private static final String TAG = "AddressActivity";

    @BindView(R.id.addressBookIbClose) ImageButton addressBookIbClose;
    @BindView(R.id.addressBookBtnSwitchView) Button addressBookBtnSwitchView;

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

    // POS relevant address
    @BindView(R.id.inputAddressBookSpinnerCity) Spinner inputAddressBookSpinnerCity;
    @BindView(R.id.inputAddressBookSpinnerDistrict) Spinner inputAddressBookSpinnerDistrict;
    @BindView(R.id.inputAddressBookSpinnerSubdistrict) Spinner inputAddressBookSpinnerSubdistrict;

    @BindView(R.id.btnAddAddressBook) Button btnAddAddressBook;

    // RESOURCE
    @BindString(R.string.btn_addressbook_submit) String strSubmitNewAddressBook;
    @BindString(R.string.btn_update_address_book) String strUpdateAddressBook;

    private long selectedId = 0;
    private boolean isAddressBookAvailable = true;

    private boolean isListMode = true;  // List mode as default
    private int DISPLAY_MODE_LIST = 0;
    private int DISPLAY_MODE_INPUT = 1;

    private AddressPresenter presenter;

    private AddressAdapter adapter;
    private ArrayList<AddressBook> listItem = new ArrayList<>();

    private ArrayList<Location> locations = new ArrayList<>();
    private LinkedHashSet<String> cityNames = new LinkedHashSet<>();
    private LinkedHashSet<String> disctrictNames = new LinkedHashSet<>();
    private LinkedHashSet<String> subdisctrictNames = new LinkedHashSet<>();
    private int foundCityNamePos = 0, foundDistrictNamePos = 0, foundSubDistrictNamePos = 0;
    private String selectedCityName;
    private String selectedDisctrictName;
    private String selectedSubdistrictName;


    private ProgressDialog progressDialog;

    // Address Book data to edit
    private AddressBook currentAddressBook;
    private int currentAddressBookPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);

        presenter = new AddressPresenter(new AddressModel(), this);

        presenter.getAddressBookData();

        initView();
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        super.onDestroy();
    }

    private void initView() {

        adapter = new AddressAdapter(this, listItem, selectedId, presenter);
        addressBookRecyclerView.setAdapter(adapter);
        addressBookRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        addressBookBtnSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListMode) {
                    setupAddressBookVisibleView(DISPLAY_MODE_INPUT);
                } else {
                    setupAddressBookVisibleView(DISPLAY_MODE_LIST);
                }
            }
        });

        int activeChildIndex = addressBookParentViewFlipper.getDisplayedChild();
        setupAddressBookVisibleView(activeChildIndex);

        addressBookIbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Setup POS address
        setupPosAddress();

        btnAddAddressBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (String.valueOf(btnAddAddressBook.getText()).equalsIgnoreCase(strSubmitNewAddressBook)) {
                    proceedNewAddressBook();
                } else if (String.valueOf(btnAddAddressBook.getText()).equalsIgnoreCase(strUpdateAddressBook)) {
                    proceedUpdateAddressBook();
                }
            }
        });
    }

    private void setupPosAddress() {

        cityNames.add("Pilih kota...");

        List<String[]> listArrayCity = Utils.readCSV(this.getResources()
                .openRawResource(R.raw.area_detail));

        // id,kelurahan,kecamatan,kabupaten,provinsi,kodepos
        // 0:regional,1:provinsi,2:kota,3:kecamatan,4:kelurahan,5:kodepos_lama,6:kodepos_baru,7:Mapping_Wilker,
        for (String[] city : listArrayCity) {
            locations.add(new Location(city[2], city[4], city[3], city[1], city[6]));
            cityNames.add(city[2]);
        }

        ArrayAdapter<String> cityDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>(cityNames));
        cityDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputAddressBookSpinnerCity.setAdapter(cityDataAdapter);
        inputAddressBookSpinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos != 0) {
                    setupDistrictBySelectedCity(String.valueOf(new ArrayList<>(cityNames).get(pos)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Utils.Log(TAG, "On nothing selected.");
            }
        });
    }

    private void setupDistrictBySelectedCity(String selectedCityName) {

        disctrictNames.clear();
        disctrictNames.add("Pilih kecamatan...");

        for (Location location : locations) {
            if (location.city.equals(selectedCityName)) {
                disctrictNames.add(location.district);
            }
        }

        Utils.Log(TAG, "District set: " + disctrictNames);

        ArrayAdapter<String> districtDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>(disctrictNames));
        districtDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        inputAddressBookSpinnerDistrict.animate().withStartAction(new Runnable() {
            @Override
            public void run() {
                inputAddressBookSpinnerDistrict.setVisibility(View.VISIBLE);
            }
        }).alpha(1F);
        inputAddressBookSpinnerDistrict.setAdapter(districtDataAdapter);
        inputAddressBookSpinnerDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Utils.Log(TAG, "[District] On item selected i:  " + pos);
                Utils.Log(TAG, "[District] On item selected item id:  " + id);
                Utils.Log(TAG, "[District] Item text  " + String.valueOf(new ArrayList<>(disctrictNames).get(pos)));

                if (pos != 0) {
                    setupSubdistrictBySelectedDistrict(String.valueOf(new ArrayList<>(disctrictNames).get(pos)));
                } else {
                    Utils.Logs('e', TAG, "District Label selected!");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Utils.Log(TAG, "On nothing selected.");
            }
        });
    }

    private void setupSubdistrictBySelectedDistrict(String selectedDistrict) {

        Utils.Log(TAG, "Selected district: " + selectedDistrict);

        subdisctrictNames.clear();
        subdisctrictNames.add("Pilih kelurahan...");

        for (Location location : locations) {
            if (location.district.equals(selectedDistrict)) {
                subdisctrictNames.add(location.subdistrict.concat(" - ").concat(location.zip));
            }
        }

        ArrayAdapter<String> subdistrictDataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, new ArrayList<>(subdisctrictNames));
        subdistrictDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputAddressBookSpinnerSubdistrict.animate().withStartAction(new Runnable() {
            @Override
            public void run() {
                inputAddressBookSpinnerSubdistrict.setVisibility(View.VISIBLE);
            }
        }).alpha(1F);
        inputAddressBookSpinnerSubdistrict.setAdapter(subdistrictDataAdapter);
        inputAddressBookSpinnerSubdistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                Utils.Log(TAG, "[Subdistrict] Item text  " + String.valueOf(new ArrayList<>(subdisctrictNames).get(pos)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Utils.Log(TAG, "On nothing selected.");
            }
        });
    }

    private void setupAddressBookVisibleView(int viewChildIndex) {

        int visibleChild = addressBookParentViewFlipper.getDisplayedChild();

        if (visibleChild != DISPLAY_MODE_INPUT
                && viewChildIndex == DISPLAY_MODE_INPUT) {
            addressBookBtnSwitchView.setText("Batal");
            addressBookParentViewFlipper.setDisplayedChild(DISPLAY_MODE_INPUT);

            isListMode = false;

        } else if (visibleChild != DISPLAY_MODE_LIST
                && viewChildIndex == DISPLAY_MODE_LIST) {
            addressBookBtnSwitchView.setText("Tambah");
            addressBookParentViewFlipper.setDisplayedChild(DISPLAY_MODE_LIST);

            isListMode = true;
        }
    }

    private AddressBook generateAddressBook(@Nullable AddressBook addressBook) {

        if (addressBook == null) {
            addressBook = new AddressBook();
        }

        addressBook.name = String.valueOf(editTextAddressBookName.getText());
        addressBook.phone = String.valueOf(editTextPhone.getText());
        addressBook.address = String.valueOf(editTextFullAddress.getText());

        Location location = new Location();
        String[] subDistrictChunk = String.valueOf(inputAddressBookSpinnerSubdistrict.getSelectedItem()).split("-");

        location.city = String.valueOf(inputAddressBookSpinnerCity.getSelectedItem());
        location.district = String.valueOf(inputAddressBookSpinnerDistrict.getSelectedItem());
        location.subdistrict = subDistrictChunk[0].trim();

        addressBook.location = location;

        return addressBook;

    }

    private void proceedNewAddressBook() {
        if (isAddressBookFormReady()) {
            presenter.addNewAddressBook(generateAddressBook(null));
        }
    }

    private void proceedUpdateAddressBook() {
        if (isAddressBookFormReady()) {
            presenter.updateAddressBook(
                    this.currentAddressBookPos,
                    generateAddressBook(this.currentAddressBook));
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

        if (inputAddressBookSpinnerCity.getSelectedItemPosition() == 0) {
            Utils.snackBar(addressBookParentViewFlipper, "Silahkan pilih kabupaten/kota untuk kode pos pengiriman.", Log.WARN);
            return false;
        }

        if (inputAddressBookSpinnerDistrict.getSelectedItemPosition() == 0) {
            Utils.snackBar(addressBookParentViewFlipper, "Silahkan pilih kecamatan untuk kode pos pengiriman.", Log.WARN);
            return false;
        }

        if (inputAddressBookSpinnerSubdistrict.getSelectedItemPosition() == 0) {
            Utils.snackBar(addressBookParentViewFlipper, "Silahkan pilih kelurahan untuk kode pos pengiriman.", Log.WARN);
            return false;
        }

        return true;
    }

    @Override
    public void showViewState(UIState uiState) {
        switch (uiState) {
            case LOADING:
                showLoadingIndicator(true);
                break;
            case FINISHED:
                showLoadingIndicator(false);
                break;
        }
    }

    private void showLoadingIndicator(boolean isLoading) {
        if (isLoading) {
            if (progressDialog == null) {
                progressDialog = ControllerDialog.getInstance().showLoadingNotCancelable(this);
                progressDialog.show();
            } else {
                progressDialog.show();
            }
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public boolean isActivityFinishing() {
        return isFinishing();
    }

    @Override
    public void populateAddressBookList(ArrayList<AddressBook> addressBookArrayList) {
        listItem.clear();
        listItem.addAll(addressBookArrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showNoReceiverAddressView(boolean isShow) {
        if (isShow) {
            setupAddressBookVisibleView(DISPLAY_MODE_INPUT);
        } else {
            setupAddressBookVisibleView(DISPLAY_MODE_LIST);
        }
    }

    @Override
    public void showMessage(String msg, int messageStatus) {
        if (messageStatus == Constants.NETWORK_CALLBACK_FAILURE) {
            Utils.snackBar(addressBookRecyclerView, msg, Log.ERROR);
        } else {
            Utils.snackBar(addressBookRecyclerView, msg, messageStatus);
        }
    }

    @Override
    public void showSelectedAddressBook(int position, AddressBook addressBook) {
        if (position < 0 || position >= listItem.size()) return;

        adapter.setSelectedIndex(position);

        Intent addressDataResIntent = new Intent();
        addressDataResIntent.putExtra(Constants.EXTRA_SELECTED_ADDRESS_BOOK, addressBook);

        this.setResult(Activity.RESULT_OK, addressDataResIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Utils.Logs('i', TAG, "Finish after transition...");
            this.finishAfterTransition();
        } else {
            this.finish();
        }
    }

    @Override
    public void showNewAddressBook(final AddressBook addressBook) {

        setupAddressBookVisibleView(DISPLAY_MODE_LIST);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                listItem.add(0, addressBook);
                adapter.notifyItemInserted(0);

                LinearLayoutManager layoutManager =
                        (LinearLayoutManager) addressBookRecyclerView.getLayoutManager();
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        }, this.getResources().getInteger(android.R.integer.config_longAnimTime));


    }

    @Override
    public void updateDeletedItemView(int posToDelete, AddressBook addressBookItemToDelete) {
        listItem.remove(posToDelete);
        adapter.notifyItemRemoved(posToDelete);
    }

    @Override
    public void setShippingAddressPic(String name) {
        editTextAddressBookName.setText(name);
    }

    @Override
    public void setShippingPhoneNumber(String phone) {
        editTextPhone.setText(phone);
    }

    @Override
    public void setShippingDetailAddress(String address) {
        editTextFullAddress.setText(address);
    }

    @Override
    public void showEditModeScreen(boolean isEditMode) {
        if (isEditMode) {
            setupAddressBookVisibleView(DISPLAY_MODE_INPUT);
            btnAddAddressBook.setText(R.string.btn_update_address_book);
        } else {
            setupAddressBookVisibleView(DISPLAY_MODE_LIST);
            btnAddAddressBook.setText(R.string.btn_addressbook_submit);
        }
    }

    @Override
    public void setAddressBookData(int position, AddressBook addressBook) {
        this.currentAddressBookPos = position;
        this.currentAddressBook = addressBook;

        // Restore selected location when available
        if (addressBook.location != null) {
            selectedCityName = addressBook.location.city;
            selectedDisctrictName = addressBook.location.district;
            selectedSubdistrictName = addressBook.location.subdistrict;

            for (String cityName : cityNames) {
                if (cityName.equalsIgnoreCase(selectedCityName)) {
                    break;
                }
                foundCityNamePos++;
            }
            Utils.Log(TAG, "Location city: " + selectedCityName + " found on pos: " + foundCityNamePos);
            inputAddressBookSpinnerCity.setSelection(foundCityNamePos);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.Logs('i', TAG, "District set: " + disctrictNames);
                    Utils.Logs('i', TAG, "District set size: " + disctrictNames.size());
                    for (String districtName : disctrictNames) {
                        if (districtName.equalsIgnoreCase(selectedDisctrictName)) {
                            break;
                        }
                        foundDistrictNamePos++;
                    }
                    Utils.Log(TAG, "Location district: " + selectedDisctrictName + " found on pos: " + foundDistrictNamePos);
                    inputAddressBookSpinnerDistrict.setSelection(foundDistrictNamePos);
                }
            }, 1000);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.Logs('i', TAG, "Subdisctrict set: " + subdisctrictNames);
                    Utils.Logs('i', TAG, "Subdisctrict set size: " + subdisctrictNames.size());
                    for (String subdistrictName : subdisctrictNames) {
                        String[] subDistrictChunk = subdistrictName.split("-");
                        if (subDistrictChunk[0].trim().equalsIgnoreCase(selectedSubdistrictName)) {
                            break;
                        }
                        foundSubDistrictNamePos++;
                    }
                    Utils.Log(TAG, "Location sudistrict: " + selectedSubdistrictName + " found on pos: " + foundSubDistrictNamePos);
                    inputAddressBookSpinnerSubdistrict.setSelection(foundSubDistrictNamePos);
                }
            }, 2000);

        }
    }

    @Override
    public void showUpdatedItem(int updatedAddressBookPos, AddressBook updatedAddressBook) {

        Utils.Logs('i', TAG, "Updated item pos: " + updatedAddressBookPos);

        listItem.set(updatedAddressBookPos, updatedAddressBook);
        adapter.notifyItemChanged(updatedAddressBookPos, null);
    }
}
