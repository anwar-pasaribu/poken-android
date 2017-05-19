package id.unware.poken.pojo;

/**
 * Help and Support data structure. Provide list data.
 *
 * @author Anwar Pasaribu
 * @since Jan 09 2017
 */
public class HelpAndSupport {

    /**
     * Item ID to identify item action.
     */
    private int itemId;

    public final String content;
    public final String details;
    public final boolean isHeader;
    public final int intImgRes;
    public final String actionString;

    // ---
    // For badge purpose
    /**
     * Indicate item has badge or no. Default has no badge (FALSE)
     */
    private boolean hasBadge = false;
    /**
     * Badge count on item
     */
    private int badgeCount;
    /**
     * Badge status tu handle how badge looks
     */
    private int badgeStatus;
    // ---

    public HelpAndSupport(String content, String details, int intImageRes, boolean isHeader, String actionString) {
        this.content = content;
        this.details = details;
        this.intImgRes = intImageRes;
        this.isHeader = isHeader;
        this.actionString = actionString;
    }

    public boolean isHasBadge() {
        return hasBadge;
    }

    public void setHasBadge(boolean hasBadge) {
        this.hasBadge = hasBadge;
    }

    public int getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(int badgeCount) {
        this.badgeCount = badgeCount;
    }

    public int getBadgeStatus() {
        return badgeStatus;
    }

    public void setBadgeStatus(int badgeStatus) {
        this.badgeStatus = badgeStatus;
    }

    @Override
    public String toString() {
        return content;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}
