package id.unware.poken.domain;

/**
 * @author Anwar Pasaribu
 * @since Jun 08 2017
 */

public class AddressBook {
    public long id;
    public long customer;
    public Location location;
    public String name;
    public String address;
    public String phone;

    @Override
    public String toString() {
        return name
                .concat(" (")
                .concat(String.valueOf(phone))
                .concat(") - ")
                .concat(String.valueOf(address));
    }
}