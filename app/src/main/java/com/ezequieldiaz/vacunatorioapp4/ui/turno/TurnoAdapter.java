package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.model.Turno;

import java.util.List;

public class TurnoAdapter extends RecyclerView.Adapter<TurnoAdapter.TurnoViewHolder> {

    private List<Turno> listaTurnos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Turno turno);
    }

    public TurnoAdapter(List<Turno> listaTurnos, OnItemClickListener listener) {
        this.listaTurnos = listaTurnos;
        this.listener = listener;
    }

    public void setLista(List<Turno> nuevaLista) {
        this.listaTurnos = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TurnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_turno, parent, false);
        return new TurnoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnoViewHolder holder, int position) {
        Turno turno = listaTurnos.get(position);

        // Hora de la cita: suponiendo formato "yyyy-MM-dd HH:mm:ss"
        if (turno.getCita() != null && turno.getCita().length() >= 16) {
            holder.tvHora.setText(turno.getCita().substring(11, 16));
        } else {
            holder.tvHora.setText("Sin hora");
        }

        // Nombre del paciente
        if (turno.getPaciente() != null && turno.getPaciente().getNombre() != null) {
            holder.tvPaciente.setText("Paciente: " + turno.getPaciente().getNombre());
        } else {
            holder.tvPaciente.setText("Paciente: Libre");
        }

        // Estado del turno
        if (turno.getPaciente() == null) {
            holder.tvEstado.setText("Estado: Libre");
            holder.tvEstado.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
        } else {
            holder.tvEstado.setText("Estado: Ocupado");
            holder.tvEstado.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red));
        }

        // Click en el turno
        holder.itemView.setOnClickListener(v -> listener.onItemClick(turno));
    }

    @Override
    public int getItemCount() {
        return listaTurnos != null ? listaTurnos.size() : 0;
    }

    static class TurnoViewHolder extends RecyclerView.ViewHolder {
        TextView tvHora;
        TextView tvPaciente;
        TextView tvEstado;

        public TurnoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHora = itemView.findViewById(R.id.tvHoraTurno);
            tvPaciente = itemView.findViewById(R.id.tvPacienteTurno);
            tvEstado = itemView.findViewById(R.id.tvEstadoTurno);
        }
    }
}
