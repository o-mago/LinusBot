package magosoftware.linusbot;

/**
 * Created by root on 06/03/18.
 */

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 03/03/18.
 */

public class LineAdapter extends RecyclerView.Adapter<LineHolder>{

    private LineAdapter.OnItemClicked onClick;

    List<CampoConfig> campoConfigs;

    public LineAdapter() {
        this.campoConfigs = new ArrayList<>();
    }

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position, Boolean habilita);
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LineHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.config_buttons, parent, false));
    }

    public interface afterTextChanged {
        void afterTextChanged(Editable editable, int position);
    }

    @Override
    public void onBindViewHolder(LineHolder holder, final int position) {
        //holder.nomeUniversidade.setText(campoConfigs.get(position).getUniversidade());
        holder.icone.setImageDrawable(campoConfigs.get(position).getIcone());
        holder.mensagem.setText(campoConfigs.get(position).getComando(), EditText.BufferType.EDITABLE);
        holder.habilita.setChecked(campoConfigs.get(position).isHabilita());
        holder.habilita.setOnClickListener(new OnClickListenerWithHolder(holder) {
            @Override
            public void onClick(View v) {
                campoConfigs.get(position).habilita = holderClick.habilita.isChecked();
                onClick.onItemClick(position, campoConfigs.get(position).isHabilita());
            }
        });
        holder.mensagem.addTextChangedListener(new TextWatcherWithHolder(holder) {

            public void afterTextChanged(Editable s) {
                campoConfigs.get(position).mensagem = holderWatcher.mensagem.getText().toString();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public String getEditText(LineHolder holder, final int position) {
        //holder.nomeUniversidade.setText(campoConfigs.get(position).getUniversidade());
        return holder.mensagem.getText().toString();
    }

    public void add(CampoConfig model) {
        campoConfigs.add(model);
    }

    public void remove(CampoConfig model) {
        campoConfigs.remove(model);
    }

    public void add(List<CampoConfig> models) {
        campoConfigs.addAll(models);
    }

    public void remove(List<CampoConfig> models) {
        for (CampoConfig model : models) {
            campoConfigs.remove(model);
        }
    }

    public void replaceAll(List<CampoConfig> models) {
        for (int i = campoConfigs.size() - 1; i >= 0; i--) {
            final CampoConfig model = campoConfigs.get(i);
            if (!models.contains(model)) {
                campoConfigs.remove(model);
            }
        }
        campoConfigs.addAll(models);
    }

    @Override
    public int getItemCount() {
        return campoConfigs.size();
    }

    public void setOnClick(LineAdapter.OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
}
