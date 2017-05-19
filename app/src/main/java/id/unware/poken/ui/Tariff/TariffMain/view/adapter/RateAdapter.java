package id.unware.poken.ui.Tariff.TariffMain.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import id.unware.poken.R;
import id.unware.poken.pojo.PojoRateItem;

/**
 * Adapter for tariff results.
 * @since (Oct. 4th, 2016)
 */
public class RateAdapter extends BaseAdapter {
    private Context mContext;
    private List<PojoRateItem> mData;

    public RateAdapter(Context mContext, List<PojoRateItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        if (mData != null)
            return mData.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_rate, viewGroup, false);
        }

        View viewServiceColor = view.findViewById(R.id.viewServiceColor);
        TextView textServiceCode = (TextView) view.findViewById(R.id.textViewServiceCode),
                textViewServiceDescription = (TextView) view.findViewById(R.id.textViewServiceDescription),
                textViewEstDay = (TextView) view.findViewById(R.id.textViewEstDay),
                textViewTariff = (TextView) view.findViewById(R.id.textViewTariff);

        PojoRateItem item = mData.get(i);

        String strEstDay;
        try {
            final int itemEstDay = Integer.valueOf(item.getEstDay());

            // Set plural for est day string (Same day, One day, or 6 days).
            strEstDay = itemEstDay == 0 ?
                    mContext.getResources().getString(R.string.lbl_est_day_same_day) :
                    mContext.getResources().getQuantityString(R.plurals.lbl_est_day, itemEstDay, itemEstDay);

        } catch (NumberFormatException nfe) {
            strEstDay = item.getEstDay();
        }

        DecimalFormat df = new DecimalFormat(
                "#,##0", new DecimalFormatSymbols(new Locale("id", "ID")));

        final Map<String, Integer> mapColor = new HashMap<String, Integer>() {{
            put("ECO", R.color.eco);
            put("REG", R.color.reg);
            put("ONS", R.color.ons);
            put("SDS", R.color.sds);
            put("HDS", R.color.hds);
        }};

        int intColor = mapColor.get(item.getService()) != null ? mapColor.get(item.getService()) : 0;
        if (intColor == 0) {
            viewServiceColor.setBackgroundResource(R.color.style_overlay_grey);
        } else {
            viewServiceColor.setBackgroundResource(mapColor.get(item.getService()));
        }

        String currency = mContext.getString(R.string.lbl_default_currency);
        if (!TextUtils.isEmpty(item.getCurrency())) {
            currency = item.getCurrency();
        }

        if (!TextUtils.isEmpty(strEstDay)) {
            textViewEstDay.setText(strEstDay);
        } else {
            textViewEstDay.setVisibility(View.GONE);
        }

        textServiceCode.setText(item.getService());
        textViewServiceDescription.setText(item.getDescription());
        textViewTariff.setText(mContext.getString(R.string.lbl_est_tariff, currency, df.format(item.getTariff())));

        return view;
    }
}
