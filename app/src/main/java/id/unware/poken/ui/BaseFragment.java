package id.unware.poken.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by marzellaalfamega on 3/20/17.
 */

public class BaseFragment extends Fragment {

    protected AppCompatActivity parent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parent = (AppCompatActivity) context;
    }
}
