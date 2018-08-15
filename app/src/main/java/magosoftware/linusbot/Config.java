package magosoftware.linusbot;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by root on 06/03/18.
 */

public class Config extends AppCompatActivity implements LineAdapter.OnItemClicked, View.OnClickListener{

    RecyclerView mRecyclerView;
    LineAdapter mAdapter;
    private List<CampoConfig> mModels;
    ArrayList<Drawable> icones;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        sharedPref = getSharedPreferences("linus", 0);
        editor = sharedPref.edit();

        //findViewById(R.id.check_button).setOnClickListener(this);

        setupRecycler();
        addItems();
        setupAB();
    }

    private void setupAB() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setClickable(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.check);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Drawable icone : icones) {
                    editor.putString("botao_"+icones.indexOf(icone), mAdapter.campoConfigs.get(icones.indexOf(icone)).mensagem);
                    editor.commit();
                }
                Intent intent = new Intent(Config.this, ControleRemoto.class);
                startActivity(intent);
            }
        });
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);
    }

    private void setupRecycler() {

        // Configurando o gerenciador de layout para ser uma lista.
        mRecyclerView = findViewById(R.id.lista_settings);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Adiciona o adapter que irá anexar os objetos à lista.
        // Está sendo criado com lista vazia, pois será preenchida posteriormente.
        mAdapter = new LineAdapter();
        //mAdapter = new LineAdapterPet(new ArrayList<>(0));
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
    }

    @Override
    public void onItemClick(int position, Boolean habilita) {
        editor.putBoolean("switch_"+position, habilita);
        Log.d("VERFAL1", habilita.toString());
        editor.commit();
    }

    public void addItems() {
        icones = new ArrayList<>();
        mModels = new ArrayList<>();
        icones.add(getResources().getDrawable(R.drawable.arrow_up));
        icones.add(getResources().getDrawable(R.drawable.arrow_down));
        icones.add(getResources().getDrawable(R.drawable.arrow_right));
        icones.add(getResources().getDrawable(R.drawable.arrow_left));
        icones.add(getResources().getDrawable(R.drawable.diagonal_cima_direita));
        icones.add(getResources().getDrawable(R.drawable.diagonal_cima_esquerda));
        icones.add(getResources().getDrawable(R.drawable.diagonal_baixo_esquerda));
        icones.add(getResources().getDrawable(R.drawable.diagonal_baixo_direita));
        icones.add(getResources().getDrawable(R.drawable.line_follower));
        icones.add(getResources().getDrawable(R.drawable.calibrate));
        icones.add(getResources().getDrawable(R.drawable.turbo));
        icones.add(getResources().getDrawable(R.drawable.cento_e_oitenta));
        List<String> arrayItens = Arrays.asList(getResources().getStringArray(R.array.botoes));
        for(Drawable icone : icones) {
            mModels.add(new CampoConfig(icone, sharedPref.getString("botao_"+icones.indexOf(icone),
                    arrayItens.get(icones.indexOf(icone))),
                    sharedPref.getBoolean("switch_"+icones.indexOf(icone), true)));
        }
        mAdapter.add(mModels);
        mAdapter.setOnClick(this);
    }
}
