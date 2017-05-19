package id.unware.poken.ui.wallet.main.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import id.unware.poken.R;
import id.unware.poken.pojo.PojoUserBank;
import id.unware.poken.tools.Constants;
import id.unware.poken.tools.Utils;

/**
 * @author Anwar Pasaribu
 * @since Feb 07 2017
 */

public class BanksSpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private List<Object> mList;

    private LayoutInflater inflater;

    public BanksSpinnerAdapter(Context context, List<Object> objects) {

        this.mContext = context;
        this.mList = objects;
        this.inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return (this.mList != null ? this.mList.size() : -1);
    }


    @Override
    public Object getItem(int position) {
        if (position < 0 || position >= this.mList.size()) return null;

        return this.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= this.mList.size()) return -1;

        Object item = this.mList.get(position);

        if (item != null && item instanceof PojoUserBank) {
            PojoUserBank pojoUserBank = (PojoUserBank) item;
            return Utils.getParsedLong(pojoUserBank.getUserBankId());
        } else if (item != null && item instanceof String) {
            return Constants.HEADER_ITEM_ID;
        }
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View itemView;

        Object item = this.mList.get(position);

        if (item != null && item instanceof PojoUserBank) {

            PojoUserBank pojoUserBank = (PojoUserBank) item;

            itemView = inflater.inflate(R.layout.row_wallet_bank, parent, false);
            TextView textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            TextView txtViewSubtitle1 = (TextView) itemView.findViewById(R.id.txtViewSubtitle1);

            textViewTitle.setText(pojoUserBank.getUserBankAccName() + " - " + pojoUserBank.getUserBankAccNo());
            txtViewSubtitle1.setText(pojoUserBank.getBankName() + " (" + pojoUserBank.getBankCode() + ")");

        } else {

            String strTitle = (String) item;

            itemView = inflater.inflate(R.layout.row_list_more, parent, false);
            TextView textViewTitle = (TextView) itemView.findViewById(R.id.textViewShowMore);
            ImageView imageViewRightButton = (ImageView) itemView.findViewById(R.id.imageViewRightButton);

            textViewTitle.setText(strTitle);

            // Remove drawable right image
            imageViewRightButton.setVisibility(View.GONE);
        }

        return itemView;
    }
}
