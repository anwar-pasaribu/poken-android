package id.unware.poken.pojo;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Created by marzellaalfamega on 2/26/16.
 *
 * @since Feb 2 2017 - [V49] Encapsulated all members.
 */
public class PojoUser extends RealmObject implements Serializable {
    private String user_email;
    private String user_id;
    private String user_name;
    private String user_phone;
    private String phone_verified;

    public PojoUser() {}

    public PojoUser(String user_email, String user_id, String user_name, String user_phone, String phone_verified) {
        this.setUserEmail(user_email);
        this.setUserId(user_id);
        this.setUserName(user_name);
        this.setUserPhone(user_phone);
        this.setPhoneVerified(phone_verified);
    }

    @Override
    public String toString() {
        return String.format("email:%s, id:%s, name:%s, phone:%s, is_verified:%s",
                getUserEmail(), getUserId(), getUserName(), getUserPhone(), getPhoneVerified());
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }

    public String getUserEmail() {
        return user_email;
    }

    public void setUserEmail(String user_email) {
        this.user_email = user_email;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getUserPhone() {
        return user_phone;
    }

    public void setUserPhone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getPhoneVerified() {
        return phone_verified;
    }

    public void setPhoneVerified(String phone_verified) {
        this.phone_verified = phone_verified;
    }
}
