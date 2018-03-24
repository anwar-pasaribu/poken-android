package id.unware.poken.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anwar Pasaribu on 10/01/2018.
 */

public class UserBank extends PokenApiBase {

    @Expose
    @SerializedName("id") public long id;

    @Expose
    @SerializedName("account_number") public String accountNumber;

    @Expose
    @SerializedName("account_name") public String accountName;

    @Expose
    @SerializedName("bank_name") public String bankName;

    @Expose
    @SerializedName("bank_code_number") public String bankCodeNumber;

    @Expose
    @SerializedName("bank_logo") public String bankLogo;

}
