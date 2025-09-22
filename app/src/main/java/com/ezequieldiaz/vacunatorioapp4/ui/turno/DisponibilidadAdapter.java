package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.model.FechasResponse;

import java.util.List;

public class DisponibilidadAdapter extends RecyclerView.Adapter<DisponibilidadAdapter.FechaViewHolder> {

    private List<FechasResponse> listaFechas;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FechasResponse disponibilidad);
    }

    public DisponibilidadAdapter(List<FechasResponse> listaFechas, OnItemClickListener listener) {
        this.listaFechas = listaFechas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FechaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fecha, parent, false);
        return new FechaViewHolder(view);
    }

    public void setLista(List<FechasResponse> nuevaLista) {
        this.listaFechas = nuevaLista;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull FechaViewHolder holder, int position) {
        FechasResponse disponibilidad = listaFechas.get(position);
        holder.tvFecha.setText(disponibilidad.getFecha());

        // Contar horarios libres
        long libres = disponibilidad.getHorarios().stream().filter(h -> h.isLibre()).count();
        holder.tvDisponibles.setText(libres + " horarios disponibles");

        // Click en la fecha
        holder.itemView.setOnClickListener(v -> listener.onItemClick(disponibilidad));
    }

    @Override
    public int getItemCount() {
        return listaFechas.size();
    }

    static class FechaViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha;
        TextView tvDisponibles;

        public FechaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvDisponibles = itemView.findViewById(R.id.tvDisponibles);
        }
    }
}
