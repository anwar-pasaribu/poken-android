package id.unware.poken.domain;

import java.io.Serializable;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class ProductImage implements Serializable {
    public long id;
    public String path;
    public String thumbnail;
    public String title;
    public String description;
}
