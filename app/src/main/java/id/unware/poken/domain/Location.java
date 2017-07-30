package id.unware.poken.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Jun 05 2017
 */

public class Location {

    @Expose
    @SerializedName("address")
    public String address;

    @Expose
    @SerializedName("city")
    public String city;

    @Expose
    @SerializedName("district")
    public String district;

    @Expose
    @SerializedName("zip")
    public String zip;

    @Expose
    @SerializedName("state")
    public String state;

    public Location() {
    }
}

/*
{
    "address": "Jl. Lapangan Bola No. 3, Gg. Mawar No. 43, Kebon Jeruk, Jakarta Barat 11530",
    "city": "Jakarta Barat",
    "district": "Kebon Jeruk",
    "zip": "11530",
    "state": "Indonesia"
}
 */