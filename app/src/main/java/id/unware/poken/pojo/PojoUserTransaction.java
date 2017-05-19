package id.unware.poken.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marzellaalfamega on 2/2/17.
 */
public class PojoUserTransaction implements Parcelable {

    @SerializedName("amount") private String amount;
    @SerializedName("created_on") private String createdOn;
    @SerializedName("id") private String id;
    @SerializedName("info") private String info;
    @SerializedName("last_balance") private String lastBalance;
    @SerializedName("transaction_id") private String transactionId;
    @SerializedName("transaction_status") private String transactionStatus;
    @SerializedName("transaction_status_description") private String transactionStatusDescription;
    @SerializedName("transaction_status_id") private String transactionStatusId;
    @SerializedName("transaction_status_number") private String transactionStatusNumber;
    @SerializedName("transaction_status_text") private String transactionStatusText;
    @SerializedName("transaction_type") private String transactionType;
    @SerializedName("updated_on") private String updatedOn;
    @SerializedName("user_id") private String userId;

    public PojoUserTransaction() {
    }

    protected PojoUserTransaction(Parcel in) {
        amount = in.readString();
        createdOn = in.readString();
        id = in.readString();
        info = in.readString();
        lastBalance = in.readString();
        transactionId = in.readString();
        transactionStatus = in.readString();
        transactionStatusDescription = in.readString();
        transactionStatusId = in.readString();
        transactionStatusNumber = in.readString();
        transactionStatusText = in.readString();
        transactionType = in.readString();
        updatedOn = in.readString();
        userId = in.readString();
    }

    public static final Creator<PojoUserTransaction> CREATOR = new Creator<PojoUserTransaction>() {
        @Override
        public PojoUserTransaction createFromParcel(Parcel in) {
            return new PojoUserTransaction(in);
        }

        @Override
        public PojoUserTransaction[] newArray(int size) {
            return new PojoUserTransaction[size];
        }
    };

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(String lastBalance) {
        this.lastBalance = lastBalance;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionStatusDescription() {
        return transactionStatusDescription;
    }

    public void setTransactionStatusDescription(String transactionStatusDescription) {
        this.transactionStatusDescription = transactionStatusDescription;
    }

    public String getTransactionStatusId() {
        return transactionStatusId;
    }

    public void setTransactionStatusId(String transactionStatusId) {
        this.transactionStatusId = transactionStatusId;
    }

    public String getTransactionStatusNumber() {
        return transactionStatusNumber;
    }

    public void setTransactionStatusNumber(String transactionStatusNumber) {
        this.transactionStatusNumber = transactionStatusNumber;
    }

    public String getTransactionStatusText() {
        return transactionStatusText;
    }

    public void setTransactionStatusText(String transactionStatusText) {
        this.transactionStatusText = transactionStatusText;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(amount);
        parcel.writeString(createdOn);
        parcel.writeString(id);
        parcel.writeString(info);
        parcel.writeString(lastBalance);
        parcel.writeString(transactionId);
        parcel.writeString(transactionStatus);
        parcel.writeString(transactionStatusDescription);
        parcel.writeString(transactionStatusId);
        parcel.writeString(transactionStatusNumber);
        parcel.writeString(transactionStatusText);
        parcel.writeString(transactionType);
        parcel.writeString(updatedOn);
        parcel.writeString(userId);
    }
}
