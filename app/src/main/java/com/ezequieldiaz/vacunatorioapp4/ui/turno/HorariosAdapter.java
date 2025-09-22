package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezequieldiaz.vacunatorioapp4.databinding.ItemHorarioBinding;
import com.ezequieldiaz.vacunatorioapp4.model.HorariosResponse;

import java.util.List;

public class HorariosAdapter extends RecyclerView.Adapter<HorariosAdapter.HorarioViewHolder> {

    private List<HorariosResponse> listaHorarios;
    private OnHorarioClickListener listener;

    public interface OnHorarioClickListener {
        void onHorarioClick(HorariosResponse horario);
    }

    public HorariosAdapter(List<HorariosResponse> listaHorarios, OnHorarioClickListener listener) {
        this.listaHorarios = listaHorarios;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HorarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemHorarioBinding binding = ItemHorarioBinding.inflate(inflater, parent, false);
        return new HorarioViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HorarioViewHolder holder, int position) {
        HorariosResponse horario = listaHorarios.get(position);
        holder.bind(horario);
    }

    @Override
    public int getItemCount() {
        return listaHorarios.size();
    }

    class HorarioViewHolder extends RecyclerView.ViewHolder {

        private ItemHorarioBinding binding;

        public HorarioViewHolder(ItemHorarioBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        void bind(HorariosResponse horario) {
            binding.tvHorario.setText(horario.getHora());

            // Cambiamos el fondo según disponibilidad
            if (horario.isLibre()) {
                binding.tvHorario.setBackgroundColor(binding.getRoot().getResources().getColor(android.R.color.holo_green_dark));
            } else {
                binding.tvHorario.setBackgroundColor(binding.getRoot().getResources().getColor(android.R.color.darker_gray));
            }

            binding.getRoot().setOnClickListener(v -> {
                if (horario.isLibre()) {
                    listener.onHorarioClick(horario);
                }
            });
        }
    }
}
