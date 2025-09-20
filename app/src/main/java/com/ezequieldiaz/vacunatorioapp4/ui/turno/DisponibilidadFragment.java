package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentDisponibilidadBinding;
import com.ezequieldiaz.vacunatorioapp4.model.Disponibilidad;
import com.ezequieldiaz.vacunatorioapp4.model.DisponibilidadResponse;

import java.util.ArrayList;
import java.util.List;

public class DisponibilidadFragment extends Fragment {

    private FragmentDisponibilidadBinding binding;
    private DisponibilidadAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDisponibilidadBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Disponibilidad> lista = new ArrayList<>();
        lista.add(new Disponibilidad("2025-09-20", 5));
        lista.add(new Disponibilidad("2025-09-21", 3));
        lista.add(new Disponibilidad("2025-09-22", 8));

        adapter = new DisponibilidadAdapter(lista, disponibilidad -> {
            // Acción al hacer click en una fecha
            Toast.makeText(getContext(),
                    "Elegiste " + disponibilidad.getFecha(),
                    Toast.LENGTH_SHORT).show();
        });

        binding.rvDisponibilidades.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvDisponibilidades.setAdapter(adapter);
    }
}
