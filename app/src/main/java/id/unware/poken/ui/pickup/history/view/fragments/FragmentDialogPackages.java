package id.unware.poken.ui.pickup.history.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.unware.poken.R;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.PojoBooking;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.pickup.history.view.adapters.AdapterPackage;

/**
 * Display picked package(s).
 */
public class FragmentDialogPackages extends DialogFragment implements OnClickRecyclerItem {

    private final String TAG = "FragmentDialogPackages";

    private AppCompatActivity parent;
    private static final String ARG_BOOKING_LIST = "bookings";

    private ArrayList<String> mListBookings;
    private List<PojoBooking> mListPojoBooking;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FragmentDialogPackages() {}

    public static FragmentDialogPackages newInstance(ArrayList<String> idBookings) {
        FragmentDialogPackages fragment = new FragmentDialogPackages();
        Bundle args = new Bundle();

        // Set offline data as bundle
        args.putStringArrayList(ARG_BOOKING_LIST, idBookings);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // Get booking_id String ArrayList from bundle
            mListBookings = getArguments().getStringArrayList(ARG_BOOKING_LIST);
        }

        mListPojoBooking = getPojoBookingLocalData(mListBookings);
    }

    /**
     * Get PojoBooking data based on multi booking_id
     * (eg. 1234, 1235 then return two PojoBooking data)
     *
     * @param mListBookings : String Array of booking_id
     * @return : List of PojoBooking object.
     */
    private List<PojoBooking> getPojoBookingLocalData(ArrayList<String> mListBookings) {
        Utils.Log(TAG, "List Booking ID: " + mListBookings.toString());
        return ControllerRealm.getInstance().getBookingByIds(mListBookings);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.f_dialog_packages, container, false);

        TextView textViewTitle = (TextView) view.findViewById(R.id.txtViewDialogPackagesTitle);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);

        textViewTitle.setText(getString(R.string.title_package));

        // Adaper with empty list
        AdapterPackage adapterPackage = new AdapterPackage(getContext(), mListPojoBooking, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(parent, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterPackage);
        recyclerView.setHasFixedSize(true);

        if (mListPojoBooking.isEmpty()) {
            view.findViewById(R.id.textViewNoData).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.textViewNoData).setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (AppCompatActivity) context;
    }

    @Override
    public void onItemClick(View view, int position) {

        Utils.Log(TAG, "Item clicked at pos: " + position);

        try {
            // Launch PackageDetailFragment2 on ActivityMainWithUp
            showDetail(
                    mListPojoBooking.get(position).getBooking_id(),
                    position);

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /** Show detail package.
     *
     * @param bookingId : String ID
     */
    private void showDetail(long bookingId, int itemPos) {
        Utils.Log(TAG, "Show package detail");
        // TODO Show package detail
//        Intent intentPackDetail = new Intent(getActivity(), ActivityMainWithUp.class);
//        intentPackDetail.putExtra(AppClass.USE_FRAGMENT, AppClass.TAG_PACKAGE_DETAIL);
//        intentPackDetail.putExtra(PojoBooking.KEY_BOOKING_ID, bookingId);
//        intentPackDetail.putExtra(Constants.EXTRA_PACKAGE_POSITION, itemPos);
//        getActivity().startActivityForResult(intentPackDetail, AppClass.TAG_PACKAGE_DETAIL);
    }

    /**
     * Prevent dialog to show it's native title.
     *
     * @param savedInstanceState : Default bundle data.
     * @return Dialog which gonna be displayed.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        if (dialog.getWindow() != null)
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setCancelable(true);
        return dialog;
    }

}
