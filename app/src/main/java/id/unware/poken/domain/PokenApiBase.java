package id.unware.poken.domain;

/**
 * @author Anwar Pasaribu
 * @since Jun 01 2017
 */

public class PokenApiBase {
    public long count;
    public String next;
    public String previous;

    /**
     * Returned when API call failed (ex. code 403)
     */
    public String detail;
}
