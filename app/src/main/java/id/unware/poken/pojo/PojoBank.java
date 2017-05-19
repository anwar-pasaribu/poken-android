package id.unware.poken.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by marzellaalfamega on 2/2/17.
 */
public class PojoBank extends RealmObject implements Parcelable{

    @SerializedName("bank_code") private String bankCode;
    @SerializedName("bank_id") private String bankId;
    @SerializedName("bank_name") private String bankName;
    @SerializedName("priority") private String priority;

    public PojoBank() {
    }

    protected PojoBank(Parcel in) {
        bankCode = in.readString();
        bankId = in.readString();
        bankName = in.readString();
        priority = in.readString();
    }

    public static final Creator<PojoBank> CREATOR = new Creator<PojoBank>() {
        @Override
        public PojoBank createFromParcel(Parcel in) {
            return new PojoBank(in);
        }

        @Override
        public PojoBank[] newArray(int size) {
            return new PojoBank[size];
        }
    };

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(bankCode);
        parcel.writeString(bankId);
        parcel.writeString(bankName);
        parcel.writeString(priority);
    }
}
