package id.unware.poken.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author Anwar Pasaribu
 * @since Jun 07 2017
 */

public class ProductCategoryDataRes extends PokenApiBase {

    @Expose
    @SerializedName("results")
    public ArrayList<Category> results;


}
