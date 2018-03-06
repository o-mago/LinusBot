package magosoftware.linusbot;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

/**
 * Created by root on 06/03/18.
 */

public class LineHolder extends RecyclerView.ViewHolder{
    //private CardView card;
    public ImageView icone;
    public EditText mensagem;
    public Switch habilita;
    //public TextView nomeUniversidade;


    public LineHolder(View itemView) {
        super(itemView);
        icone = itemView.findViewById(R.id.icone);
        mensagem = itemView.findViewById(R.id.mensagem);
        habilita = itemView.findViewById(R.id.botao_switch);
    }
}
