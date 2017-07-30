package id.unware.poken.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import id.unware.poken.tools.StringUtils;

/**
 * @author Anwar Pasaribu
 * @since Jul 24 2017
 */

public class User {

    @Expose
    @SerializedName("username")
    public String username;

    @Expose
    @SerializedName("first_name")
    public String first_name;

    @Expose
    @SerializedName("last_name")
    public String last_name;

    @Expose
    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("token")
    public String token;

    public User() {
    }

    public String getFullName() {
        if (!StringUtils.isEmpty(first_name) && !StringUtils.isEmpty(last_name)) {
            return String.valueOf(first_name).concat(" ").concat(String.valueOf(last_name));
        } else {
            return String.valueOf(username);
        }
    }
}

/*
{
    "username": "porkot",
    "first_name": "Porkot",
    "last_name": "Sarumpaet",
    "email": "porkot@icloud.com"
}
 */
