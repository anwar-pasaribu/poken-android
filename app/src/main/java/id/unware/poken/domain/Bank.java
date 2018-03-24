package id.unware.poken.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anwar Pasaribu on 10/01/2018.
 */

public class Bank {

    @Expose
    @SerializedName("name") public String name;

    @Expose
    @SerializedName("code_number") public String code_number;

    @Expose
    @SerializedName("logo") public String logo;

}
