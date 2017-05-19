package id.unware.poken.ui.NewPackageSummary.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import id.unware.poken.R;
import id.unware.poken.interfaces.OnClickRecyclerItem;
import id.unware.poken.pojo.PojoTarif;
import id.unware.poken.tools.Utils;

/**
 * Created by IT11 on 4/21/2015.
 * Adapter for Tariff check result item.
 *
 * @since Nov 27 2016 - Apply new {@code tariff3} response.
 */
public class AdapterTarif extends ArrayAdapter<PojoTarif> {

    private List<PojoTarif> listTarif;
    private OnClickRecyclerItem mListener;
    private Context mContext;

    public AdapterTarif(Context context, int resource, List<PojoTarif> objects, OnClickRecyclerItem listener) {
        super(context, resource, objects);

        this.listTarif = objects;
        this.mContext = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(R.layout.row_tarif, null);

            holder = new ViewHolder();

            holder.imageViewVendor = (ImageView) v.findViewById(R.id.imageViewVendor);

            holder.textViewVendorName = (TextView) v.findViewById(R.id.textViewVendorName);
            holder.txtService = (TextView) v.findViewById(R.id.txtViewSubtitle1);
            holder.textViewServiceDesc = (TextView) v.findViewById(R.id.textViewSubtitle2);

            holder.txtETA = (TextView) v.findViewById(R.id.txtETA);
            holder.txtTarif = (TextView) v.findViewById(R.id.txtTarif);

            v.setClickable(mListener != null);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        Utils.Log("AdapterPostcode", "click pos on adapter: " + position);
                        mListener.onItemClick(view, position);
                    }
                }
            });

            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

        PojoTarif pojoTarif = listTarif.get(position);

        // Set Vendor Image
        Picasso.with(mContext)
                .load(pojoTarif.getVendorLogoUrl())
                .into(holder.imageViewVendor);

        // Format tariff
        DecimalFormat df = new DecimalFormat(
                "#,##0", new DecimalFormatSymbols(new Locale("id", "ID")));

        // Setup vendor name (TIKI, JNE, etc.) text.
        String strVendorName = Utils.isEmpty(pojoTarif.getVendorCode())
                ? pojoTarif.getVendorName()
                : pojoTarif.getVendorCode().toUpperCase();

        // Add small bullet to separate SDS * Sameday service
        String textServiceName = "&#8226; " + Utils.CapsFirst(pojoTarif.getServiceName());

        // Set text to views
        holder.textViewVendorName.setText(strVendorName);
        holder.txtService.setText(pojoTarif.getServiceCode());
        holder.textViewServiceDesc.setText(Html.fromHtml(textServiceName));
        holder.txtETA.setText(pojoTarif.getEstDay());
        holder.txtTarif.setText(mContext.getString(R.string.lbl_currency_idr, df.format(pojoTarif.getTariff())));

        return v;
    }

    @Override
    public int getCount() {
        return listTarif.size();
    }

    /** Disable click event to list items (remove method to restore)*/
    @Override
    public boolean isEnabled(int position) {
        return false;
    }


    private class ViewHolder {
        ImageView imageViewVendor;
        TextView textViewVendorName,
                txtService,
                textViewServiceDesc;

        TextView txtETA, txtTarif;
    }
}