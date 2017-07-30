package id.unware.poken.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class PokenApiBase {

    @Expose
    @SerializedName("count")
    public long count;

    @Expose
    @SerializedName("next")
    public String next;

    @Expose
    @SerializedName("previous")
    public String previous;

    /**
     * Returned when API call failed (ex. code 403)
     */
    @Expose
    @SerializedName("detail")
    public String detail;

    /**
     * Returned for AUTH FAILED
     */
    @Expose
    @SerializedName("non_field_errors")
    public String[] non_field_errors;

    public PokenApiBase() {
    }

    @Override
    public String toString() {
        return "API BASE. Count: "
                .concat(String.valueOf(count))
                .concat(", Next: ")
                .concat(String.valueOf(next))
                .concat(", Previous: ")
                .concat(String.valueOf(previous))
                .concat(", Detail: ")
                .concat(String.valueOf(detail))
                .concat(", NON Field ERROR: ")
                .concat(Arrays.toString(non_field_errors));
    }
}
