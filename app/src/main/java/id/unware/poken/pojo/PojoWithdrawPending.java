package id.unware.poken.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marzellaalfamega on 2/2/17.
 */
public class PojoWithdrawPending implements Parcelable{

    @SerializedName("pending") private boolean pending;
    private PojoWithdraw withdraw;

    public PojoWithdrawPending() {
    }

    protected PojoWithdrawPending(Parcel in) {
        pending = in.readByte() != 0;
        withdraw = in.readParcelable(PojoWithdraw.class.getClassLoader());
    }

    public static final Creator<PojoWithdrawPending> CREATOR = new Creator<PojoWithdrawPending>() {
        @Override
        public PojoWithdrawPending createFromParcel(Parcel in) {
            return new PojoWithdrawPending(in);
        }

        @Override
        public PojoWithdrawPending[] newArray(int size) {
            return new PojoWithdrawPending[size];
        }
    };

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public PojoWithdraw getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(PojoWithdraw withdraw) {
        this.withdraw = withdraw;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (pending ? 1 : 0));
        parcel.writeParcelable(withdraw, i);
    }
}
