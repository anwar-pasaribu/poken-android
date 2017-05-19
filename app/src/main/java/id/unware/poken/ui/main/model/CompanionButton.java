package id.unware.poken.ui.main.model;

/**
 * @author Anwar Pasaribu
 * @since Mar 12 2017
 */

public class CompanionButton {
    private CharSequence text;
    private int drawable;
    private boolean isActive;

    public CompanionButton() {
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "Companion button text: " + getText();
    }
}
