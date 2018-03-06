package magosoftware.linusbot;

import android.graphics.drawable.Drawable;

/**
 * Created by root on 06/03/18.
 */

public class CampoConfig {

    Drawable icone;
    String mensagem;
    String comando;
    boolean habilita;

    public CampoConfig(Drawable icone, String comando, boolean habilita) {
        this.icone = icone;
        this.comando = comando;
        this.habilita = habilita;
    }

    public String getMensagem() { return mensagem; }

    public String getComando() { return comando; }

    public Drawable getIcone() { return icone; }

    public boolean isHabilita() { return habilita; }
}
