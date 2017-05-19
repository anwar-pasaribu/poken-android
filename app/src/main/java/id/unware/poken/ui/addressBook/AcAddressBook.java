package id.unware.poken.ui.addressBook;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import id.unware.poken.R;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseActivityWithup;

/**
 * Activity to hold Fragment AddressBook and set flag "fromNewPackage" to true in order
 * to finish the activity when tapping on list item. Finally tapped item data like
 * name, phone_number, and address.
 */
public class AcAddressBook extends BaseActivityWithup {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Force Activity title
        setTitle(getString(R.string.title_address_book));

        FragmentAddressBook fragmentAddressBook = new FragmentAddressBook();

        // Set bundle to flag "fromNewPackage" to true
        Bundle bundle = new Bundle();
        bundle.putBoolean(Constants.EXTRA_FROM_NEW_PACKAGE, true);

        fragmentAddressBook.setArguments(bundle);
        Utils.changeFragment(this, R.id.f_address_book, fragmentAddressBook);

    }

    // Prevent Google dialog (fade out effect)
//    @Override
//    protected boolean isAutoLogginEnabled() {
//        return false;
//    }
}
