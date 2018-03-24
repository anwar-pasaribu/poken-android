package id.unware.poken.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Anwar Pasaribu on 10/01/2018.
 */

public class UserBankDataRes extends PokenApiBase {

    @Expose
    @SerializedName("results") public ArrayList<UserBank> results;

}
