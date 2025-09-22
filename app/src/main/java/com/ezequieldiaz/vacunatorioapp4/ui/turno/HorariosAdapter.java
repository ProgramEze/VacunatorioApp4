package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.model.HorariosResponse;

import java.util.List;

public class HorariosAdapter extends RecyclerView.Adapter<HorariosAdapter.HorarioViewHolder> {

    private List<HorariosResponse> horarios;
    private Context context;

    public interface OnHorarioClickListener {
        void onHorarioClick(HorariosResponse horario);
    }

    private final OnHorarioClickListener listener;

    public HorariosAdapter(List<HorariosResponse> horarios, OnHorarioClickListener listener) {
        this.horarios = horarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_horario, parent, false);
        return new HorarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorarioViewHolder holder, int position) {
        HorariosResponse horario = horarios.get(position);
        holder.tvHorario.setText(horario.getHora());

        // Cambiar color según disponibilidad
        if (horario.isLibre()) {
            holder.tvHorario.setBackgroundColor(ContextCompat.getColor(context, R.color.verdeLibre));
            holder.tvHorario.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        } else {
            holder.tvHorario.setBackgroundColor(ContextCompat.getColor(context, R.color.grisOcupado));
            holder.tvHorario.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        }

        holder.itemView.setOnClickListener(v -> {
            if (horario.isLibre()) {
                listener.onHorarioClick(horario);
            }
        });
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    static class HorarioViewHolder extends RecyclerView.ViewHolder {
        TextView tvHorario;

        public HorarioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHorario = itemView.findViewById(R.id.tvHorario);
        }
    }
}
