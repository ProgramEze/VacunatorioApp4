package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentFechasBinding;
import com.ezequieldiaz.vacunatorioapp4.model.FechaSeleccionada;
import com.ezequieldiaz.vacunatorioapp4.model.FechasResponse;

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

        // Inicializamos RecyclerView y Adapter vacíos
        fechasAdapter = new FechasAdapter(new ArrayList<>(), getLayoutInflater(), fechaSeleccionada -> {
            HorariosDialog dialog = HorariosDialog.newInstance(fechaSeleccionada);
            dialog.show(getParentFragmentManager(), "horariosDialog");
        });
        RecyclerView rv = binding.rvFechas;
        GridLayoutManager glm = new GridLayoutManager(getContext(), 3);
        rv.setLayoutManager(glm);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
        rv.setAdapter(fechasAdapter);

        // Cargar fechas desde Bundle
        Bundle args = getArguments();
        if (args != null) {
            FechaSeleccionada fechaSeleccionada = (FechaSeleccionada) args.getSerializable("fechaSeleccionada");
            if (fechaSeleccionada != null && fechaSeleccionada.getAnio() > 0 && fechaSeleccionada.getMes() > 0) {
                vm.cargarFechas(fechaSeleccionada.getMes(), fechaSeleccionada.getAnio());
            }
        }

        // Observer
        vm.getMFechas().observe(getViewLifecycleOwner(), listaDeFechas -> {
            if (listaDeFechas == null) {
                Log.d("FechasFragment", "Lista de fechas nula");
                fechasAdapter.actualizarLista(new ArrayList<>());
                return;
            }
            Log.d("FechasFragment", "Fechas recibidas: " + listaDeFechas.size());

            // Filtrar fechas con al menos un horario libre
            List<FechasResponse> fechasFiltradas = new ArrayList<>();
            for (FechasResponse f : listaDeFechas) {
                if (f.getHorarios() != null && f.getHorarios().stream().anyMatch(h -> h != null && h.isLibre())) {
                    fechasFiltradas.add(f);
                }
            }
            Log.d("FechasFragment", "Fechas filtradas con horarios libres: " + fechasFiltradas.size());

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
