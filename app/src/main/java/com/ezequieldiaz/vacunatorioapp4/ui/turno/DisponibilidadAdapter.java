package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.model.Disponibilidad;
import com.ezequieldiaz.vacunatorioapp4.model.DisponibilidadResponse;

import java.util.List;

public class DisponibilidadAdapter extends RecyclerView.Adapter<DisponibilidadAdapter.DisponibilidadViewHolder> {

    private List<Disponibilidad> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Disponibilidad disponibilidad);
    }

    public DisponibilidadAdapter(List<Disponibilidad> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DisponibilidadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_disponibilidad, parent, false);
        return new DisponibilidadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisponibilidadViewHolder holder, int position) {
        Disponibilidad disponibilidad = lista.get(position);
        holder.bind(disponibilidad, listener);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class DisponibilidadViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvDisponibles;

        public DisponibilidadViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvDisponibles = itemView.findViewById(R.id.tvDisponibles);
        }

        public void bind(final Disponibilidad disponibilidad, final OnItemClickListener listener) {
            tvFecha.setText(disponibilidad.getFecha());
            tvDisponibles.setText(disponibilidad.getDisponibles() + " horarios disponibles");

            itemView.setOnClickListener(v -> listener.onItemClick(disponibilidad));
        }
    }
}
