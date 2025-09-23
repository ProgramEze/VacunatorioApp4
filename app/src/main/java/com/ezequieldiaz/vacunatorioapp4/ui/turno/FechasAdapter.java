package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.model.FechasResponse;
import com.ezequieldiaz.vacunatorioapp4.model.HorariosResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FechasAdapter extends RecyclerView.Adapter<FechasAdapter.FechaViewHolder> {
    private List<FechasResponse> listaDeFechas;
    private LayoutInflater li;
    private Context context;
    private OnFechaClickListener listener; // <- listener agregado

    public interface OnFechaClickListener {
        void onFechaClick(FechasResponse fecha);
    }

    // Nuevo constructor con listener
    public FechasAdapter(List<FechasResponse> listaDeFechas, LayoutInflater li, OnFechaClickListener listener) {
        this.listaDeFechas = listaDeFechas;
        this.li = li;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FechaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = li.inflate(R.layout.item_fecha, parent, false);
        context = parent.getContext();
        return new FechaViewHolder(view);
    }

    public void actualizarLista(List<FechasResponse> nuevasFechas) {
        listaDeFechas.clear();
        if (nuevasFechas != null) {
            listaDeFechas.addAll(nuevasFechas);
        }
        notifyDataSetChanged();
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FechaViewHolder holder, int position) {
        FechasResponse fecha = listaDeFechas.get(position);

        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = isoFormat.parse(fecha.getFecha());

            SimpleDateFormat argFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fechaFormateada = argFormat.format(date);

            holder.tvFecha.setText(fechaFormateada);
        } catch (Exception e) {
            e.printStackTrace();
            holder.tvFecha.setText(fecha.getFecha());
        }

        int libres = 0;
        if (fecha.getHorarios() != null) {
            for (HorariosResponse h : fecha.getHorarios()) {
                if (h.isLibre()) libres++;
            }
        }
        holder.tvDisponibles.setText(libres + " horarios disponibles");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFechaClick(fecha);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDeFechas.size();
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
