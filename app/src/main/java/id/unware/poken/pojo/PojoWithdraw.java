package id.unware.poken.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by marzellaalfamega on 2/14/17.
 */
public class PojoWithdraw implements Parcelable{

    public String amount;
    public String created_on;
    public String expired_on;
    public String internal_bank_id;
    public String request_id;
    public String request_type;
    public String unique_id;
    public String updated_on;
    public String user_bank_id;
    public String user_id;
    public String verified;
    public String verified_by;
    public String verified_on;

    protected PojoWithdraw(Parcel in) {
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

    public static final Creator<PojoWithdraw> CREATOR = new Creator<PojoWithdraw>() {
        @Override
        public PojoWithdraw createFromParcel(Parcel in) {
            return new PojoWithdraw(in);
        }

        @Override
        public PojoWithdraw[] newArray(int size) {
            return new PojoWithdraw[size];
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
