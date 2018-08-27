package magosoftware.linusbot;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.OutputStream;

import io.github.controlwear.virtual.joystick.android.JoystickView;

/**
 * Created by root on 05/03/18.
 */

public class ControleRemoto extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener{

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
    JoystickView joystick;
    OutputStream outputStream;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controle_remoto_v2);

        sharedPref = getSharedPreferences("linus", 0);
        editor = sharedPref.edit();
        outputStream = Bluetooth.getOutStream();

        findViewById(R.id.config).setOnClickListener(this);
        findViewById(R.id.disconnect).setOnClickListener(this);
//        center_buttons = findViewById(R.id.arrows);
//        right_buttons = findViewById(R.id.buttons_right);
//        left_buttons = findViewById(R.id.buttons_left);
//        arrowUp = findViewById(R.id.arrow_up);
//        arrowDown = findViewById(R.id.arrow_down);
//        arrowRight = findViewById(R.id.arrow_right);
//        arrowLeft = findViewById(R.id.arrow_left);
        lineFollower = findViewById(R.id.switch_line_follower);
        calibrate = findViewById(R.id.calibrate);
        turbo = findViewById(R.id.turbo);
        vira = findViewById(R.id.cento_e_oitenta);
        joystick = findViewById(R.id.joystick);
//        setButtons(arrowUp, 0, getResources().getDrawable(R.drawable.arrow_up));
//        setButtons(arrowDown, 1, getResources().getDrawable(R.drawable.arrow_down));
//        setButtons(arrowRight, 2, getResources().getDrawable(R.drawable.arrow_right));
//        setButtons(arrowLeft, 3, getResources().getDrawable(R.drawable.arrow_left));
        setButtons(lineFollower, 4, getResources().getDrawable(R.drawable.line_follower));
        setButtons(calibrate, 5, getResources().getDrawable(R.drawable.calibrate));
        setButtons(turbo, 6, getResources().getDrawable(R.drawable.turbo));
        setButtons(vira, 7, getResources().getDrawable(R.drawable.cento_e_oitenta));
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                try {
                    if ((angle > 333 && angle < 360) || (angle> 0 && angle <= 27)) {
                        write(sharedPref.getString("switch_2", "R"));
                    }
                    else if (angle > 27 && angle <= 72) {
                        write(sharedPref.getString("switch_4", "FR"));
                    }
                    else if (angle > 72 && angle <= 117) {
                        write(sharedPref.getString("switch_0", "F"));
                    }
                    else if (angle > 117 && angle <= 162) {
                        write(sharedPref.getString("switch_5", "FL"));
                    }
                    else if (angle > 162 && angle <= 207) {
                        write(sharedPref.getString("switch_3", "L"));
                    }
                    else if (angle > 207 && angle <= 252) {
                        write(sharedPref.getString("switch_6", "BL"));
                    }
                    else if (angle > 252 && angle <= 297) {
                        write(sharedPref.getString("switch_1", "B"));
                    }
                    else if (angle > 297 && angle <= 333) {
                        write(sharedPref.getString("switch_7", "BR"));
                    }
                    else {
                        write("S");
                    }
                } catch (IOException e) {

                }
            }
        });

        outputStream = Bluetooth.getOutStream();
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
            BluetoothSocket socket = Bluetooth.getSocket();
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
//            if (id == R.id.arrow_up && sharedPref.getBoolean("switch_0", true)) {
//                try {
//                    write(sharedPref.getString("botao_0", "F"));
//                    Log.d("CARRO", "FRENTE");
//                } catch (IOException e) {
//                    Log.d("CARRO", "MERDA");
//                }
//            }
//            if (id == R.id.arrow_down && sharedPref.getBoolean("switch_1", true)) {
//                try {
//                    write(sharedPref.getString("botao_1", "B"));
//                } catch (IOException e) {
//
//                }
//            }
//            if (id == R.id.arrow_right && sharedPref.getBoolean("switch_2", true)) {
//                try {
//                    write(sharedPref.getString("botao_2", "R"));
//                } catch (IOException e) {
//
//                }
//            }
//            if (id == R.id.arrow_left && sharedPref.getBoolean("switch_3", true)) {
//                try {
//                    write(sharedPref.getString("botao_3", "L"));
//                } catch (IOException e) {
//
//                }
//            }
            if (id == R.id.switch_line_follower && sharedPref.getBoolean("switch_4", true)) {
                try {
                    write(sharedPref.getString("botao_8", "LF"));
                } catch (IOException e) {

                }
            }
            if (id == R.id.calibrate && sharedPref.getBoolean("switch_5", true)) {
                try {
                    write(sharedPref.getString("botao_9", "C"));
                } catch (IOException e) {

                }
            }
            if (id == R.id.turbo && sharedPref.getBoolean("switch_6", true)) {
                try {
                    write(sharedPref.getString("botao_10", "T"));
                } catch (IOException e) {

                }
            }
            if (id == R.id.cento_e_oitenta && sharedPref.getBoolean("switch_7", true)) {
                try {
                    write(sharedPref.getString("botao_11", "V"));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BluetoothSocket socket = Bluetooth.getSocket();
        try {
            socket.close();
        } catch (IOException e) {

        }
        finish();
    }
}
