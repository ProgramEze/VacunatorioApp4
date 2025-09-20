package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.model.DisponibilidadResponse;
import com.ezequieldiaz.vacunatorioapp4.model.Horario;

import java.util.List;

public class FechaAdapter extends RecyclerView.Adapter<FechaAdapter.FechaViewHolder> {
    private List<DisponibilidadResponse> fechas;
    private OnFechaClickListener listener;

    public interface OnFechaClickListener {
        void onFechaClick(DisponibilidadResponse fecha);
    }

    public FechaAdapter(List<DisponibilidadResponse> fechas, OnFechaClickListener listener) {
        this.fechas = fechas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FechaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fecha, parent, false);
        return new FechaViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FechaViewHolder holder, int position) {
        DisponibilidadResponse fecha = fechas.get(position);
        holder.tvFecha.setText(fecha.getFecha());
        holder.tvDisponibles.setText(fecha.getHorarios().stream().filter(Horario::isLibre).count() + " horarios disponibles");
        holder.itemView.setOnClickListener(v -> listener.onFechaClick(fecha));
    }

    @Override
    public int getItemCount() {
        return fechas.size();
    }

    static class FechaViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvDisponibles;
        public FechaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvDisponibles = itemView.findViewById(R.id.tvDisponibles);
        }
    }
}