package ch.fhnw.taurus;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

/**
 * Created by nozdormu on 06/05/2016.
 */
public class TextWatcherAdapter implements TextWatcher {
    private static final  String LOG_TAG = TextWatcherAdapter.class.getName();

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // empty implementation
        Log.d(LOG_TAG,"beforeTextChanged() ignored");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // empty implementation
        Log.d(LOG_TAG,"onTextChanged() ignored");
    }

    @Override
    public void afterTextChanged(Editable s) {
        // empty I
        Log.d(LOG_TAG,"afterTextChanged() ignored");
    }
}
