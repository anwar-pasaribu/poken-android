package id.unware.poken.ui.payment.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import id.unware.poken.R;
import id.unware.poken.domain.UserBank;
import id.unware.poken.tools.glide.GlideRequest;
import id.unware.poken.tools.glide.GlideRequests;
import id.unware.poken.ui.payment.presenter.IPaymentPresenter;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 * Category barang.
 */
public class PokenBankAdapter extends RecyclerView.Adapter<PokenBankAdapter.SingleItemRowHolder> {

    private ArrayList<UserBank> itemsList;
    private Context mContext;
    private IPaymentPresenter presenter;
    private final GlideRequest<Drawable> requestBuilder;

    public PokenBankAdapter(Context context,
                            ArrayList<UserBank> itemsList,
                            IPaymentPresenter presenter,
                            GlideRequests glideRequest) {
        this.itemsList = itemsList;
        this.mContext = context;
        this.presenter = presenter;

        requestBuilder = glideRequest.asDrawable().fitCenter();

    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_available_bank, null);
        return new SingleItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int i) {

        holder.textViewBankAccountName.setText(itemsList.get(i).accountName);
        holder.buttonAccountNumber.setText(itemsList.get(i).accountNumber);

        this.requestBuilder
                .clone()
                .load(itemsList.get(i).bankLogo)
                .dontTransform()
                .centerInside()
                .into(holder.imageViewBankLogo);

        holder.buttonCopyBankAccountNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter != null) {
                    presenter.copyBankAccountNumber(itemsList.get(holder.getAdapterPosition()).accountNumber);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewBankAccountName) TextView textViewBankAccountName;
        @BindView(R.id.buttonAccountNumber) Button buttonAccountNumber;
        @BindView(R.id.buttonCopyBankAccountNumber) Button buttonCopyBankAccountNumber;
        @BindView(R.id.imageViewBankLogo) ImageView imageViewBankLogo;
        @BindView(R.id.viewSep) View viewSep;

        @BindDimen(R.dimen.clickable_size_64) int iconWidth;
        @BindDimen(R.dimen.clickable_size_64) int iconHeight;

        public SingleItemRowHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }

}