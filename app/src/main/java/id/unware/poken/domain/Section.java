package id.unware.poken.domain;

import java.util.ArrayList;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class Section {
    public String name;
    public String section_action_value;
    public int section_action_id;
    public ArrayList<Product> products;
    public ArrayList<Seller> top_sellers;

    public Section() {
    }
}
