package id.unware.poken.ui.view;


import id.unware.poken.pojo.UIState;

/**
 * General view functionality.
 *
 * @author Anwar Pasaribu
 * @since Jan 28 2017
 */

public interface BaseView {
    void showViewState(UIState uiState);

    boolean isActivityFinishing();

}
