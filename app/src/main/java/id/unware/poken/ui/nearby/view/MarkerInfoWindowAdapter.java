package id.unware.poken.ui.nearby.view;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import id.unware.poken.R;


/**
 * Custom adapter for maps info window. (May 24)
 */
public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private NearbyActivity nearbyActivity;

    public MarkerInfoWindowAdapter(NearbyActivity nearbyActivity) {
        this.nearbyActivity = nearbyActivity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        @SuppressLint("InflateParams") View v = nearbyActivity.getLayoutInflater().inflate(R.layout.item_info_window, null);

        TextView markerTitle = (TextView) v.findViewById(R.id.tvMapName);
        TextView markerPhone = (TextView) v.findViewById(R.id.tvMapPhone);
        TextView markerAddress = (TextView) v.findViewById(R.id.tvMapAddress);
        TextView markerTime = (TextView) v.findViewById(R.id.tvMapTime);

        String snippetStrData = marker.getSnippet();
        String datas[] = snippetStrData.split("#");

        // Info window data
        String sName = datas[0],
                sPhone = datas[1],
                sAddress = datas[2],
                //ini ngga usah di substring. biar dari server aja yang ubah. kalo belum di ubah bilang sama pak dika.
                sTime = String.format("Open : %s - %s", datas[3], datas[4]);

        markerTitle.setText(sName);
        markerPhone.setText(sPhone);
        markerAddress.setText(sAddress);
        markerTime.setText(sTime);

        return v;
    }
}
