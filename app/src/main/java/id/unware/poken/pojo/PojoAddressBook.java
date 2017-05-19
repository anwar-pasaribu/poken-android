package id.unware.poken.pojo;

import android.support.annotation.NonNull;

/**
 * Created by Anwar Pasaribu on 9/19/2016.<br/>
 *
 * Address Book item.
 *
 * @since (Sep 19th 2016) - Version 41
 */
public class PojoAddressBook implements Comparable<PojoAddressBook>{

    private String name, phoneNumber, address;

    /** State for the item */
    private int mIntState;

    /** Indicate the item is FROM / Sender data */
    private boolean mIsSenderData;

    /** Default state */
    public static int STATE_ADDRESS_HIDDEN = -1;
    public static int STATE_ADDRESS_NORMAL = 0;

    private static PojoAddressBook pojoAddressBookInstance;


    public static PojoAddressBook getInstance() {
        return pojoAddressBookInstance;
    }

    public PojoAddressBook(String name, String phoneNumber, String address, int mIntState, boolean mIsSenderData) {
        pojoAddressBookInstance = this;

        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.mIntState = mIntState;
        this.mIsSenderData = mIsSenderData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getmIntState() {
        return mIntState;
    }

    public void setmIntState(int mIntState) {
        this.mIntState = mIntState;
    }

    public boolean ismIsSenderData() {
        return mIsSenderData;
    }

    public void setmIsSenderData(boolean mIsSenderData) {
        this.mIsSenderData = mIsSenderData;
    }

    @Override
    public int compareTo(@NonNull PojoAddressBook pojoAddressBook) {
        return this.getName().compareTo(pojoAddressBook.getName());
    }
}
