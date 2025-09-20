package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HorarioAdapter extends RecyclerView.Adapter<HorarioAdapter.HorarioViewHolder> {
    private List<String> horarios;
    private OnHorarioClickListener listener;

    public interface OnHorarioClickListener {
        void onHorarioClick(String horario);
    }

    public HorarioAdapter(List<String> horarios, OnHorarioClickListener listener) {
        this.horarios = horarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new HorarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorarioViewHolder holder, int position) {
        String horario = horarios.get(position);
        holder.textView.setText(horario);

        holder.itemView.setOnClickListener(v -> listener.onHorarioClick(horario));
    }

    @Override
    public int getItemCount() {
        return horarios.size();
    }

    static class HorarioViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public HorarioViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
