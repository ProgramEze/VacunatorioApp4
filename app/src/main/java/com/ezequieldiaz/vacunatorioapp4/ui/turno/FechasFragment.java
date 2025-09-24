package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentFechasBinding;
import com.ezequieldiaz.vacunatorioapp4.model.FechaSeleccionada;
import com.ezequieldiaz.vacunatorioapp4.model.FechasResponse;
import com.ezequieldiaz.vacunatorioapp4.model.HorariosResponse;

import java.util.ArrayList;
import java.util.List;

public class FechasFragment extends Fragment {

    private FragmentFechasBinding binding;
    private FechasFragmentViewModel vm;
    private FechasAdapter fechasAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFechasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializamos ViewModel
        vm = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
                .create(FechasFragmentViewModel.class);

        fechasAdapter = new FechasAdapter(new ArrayList<>(), getLayoutInflater(), fechaSeleccionada -> {
            HorariosDialog dialog = HorariosDialog.newInstance(fechaSeleccionada, vm.getDarTurnoOCargarTurno());
            dialog.show(getParentFragmentManager(), "horariosDialog");
        });

        RecyclerView rv = binding.rvFechas;
        GridLayoutManager glm = new GridLayoutManager(getContext(), 3);
        rv.setLayoutManager(glm);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        rv.setAdapter(fechasAdapter);

        Bundle args = getArguments();
        if (args != null) {
            FechaSeleccionada fechaSeleccionada = (FechaSeleccionada) args.getSerializable("fechaSeleccionada");
            int dniPaciente = args.getInt("dniPaciente");
            if (fechaSeleccionada != null && fechaSeleccionada.getAnio() > 0 && fechaSeleccionada.getMes() > 0 && dniPaciente == -1) {
                vm.cargarFechas(fechaSeleccionada.getMes(), fechaSeleccionada.getAnio());
            } else {
                vm.cargarTurnos(dniPaciente, fechaSeleccionada.getMes(), fechaSeleccionada.getAnio());
            }
        }

        vm.getMFechas().observe(getViewLifecycleOwner(), listaDeFechas -> {
            if (listaDeFechas == null || listaDeFechas.isEmpty()) {
                fechasAdapter.actualizarLista(new ArrayList<>());
                return;
            }

            List<FechasResponse> fechasFiltradas = new ArrayList<>();
            for (FechasResponse fecha : listaDeFechas) {
                if (fecha.getHorarios() != null) {
                    boolean tieneLibre = false;
                    for (HorariosResponse h : fecha.getHorarios()) {
                        if (h.isLibre()) {
                            tieneLibre = true;
                            break;
                        }
                    }
                    if (tieneLibre) fechasFiltradas.add(fecha);
                }
            }

            fechasAdapter.actualizarLista(fechasFiltradas);
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
