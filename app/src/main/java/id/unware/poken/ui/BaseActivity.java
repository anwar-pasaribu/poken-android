package id.unware.poken.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import id.unware.poken.tools.Utils;

/**
 * Created by marzellaalfamega on 3/13/17.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            Utils.snackbarDismiss();
        }
        return super.dispatchTouchEvent(ev);
    }

}
