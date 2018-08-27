package magosoftware.linusbot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Cronometro extends Fragment{

    FirebaseFirestore db;
    DocumentReference docRef;
    Date dataInscricoes;
    Date dataFimInscricoes;
    Date dataCompeticao;
    Date horarioInscricoes;
    Date horarioFimInscricoes;
    Date horarioCompeticao;
    TextView textoBase;
    TextView diasFaltando;
    TextView paraQue;
    ProgressBar progressBar;
    LinearLayout textos;
    long diferenca;
    CardView inscricao;
    String html;

    private static final String TAG = "DEV/CRONOMETRO";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("datas").document("9kBbmdP4tvamSujk4PxV");
        return inflater.inflate(R.layout.cronometro, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedIntanceState) {
        super.onActivityCreated(savedIntanceState);
        diasFaltando = getView().findViewById(R.id.dias_faltando);
        paraQue = getView().findViewById(R.id.para_o_que_falta);
        progressBar = getView().findViewById(R.id.progress_bar);
        textos = getView().findViewById(R.id.textos);
        textoBase = getView().findViewById(R.id.texto_base);
        inscricao = getView().findViewById(R.id.inscricao);
        inscricao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(html));
                startActivity(intent);
            }
        });
        inscricao.setVisibility(View.GONE);
        textos.setVisibility(View.GONE);
        if(savedIntanceState == null) {
            getDatas();
        }
    }

    private void getDatas() {
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    String dataInscricoesString = ((String) document.get("inscricoes")).split("-")[0];
                    String dataFimInscricoesString = ((String) document.get("inscricoes")).split("-")[1];
                    String dataCompeticaoString = (String) document.get("competicao");
                    html = (String) document.get("link");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    try {
                        dataInscricoes = dateFormat.parse(dataInscricoesString);
                        dataFimInscricoes = dateFormat.parse(dataFimInscricoesString);
                        dataCompeticao = dateFormat.parse(dataCompeticaoString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date hoje = Calendar.getInstance().getTime();
                    if(dataInscricoes.after(hoje)) {
                        diferenca = dataInscricoes.getTime() - hoje.getTime();
                        mudaInformacoes();
                        paraQue.setText("Para o início das inscrições");
                    } else if(dataFimInscricoes.after(hoje)) {
                        diferenca = dataFimInscricoes.getTime() - hoje.getTime();
                        mudaInformacoes();
                        inscricao.setVisibility(View.VISIBLE);
                        paraQue.setText("Para o fim das inscrições");
                    } else if(dataCompeticao.after(hoje)) {
                        diferenca = dataCompeticao.getTime() - hoje.getTime();
                        mudaInformacoes();
                        paraQue.setText("Para a competição");
                    } else {
                        diasFaltando.setVisibility(View.GONE);
                        paraQue.setVisibility(View.GONE);
                        textoBase.setText("Novidades em breve");
                    }

                    textos.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + dataInscricoes +" "+dataFimInscricoes +" "+dataCompeticao +" ");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private String updateTempo(long diferenca) {
        long difHora = diferenca/3600000;
        long difMin = (diferenca%3600000)/60000;
        long difSeg = ((diferenca%3600000)%60000)/100;
        String diasFaltandoTexto = String.format(new Locale("pt-BR"),"%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(diferenca),
                TimeUnit.MILLISECONDS.toMinutes(diferenca) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diferenca)),
                TimeUnit.MILLISECONDS.toSeconds(diferenca) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diferenca)));
        return diasFaltandoTexto;
    }

    private void mudaInformacoes() {
        long difDias = diferenca/86400000;
        if(difDias < 1) {
            diasFaltando.setText(updateTempo(diferenca));
            Timer T=new Timer();
            T.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    getActivity().runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            diasFaltando.setText(updateTempo(diferenca));
                            diferenca = diferenca - 1000;
                            if(diferenca <= 0) {
                                diferenca = 0;
                            }
                        }
                    });
                }
            }, 1000, 1000);
        }
        else if(difDias == 1) {
            textoBase.setText("Falta");
            String diasFaltandoTexto = difDias + " DIA";
            diasFaltando.setText(diasFaltandoTexto);
        } else {
            String diasFaltandoTexto = difDias + " DIAS";
            diasFaltando.setText(diasFaltandoTexto);
        }
    }

}
