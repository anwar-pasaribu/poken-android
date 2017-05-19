package id.unware.poken.ui.TrackPackage.TrackingResult.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.pojo.PojoTracking;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.TrackPackage.TrackingResult.view.adapter.TrackingAdapter;
import io.realm.Realm;

/**
 * @author Anwar Pasaribu
 * @since Dec 27 2016
 */

public class TrackingResultFragment extends Fragment {

    private final String TAG = "TrackingResultFragment";

    public static String BUNDLE_CNNO = "cnno";

    private AppCompatActivity parent;

    @BindView(R.id.recyclerViewTracking) RecyclerView recyclerViewTracking;

    private TrackingAdapter trackingAdapter;

    private ArrayList<Object> mPojoTrackingList = new ArrayList<>();
    private String mTrackingCnno;


    public static TrackingResultFragment newInstance(String trackingCnno) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_CNNO, trackingCnno);

        TrackingResultFragment trackingResultFragment = new TrackingResultFragment();
        trackingResultFragment.setArguments(bundle);

        return trackingResultFragment;
    }

    public TrackingResultFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mTrackingCnno = getArguments().getString(BUNDLE_CNNO, "");

            Utils.Log(TAG, "Tracking CNNO: " + mTrackingCnno);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Utils.Log(TAG, "On create view");
        View rootView = inflater.inflate(R.layout.fragment_tracking_result, container, false);

        ButterKnife.bind(this, rootView);

        Realm realm = Realm.getDefaultInstance();
        PojoTracking pojoTracking = realm
                .where(PojoTracking.class)
                .equalTo(PojoTracking.KEY_CNNO, mTrackingCnno)
                .findFirst();

        if (pojoTracking != null) {

            Utils.Log(TAG, "Pojo tracking status: " + pojoTracking.getStatusList().size());

            mPojoTrackingList.add(pojoTracking);
            mPojoTrackingList.addAll(pojoTracking.getStatusList());

        } else {
            Utils.Log(TAG, "POJO TRACKING IS EMPTY ON PAGER");
        }

        // Adaper with empty list
        trackingAdapter = new TrackingAdapter(parent, mPojoTrackingList);

        recyclerViewTracking.setLayoutManager(new LinearLayoutManager(parent));
        recyclerViewTracking.setAdapter(trackingAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (AppCompatActivity) context;
    }
}
