package id.unware.poken.ui.pickup.history.view.adapters;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.BuildConfig;
import id.unware.poken.R;
import id.unware.poken.controller.ControllerDate;
import id.unware.poken.controller.ControllerRealm;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.PojoCourier;
import id.unware.poken.pojo.PojoPickupHistory;
import id.unware.poken.tools.BitmapUtil;
import id.unware.poken.tools.StringUtils;
import id.unware.poken.tools.Utils;


/**
 * Adapter for Pickup history.
 */
public class AdapterPickupHistory extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PojoPickupHistory> listPickupHistory = new ArrayList<>();
    private final String VEHICLE_TYPE_BIKE = "bike";
    private OnClickRecyclerItem listener;
    private Context mContext;

    private final int TYPE_OTW = 3;
    private final int TYPE_PICKED = 4;
    private final int TYPE_DONE = 5;

    // Member var for styling purpose
    @ColorInt private int mColorRed;
    @ColorInt private int mColorGrey;

    private PorterDuffColorFilter mColorFilterRed;
    private PorterDuffColorFilter mColorFilterGrey;
    private PorterDuffColorFilter mColorFilterBlack;
    private PorterDuffColorFilter mColorFilterBlue;


    public AdapterPickupHistory(List<PojoPickupHistory> listPickupHistory, OnClickRecyclerItem listener, Context context) {
        this.listPickupHistory = listPickupHistory;
        this.listener = listener;
        this.mContext = context;

        // Init styling stuff
        mColorRed = ContextCompat.getColor(mContext, R.color.red);
        mColorGrey = ContextCompat.getColor(mContext, R.color.style_overlay_grey);
        mColorFilterRed = BitmapUtil.getDrawableFilter(mContext, R.color.red);
        mColorFilterGrey = BitmapUtil.getDrawableFilter(mContext, R.color.style_overlay_grey);
        mColorFilterBlack = BitmapUtil.getDrawableFilter(mContext, R.color.black_90);
        mColorFilterBlue = BitmapUtil.getDrawableFilter(mContext, R.color.myPrimaryColor);
    }

    /**
     * View holder for regular pickup history item.
     */
    class RegularViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Vendor info
        @BindView(R.id.imageVendorLogo) ImageView imageVendorLogo;
        @BindView(R.id.textViewVendorBranchName) TextView textViewVendorBranchName;

        @BindView(R.id.txtTitle) TextView txtAddress;
        @BindView(R.id.textViewExtraDetail) TextView textViewExtraDetail;
        @BindView(R.id.txtDate) TextView txtDate;
        @BindView(R.id.txtStatus) TextView txtStatus;
        @BindView(R.id.ivStatus1) ImageView ivStatus1;
        @BindView(R.id.ivStatus2) ImageView ivStatus2;
        @BindView(R.id.ivStatus3) ImageView ivStatus3;
        @BindView(R.id.ivStatus4) ImageView ivStatus4;
        @BindView(R.id.parentStatus) LinearLayout parentStatus;

        // Button to show cancel menu
        // @BindView(R.id.btnMenu) ImageButton imgBtnMenu;

        // Buttons to see more, cancel, or re-pickup
        @BindView(R.id.buttonCancel) Button buttonCancel;
        @BindView(R.id.buttonRepickup) Button buttonRepickup;
        @BindView(R.id.buttonDetail) Button buttonDetail;

        ImageView[] ivStatus;

        int ivStatusLength;

        RegularViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            ivStatus = new ImageView[4];
            ivStatus[0] = ivStatus1;
            ivStatus[1] = ivStatus2;
            ivStatus[2] = ivStatus3;
            ivStatus[3] = ivStatus4;

            ivStatusLength = ivStatus.length;

            buttonCancel.setOnClickListener(this);
            buttonRepickup.setOnClickListener(this);
            buttonDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());  // External interface
        }

    }

    /**
     * View holder for OTW pickup history item.
     */
    class OtwViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Vendor info
        @BindView(R.id.imageVendorLogo) ImageView imageVendorLogo;
        @BindView(R.id.textViewVendorBranchName) TextView textViewVendorBranchName;

        @BindView(R.id.ivVehicleType) ImageView ivVehicleType;
        @BindView(R.id.ivDriverImage) ImageView ivDriverImage;

        @BindView(R.id.txtTitle) TextView txtAddress;
        @BindView(R.id.textViewExtraDetail) TextView textViewExtraDetail;
        @BindView(R.id.txtDate) TextView txtDate;
        @BindView(R.id.txtStatus) TextView txtStatus;

        // Driver name, flat number, call the number, or sms.
        @BindView(R.id.textViewDriverName) TextView textViewDriverName;
        @BindView(R.id.textViewDriverPlateNumber) TextView textViewDriverPlateNumber;
        @BindView(R.id.imageButtonDriverCall) ImageButton imageButtonDriverCall;
        @BindView(R.id.imageButtonDriverSms) ImageButton imageButtonDriverSms;

        // Badge when on picked status
        @BindView(R.id.textViewPackageCount) TextView textViewPackageCount;
        @BindView(R.id.badgePackageCount) ImageView badgePackageCount;

        @BindView(R.id.ivStatus1) ImageView ivStatus1;
        @BindView(R.id.ivStatus2) ImageView ivStatus2;
        @BindView(R.id.ivStatus3) ImageView ivStatus3;
        @BindView(R.id.ivStatus4) ImageView ivStatus4;

        @BindView(R.id.parentStatus) LinearLayout parentStatus;

        @BindView(R.id.relativeLayoutOtwInfo) RelativeLayout relativeLayoutOtwInfo;

        // Buttons to see more, cancel, or re-pickup
        @BindView(R.id.buttonCancel) Button buttonCancel;
        @BindView(R.id.buttonRepickup) Button buttonRepickup;
        @BindView(R.id.buttonDetail) Button buttonDetail;

        // ViewHolder to show picked packages. Click this will show list of picked packages.
        @BindView(R.id.frameLayoutPickedPackage) FrameLayout frameLayoutPickedPackage;

        ImageView[] ivStatus;

        OtwViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            ivStatus = new ImageView[4];
            ivStatus[0] = ivStatus1;
            ivStatus[1] = ivStatus2;
            ivStatus[2] = ivStatus3;
            ivStatus[3] = ivStatus4;

            ivVehicleType.setColorFilter(mColorFilterBlack);
            ivDriverImage.setColorFilter(mColorFilterBlack);

            // The trio buttons
            buttonCancel.setOnClickListener(this);
            buttonRepickup.setOnClickListener(this);
            buttonDetail.setOnClickListener(this);

            // Setup trio buttons tint to white
            // buttonCancel.getCompoundDrawables()[1].setColorFilter(mColorFilterBlack);
            buttonRepickup.getCompoundDrawables()[1].setColorFilter(mColorFilterBlack);
            buttonDetail.getCompoundDrawables()[1].setColorFilter(mColorFilterBlack);

            /* Make badge clickable to whow picked packes*/
            frameLayoutPickedPackage.setClickable(true);
            frameLayoutPickedPackage.setOnClickListener(this);

            imageButtonDriverCall.setOnClickListener(this);
            imageButtonDriverCall.setColorFilter(mColorFilterBlack);

            imageButtonDriverSms.setOnClickListener(this);
            imageButtonDriverSms.setColorFilter(mColorFilterBlack);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());  // External interface
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder mViewHolder;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        switch (viewType) {
            case TYPE_OTW:
            case TYPE_PICKED:
            case TYPE_DONE:
                View otwItemView = inflater.inflate(R.layout.row_pickup_history_otw, parent, false);
                mViewHolder = new OtwViewHolder(otwItemView);
                break;
            default:
                View regularView = inflater.inflate(R.layout.row_pickup_history, parent, false);
                mViewHolder = new RegularViewHolder(regularView);
                break;
        }

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case TYPE_OTW:
            case TYPE_PICKED:
            case TYPE_DONE:
                OtwViewHolder otwViewHolder = (OtwViewHolder) holder;
                configureOtwItem(otwViewHolder, position);
                break;
            default:
                RegularViewHolder regularViewHolder = (RegularViewHolder) holder;
                configureRegularItem(regularViewHolder, position);
        }

    }

    /**
     * Handle list item for OTW, Picked, and Done item.
     *
     * @param holder   : Holder for view
     * @param position : Item position.
     */
    private void configureOtwItem(OtwViewHolder holder, int position) {
        PojoPickupHistory item = listPickupHistory.get(position);

        int intStatusNumber = 0;
        int intPackageCount = 0;

        try {
            intStatusNumber = Integer.parseInt(item.getStatus());
            intPackageCount = Integer.valueOf(item.getPackage_count());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        // Get PojoCourier data based on "vendor_id"
        PojoCourier pojoCourier = getCourierById(item.getVendor_id());

        // Vendor name, set default when it's empty
        String strVendorName =  Utils.isEmpty(item.getVendorBranch())?
                (pojoCourier != null)? pojoCourier.getVendorName() : ""
                : item.getVendorBranch();
        String strAddress = item.getAddress();
        String strExtraDetail = Utils.isEmpty(item.getExtra_detail())?
                mContext.getString(R.string.lbl_note_empty)
                : item.getExtra_detail();

        // Set Vendor Image
        Picasso.with(mContext)
                .load(pojoCourier != null ? pojoCourier.getVendorLogoUrl() : null)
                .into(holder.imageVendorLogo);

        if (BuildConfig.DEV_MODE) {
            strAddress = strAddress + " - " + item.getPickup_id();
        }

        holder.textViewVendorBranchName.setText(strVendorName);
        holder.txtAddress.setText(strAddress);
        holder.textViewExtraDetail.setText(strExtraDetail);

        if (!StringUtils.isEmpty(item.getCreated_on()))
            holder.txtDate.setText(ControllerDate.getInstance().toPickupHistory(item.getCreated_on()));

        // Only highlight up to index OTW (3)
        int ivStatusCount = holder.ivStatus.length;
        for (int i = 0; i < ivStatusCount; i++) {
            ImageView iv = holder.ivStatus[i];

            if (i == 1) {
                int otwVehicleType = item.getVehicle_type().equals(VEHICLE_TYPE_BIKE) ?
                        R.drawable.ic_pickup_otw_bike : R.drawable.ic_pickup_otw;

                iv.setImageResource(otwVehicleType);
            }

            iv.setColorFilter(i < (intStatusNumber - 1)
                    ? mColorFilterBlue
                    : mColorFilterGrey);
        }

        int resVehicle = item.getVehicle_type().equals(VEHICLE_TYPE_BIKE) ?
                R.drawable.ic_pickup_bike : R.drawable.ic_pickup_car;
        holder.ivVehicleType.setImageResource(resVehicle);

        // Handle status text for OTW and Picked
        int intBgRes = R.color.black_90;
        String statusText = item.getStatus_text();

        if (intStatusNumber == TYPE_OTW) {

            // When Pickup is on the way (OTW)
            intBgRes = R.color.myAccentColor;

            holder.badgePackageCount.setVisibility(View.GONE);
            holder.textViewPackageCount.setVisibility(View.GONE);

        } else if (intStatusNumber == TYPE_PICKED || intStatusNumber == TYPE_DONE) {

            // When Pickup is being Picked
            // 1. Set badge
            // 2. Set all badge view clickable in order to show list of picked packages.
            intBgRes = R.color.myPrimaryColor;

            statusText = intPackageCount == 0 ?
                    mContext.getString(R.string.lbl_no_package_count) :
                    mContext.getResources()
                            .getQuantityString(
                                    R.plurals.lbl_plural_header_picked_package_count,
                                    intPackageCount, intPackageCount);

            if (intPackageCount > 0) {

                // [V49] Limit Package number max only 99, in order prevent layout
                // but add plus sign to indicate package count is more than 99
                intPackageCount = intPackageCount > 99
                        ? 99
                        : intPackageCount;

                holder.badgePackageCount.setVisibility(View.VISIBLE);
                holder.textViewPackageCount.setVisibility(View.VISIBLE);

                holder.textViewPackageCount.setText(intPackageCount < 99 ?
                        String.format(Locale.ENGLISH, "%d", intPackageCount) :
                        String.format(Locale.ENGLISH, "%d+", intPackageCount));
            } else {
                holder.badgePackageCount.setVisibility(View.GONE);
                holder.textViewPackageCount.setVisibility(View.GONE);
            }
        }

        if (intStatusNumber == TYPE_DONE) {
            // When Pickup is DONE
            statusText = item.getStatus_text().toUpperCase();
        }

        // Toggle Cancel button ON/OFF
        if (intStatusNumber >= 0 && intStatusNumber <= 3) {
            holder.buttonCancel.setEnabled(true);
            holder.buttonCancel.setTextColor(mColorRed);
            holder.buttonCancel.getCompoundDrawables()[1].setColorFilter(mColorFilterRed);
        } else {
            holder.buttonCancel.setEnabled(false);
            holder.buttonCancel.setTextColor(mColorGrey);
            holder.buttonCancel.getCompoundDrawables()[1].setColorFilter(mColorFilterGrey);
        }

        /* Set whether picked badge clickable or not*/

        holder.frameLayoutPickedPackage.setClickable(item.getBookings() != null);

        // Text color for OTW item become Orange
        holder.txtStatus.setTextColor(ContextCompat.getColor(mContext, intBgRes));
        holder.txtStatus.setText(statusText);

        // Set BOLD only for OTW, PICKED, DONE
        if (intStatusNumber == TYPE_OTW
                || intStatusNumber == TYPE_PICKED
                || intStatusNumber == TYPE_DONE) {
            holder.txtStatus.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.txtStatus.setTypeface(Typeface.DEFAULT);
        }

        // Setup driver specifik data
        holder.textViewDriverName.setText(item.getDriver_name() == null ? "" : item.getDriver_name());
        holder.textViewDriverPlateNumber.setText(item.getPlate_number() == null ? "0" : item.getPlate_number());

    }

    private PojoCourier getCourierById(long vendor_id) {
        if (vendor_id == 0) return null;

        return ControllerRealm.getInstance().getCourierById(vendor_id);
    }

    private void configureRegularItem(RegularViewHolder holder, int position) {

        final PojoPickupHistory item = listPickupHistory.get(position);

        final int intStatusNumber = item.getIntStatus();

        // Get PojoCourier data based on "vendor_id"
        final PojoCourier pojoCourier = getCourierById(item.getVendor_id());

        // Vendor name, set default when it's empty
        String strVendorName =  Utils.isEmpty(item.getVendorBranch())?
                                (pojoCourier != null)? pojoCourier.getVendorName() : ""
                                : item.getVendorBranch();
        String strAddress = item.getAddress();
        String strExtraDetail = Utils.isEmpty(item.getExtra_detail())
                                ? mContext.getString(R.string.lbl_note_empty)
                                : item.getExtra_detail();

        if (BuildConfig.DEV_MODE) {
            strAddress = strAddress + " - " + item.getPickup_id();
        }

        // Toggle Cancel button
        if (intStatusNumber >= 0 && intStatusNumber <= 3) {
            holder.buttonCancel.setEnabled(true);
            holder.buttonCancel.setTextColor(mColorRed);
            holder.buttonCancel.getCompoundDrawables()[1].setColorFilter(mColorFilterRed);
        } else {
            holder.buttonCancel.setEnabled(false);
            holder.buttonCancel.setTextColor(mColorGrey);
            holder.buttonCancel.getCompoundDrawables()[1].setColorFilter(mColorFilterGrey);
        }

        // Set view content
        holder.textViewVendorBranchName.setText(strVendorName);
        // Set Vendor Image
        Picasso.with(mContext)
                .load(pojoCourier != null ? pojoCourier.getVendorLogoUrl() : null)
                .into(holder.imageVendorLogo);
        holder.txtAddress.setText(strAddress);
        holder.textViewExtraDetail.setText(strExtraDetail);
        if (!Utils.isEmpty(item.getCreated_on())) {
            holder.txtDate.setText(ControllerDate.getInstance().toPickupHistory(item.getCreated_on()));
        }
        holder.txtStatus.setText(item.getStatus_text());

        int ivStatusesCount = holder.ivStatus.length;
        for (int i = 0; i < ivStatusesCount; i++) {
            ImageView iv = holder.ivStatus[i];

            if (i == 1) {
                int otwVehicleType = item.getVehicle_type().equals(VEHICLE_TYPE_BIKE) ?
                        R.drawable.ic_pickup_otw_bike
                        : R.drawable.ic_pickup_otw;

                iv.setImageResource(otwVehicleType);
            }

            iv.setColorFilter(i < intStatusNumber
                    ? mColorFilterBlue
                    : mColorFilterGrey);
        }

    }

    @Override
    public int getItemCount() {
        return listPickupHistory != null ? listPickupHistory.size() : 0;

    }

    @Override
    public int getItemViewType(int pos) {

        int intStatusNumber = Utils.getParsedInt(listPickupHistory.get(pos).getStatus_number());

        return intStatusNumber == TYPE_OTW ?
                TYPE_OTW : intStatusNumber == TYPE_PICKED ?
                TYPE_PICKED : intStatusNumber == TYPE_DONE ? TYPE_DONE : intStatusNumber;
    }

}