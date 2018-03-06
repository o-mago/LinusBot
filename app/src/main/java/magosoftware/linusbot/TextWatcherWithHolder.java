package magosoftware.linusbot;

import android.content.pm.LabeledIntent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by root on 06/03/18.
 */

public class TextWatcherWithHolder implements TextWatcher {

    LineHolder holderWatcher;

    public TextWatcherWithHolder (LineHolder holder) {
        this.holderWatcher = holder;
    }

    public void afterTextChanged(Editable s) {

    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }
}
