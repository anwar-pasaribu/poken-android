package id.unware.poken.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Feb 16 2017
 */
public class PojoWalletConfig implements Parcelable {

    @SerializedName("wallet_min_deposit") private long walletMinDeposit;
    @SerializedName("wallet_max_deposit") private long walletMaxDeposit;
    @SerializedName("wallet_min_withdraw") private long walletMinWithdraw;

    public static final Creator<PojoWalletConfig> CREATOR = new Creator<PojoWalletConfig>() {
        @Override
        public PojoWalletConfig createFromParcel(Parcel in) {
            return new PojoWalletConfig(in);
        }

        @Override
        public PojoWalletConfig[] newArray(int size) {
            return new PojoWalletConfig[size];
        }
    };

    public PojoWalletConfig() {
    }

    protected PojoWalletConfig(Parcel in) {
        walletMinDeposit = in.readLong();
        walletMaxDeposit = in.readLong();
        walletMinWithdraw = in.readLong();
    }

    public long getWalletMinDeposit() {
        return walletMinDeposit;
    }

    public void setWalletMinDeposit(long walletMinDeposit) {
        this.walletMinDeposit = walletMinDeposit;
    }

    public long getWalletMaxDeposit() {
        return walletMaxDeposit;
    }

    public void setWalletMaxDeposit(long walletMaxDeposit) {
        this.walletMaxDeposit = walletMaxDeposit;
    }

    public long getWalletMinWithdraw() {
        return walletMinWithdraw;
    }

    public void setWalletMinWithdraw(long walletMinWithdraw) {
        this.walletMinWithdraw = walletMinWithdraw;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(walletMinDeposit);
        dest.writeLong(walletMaxDeposit);
        dest.writeLong(walletMinWithdraw);
    }
}
