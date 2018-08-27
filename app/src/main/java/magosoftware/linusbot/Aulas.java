package magosoftware.linusbot;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Aulas extends Fragment {

    FirebaseFirestore db;
    CollectionReference docRef;
    private ExpandableListView expandableListView;
    ExpandableListAdapter adapter;
    LinkedHashMap<String, Aula> lstItensGrupo;
    List<String> lstGrupo;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("aulas");
        return inflater.inflate(R.layout.aulas, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedIntanceState) {
        super.onActivityCreated(savedIntanceState);
        progressBar = getView().findViewById(R.id.progress_bar);
        expandableListView = getView().findViewById(R.id.expandable_list);
        expandableListView.setVisibility(View.GONE);
        lstGrupo = new ArrayList<>();
        lstItensGrupo = new LinkedHashMap<>();
        docRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot query = task.getResult();
                    for(DocumentSnapshot doc : query.getDocuments()) {
                        lstGrupo.add(doc.getId());
                        Aula aula = new Aula((String)doc.get("titulo"),
                                ((String)doc.get("data")).replace("_","\n"),
                                (String)doc.get("professor"),
                                (String)doc.get("descricao"),
                                (String)doc.get("local"));
                        lstItensGrupo.put(doc.getId(), aula);
                    }
                    setupExpandableView();
                }
            }
        });
    }

    private void setupExpandableView() {
        Log.d("Informacion", "lstGrupo: "+lstGrupo.size());
        Log.d("Informacion", "lstItens: "+lstItensGrupo.size());
        adapter = new ExpandableListAdapter(getActivity(), lstGrupo, lstItensGrupo);
        // define o apadtador do ExpandableListView
        expandableListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        expandableListView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        Log.d("DEV/TAREFASCONC", "Passou do trem");
    }
}
