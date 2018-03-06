package magosoftware.linusbot;

import android.view.View;

/**
 * Created by root on 06/03/18.
 */

public class OnClickListenerWithHolder implements View.OnClickListener{

    LineHolder holderClick;

    public OnClickListenerWithHolder (LineHolder holderClick){
        this.holderClick = holderClick;
    }

    public void onClick(View v){

    }
}
