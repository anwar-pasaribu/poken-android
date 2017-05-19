package id.unware.poken.pojo;

import android.support.annotation.NonNull;

public class NavigationItem implements Comparable<NavigationItem> {

    /**
     * [49]
     * Navigation item ID. To give each navigation identifier instead of it's position
     * on Navigation Drawer.
     */
    private int itemId;

    private CharSequence mText;
    private int mDrawable;
    private boolean isActive = false;
    private boolean isNotified = false;

    /**
     * [V49]
     * Decide whether this nav. item will open other activity.
     */
    private boolean newActivity;

    /**
     * [53]
     * Decide when destination page is a sub-page of it's parent page.
     */
    private boolean subPage;

    /** Parent page TAG number. Identify which page open the sup-page */
    private int parentPageTag;

    /** Companion button - button on header - one line with header */
    private CharSequence companionButtonText;


    public NavigationItem() {
    }

    public NavigationItem(int itemId, CharSequence text, int drawable, boolean isNotified, boolean isNewActivity) {
        this.itemId = itemId;
        this.mText = text;
        this.mDrawable = drawable;
        this.isNotified = isNotified;
        this.newActivity = isNewActivity;
    }

    public CharSequence getText() {
        return mText;
    }

    public void setText(CharSequence text) {
        mText = text;
    }


    public int getDrawable() {
        return mDrawable;
    }

    public void setDrawable(int mDrawable) {
        this.mDrawable = mDrawable;
    }

    /**
     * Get whether navigation item is active.
     *
     * @return {@code true} if item is activated.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Set whether navigation drawer is active or no.
     *
     * @param isEnable {@code true} to activate item.
     */
    public void setIsActive(boolean isEnable) {
        this.isActive = isEnable;
    }

    /**
     * Indicate navigation item is in notification mode.
     *
     * @return Is item being notified such as "on going pickup"
     */
    public boolean isNotified() {
        return isNotified;
    }

    /**
     * Set navigation item with notification indicator.
     *
     * @param notified {@code true} to activate indicator.
     */
    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    @Override
    public int compareTo(@NonNull NavigationItem navigationItem) {
        return ((Boolean) this.isActive()).compareTo(navigationItem.isActive());
    }

    public CharSequence getmText() {
        return mText;
    }

    public void setmText(CharSequence mText) {
        this.mText = mText;
    }

    public int getmDrawable() {
        return mDrawable;
    }

    public void setmDrawable(int mDrawable) {
        this.mDrawable = mDrawable;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isSubPage() {
        return subPage;
    }

    public void setSubPage(boolean subPage) {
        this.subPage = subPage;
    }

    public int getParentPageTag() {
        return parentPageTag;
    }

    public void setParentPageTag(int parentPageTag) {
        this.parentPageTag = parentPageTag;
    }

    public CharSequence getCompanionButtonText() {
        return companionButtonText;
    }

    public void setCompanionButtonText(CharSequence companionButtonText) {
        this.companionButtonText = companionButtonText;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public boolean isNewActivity() {
        return newActivity;
    }

    public void setNewActivity(boolean newActivity) {
        this.newActivity = newActivity;
    }
}