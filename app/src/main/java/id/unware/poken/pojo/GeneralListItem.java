package id.unware.poken.pojo;

import android.support.annotation.IntegerRes;

/**
 * Information chunks from Pojo object for list presentation purpose.
 *
 * @author Anwar Pasaribu
 * @since [V49] - Add badge count
 */

public class GeneralListItem {

    private int id;

    @IntegerRes private int mListIcon;
    @IntegerRes private int mActionIcon;

    // URI
    private String mStrListIcon;
    private String mStrActionIcon;
    private String mTitle;
    private String mSubTitle;
    private String mContent;
    private String mActionUri;
    private String mActionType;

    private boolean isSelected;
    private boolean isHeader;
    private int itemType;

    /**
     * Is badge available
     */
    private boolean isBadgeAvailable = false;
    private int badgeCount;

    public GeneralListItem() {
    }

    public GeneralListItem(int listIcon, int mActionIcon, String mTitle, String mSubTitle, String mContent, String mActionUri, String mActionType) {
        this.mListIcon = listIcon;
        this.mActionIcon = mActionIcon;
        this.mTitle = mTitle;
        this.mSubTitle = mSubTitle;
        this.mContent = mContent;
        this.mActionUri = mActionUri;
        this.mActionType = mActionType;
    }

    public GeneralListItem(int listIcon, int mActionIcon, String strListIcon, String strActionIcon, String mTitle, String mSubTitle, String mContent, String mActionUri, String mActionType) {
        this.mListIcon = listIcon;
        this.mActionIcon = mActionIcon;
        this.mStrListIcon = strListIcon;
        this.mStrActionIcon = strActionIcon;
        this.mTitle = mTitle;
        this.mSubTitle = mSubTitle;
        this.mContent = mContent;
        this.mActionUri = mActionUri;
        this.mActionType = mActionType;
    }

    public boolean isBadgeAvailable() {
        return isBadgeAvailable;
    }

    public void setBadgeAvailable(boolean badgeAvailable) {
        isBadgeAvailable = badgeAvailable;
    }

    public int getBadgeCount() {
        return badgeCount;
    }

    public void setBadgeCount(int badgeCount) {
        this.badgeCount = badgeCount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getActionUri() {
        return mActionUri;
    }

    public void setActionUri(String mActionUri) {
        this.mActionUri = mActionUri;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String mSubTitle) {
        this.mSubTitle = mSubTitle;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public int getListIcon() {
        return mListIcon;
    }

    public void setListIcon(int mListIcon) {
        this.mListIcon = mListIcon;
    }

    public int getActionIcon() {
        return mActionIcon;
    }

    public void setActionIcon(int mActionIcon) {
        this.mActionIcon = mActionIcon;
    }

    public String getActionType() {
        return mActionType;
    }

    public void setActionType(String mActionType) {
        this.mActionType = mActionType;
    }

    public String getStrListIcon() {
        return mStrListIcon;
    }

    public void setStrListIcon(String mStrListIcon) {
        this.mStrListIcon = mStrListIcon;
    }

    public String getStrActionIcon() {
        return mStrActionIcon;
    }

    public void setStrActionIcon(String mStrActionIcon) {
        this.mStrActionIcon = mStrActionIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
