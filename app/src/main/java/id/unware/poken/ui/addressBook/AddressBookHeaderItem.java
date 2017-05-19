package id.unware.poken.ui.addressBook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import id.unware.poken.R;
import id.unware.poken.tools.Utils;

public class AddressBookHeaderItem extends AbstractHeaderItem<AddressBookHeaderItem.HeaderViewHolder> {

    private final String TAG = this.getClass().getSimpleName();

	private String id;
	private String title;

	public AddressBookHeaderItem(String id) {
		super();
		this.id = id;
	}

	@Override
	public boolean equals(Object inObject) {
		if (inObject instanceof AddressBookHeaderItem) {
			AddressBookHeaderItem inItem = (AddressBookHeaderItem) inObject;
			return this.getId().equals(inItem.getId());
		}
		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int getLayoutRes() {
		return R.layout.row_header_general;
	}

	@Override
	public HeaderViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
		return new HeaderViewHolder(inflater.inflate(getLayoutRes(), parent, false), adapter);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void bindViewHolder(FlexibleAdapter adapter, HeaderViewHolder holder, int position, List payloads) {

		if (payloads.size() > 0) {
			Utils.Log(TAG, "InstagramHeaderItem Payload : " + payloads);
		} else {

            final char cUpper = Character.toUpperCase(getTitle().charAt(0));
			holder.textViewHeader.setText( String.valueOf(!(cUpper>=65 && cUpper<=90) ? "#" : cUpper) );
		}
	}

	static class HeaderViewHolder extends FlexibleViewHolder {

		public TextView textViewHeader;
		public Button buttonPickupNow;

		public HeaderViewHolder(View view, FlexibleAdapter adapter) {
			super(view, adapter, true);//True for sticky
			textViewHeader = (TextView) view.findViewById(R.id.textViewHeader);

			buttonPickupNow = (Button) view.findViewById(R.id.buttonHeader);
			buttonPickupNow.setVisibility(View.GONE);
		}
	}

	@Override
	public String toString() {
		return "AddressBookHeaderItem[id=" + id + ", title=" + title + "]";
	}

}