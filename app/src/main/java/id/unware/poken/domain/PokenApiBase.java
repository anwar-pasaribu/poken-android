package id.unware.poken.domain;

import android.support.annotation.Nullable;

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

    @Nullable
    @Expose
    @SerializedName("next")
    public String next;

    @Nullable
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

    @Override public String toString() {
        return "PokenApiBase{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", detail='" + detail + '\'' +
                ", non_field_errors=" + Arrays.toString(non_field_errors) +
                '}';
    }
}
