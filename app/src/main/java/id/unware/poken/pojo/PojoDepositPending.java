package id.unware.poken.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marzellaalfamega on 2/2/17.
 */
public class PojoDepositPending implements Parcelable {

    @SerializedName("pending") private boolean pending;
    @SerializedName("deposit") private PojoDeposit deposit;

    public PojoDepositPending() {
    }

    protected PojoDepositPending(Parcel in) {
        pending = in.readByte() != 0;
        deposit = in.readParcelable(PojoDeposit.class.getClassLoader());
    }

    public static final Creator<PojoDepositPending> CREATOR = new Creator<PojoDepositPending>() {
        @Override
        public PojoDepositPending createFromParcel(Parcel in) {
            return new PojoDepositPending(in);
        }

        @Override
        public PojoDepositPending[] newArray(int size) {
            return new PojoDepositPending[size];
        }
    };

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public PojoDeposit getDeposit() {
        return deposit;
    }

    public void setDeposit(PojoDeposit deposit) {
        this.deposit = deposit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (pending ? 1 : 0));
        parcel.writeParcelable(deposit, i);
    }
}
