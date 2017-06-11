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
}

/*"address": {
                    "id": 2,
                    "customer": 1,
                    "location": {
                        "city": "Jakarta Barat",
                        "district": "Kembangan",
                        "zip": "11620",
                        "state": "Indonesia"
                    },
                    "name": "Kantor Anwar",
                    "address": "Jl. Meruya Ilir No. 88, Kembangan",
                    "phone": "082362588301"
                }*/