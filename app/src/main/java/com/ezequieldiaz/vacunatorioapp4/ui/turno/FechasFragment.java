package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentFechasBinding;
import com.ezequieldiaz.vacunatorioapp4.model.FechaSeleccionada;
import com.ezequieldiaz.vacunatorioapp4.model.FechasResponse;
import com.ezequieldiaz.vacunatorioapp4.request.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FechasFragment extends Fragment {

    private FragmentFechasBinding binding;
    private FechasFragmentViewModel vm;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFechasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        vm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(FechasFragmentViewModel.class);

        /*vm.getMFechas().observe(getViewLifecycleOwner(), listaDeFechas -> {
            if (listaDeFechas != null) {
                actualizarRecyclerView(listaDeFechas); // método que arma adapter
            }
        });*/

        // Cada vez que llegás, cargás las fechas del bundle
        Bundle args = getArguments();
        if (args != null) {
            FechaSeleccionada fechaSeleccionada = (FechaSeleccionada) args.getSerializable("fechaSeleccionada");
            if (fechaSeleccionada.getAnio() > 0 && fechaSeleccionada.getMes() > 0) {
                // Llamar al ViewModel para cargar los turnos de ese mes/año
                vm.cargarFechas(fechaSeleccionada.getMes(), fechaSeleccionada.getAnio());
            }
        }

        vm.getMFechas().observe(getViewLifecycleOwner(), listaDeFechas -> {
            // Creamos el adapter pasando el listener
            FechasAdapter fechasAdapter = new FechasAdapter(listaDeFechas, getLayoutInflater(), fechaSeleccionada -> {
                // Aquí abrimos el diálogo con los horarios
                HorariosDialog dialog = HorariosDialog.newInstance(fechaSeleccionada);
                dialog.show(getParentFragmentManager(), "horariosDialog");
            });

            // Configuramos RecyclerView
            RecyclerView rv = binding.rvFechas;
            GridLayoutManager glm = new GridLayoutManager(getContext(), 3); // 3 columnas
            rv.setLayoutManager(glm);
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
            rv.addItemDecoration(new SpaceItemDecoration(spacingInPixels));
            rv.setLayoutManager(glm);
            rv.setAdapter(fechasAdapter);
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- RecyclerView ---
        /*adapter = new FechasAdapter(listaFechas, fecha -> {
            // Aquí manejás el click sobre una fecha, por ejemplo abrir horarios
            Toast.makeText(getContext(), "Fecha seleccionada: " + fecha.getFecha(), Toast.LENGTH_LONG).show();
        });

        binding.rvFechas.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvFechas.setAdapter(adapter);

        // --- Recuperar mes y año del Bundle ---
        if (getArguments() != null) {
            mesSeleccionado = getArguments().getInt("mes");
            anioSeleccionado = getArguments().getInt("anio");

            cargarTurnosDelMes(anioSeleccionado, mesSeleccionado);
        }*/
    }

    /*private void cargarTurnosDelMes(int anio, int mes) {
        // Formatear fecha en "yyyy-MM"
        String fecha = String.format("%04d-%02d", anio, mes);

        // Obtener token si es necesario
        String token = ApiClient.leerToken(requireContext());

        // Llamada usando tu ApiClient real
        Call<List<FechasResponse>> call = ApiClient.getEndPoints().getDisponibilidadDelMes(token, fecha);
        call.enqueue(new retrofit2.Callback<List<FechasResponse>>() {
                    @Override
                    public void onResponse(Call<List<FechasResponse>> call, Response<List<FechasResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            listaFechas.clear();
                            listaFechas.addAll(response.body());
                            adapter.setLista(listaFechas);
                        } else {
                            Toast.makeText(getContext(), "No se encontraron turnos para este mes", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<List<FechasResponse>> call, Throwable t) {
                        Toast.makeText(getContext(), "Error al cargar turnos: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }*/


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
