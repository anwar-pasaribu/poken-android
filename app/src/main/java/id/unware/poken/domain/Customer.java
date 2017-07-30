package id.unware.poken.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author Anwar Pasaribu
 * @since Jul 29 2017
 */

public class Customer {

    @Expose
    @SerializedName("id")
    public long id;

    @Expose
    @SerializedName("related_user")
    public User related_user;

    @Expose
    @SerializedName("phone_number")
    public String phone_number;

    @Expose
    @SerializedName("location")
    public Location location;

    public Customer() {
    }
}

/*
{
    "id": 3,
    "related_user": {
        "username": "porkot",
        "first_name": "Porkot",
        "last_name": "Sarumpaet",
        "email": "porkot@icloud.com"
    },
    "phone_number": "01234",
    "location": {
        "address": "Jl. Lapangan Bola No. 3, Gg. Mawar No. 43, Kebon Jeruk, Jakarta Barat 11530",
        "city": "Jakarta Barat",
        "district": "Kebon Jeruk",
        "zip": "11530",
        "state": "Indonesia"
    }
}
 */
