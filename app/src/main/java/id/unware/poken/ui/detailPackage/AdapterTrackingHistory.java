package id.unware.poken.ui.detailPackage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

import id.unware.poken.R;
import id.unware.poken.controller.ControllerDate;
import id.unware.poken.pojo.PojoHistory;
import id.unware.poken.tools.Utils;

public class AdapterTrackingHistory extends ArrayAdapter<PojoHistory> {

    public AdapterTrackingHistory(Context context, int resource,
                                  List<PojoHistory> objects) {
        super(context, resource, objects);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            v = inflater.inflate(R.layout.row_history, null);
            holder = new ViewHolder();
            holder.tvDate = (TextView) v.findViewById(R.id.tvDate);
            holder.tvStatus = (TextView) v.findViewById(R.id.tvStatus);
            holder.tvTime = (TextView) v.findViewById(R.id.tvTime);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        PojoHistory history = getItem(position);

        if (history != null) {

            String strDate = ControllerDate.getInstance().toBookingHistoryDate(history.getDatetime());
            String strTime = ControllerDate.getInstance().toBookingHistoryTime(history.getDatetime());

            strDate = (Utils.isEmpty(strDate)? "-" : strDate);
            strTime = (Utils.isEmpty(strTime)? "-" : strTime);


            holder.tvDate.setText(strDate);
            holder.tvStatus.setText(history.getStatus());
            holder.tvTime.setText(strTime);
        }

        return v;
    }

    private class ViewHolder {
        TextView tvDate;
        TextView tvTime;
        TextView tvStatus;
    }
}
