package id.unware.poken.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by marzellaalfamega on 2/2/17.
 * @since [V49] Feb 14 '17 - Replace "userBankId" to "user_bank_id"
 */
public class PojoUserBank extends RealmObject implements Parcelable {

    @PrimaryKey
    @SerializedName("user_bank_id") private String userBankId;
    @SerializedName("bank_code") private String bankCode;
    @SerializedName("bank_id") private String bankId;
    @SerializedName("bank_name") private String bankName;
    @SerializedName("priority") private String priority;
    @SerializedName("user_bank_acc_name") private String userBankAccName;
    @SerializedName("user_bank_acc_no") private String userBankAccNo;
    @SerializedName("user_id") private String userId;
    @SerializedName("verified") private String verified;

    public PojoUserBank() {
    }

    protected PojoUserBank(Parcel in) {
        bankCode = in.readString();
        bankId = in.readString();
        bankName = in.readString();
        userBankId = in.readString();
        priority = in.readString();
        userBankAccName = in.readString();
        userBankAccNo = in.readString();
        userId = in.readString();
        verified = in.readString();
    }

    public static final Creator<PojoUserBank> CREATOR = new Creator<PojoUserBank>() {
        @Override
        public PojoUserBank createFromParcel(Parcel in) {
            return new PojoUserBank(in);
        }

        @Override
        public PojoUserBank[] newArray(int size) {
            return new PojoUserBank[size];
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

    public String getUserBankId() {
        return userBankId;
    }

    public void setUserBankId(String userBankId) {
        this.userBankId = userBankId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getUserBankAccName() {
        return userBankAccName;
    }

    public void setUserBankAccName(String userBankAccName) {
        this.userBankAccName = userBankAccName;
    }

    public String getUserBankAccNo() {
        return userBankAccNo;
    }

    public void setUserBankAccNo(String userBankAccNo) {
        this.userBankAccNo = userBankAccNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
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
        parcel.writeString(userBankId);
        parcel.writeString(priority);
        parcel.writeString(userBankAccName);
        parcel.writeString(userBankAccNo);
        parcel.writeString(userId);
        parcel.writeString(verified);
    }
}
