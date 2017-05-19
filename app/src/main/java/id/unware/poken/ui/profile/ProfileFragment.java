package id.unware.poken.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.unware.poken.R;
import id.unware.poken.helper.SPHelper;
import id.unware.poken.pojo.PojoUser;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;
import id.unware.poken.ui.BaseFragment;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.txtName) TextView txtName;
    @BindView(R.id.txtUserPhone) TextView txtUserPhone;
    @BindView(R.id.txtUserEmail) TextView txtEmail;
    @BindView(R.id.buttonLogout) TextView buttonLogout;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentProfileInteractionListener mListener;
    private final String TAG = "ProfileFragment";
    private SPHelper mSpHelper;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.profile, container, false);
        ButterKnife.bind(this, parentView);
        mSpHelper = SPHelper.getInstance();
        buttonLogout.setText(Html.fromHtml(this.parent.getString(R.string.sign_out)));
        configureProfile(generateUserProfileItem());
        return parentView;
    }

    public PojoUser generateUserProfileItem() {

        String strName = mSpHelper.getSharedPreferences(Constants.SHARED_PROFILE_NAME, getString(R.string.lbl_no_account_name));
        String strPhone = mSpHelper.getSharedPreferences(Constants.SHARED_PROFILE_PHONE, getString(R.string.lbl_no_account_phone_number));
        String strEmail = mSpHelper.getSharedPreferences(Constants.SHARED_EMAIL, getString(R.string.lbl_no_account_email));
        String strIsPhoneVerifiedState = mSpHelper.getSharedPreferences(Constants.SHARED_PROFILE_PHONE_VERIFY, "0");

        Utils.Log(TAG, "Profile name: \"" + strName + "\"");
        Utils.Log(TAG, "Profile phone: \"" + strPhone + "\"");
        Utils.Log(TAG, "Profile email: \"" + strEmail + "\"");
        Utils.Log(TAG, "Profile is phone verified: \"" + strIsPhoneVerifiedState + "\"");

        strName = StringUtils.isEmpty(strName)? getString(R.string.lbl_no_account_name) : strName;
        strPhone = StringUtils.isEmpty(strPhone)? getString(R.string.lbl_no_account_phone_number) : strPhone;
        strEmail = StringUtils.isEmpty(strEmail)? getString(R.string.lbl_no_account_email) : strEmail;

        PojoUser pojoUser = new PojoUser();

        pojoUser.setUserId(String.valueOf(Constants.FOOTER_LOAD_MORE_ITEM_ID));

        pojoUser.setUserName(strName);
        pojoUser.setUserPhone(strPhone);
        pojoUser.setUserEmail(strEmail);

        pojoUser.setPhoneVerified(strIsPhoneVerifiedState);

        return pojoUser;
    }

    private void configureProfile(PojoUser item) {

        //noinspection deprecation
        txtName.setText(Html.fromHtml(String.valueOf(item.getUserName())));
        //noinspection deprecation
        txtUserPhone.setText(Html.fromHtml(item.getUserPhone()));
        //noinspection deprecation
        txtEmail.setText(Html.fromHtml(String.valueOf(item.getUserEmail())));


        if (item.getPhoneVerified().equals("0")) {
            txtUserPhone.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        } else {
            txtUserPhone.setTextColor(ContextCompat.getColor(getContext(), R.color.black_90));
        }
    }

    @OnClick(R.id.buttonLogout)
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onLogOut();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentProfileInteractionListener) {
            mListener = (OnFragmentProfileInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentProfileInteractionListener {
        void onLogOut();
    }
}
