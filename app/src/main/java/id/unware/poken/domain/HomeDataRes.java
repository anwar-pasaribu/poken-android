package id.unware.poken.domain;

import java.util.ArrayList;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class HomeDataRes extends PokenApiBase {

    public ArrayList<Result> results;

    public class Result {
        public ArrayList<Featured> featured_items;
        public ArrayList<Section> sections;
    }

}
