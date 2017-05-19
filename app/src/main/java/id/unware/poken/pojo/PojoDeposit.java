package id.unware.poken.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by marzellaalfamega on 2/9/17.
 */
public class PojoDeposit implements Parcelable{

    @SerializedName("amount") public String amount;
    @SerializedName("created_on") public String created_on;
    @SerializedName("expired_on") public String expired_on;
    @SerializedName("internal_bank_id") public String internal_bank_id;
    @SerializedName("request_id") public String request_id;
    @SerializedName("request_type") public String request_type;
    @SerializedName("unique_id") public String unique_id;
    @SerializedName("updated_on") public String updated_on;
    @SerializedName("user_bank_id") public String user_bank_id;
    @SerializedName("user_id") public String user_id;
    @SerializedName("verified") public String verified;
    @SerializedName("verified_by") public String verified_by;
    @SerializedName("verified_on") public String verified_on;

    protected PojoDeposit(Parcel in) {
        amount = in.readString();
        created_on = in.readString();
        expired_on = in.readString();
        internal_bank_id = in.readString();
        request_id = in.readString();
        request_type = in.readString();
        unique_id = in.readString();
        updated_on = in.readString();
        user_bank_id = in.readString();
        user_id = in.readString();
        verified = in.readString();
        verified_by = in.readString();
        verified_on = in.readString();
    }

    public static final Creator<PojoDeposit> CREATOR = new Creator<PojoDeposit>() {
        @Override
        public PojoDeposit createFromParcel(Parcel in) {
            return new PojoDeposit(in);
        }

        @Override
        public PojoDeposit[] newArray(int size) {
            return new PojoDeposit[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(amount);
        parcel.writeString(created_on);
        parcel.writeString(expired_on);
        parcel.writeString(internal_bank_id);
        parcel.writeString(request_id);
        parcel.writeString(request_type);
        parcel.writeString(unique_id);
        parcel.writeString(updated_on);
        parcel.writeString(user_bank_id);
        parcel.writeString(user_id);
        parcel.writeString(verified);
        parcel.writeString(verified_by);
        parcel.writeString(verified_on);
    }
}
