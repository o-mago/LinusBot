package magosoftware.linusbot;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by root on 05/03/18.
 */

public class ControleRemoto extends MainActivity implements View.OnTouchListener, View.OnClickListener{

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    RelativeLayout center_buttons;
    RelativeLayout right_buttons;
    RelativeLayout left_buttons;
    ImageButton arrowUp;
    ImageButton arrowDown;
    ImageButton arrowRight;
    ImageButton arrowLeft;
    ImageButton lineFollower;
    ImageButton calibrate;
    ImageButton turbo;
    ImageButton vira;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controle_remoto);

        sharedPref = getSharedPreferences("linus", 0);
        editor = sharedPref.edit();

        findViewById(R.id.config).setOnClickListener(this);
        findViewById(R.id.disconnect).setOnClickListener(this);
        center_buttons = findViewById(R.id.arrows);
        right_buttons = findViewById(R.id.buttons_right);
        left_buttons = findViewById(R.id.buttons_left);
        arrowUp = findViewById(R.id.arrow_up);
        arrowDown = findViewById(R.id.arrow_down);
        arrowRight = findViewById(R.id.arrow_right);
        arrowLeft = findViewById(R.id.arrow_left);
        lineFollower = findViewById(R.id.switch_line_follower);
        calibrate = findViewById(R.id.calibrate);
        turbo = findViewById(R.id.turbo);
        vira = findViewById(R.id.cento_e_oitenta);
        setButtons(arrowUp, 0, getResources().getDrawable(R.drawable.arrow_up));
        setButtons(arrowDown, 1, getResources().getDrawable(R.drawable.arrow_down));
        setButtons(arrowRight, 2, getResources().getDrawable(R.drawable.arrow_right));
        setButtons(arrowLeft, 3, getResources().getDrawable(R.drawable.arrow_left));
        setButtons(lineFollower, 4, getResources().getDrawable(R.drawable.line_follower));
        setButtons(calibrate, 5, getResources().getDrawable(R.drawable.calibrate));
        setButtons(turbo, 6, getResources().getDrawable(R.drawable.turbo));
        setButtons(vira, 7, getResources().getDrawable(R.drawable.cento_e_oitenta));

        outputStream = MainActivity.getOutStream();
    }

    public void setButtons(ImageButton imageButton, int position, Drawable imagem) {
        imageButton.setOnTouchListener(this);
        Boolean switchButton = sharedPref.getBoolean("switch_"+position, true);
        Log.d("VERFAL", switchButton.toString());
        if(switchButton) {
            imageButton.setBackgroundResource(R.drawable.round_corner);
            imageButton.setImageDrawable(imagem);
        }
        else {
            imageButton.setBackgroundColor(getResources().getColor(R.color.transparent));
            imageButton.setImageDrawable(null);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.config) {
            Intent intent = new Intent(this, Config.class);
            startActivity(intent);
        }
        if(id == R.id.disconnect) {
            BluetoothSocket socket = MainActivity.getSocket();
            try {
                socket.close();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            catch (IOException e) {

            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int id = view.getId();
        Log.d("CARRO", "TO AQUI");
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (id == R.id.arrow_up && sharedPref.getBoolean("switch_0", true)) {
                try {
                    write(sharedPref.getString("botao_0", "F"));
                    Log.d("CARRO", "FRENTE");
                } catch (IOException e) {
                    Log.d("CARRO", "MERDA");
                }
            }
            if (id == R.id.arrow_down && sharedPref.getBoolean("switch_0", true)) {
                try {
                    write(sharedPref.getString("botao_1", "B"));
                } catch (IOException e) {

                }
            }
            if (id == R.id.arrow_right && sharedPref.getBoolean("switch_0", true)) {
                try {
                    write(sharedPref.getString("botao_2", "R"));
                } catch (IOException e) {

                }
            }
            if (id == R.id.switch_line_follower && sharedPref.getBoolean("switch_0", true)) {
                try {
                    write(sharedPref.getString("botao_4", "LF"));
                } catch (IOException e) {

                }
            }
            if (id == R.id.calibrate && sharedPref.getBoolean("switch_0", true)) {
                try {
                    write(sharedPref.getString("botao_5", "C"));
                } catch (IOException e) {

                }
            }
            if (id == R.id.turbo && sharedPref.getBoolean("switch_0", true)) {
                try {
                    write(sharedPref.getString("botao_6", "T"));
                } catch (IOException e) {

                }
            }
            if (id == R.id.cento_e_oitenta && sharedPref.getBoolean("switch_0", true)) {
                try {
                    write(sharedPref.getString("botao_7", "V"));
                } catch (IOException e) {

                }
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            try {
                write("S");
            } catch (IOException e) {

            }
        }
        return true;
    }

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
        Log.d("SAIDA", s);
    }
}
