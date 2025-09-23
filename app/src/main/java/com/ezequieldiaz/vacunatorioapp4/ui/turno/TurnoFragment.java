package com.ezequieldiaz.vacunatorioapp4.ui.turno;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.ezequieldiaz.vacunatorioapp4.R;
import com.ezequieldiaz.vacunatorioapp4.databinding.FragmentTurnoBinding;
import com.ezequieldiaz.vacunatorioapp4.model.FechaSeleccionada;
import com.ezequieldiaz.vacunatorioapp4.model.Paciente;
import com.ezequieldiaz.vacunatorioapp4.model.TipoDeVacuna;
import com.ezequieldiaz.vacunatorioapp4.model.Turno;
import com.ezequieldiaz.vacunatorioapp4.model.Tutor;
import com.ezequieldiaz.vacunatorioapp4.ui.registro.EscanerActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TurnoFragment extends Fragment {
    private TurnoFragmentViewModel vm;
    private FragmentTurnoBinding binding;
    private NavController navController;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTurnoBinding.inflate(inflater, container, false);
        vm = new ViewModelProvider(this).get(TurnoFragmentViewModel.class);

        navController = NavHostFragment.findNavController(this);
        navController.getCurrentBackStackEntry()
                .getSavedStateHandle()
                .getLiveData("fechaSeleccionadaCompleta")
                .observe(getViewLifecycleOwner(), fechaIso -> {
                    String hora = navController.getCurrentBackStackEntry()
                            .getSavedStateHandle()
                            .get("horaSeleccionada");
                    String otorgarOCargar = navController.getCurrentBackStackEntry()
                            .getSavedStateHandle()
                            .get("darTurnoOCargarTurno");
                    if (fechaIso != null && hora != null) {
                        try {
                            String cita = fechaIso + "T" + hora+":00";
                            Log.d("Cita", cita);
                            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            Date date = isoFormat.parse(fechaIso.toString());
                            Log.d("cargarTurno", date.toString());
                            SimpleDateFormat argFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            String fechaFormateada = argFormat.format(date);
                            if (otorgarOCargar.equalsIgnoreCase("Dar turno")) {
                                vm.setFechaYHora(fechaFormateada + " - " + hora);
                            } else {
                                String citaIso = fechaIso + "T" + hora + ":00";
                                vm.cargarTurno(citaIso);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            binding.etdFecha.setText(fechaIso + " / " + hora);
                        }
                    } else {

                    }
                });

        vm.getMPaciente().observe(getViewLifecycleOwner(), paciente -> {
            if (paciente != null && vm.getMTutor().getValue() != null && binding.btnConfirmarTurno.getText().toString().equalsIgnoreCase("Otorgar turno")) {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(), binding.spnRelacionTutor.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarTurno.getText().toString());
            }
        });

        vm.getMListaTipo().observe(getViewLifecycleOwner(), tiposDeVacuna -> {
            ArrayAdapter<TipoDeVacuna> adapter = new ArrayAdapter<>(
                    requireContext(),
                    R.layout.spinner_item_dialog,
                    tiposDeVacuna
            ) {
                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }
            };
            adapter.setDropDownViewResource(R.layout.spinner_item_dialog);
            binding.spnTipoDeVacuna.setAdapter(adapter);

            // 🔹 Restaurar la selección guardada en el ViewModel
            Integer index = vm.getSelectedTipoVacunaIndex().getValue();
            if (index != null && index < adapter.getCount()) {
                binding.spnTipoDeVacuna.setSelection(index);
            }
        });

        vm.getMTutor().observe(getViewLifecycleOwner(), tutor -> {
            if (tutor != null && vm.getMPaciente().getValue() != null && binding.btnConfirmarTurno.getText().toString().equalsIgnoreCase("Otorgar turno")) {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(), binding.spnRelacionTutor.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarTurno.getText().toString());
            }
        });

        vm.getMRelaciones().observe(getViewLifecycleOwner(), relaciones -> {
            ArrayAdapter<String> relacionAdapter = new ArrayAdapter<>(
                    requireContext(),
                    R.layout.spinner_item_dialog,
                    relaciones
            ) {
                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }
            };
            relacionAdapter.setDropDownViewResource(R.layout.spinner_item_dialog);
            binding.spnRelacionTutor.setAdapter(relacionAdapter);

            // 🔹 Restaurar la selección guardada en el ViewModel
            Integer index = vm.getSelectedRelacionIndex().getValue();
            if (index != null && index < relacionAdapter.getCount()) {
                binding.spnRelacionTutor.setSelection(index);
            }
        });

        vm.getMFechaYHora().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(String fecha) {
                binding.etdFecha.setText(fecha);
            }
        });

        vm.getMTurno().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Turno turno) {
                if(turno != null){
                    try {
                        binding.etDNIPaciente.setText(turno.getPaciente().getDni());
                        binding.etDNITutor.setText(turno.getTutor().getDni());
                        binding.spnTipoDeVacuna.setSelection(turno.getTipoDeVacuna().getId());
                        String relacion = turno.getRelacionTutor();
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) binding.spnRelacionTutor.getAdapter();
                        int pos = adapter.getPosition(relacion);
                        if (pos >= 0) {
                            binding.spnRelacionTutor.setSelection(pos);
                        }
                    } catch (NullPointerException e){
                        Log.d("turno", e.getMessage());
                    }
                }
            }
        });

        vm.getMConfirmar().observe(getViewLifecycleOwner(), s -> binding.btnConfirmarTurno.setText(s));

        vm.getMLimpiar().observe(getViewLifecycleOwner(), limpiar -> {
            if (limpiar != null && limpiar) {
                vm.limpiarMutables();
                binding.etDNIPaciente.setText("");
                binding.etDNITutor.setText("");
                binding.spnTipoDeVacuna.setSelection(0);
                binding.spnRelacionTutor.setSelection(0);
                binding.etdFecha.setText("");
                vm.setLimpiar(false);
            }
        });

        vm.getMMensaje().observe(getViewLifecycleOwner(), mensaje -> {
            if (mensaje != null && !mensaje.isEmpty()) {
                Toast.makeText(getContext(), mensaje, Toast.LENGTH_SHORT).show();
                vm.limpiarMensaje();
            }
        });

        vm.getMCancelarYConfirmar().observe(getViewLifecycleOwner(), mostrar -> {
            binding.btnCancelarTurno.setVisibility(mostrar != null && mostrar ? View.VISIBLE : View.GONE);
            binding.btnConfirmarAplicacion.setVisibility(mostrar != null && mostrar ? View.VISIBLE : View.GONE);
        });

        vm.getMFechaSeleccionada().observe(getViewLifecycleOwner(), fecha -> {
            if (fecha != null) {
                if (navController.getCurrentDestination() != null &&
                        navController.getCurrentDestination().getId() == R.id.nav_turno) {
                    Bundle bundle = new Bundle();
                    if(!binding.etDNIPaciente.getText().toString().isEmpty()){
                        if(vm.getMPaciente().getValue() != null && binding.etDNITutor.getText().toString().isEmpty()) {
                            bundle.putInt("dniPaciente", vm.getMPaciente().getValue().getId());
                        } else {
                            bundle.putInt("dniPaciente", -1);
                        }
                    } else {
                        bundle.putInt("dniPaciente", -1);
                    }
                    bundle.putSerializable("fechaSeleccionada", fecha);
                    navController.navigate(R.id.action_nav_turno_to_nav_fechas, bundle);
                    vm.setFechaSeleccionada(null);
                }
            }
        });

        vm.getMMostrarDialog().observe(getViewLifecycleOwner(), mostrar -> {
            if (mostrar != null && mostrar) {
                MesAnioDialog dialog = new MesAnioDialog();
                dialog.setOnFechaSeleccionadaListener((mes, anio) -> {
                    vm.setFechaSeleccionada(new FechaSeleccionada(mes, anio));
                });
                dialog.show(getParentFragmentManager(), "MesAnioDialog");
            }
        });

        binding.ibCargarPaciente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etDNIPaciente.setText("");
                Intent intent = new Intent(requireContext(), EscanerActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.ibCargarTutor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etDNITutor.setText("");
                Intent intent = new Intent(requireContext(), EscanerActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        // 🔹 Guardar selección en el ViewModel
        binding.spnTipoDeVacuna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vm.setSelectedTipoVacunaIndex(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // 🔹 Guardar selección en el ViewModel
        binding.spnRelacionTutor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vm.setSelectedRelacionIndex(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        binding.etdFecha.setOnClickListener(v -> {
            vm.solicitarSeleccionFecha(binding.etDNIPaciente.getText().toString());
        });

        binding.btnLimpiar.setOnClickListener(v -> {
            vm.limpiarMutables();
        });

        binding.btnConfirmarTurno.setOnClickListener(v -> {
            if(binding.btnConfirmarTurno.getText().toString().equalsIgnoreCase("Otorgar turno")){
                vm.buscarDNIPaciente(binding.etDNIPaciente.getText().toString());
                vm.buscarDNITutor(binding.etDNITutor.getText().toString());
            } else {
                vm.guardarTurno((TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem(),binding.spnRelacionTutor.getSelectedItem().toString(), binding.etdFecha.getText().toString(), binding.btnConfirmarTurno.getText().toString());
            }
        });

        binding.btnCancelarTurno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.cancelarTurno();
            }
        });

        binding.btnConfirmarAplicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.confirmarAplicacion();
            }
        });

        vm.cargarSpinners();

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vm.cargarImagen(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

/*
        binding.btnConfirmarTurno.setOnClickListener(v -> {
            vm.buscarDNIPaciente(binding.etDNIPaciente.getText().toString());
            vm.buscarDNITutor(binding.etDNITutor.getText().toString());

            TipoDeVacuna tipo = (TipoDeVacuna) binding.spnTipoDeVacuna.getSelectedItem();
            String relacion = binding.spnRelacionTutor.getSelectedItem().toString();
            String fecha = binding.etdFecha.getText().toString();

            vm.guardarTurno(tipo, relacion, fecha, binding.btnConfirmarTurno.getText().toString());
        });

        vm.getMPaciente().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Paciente p) {
                if(p != null){
                    binding.etDNIPaciente.setText(p.getDni());
                } else {
                    binding.etDNIPaciente.setText("");
                }
            }
        });

        vm.getMTutor().observe(getViewLifecycleOwner(), new Observer<>() {
            @Override
            public void onChanged(Tutor t) {
                if(t != null){
                    binding.etDNITutor.setText(t.getDni());
                } else {
                    binding.etDNITutor.setText("");
                }
            }
        });

        */